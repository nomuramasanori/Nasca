package pisces;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

public class ElementDAO extends BaseDAO {
	
	//コンストラクタ
	public ElementDAO(){}
	
	public Element SelectByID(String id){
		Element result = null;
		
		try (SqlSession session = this.getSessionFactory().openSession()) {
            result = session.selectOne("pisces.nasca.element.selectByID", id);
        }
		
		return result;
	}
	
	public List<Element> SelectAll(){
		List<Element> result = null;
		
		try (SqlSession session = this.getSessionFactory().openSession()) {
			result = session.selectList("pisces.nasca.element.selectAll");
        }
		
		return result;
	}
	
	public boolean IsLeaf(String id){
		boolean result = true;
		List<Element> elements = null;
		
		try (SqlSession session = this.getSessionFactory().openSession()) {
			elements = session.selectList("pisces.nasca.element.selectChild", id);
        }
		
		if(elements.size() > 1) result = false;
		
		return result;
	}
}
