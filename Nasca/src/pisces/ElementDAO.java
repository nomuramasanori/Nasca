package pisces;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

public class ElementDAO extends BaseDAO {
	
	//コンストラクタ
	public ElementDAO(){}
	
	public Element SelectByID(String id){
		Element result = null;
		
		try (SqlSession session = this.getSessionFactory().openSession()) {
            result = session.selectOne("pisces.nasca.selectByID", id);
        }
		
		System.out.println(result.getName());
		
		return result;
	}
	
	public List<Element> SelectAll(){
		List<Element> result = null;
		
		try (SqlSession session = this.getSessionFactory().openSession()) {
			result = session.selectList("pisces.nasca.selectAll");
        }
		
		return result;
	}
}
