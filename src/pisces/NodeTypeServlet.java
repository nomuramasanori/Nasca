package pisces;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

/**
 * Servlet implementation class NodeTypeServlet
 */
@WebServlet("/NodeType")
public class NodeTypeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NodeTypeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		
		ElementTypeDAO dao = new ElementTypeDAO();
		List<ElementType> elementTypes = dao.selectAll();
		Iterator<ElementType> itr = elementTypes.iterator();
		
		// JsonFactoryの生成
		JsonFactory jsonFactory = new JsonFactory();
		// JsonGeneratorの取得
		JsonGenerator generator = jsonFactory.createGenerator(out);
			
		//オブジェクトの書き込み
		generator.writeStartArray();
		
		while(itr.hasNext()){
			ElementType elementType = itr.next();
			
			generator.writeStartObject();
			generator.writeStringField("value", elementType.getElementType());
			generator.writeStringField("text", elementType.getElementType() + "TEXT");
			generator.writeStringField("image", "imgxxxx/" + elementType.getSvgFile());
			generator.writeEndObject();
			
		}
		generator.writeEndArray();

		//ストリームへの書き出し
		generator.flush();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
