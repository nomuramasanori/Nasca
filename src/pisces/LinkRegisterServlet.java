package pisces;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Servlet implementation class LinkRegisterServlet
 */
@WebServlet(name = "LinkRegister", urlPatterns = {"/LinkRegister/*"})
public class LinkRegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LinkRegisterServlet() {
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
		
		DependencyDAO dependnecyDAO = new DependencyDAO();
		
		switch(operation){
			case "insert":
				dependnecyDAO.insert(
					root.get("source").get("id").asText(), 
					root.get("target").get("id").asText(),
					root.get("dependencyTypeC").asBoolean(),
					root.get("dependencyTypeR").asBoolean(),
					root.get("dependencyTypeU").asBoolean(),
					root.get("dependencyTypeD").asBoolean(),
					root.get("remark").asText());
				break;
			case "update":
				dependnecyDAO.update(
					root.get("source").get("id").asText(), 
					root.get("target").get("id").asText(),
					root.get("source").get("id").asText(), 
					root.get("target").get("id").asText(),
					root.get("dependencyTypeC").asBoolean(),
					root.get("dependencyTypeR").asBoolean(),
					root.get("dependencyTypeU").asBoolean(),
					root.get("dependencyTypeD").asBoolean(),
					root.get("remark").asText());
				break;
			case "delete":
				dependnecyDAO.delete(
					root.get("source").get("id").asText(), 
					root.get("target").get("id").asText());
				break;
			
		}
	}

}
