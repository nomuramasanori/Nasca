package pisces;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

public class DependencyDAO extends BaseDAO {
	
	//コンストラクタ
	public DependencyDAO(){}
	
	public List<Dependency> selectByElementID(String id){
		List<Dependency> result = null;
		
		try (SqlSession session = this.getSessionFactory().openSession()) {
            result = session.selectList("pisces.nasca.dependency.selectByElementID", id);
        }
		
		return result;
	}
	
	public List<Dependency> selectByDependencyElementID(String id){
		List<Dependency> result = null;
		
		try (SqlSession session = this.getSessionFactory().openSession()) {
            result = session.selectList("pisces.nasca.dependency.selectByDependencyElementID", id);
        }
		
		return result;
	}
	
	public void insert(String elementID, String dependencyElementID, Boolean isDependencyTypeCreate, boolean isDependencyTypeRead, boolean isDependencyTypeUpdate, boolean isDependencyTypeDelete, String remark){
		try (SqlSession session = this.getSessionFactory().openSession()) {
			Map<String, Object> param = new HashMap<>();
            param.put("elmtid", elementID);
            param.put("dpdeid", dependencyElementID);
            param.put("dpdtpc", isDependencyTypeCreate);
            param.put("dpdtpr", isDependencyTypeRead);
            param.put("dpdtpu", isDependencyTypeUpdate);
            param.put("dpdtpd", isDependencyTypeDelete);
            param.put("remark", remark);
            
            session.insert("pisces.nasca.dependency.insert", param);
            session.commit();
        }
	}
	
	public void update(String elementID, String dependencyElementID, Boolean isDependencyTypeCreate, boolean isDependencyTypeRead, boolean isDependencyTypeUpdate, boolean isDependencyTypeDelete, String remark){
		try (SqlSession session = this.getSessionFactory().openSession()) {
			Map<String, Object> param = new HashMap<>();
            param.put("elmtid", elementID);
            param.put("dpdeid", dependencyElementID);
            param.put("dpdtpc", isDependencyTypeCreate);
            param.put("dpdtpr", isDependencyTypeRead);
            param.put("dpdtpu", isDependencyTypeUpdate);
            param.put("dpdtpd", isDependencyTypeDelete);
            param.put("remark", remark);
            
            session.insert("pisces.nasca.dependency.update", param);
            session.commit();
        }
	}
	
	public void delete(String elementID, String dependencyElementID){
		try (SqlSession session = this.getSessionFactory().openSession()) {
			Map<String, Object> param = new HashMap<>();
            param.put("elmtid", elementID);
            param.put("dpdeid", dependencyElementID);
            
            session.insert("pisces.nasca.dependency.delete", param);
            session.commit();
        }
	}
}
