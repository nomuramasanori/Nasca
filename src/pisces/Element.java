package pisces;

import java.util.List;
import java.util.Objects;

public class Element {
	private static ElementDAO elementDAO = new ElementDAO();
	private static DependencyDAO dependencyDAO = new DependencyDAO();
	
	private static Element root = new Element("root", "ROOT", "TABLE", "This is root", "database.svg");
	
	private String id;
	private String name;
	private String type;
	private String remark;
	private String svgFile;
	
	private Element(){}
	private Element(String id, String name, String type, String remark, String svgFile){
		this.id = id;
		this.name = name;
		this.type = type;
		this.remark = remark;
		this.svgFile = svgFile;
	}
	
	public static Element getRoot(){
		return root;
	}
	
	public static Element getElement(String id){
		return elementDAO.selectByID(id);
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getSvgFile() {
		return svgFile;
	}
	public void setSvgFile(String svgFile) {
		this.svgFile = svgFile;
	}
	
	public List<Dependency> getDependency(){
		return dependencyDAO.selectByElementID(this.id);
	}
	
	public List<Dependency> getDependencyDependOnMe(){
		return dependencyDAO.selectByDependencyElementID(this.id);
	}
	
	public Element getParent(){
		Element result = null;
		String parentid = "";
		String[] idStrings = this.id.split("\\.");
		
		//親ノードの設定を行います
		if(idStrings.length == 1){
			result = Element.getRoot();
		}
		else{
			//区切り文字"."で完全IDを区切り末尾IDを除いたIDを親ノードとします。
			for(int i = 0; i < idStrings.length - 1; i++){
				parentid = parentid + "." + idStrings[i];
			}
			parentid = parentid.substring(1, parentid.length());
			result = elementDAO.selectByID(parentid);
		}
		
		return result;
	}
	
	public List<Element> getChild(){
		return elementDAO.selectChild(this.id);
	}
	
	public boolean isLeaf(){
		boolean result = true;
		
		List<Element> elements = this.getChild();
		if(elements.size() > 0) result = false;
		
		return result;
	}
	
	public boolean contain(String id){
		boolean result = false;
		
		if(id.length() <= this.getId().length()){
			result = false;
		} else if(id.substring(0, this.getId().length() + 1).equals(this.getId() + ".")){
			result = true;
		}
		
		return result;
	}
	
	public boolean contain(Element element){
		return this.contain(element.getId());
	}
	
	@Override
	public boolean equals(Object obj){
        // オブジェクトがnullでないこと
        if (obj == null) {
            return false;
        }
        // オブジェクトが同じ型であること
        if (!(obj instanceof Element)) {
            return false;
        }
        // 同値性を比較
        return this.getId().equals(((Element)obj).getId());
	}
	
	@Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}
