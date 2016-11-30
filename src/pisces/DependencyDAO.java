package pisces;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

public class DependencyDAO extends BaseDAO {
	
	//コンストラクタ
	public DependencyDAO(){}
	
	public List<Dependency> SelectByElementID(String id){
		List<Dependency> result = null;
		
		try (SqlSession session = this.getSessionFactory().openSession()) {
            result = session.selectList("pisces.nasca.dependency.selectByElementID", id);
        }
		
		return result;
	}
	
	public List<Dependency> SelectByDependencyElementID(String id){
		List<Dependency> result = null;
		
		try (SqlSession session = this.getSessionFactory().openSession()) {
            result = session.selectList("pisces.nasca.dependency.selectByDependencyElementID", id);
        }
		
		return result;
	}
}
