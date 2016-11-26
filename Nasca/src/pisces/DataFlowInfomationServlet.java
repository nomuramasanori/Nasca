package pisces;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

/**
 * Servlet implementation class IO
 */
@WebServlet("/DataFlowInfomation")
public class DataFlowInfomationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private class AddtionalNodeInfomation{
		private int distance;
		public int getDistance() {
			return distance;
		}
		public AddtionalNodeInfomation(int distance){
			this.distance = distance;
		}
	}
	
	private class AddtionalLinkInfomation{
		private Dependency dependency;
		private int distance;
		public Dependency getDependency() {
			return dependency;
		}
		public int getDistance() {
			return distance;
		}
		public AddtionalLinkInfomation(Dependency dependency, int distance){
			this.dependency = dependency;
			this.distance = distance;
		}
	}
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DataFlowInfomationServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	request.setAttribute("parameter", "PROCEDURE1");
    	this.doPost(request, response);
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		ElementDAO elementDAO = new ElementDAO();

		// JsonFactoryの生成
		JsonFactory jsonFactory = new JsonFactory();
		// JsonGeneratorの取得
		JsonGenerator generator = jsonFactory.createGenerator(out);
		
		Map<Element, AddtionalNodeInfomation> nodes = new HashMap<Element, AddtionalNodeInfomation>();
		Map<String, AddtionalLinkInfomation> links = new HashMap<String, AddtionalLinkInfomation>();

		//デバッグのためGETからでも動作するようにします
		String[] nodeStrings = null;
		if(request.getAttribute("parameter") != null){
			nodeStrings = ((String)(request.getAttribute("parameter"))).split("/");
		}else{
			nodeStrings = request.getParameter("parameter").split("/");
		}
		
		for(int i=0 ; i < nodeStrings.length ; i++){
			//空文字の場合はスキップ
			if(nodeStrings[i] == "") continue;
			
			Element element = elementDAO.selectByID(nodeStrings[i]);
			
			//名前空間の場合はスキップ
			if(!element.isLeaf()) continue;
			
			//依存情報のないノードが存在する可能性があるのでパラメータで渡されたIDをノードリストに追加します。
			nodes.put(element, new AddtionalNodeInfomation(1));
			
			//依存する要素を取得し結果変数に格納します。
			//結果変数はHashSetなので重複を考慮しなくても問題ありません。
			List<Dependency> dependencies = element.getDependency();
			for(Dependency dependency : dependencies){
				nodes.put(dependency.element, new AddtionalNodeInfomation(1));
				links.put(element.getId()+"-"+dependency.element.getId() , new AddtionalLinkInfomation(dependency, 1));
			}
			
			//依存される要素を取得し結果変数に格納します。
			//結果変数はHashSetなので重複を考慮しなくても問題ありません。
			List<Dependency> dependenciesDependOnMe = element.getDependencyDependOnMe();
			for(Dependency dependencyDependOnMe : dependenciesDependOnMe){
				nodes.put(dependencyDependOnMe.element, new AddtionalNodeInfomation(1));
				links.put(dependencyDependOnMe.element.getId()+"-"+element.getId() , new AddtionalLinkInfomation(dependencyDependOnMe, 1));
			}
		}
		
		//2段階目を取得する際に、ループを回すコレクションに対し要素追加を行うとConcurrentModificationExceptionが発生するためコピーします
		Map<Element, AddtionalNodeInfomation> nodes2 = new HashMap<Element, AddtionalNodeInfomation>();
		nodes2.putAll(nodes);
		
		//2段階目を取得します
		for(Map.Entry<Element, AddtionalNodeInfomation> element : nodes.entrySet()){
			List<Dependency> dependencies = element.getKey().getDependency();
			for(Dependency dependency : dependencies){
				if(!nodes2.containsKey(dependency.element)){
					nodes2.put(dependency.element, new AddtionalNodeInfomation(2));
				}
				if(!links.containsKey(element.getKey().getId()+"-"+dependency.element.getId())){
					links.put(element.getKey().getId()+"-"+dependency.element.getId(), new AddtionalLinkInfomation(dependency, 2));
				}
			}
			
			List<Dependency> dependenciesDependOnMe = element.getKey().getDependencyDependOnMe();
			for(Dependency dependencyDependOnMe : dependenciesDependOnMe){
				if(!nodes2.containsKey(dependencyDependOnMe.element)){
					nodes2.put(dependencyDependOnMe.element, new AddtionalNodeInfomation(2));
				}
				if(!links.containsKey(dependencyDependOnMe.element.getId()+"-"+element.getKey().getId())){
					links.put(dependencyDependOnMe.element.getId()+"-"+element.getKey().getId(), new AddtionalLinkInfomation(dependencyDependOnMe, 2));
				}
			}
		}
		
		//JSON生成の開始
		generator.writeStartObject();
		
		//ノード情報を作成します。
		generator.writeFieldName("nodes");
		generator.writeStartArray();
	    for(Map.Entry<Element, AddtionalNodeInfomation> element : nodes2.entrySet()) {
	    	generator.writeStartObject();
	    	generator.writeStringField("id", element.getKey().getId());
	    	generator.writeStringField("name", element.getKey().getName());
	    	generator.writeStringField("type", element.getKey().getType());
	    	generator.writeStringField("remark", element.getKey().getRemark());
	    	generator.writeStringField("svg-file", element.getKey().getSvgFile());
	    	generator.writeBooleanField("visible", element.getValue().getDistance() == 2 ? false : true);
	    	generator.writeEndObject();
	    }
		generator.writeEndArray();
		
		//リンク情報を作成します
		generator.writeFieldName("links");
		generator.writeStartArray();
	    for(Map.Entry<String, AddtionalLinkInfomation> link : links.entrySet()) {
	    	String[] splittedKey = link.getKey().split("-");
	    	Dependency depndency = link.getValue().getDependency();
	        generator.writeStartObject();
	        generator.writeStringField("source", splittedKey[0]);
	    	generator.writeStringField("target", splittedKey[1]);
	    	generator.writeStringField("remark", depndency.getRemark());
	    	generator.writeStringField("io", this.getIO(depndency.getDependencyType()));
	    	generator.writeStringField("colorIndex", String.valueOf(Integer.parseInt(depndency.getDependencyType(), 2)));
	    	generator.writeBooleanField("visible", link.getValue().getDistance() == 2 ? false : true);
	    	generator.writeEndObject();
	    }
		generator.writeEndArray();
		
		//JSON生成の終了
		generator.writeEndObject();
		
		//JSON書き出し
		generator.flush();
	}
	
	//CRUD情報からIO情報（矢印の方向）
	private String getIO(String crud){
		String result;
		String r;
		String cud;
		r = crud.substring(1,2);
		cud = crud.substring(0, 1) + crud.substring(2, 4);
		
		if(r.equals("1") && !cud.equals("000")){
			result = "IO";
		} else if(r.equals("0") && !cud.equals("000")){
			result = "O";
		} else{
			result = "I";
		}
		return result;
	}
}