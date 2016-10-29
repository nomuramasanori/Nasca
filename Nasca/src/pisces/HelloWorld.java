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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
 
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

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
		Connection con = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    try {
			// データソースの取得
			Context ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/test");
			 
			// データベースへ接続
			con = ds.getConnection();
			 
			// SQLの実行
//			pstmt = con.prepareStatement("select 'TABLE' OBJTID,'#' PARENT,'TABLE' OBJTNM,'TABLE' OBJTTP from dual union all "
//					+ "select 'PROCEDURE' OBJTID,'#' PARENT,'PROCEDURE' OBJTNM,'PROCEDURE' OBJTTP from dual union all "
//					+ "select OBJTID,OBJTTP PARENT,OBJTNM,OBJTTP from M_OBJECT");
			pstmt = con.prepareStatement("select OBJTID,OBJTNM,OBJTTP from M_OBJECT");
//			pstmt = con.prepareStatement("select distinct nodeid from (select SRCNID NODEID from m_objtprnt union all select DESNID NODEID from m_objtprnt) sub");
			rs = pstmt.executeQuery();
			 
			//rs.first();
			//request.setAttribute("ID", rs.getString("ID"));
			//equest.setAttribute("TITLE", rs.getString("TITLE"));
		      
			// JsonFactoryの生成
			JsonFactory jsonFactory = new JsonFactory();
			// JsonGeneratorの取得
			JsonGenerator generator = jsonFactory.createGenerator(out);
			
			//オブジェクトの書き込み
			generator.writeStartArray();
			while(rs.next()){				
				String parent = "";
				String[] idStrings = rs.getString("OBJTID").split("\\.");
				
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
				generator.writeStringField("id",rs.getString("OBJTID"));
				generator.writeStringField("parent", parent);
				generator.writeStringField("text", rs.getString("OBJTNM"));
				generator.writeStringField("type", rs.getString("OBJTTP"));
				generator.writeEndObject();
				
			}
			generator.writeEndArray();

			//ストリームへの書き出し
			generator.flush();
	    } catch (Exception e) {
	      System.out.println(e.getMessage());
	      throw new ServletException(e);
	    } finally {
	      try {
	        rs.close();
	        pstmt.close();
	        con.close();
	      } catch (Exception e) {
	      }
	    }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
