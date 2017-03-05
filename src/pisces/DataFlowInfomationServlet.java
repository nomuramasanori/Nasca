package pisces;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
	
	private enum Direction{
		I,O,IO,None;
		
		public Direction reverse(){
			Direction result = Direction.None;
			if(this == Direction.IO || this == Direction.None) result = this;
			if(this == Direction.I) result = Direction.O;
			if(this == Direction.O) result =  Direction.I;
			return result;
		}
		
		public Direction add(Direction direction){
			Direction result = this;
			if(direction == Direction.IO){
				result = direction;
			}
			if(this == Direction.None){
				result = direction;
			}
			if(this == Direction.I && direction == Direction.O){
				result = Direction.IO;
			}
			if(this == Direction.O && direction == Direction.I){
				result = Direction.IO;
			}
			return result;
		}
	}
	
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
		private Direction direction;
		private boolean isVirtual;
		
		public boolean isVirtual() {
			return isVirtual;
		}
		public void setVirtual(boolean isVirtual) {
			this.isVirtual = isVirtual;
		}
		public Dependency getDependency() {
			return dependency;
		}
		public int getDistance() {
			return distance;
		}
		public Direction getDirection() {
			return direction;
		}
		public void setDirection(Direction direction) {
			this.direction = direction;
		}
		public AddtionalLinkInfomation(Dependency dependency, int distance, Direction direction){
			this.dependency = dependency;
			this.distance = distance;
			this.direction = direction;
		}
		public void addDirection(Direction direction){
			this.setDirection(this.getDirection().add(direction));
		}
	}
	
	private class LinkKey{
		private Element source;
		private Element target;
		public Element getSource() {
			return source;
		}
		public Element getTarget() {
			return target;
		}
		
		public LinkKey(Element source, Element target){
			this.source = source;
			this.target = target;
		}

		@Override
		public boolean equals(Object obj){
			LinkKey linkKey = null;
			
	        // オブジェクトがnullでないこと
	        if (obj == null) {
	            return false;
	        }
	        // オブジェクトが同じ型であること
	        if (obj instanceof LinkKey) {
	            linkKey = (LinkKey)obj;
	        }else{
	        	return false;
	        }
	        // 同値性を比較
	        return this.getSource().equals(linkKey.getSource()) && this.getTarget().equals(linkKey.getTarget()); 
		}
		
		@Override
	    public int hashCode() {
	        return Objects.hash(this.getSource().getId() + this.getTarget().getId());
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
    	request.setAttribute("parameter", "TABLE2");
    	this.doPost(request, response);
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();

		// JsonFactoryの生成
		JsonFactory jsonFactory = new JsonFactory();
		// JsonGeneratorの取得
		JsonGenerator generator = jsonFactory.createGenerator(out);
		
		Map<Element, AddtionalNodeInfomation> nodes = new HashMap<Element, AddtionalNodeInfomation>();
		Map<LinkKey, AddtionalLinkInfomation> links = new HashMap<LinkKey, AddtionalLinkInfomation>(); 
		List<Element> groups = new ArrayList<Element>();

		//デバッグのためGETからでも動作するようにします
		String[] nodeStrings = null;
		if(request.getAttribute("parameter") != null){
			nodeStrings = ((String)(request.getAttribute("parameter"))).split("/");
		}else{
			nodeStrings = request.getParameter("parameter").split("/");
		}
		
		for(String nm : nodeStrings){
			System.out.println(nm);
		}
		
		//1段階目の取得■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
		for(int i=0 ; i < nodeStrings.length ; i++){
			//空文字の場合はスキップ
			if(nodeStrings[i].equals("")) continue;
			
			//rootノードの場合はスキップ
			if(nodeStrings[i].equals("root")) continue;
			
			Element element = Element.getElement(nodeStrings[i]);

			this.putNodeAndLink(nodes, links, element, 1);
			if(!element.isLeaf()){
				for(Element child : element.getChild()){
					this.putNodeAndLink(nodes, links, child, 1);
				}
				
				//昇格のためのgroupエレメントを配列に格納します。
				//同一Groupの親－子－孫が候補の場合、格納するのは親Groupエレメントのみです。
				boolean isAdd = true;
				Element unnecesarryGroup = null;
				for(Element group : groups){
					if(group.contain(element)){
						isAdd = false;
						break;
					}
					
					if(element.contain(group)){
						unnecesarryGroup = group;
						break;
					}
				}
				if(isAdd){
					groups.add(element);
				}
				if(unnecesarryGroup != null){
					groups.remove(unnecesarryGroup);
				}
			}
		}

		//2段階目の取得■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
		
		//2段階目を取得する際に、ループを回すコレクションに対し要素追加を行うとConcurrentModificationExceptionが発生するためコピーします
		Map<Element, AddtionalNodeInfomation> nodes2 = new HashMap<Element, AddtionalNodeInfomation>();
		nodes2.putAll(nodes);
		
		//2段階目を取得します
		for(Map.Entry<Element, AddtionalNodeInfomation> nodeSet : nodes.entrySet()){
			this.putNodeAndLink(nodes2, links, nodeSet.getKey(), 2);
		}
		
		//グループ化■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
		Map<Element, AddtionalNodeInfomation> nodes3 = new HashMap<Element, AddtionalNodeInfomation>();
		Map<LinkKey, AddtionalLinkInfomation> links3 = new HashMap<LinkKey, AddtionalLinkInfomation>();
		
		for(Map.Entry<Element, AddtionalNodeInfomation> nodeSet : nodes2.entrySet()){
			boolean isGroupNode = false;
			
			for(Element group : groups){
				//グループ内のノード以外を対象とします
				if(group.contain(nodeSet.getKey().getId())){
					isGroupNode = true;
					break;
				}
			}
			
			if(!isGroupNode){
				nodes3.put(nodeSet.getKey(), nodeSet.getValue());
			}
		}
		for(Map.Entry<LinkKey, AddtionalLinkInfomation> linkSet : links.entrySet()){
			boolean isGroupLink = false;
			boolean isVirtual = false;
			LinkKey linkKey = null;
			Element source = linkSet.getKey().getSource();
			Element target = linkSet.getKey().getTarget();
			
			for(Element group : groups){
				//グループ内のリンク以外を対象とします
				if(group.contain(linkSet.getKey().getSource()) && group.contain(linkSet.getKey().getTarget())){
					isGroupLink = true;
					break;
				} else if(group.contain(linkSet.getKey().getSource())){
					//グループ内のノードはグループノードに昇格します（ソース側）
					source = group;
					isVirtual = true;
				} else if(group.contain(linkSet.getKey().getTarget())){
					//グループ内のノードはグループノードに昇格します（ターゲット側）
					target = group;
					isVirtual = true;
				}
			}
			
			linkKey = new LinkKey(source, target);
			
			if(!isGroupLink){
				//昇格した結果、そのリンクがすでに存在していれば集約します。
				if(links3.containsKey(linkKey)){
					this.summarizeLink(linkSet.getValue(), links3.get(linkKey), false);
				}else{
					linkSet.getValue().setVirtual(isVirtual);
					links3.put(linkKey, linkSet.getValue());
				}
			}
		}
		
		//逆向きリンクの集約■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
		Map<LinkKey, AddtionalLinkInfomation> links4 = new HashMap<LinkKey, AddtionalLinkInfomation>();
		
		for(Map.Entry<LinkKey, AddtionalLinkInfomation> linkSet3 : links3.entrySet()){
			boolean duplex = false;
			for(Map.Entry<LinkKey, AddtionalLinkInfomation> linkSet4 : links4.entrySet()){
				//ソースとターゲットが同一ノードで逆向きとなっているリンクがあれば集約します
				if(linkSet3.getKey().getSource() == linkSet4.getKey().getTarget() && linkSet3.getKey().getTarget() == linkSet4.getKey().getSource()){			
					this.summarizeLink(linkSet3.getValue(), linkSet4.getValue(), true);
					duplex = true;
					break;
				}
			}
			
			if(!duplex){
				links4.put(linkSet3.getKey(), linkSet3.getValue());
			}
		}

		//JSON生成■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
		generator.writeStartObject();
		
		//ノード情報を作成します。
		generator.writeFieldName("nodes");
		generator.writeStartArray();
	    for(Map.Entry<Element, AddtionalNodeInfomation> element : nodes3.entrySet()) {
	    	generator.writeStartObject();
	    	generator.writeStringField("parent", element.getKey().getParent().getId());
	    	generator.writeStringField("id", element.getKey().getId());
	    	generator.writeStringField("name", element.getKey().getName());
	    	generator.writeStringField("type", element.getKey().getType());
	    	generator.writeStringField("remark", element.getKey().getRemark());
	    	generator.writeStringField("svg-file", element.getKey().getSvgFile()+".svg");
	    	generator.writeBooleanField("visible", element.getValue().getDistance() == 2 ? false : true);
	    	generator.writeNumberField("size", element.getKey().isLeaf() ? 32 : 64);
	    	generator.writeBooleanField("group", !element.getKey().isLeaf());
	    	generator.writeNumberField("depth", element.getKey().getParent().getId().equals(Element.getRoot().getId()) ? 0 : element.getKey().getParent().getId().split("\\.").length);
	    	generator.writeEndObject();
	    }
		generator.writeEndArray();
		
		//リンク情報を作成します
		generator.writeFieldName("links");
		generator.writeStartArray();
	    for(Map.Entry<LinkKey, AddtionalLinkInfomation> linkSet : links4.entrySet()) {
	    	Dependency depndency = linkSet.getValue().getDependency();
	        generator.writeStartObject();
	        generator.writeStringField("source", linkSet.getKey().getSource().getId());
	    	generator.writeStringField("target", linkSet.getKey().getTarget().getId());
	    	generator.writeBooleanField("isCreate", depndency.isDependencyTypeCreate());
	    	generator.writeBooleanField("isRead", depndency.isDependencyTypeRead());
	    	generator.writeBooleanField("isUpdate", depndency.isDependencyTypeUpdate());
	    	generator.writeBooleanField("isDelete", depndency.isDependencyTypeDelete());
	    	generator.writeStringField("remark", depndency.getRemark());
	    	generator.writeStringField("io", linkSet.getValue().getDirection().toString());
	    	generator.writeStringField("colorIndex", String.valueOf(Integer.parseInt(depndency.getDependencyType(), 2)));
	    	generator.writeBooleanField("visible", linkSet.getValue().getDistance() == 2 ? false : true);
	    	generator.writeBooleanField("virtual", linkSet.getValue().isVirtual());
	    	generator.writeEndObject();
	    }
		generator.writeEndArray();
		
		//JSON生成の終了
		generator.writeEndObject();
		
		//JSON書き出し
		generator.flush();
	}
	
	private void putNodeAndLink(Map<Element, AddtionalNodeInfomation> nodes, Map<LinkKey, AddtionalLinkInfomation> links, Element element, Integer distance){
		//自分自身をノードリストに追加します。
		//結果変数はHashSetなので重複を考慮しなくても本来は問題ないですが、重複した場合後勝ちとなるため、先勝ちとなるよう存在チェックを行います。
		if(!nodes.containsKey(element)){
			nodes.put(element, new AddtionalNodeInfomation(distance));
		}
		
		//依存する要素を取得し結果変数に格納します。
		//結果変数はHashSetなので重複を考慮しなくても本来は問題ないですが、重複した場合後勝ちとなるため、先勝ちとなるよう存在チェックを行います。
		List<Dependency> dependencies = element.getDependency();
		for(Dependency dependency : dependencies){
			if(!nodes.containsKey(dependency.getDependencyElement())){
				nodes.put(dependency.getDependencyElement(), new AddtionalNodeInfomation(distance));
			}
			LinkKey linkKey = new LinkKey(element, dependency.getDependencyElement());
			if(!links.containsKey(linkKey)){
				links.put(linkKey, new AddtionalLinkInfomation(dependency, distance, this.getDrection(dependency)));
			}
		}
		
		//依存される要素を取得し結果変数に格納します。
		//結果変数はHashSetなので重複を考慮しなくても本来は問題ないですが、重複した場合後勝ちとなるため、先勝ちとなるよう存在チェックを行います。
		List<Dependency> dependenciesDependOnMe = element.getDependencyDependOnMe();
		for(Dependency dependencyDependOnMe : dependenciesDependOnMe){
			if(!nodes.containsKey(dependencyDependOnMe.getElement())){
				nodes.put(dependencyDependOnMe.getElement(), new AddtionalNodeInfomation(distance));
			}
			LinkKey linkKey = new LinkKey(dependencyDependOnMe.getElement(), element);
			if(!links.containsKey(linkKey)){
				links.put(linkKey, new AddtionalLinkInfomation(dependencyDependOnMe, distance, this.getDrection(dependencyDependOnMe)));
			}
		}
	}
	
	private void summarizeLink(AddtionalLinkInfomation source, AddtionalLinkInfomation target, boolean isReverseDirection){
		target.getDependency().setDependencyTypeCreate(target.getDependency().isDependencyTypeCreate() || source.getDependency().isDependencyTypeCreate());
		target.getDependency().setDependencyTypeRead(target.getDependency().isDependencyTypeRead() || source.getDependency().isDependencyTypeRead());
		target.getDependency().setDependencyTypeUpdate(target.getDependency().isDependencyTypeUpdate() || source.getDependency().isDependencyTypeUpdate());
		target.getDependency().setDependencyTypeDelete(target.getDependency().isDependencyTypeDelete() || source.getDependency().isDependencyTypeDelete());
		target.getDependency().setRemark(target.getDependency().getRemark() + source.getDependency().getRemark());
		target.isVirtual = true;
		if(isReverseDirection){
			target.addDirection(source.getDirection().reverse());
		}else{
			target.addDirection(source.getDirection());
		}
	}
	
	//CRUD情報からIO情報（矢印の方向）
	private Direction getDrection(Dependency dependency){
		Direction result;
		
		if(dependency.isDependencyTypeRead() && (dependency.isDependencyTypeCreate() || dependency.isDependencyTypeUpdate() || dependency.isDependencyTypeDelete())){
			result = Direction.IO;
		} else if(!dependency.isDependencyTypeRead() && (dependency.isDependencyTypeCreate() || dependency.isDependencyTypeUpdate() || dependency.isDependencyTypeDelete())){
			result = Direction.O;
		} else if(dependency.isDependencyTypeRead() && !(dependency.isDependencyTypeCreate() || dependency.isDependencyTypeUpdate() || dependency.isDependencyTypeDelete())){
			result = Direction.I;
		} else{
			result = Direction.None;
		}
		return result;
	}
}