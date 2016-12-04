package pisces;

import java.util.List;
import java.util.Objects;

public class Element {
	private static ElementDAO elementDAO = new ElementDAO();
	private static DependencyDAO dependencyDAO = new DependencyDAO();
	
	private String id;
	private String name;
	private String type;
	private String remark;
	private String svgFile;
	
	public Element(){}
	
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
		return dependencyDAO.SelectByElementID(this.id);
	}
	
	public List<Dependency> getDependencyDependOnMe(){
		return dependencyDAO.SelectByDependencyElementID(this.id);
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
