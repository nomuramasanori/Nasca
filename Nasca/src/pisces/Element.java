package pisces;

import java.util.List;

public class Element {
	private DependencyDAO dao;
	
	private String id;
	private String name;
	private String type;
	private String remark;
	private String svgFile;
	
	public Element(){
		this.dao = new DependencyDAO();
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
	
	public List<Dependency> GetDependency(){
		return this.dao.SelectByElementID(this.id);
	}
	
	public List<Dependency> GetDependencyDependOnMe(){
		return this.dao.SelectByDependencyElementID(this.id);
	}
}
