package pisces;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

import java.util.Iterator;
import java.util.List;

/**
 * Servlet implementation class HelloWorld
 */
@WebServlet("/HelloWorld")
public class HelloWorld extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public HelloWorld() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		PrintWriter out = response.getWriter();
		
		ElementDAO dao = new ElementDAO();
		List<Element> elements = dao.SelectAll();
		Iterator<Element> itr = elements.iterator();
		
		// JsonFactoryの生成
		JsonFactory jsonFactory = new JsonFactory();
		// JsonGeneratorの取得
		JsonGenerator generator = jsonFactory.createGenerator(out);
			
		//オブジェクトの書き込み
		generator.writeStartArray();
		while(itr.hasNext()){
			Element element = itr.next();
			String parent = "";
			String[] idStrings = element.getId().split("\\.");
			
			//親ノードの設定を行います
			if(idStrings.length == 1){
				parent = "#";
			} else{
				//区切り文字"."で完全IDを区切り末尾IDを除いたIDを親ノードとします。
				for(int i = 0; i < idStrings.length - 1; i++){
					parent = parent + "." + idStrings[i];
				}
				parent = parent.substring(1, parent.length());
			}
			
			generator.writeStartObject();
			generator.writeStringField("id",element.getId());
			generator.writeStringField("parent", parent);
			generator.writeStringField("text", element.getName());
			generator.writeStringField("type", element.getType());
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
