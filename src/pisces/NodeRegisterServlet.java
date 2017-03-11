package pisces;

import java.util.List;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Servlet implementation class NodeRegisterServlet
 */
@WebServlet(name = "NodeRegister", urlPatterns = {"/NodeRegister/*"})
public class NodeRegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NodeRegisterServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println(request.getRequestURI());
		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree((String)request.getParameter("parameter"));
		
		String[] paths = request.getRequestURI().split("/");
		String operation = paths[3];
		
		ElementDAO elementDAO = new ElementDAO();
		
		String parent = (root.get("parentid").asText().equals(Element.getRoot().getId()) ? "" : root.get("parentid").asText() + ".");
		switch(operation){
			case "insert":
				try(SqlSession session = SessionManager.createSession()){
					elementDAO.insert(
						session,
						parent + root.get("id").asText(), 
						root.get("name").asText(),
						root.get("type").asText(),
						root.get("remark").asText());
					
					this.updateDependencyID(session, root.get("parentid").asText(), parent + root.get("id").asText());

					session.commit();
				}catch(PersistenceException ex){
		        }
				break;
			case "update":
				try(SqlSession session = SessionManager.createSession()){
					//更新対象とその子エレメントを取得します。
					Element target = elementDAO.selectByID(root.get("originalid").asText());
					List<Element> children = target.getChild();
					
					//ID変更の場合
					if(!root.get("originalid").asText().equals(parent + root.get("id").asText())){
						//子エレメントとその依存を更新します。
						for(Element child : children){
							this.updateDependencyID(session, child.getId(), child.getId().replaceFirst(root.get("originalid").asText(), parent + root.get("id").asText()));

							//子エレメントの更新
							elementDAO.update(
									session,
									child.getId(), 
									child.getId().replaceFirst(root.get("originalid").asText(), parent + root.get("id").asText()), 
									child.getName(),
									child.getType(),
									child.getRemark());
						}
					}
					
					this.updateDependencyID(session, root.get("originalid").asText(), parent + root.get("id").asText());
					
					elementDAO.update(
						session,
						root.get("originalid").asText(), 
						parent + root.get("id").asText(), 
						root.get("name").asText(),
						root.get("type").asText(),
						root.get("remark").asText());
					
					session.commit();
				}
				
				break;
			case "delete":
				try(SqlSession session = SessionManager.createSession()){					
					//更新対象とその子エレメントを取得します。
					Element target = elementDAO.selectByID(parent + root.get("id").asText());
					List<Element> children = target.getChild();
					
					//子エレメントとその依存を更新します。
					for(Element child : children){
						this.deleteDependency(session, child.getId());

						//子エレメントの更新
						elementDAO.delete(session, child.getId());
					}
					
					this.deleteDependency(session, parent + root.get("id").asText());
					
					elementDAO.delete(session, parent + root.get("id").asText());
					
					session.commit();
				}
				
				break;
		}
	}

	private void updateDependencyID(SqlSession session, String originalID, String newID){
		if(originalID.equals(Element.getRoot().getId())) return;
		
		DependencyDAO dependencyDAO = new DependencyDAO();
		
		List<Dependency> dependencies = dependencyDAO.selectByElementID(originalID);
		for(Dependency dependency : dependencies){
			dependencyDAO.update(
				session, 
				dependency.getElement().getId(),
				dependency.getDependencyElement().getId(),
				newID,
				dependency.getDependencyElement().getId(),
				dependency.isDependencyTypeCreate(),
				dependency.isDependencyTypeRead(),
				dependency.isDependencyTypeUpdate(),
				dependency.isDependencyTypeDelete(),
				dependency.getRemark());
		}
		
		dependencies = dependencyDAO.selectByDependencyElementID(originalID);
		for(Dependency dependency : dependencies){
			dependencyDAO.update(
				session, 
				dependency.getElement().getId(),
				dependency.getDependencyElement().getId(),
				dependency.getElement().getId(),
				newID,
				dependency.isDependencyTypeCreate(),
				dependency.isDependencyTypeRead(),
				dependency.isDependencyTypeUpdate(),
				dependency.isDependencyTypeDelete(),
				dependency.getRemark());
		}
	}
	
	private void deleteDependency(SqlSession session, String id){
		DependencyDAO dependencyDAO = new DependencyDAO();
		
		List<Dependency> dependencies = dependencyDAO.selectByElementID(id);
		for(Dependency dependency : dependencies){
			dependencyDAO.delete(session, dependency.getElement().getId(), dependency.getDependencyElement().getId());
		}
		
		dependencies = dependencyDAO.selectByDependencyElementID(id);
		for(Dependency dependency : dependencies){
			dependencyDAO.delete(session, dependency.getElement().getId(), dependency.getDependencyElement().getId());
		}
	}
}
