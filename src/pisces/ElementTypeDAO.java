package pisces;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

public class ElementTypeDAO extends SessionManager {
	
	//コンストラクタ
	public ElementTypeDAO(){}
		
	public List<ElementType> selectAll(){
		List<ElementType> result = null;
		
		try (SqlSession session = this.getSessionFactory().openSession()) {
			result = session.selectList("pisces.nasca.elementType.selectAll");
        }
		
		return result;
	}
}
