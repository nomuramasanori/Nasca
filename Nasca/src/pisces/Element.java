package pisces;

import java.util.List;

public class Element {
	private ElementDAO elementDAO;
	private DependencyDAO dependencyDAO;
	
	private String id;
	private String name;
	private String type;
	private String remark;
	private String svgFile;
	
	public Element(){
		this.elementDAO = new ElementDAO();
		this.dependencyDAO = new DependencyDAO();
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
		return this.dependencyDAO.SelectByElementID(this.id);
	}
	
	public List<Dependency> getDependencyDependOnMe(){
		return this.dependencyDAO.SelectByDependencyElementID(this.id);
	}
	
	public boolean isLeaf(){
		boolean result = true;
		
		List<Element> elements = this.elementDAO.selectChild(this.id);
		if(elements.size() > 0) result = false;
		
		return result;
	}
}
