package pisces;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    	request.setAttribute("parameter", "NS.NOM.harada/TABLE8");
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
		
		Set<Element> nodes = new HashSet<Element>();
		Map<String, Dependency> links = new HashMap<String, Dependency>();

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
			nodes.add(element);
			
			//依存する要素を取得し結果変数に格納します。
			//結果変数はHashSetなので重複を考慮しなくても問題ありません。
			List<Dependency> dependencies = element.getDependency();
			for(Dependency dependency : dependencies){
				nodes.add(dependency.element);
				links.put(element.getId()+"-"+dependency.element.getId() , dependency);
			}
			
			//依存される要素を取得し結果変数に格納します。
			//結果変数はHashSetなので重複を考慮しなくても問題ありません。
			List<Dependency> dependenciesDependOnMe = element.getDependencyDependOnMe();
			for(Dependency dependencyDependOnMe : dependenciesDependOnMe){
				nodes.add(dependencyDependOnMe.element);
				links.put(dependencyDependOnMe.element.getId()+"-"+element.getId() , dependencyDependOnMe);
			}
		}
		
		//JSON生成の開始
		generator.writeStartObject();
		
		//ノード情報を作成します。
		generator.writeFieldName("nodes");
		generator.writeStartArray();
	    for(Element element : nodes) {
	    	generator.writeStartObject();
	    	generator.writeStringField("id", element.getId());
	    	generator.writeStringField("name", element.getName());
	    	generator.writeStringField("type", element.getType());
	    	generator.writeStringField("remark", element.getRemark());
	    	generator.writeStringField("svg-file", element.getSvgFile());
	    	generator.writeEndObject();
	    }
		generator.writeEndArray();
		
		//リンク情報を作成します
		generator.writeFieldName("links");
		generator.writeStartArray();
	    for(Map.Entry<String, Dependency> link : links.entrySet()) {
	    	String[] splittedKey = link.getKey().split("-");
	        generator.writeStartObject();
	        generator.writeStringField("source", splittedKey[0]);
	    	generator.writeStringField("target", splittedKey[1]);
	    	generator.writeStringField("crud", link.getValue().getDependencyType());
	    	generator.writeStringField("remark", link.getValue().getRemark());
	    	generator.writeEndObject();
	    }
		generator.writeEndArray();
		
		//JSON生成の終了
		generator.writeEndObject();
		
		//JSON書き出し
		generator.flush();
	}
}