package pisces;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

/**
 * Servlet implementation class IO
 */
@WebServlet("/IO")
public class IO extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public IO() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		out.println("doGet!");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

			// JsonFactoryの生成
			JsonFactory jsonFactory = new JsonFactory();
			// JsonGeneratorの取得
			JsonGenerator generator = jsonFactory.createGenerator(out);
			
			Set<String> nodes = new HashSet<>();
			Map<String, String> links = new HashMap<>();
			
			String[] nodeStrings = request.getParameter("parameter").split("/");

			String conditionIN = "";
			for(int i=0 ; i < nodeStrings.length ; i++){
				//名前空間の場合はスキップ
				rs = con.prepareStatement("select count(*) cnt from M_OBJECT where OBJTID like '" + nodeStrings[i] + ".%'").executeQuery();
				rs.next();
				if(rs.getInt("cnt") > 0){
					continue;
				}
				rs.close();
				
				//依存情報検索で使用する、IDを列挙したIN句を作成します。
				conditionIN = conditionIN + ",\'" + nodeStrings[i] + "\'";
				
				//依存情報のないノードが存在する可能性があるのでパラメータで渡されたIDをノードリストに追加します。
				nodes.add(nodeStrings[i]);
			}
			conditionIN = conditionIN.substring(1, conditionIN.length());
			
			//チェックされたノードに関係するノードとIO情報を取得
			String sql = ""
					+ " SELECT"
					+ "   OBJTID"
					+ "   , DPDOID"
					+ "   , REMARK"
					+ "   , CONCAT(DPDTPC, DPDTPR, DPDTPU, DPDTPD) DPDTYP" 
					+ " FROM"
					+ "   t_depndncy"
					+ " WHERE"
					+ "   OBJTID IN (" + conditionIN + ") OR"
					+ "   DPDOID IN (" + conditionIN + ")";

			//SQLの実行
			rs = con.prepareStatement(sql).executeQuery();
			
			//検索結果からノードリスト・リンクリストを作成します。
			//結果変数はHashSetなので重複を考慮しなくても問題ありません。
			while(rs.next()){
				nodes.add(rs.getString("OBJTID"));
				nodes.add(rs.getString("DPDOID"));
				links.put(rs.getString("OBJTID")+"-"+rs.getString("DPDOID"), rs.getString("DPDTYP") + "/" + rs.getString("REMARK"));
			}
			
			//JSON生成の開始
			generator.writeStartObject();
			
			//ノード情報を作成します。
			generator.writeFieldName("nodes");
			generator.writeStartArray();
			Iterator<String> node = nodes.iterator();
		    while (node.hasNext()) {
		    	String id = node.next();
		    	//オブジェクトタイプを取得
		    	sql = "select OBJ.OBJTNM, OBJ.OBJTTP, OBJ.REMARK, OTP.SVGFLE from M_OBJECT OBJ INNER JOIN M_OBJTTYPE OTP ON OBJ.OBJTTP = OTP.OBJTTP where OBJ.OBJTID = \'"+ id + "\'";
				pstmt = con.prepareStatement(sql);
				rs = pstmt.executeQuery();
				while(rs.next()){
			    	generator.writeStartObject();
			    	generator.writeStringField("id", id);
			    	generator.writeStringField("name", rs.getString("OBJTNM"));
			    	generator.writeStringField("type", rs.getString("OBJTTP"));
			    	generator.writeStringField("remark", rs.getString("REMARK"));
			    	generator.writeStringField("svg-file", rs.getString("SVGFLE"));
			    	generator.writeEndObject();
				}
		    }
			generator.writeEndArray();
			
			//リンク情報を作成します
			generator.writeFieldName("links");
			generator.writeStartArray();
			Iterator<Map.Entry<String, String>> link = links.entrySet().iterator();
		    while (link.hasNext()) {
		    	Map.Entry<String, String> linkString = link.next();
		    	String[] splittedKey = linkString.getKey().split("-");
		    	String[] splittedValue = linkString.getValue().split("/");
		        generator.writeStartObject();
		        generator.writeStringField("source",splittedKey[0]);
		    	generator.writeStringField("target",splittedKey[1]);
		    	generator.writeStringField("crud",splittedValue[0]);
		    	generator.writeStringField("remark",splittedValue[1]);
		    	generator.writeEndObject();
		    }
			generator.writeEndArray();
			
			//JSON生成の終了
			generator.writeEndObject();
			
			//JSON書き出し
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
}