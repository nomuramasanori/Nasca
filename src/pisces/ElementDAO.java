package pisces;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

public class ElementDAO extends SessionManager {
	
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
	
	public void insert(String elementID, String elementName, String elementType, String remark){
		try (SqlSession session = this.getSessionFactory().openSession()) {
			this.insert(session, elementID, elementName, elementType, remark);
            session.commit();
        }
	}
	public void insert(SqlSession session, String elementID, String elementName, String elementType, String remark){
		Map<String, Object> param = new HashMap<>();
        param.put("elmtid", elementID);
        param.put("elmtnm", elementName);
        param.put("elmttp", elementType);
        param.put("remark", remark);
        
        session.insert("pisces.nasca.element.insert", param);
	}
	
	public void update(String originalElementID, String elementID, String elementName, String elementType, String remark){
		try (SqlSession session = this.getSessionFactory().openSession()) {
			this.update(session, originalElementID, elementID, elementName, elementType, remark);
            session.commit();
        }
	}
	public void update(SqlSession session, String originalElementID, String elementID, String elementName, String elementType, String remark){
		Map<String, Object> param = new HashMap<>();
		param.put("originalelmtid", originalElementID);
        param.put("elmtid", elementID);
        param.put("elmtnm", elementName);
        param.put("elmttp", elementType);
        param.put("remark", remark);
        
        session.update("pisces.nasca.element.update", param);
	}
	
	public void delete(String elementID){
		try (SqlSession session = this.getSessionFactory().openSession()) {
			this.delete(session, elementID);
            session.commit();
        }
	}
	public void delete(SqlSession session, String elementID){
		Map<String, Object> param = new HashMap<>();
        param.put("elmtid", elementID);
        
        session.delete("pisces.nasca.element.delete", param);
	}
}
