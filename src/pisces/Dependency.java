package pisces;

public class Dependency {	
	private String id;
	private Element element;
	private Element dependencyElement;
	private boolean dependencyTypeCreate;
	private boolean dependencyTypeRead;
	private boolean dependencyTypeUpdate;
	private boolean dependencyTypeDelete;
	private String remark;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Element getElement() {
		return element;
	}
	public void setElement(Element element) {
		this.element = element;
	}
	public Element getDependencyElement() {
		return dependencyElement;
	}
	public void setDependencyElement(Element element) {
		this.dependencyElement = element;
	}
	public boolean isDependencyTypeCreate() {
		return dependencyTypeCreate;
	}
	public void setDependencyTypeCreate(boolean dependencyTypeCreate) {
		this.dependencyTypeCreate = dependencyTypeCreate;
	}
	public boolean isDependencyTypeRead() {
		return dependencyTypeRead;
	}
	public void setDependencyTypeRead(boolean dependencyTypeRead) {
		this.dependencyTypeRead = dependencyTypeRead;
	}
	public boolean isDependencyTypeUpdate() {
		return dependencyTypeUpdate;
	}
	public void setDependencyTypeUpdate(boolean dependencyTypeUpdate) {
		this.dependencyTypeUpdate = dependencyTypeUpdate;
	}
	public boolean isDependencyTypeDelete() {
		return dependencyTypeDelete;
	}
	public void setDependencyTypeDelete(boolean dependencyTypeDelete) {
		this.dependencyTypeDelete = dependencyTypeDelete;
	}
	public String getDependencyType() {
		return
			(this.isDependencyTypeCreate() ? "1" : "0") +
			(this.isDependencyTypeRead() ? "1" : "0") +
			(this.isDependencyTypeUpdate() ? "1" : "0") +
			(this.isDependencyTypeDelete() ? "1" : "0");
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
