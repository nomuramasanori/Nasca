package pisces;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

public class ElementDAO extends BaseDAO {
	
	//コンストラクタ
	public ElementDAO(){}
	
	public Element selectByID(String id){
		Element result = null;
		
		try (SqlSession session = this.getSessionFactory().openSession()) {
            result = session.selectOne("pisces.nasca.element.selectByID", id);
        }
		
		return result;
	}
	
	public List<Element> selectAll(){
		List<Element> result = null;
		
		try (SqlSession session = this.getSessionFactory().openSession()) {
			result = session.selectList("pisces.nasca.element.selectAll");
        }
		
		return result;
	}
	
	public List<Element> selectChild(String id){
		List<Element> result = null;
		
		try (SqlSession session = this.getSessionFactory().openSession()) {
			result = session.selectList("pisces.nasca.element.selectChild", id);
        }

		return result;
	}
}
