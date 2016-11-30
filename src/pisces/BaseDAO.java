package pisces;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class BaseDAO {
	private static SqlSessionFactory sessionFactory;
	
	protected SqlSessionFactory getSessionFactory() {
		return sessionFactory;
	}

	protected BaseDAO() {
		if(sessionFactory != null) return;
		
        try (InputStream in = BaseDAO.class.getResourceAsStream("/resources/mybatis-config.xml")) {
            //設定ファイルを元に、 SqlSessionFactory を作成する
            sessionFactory = new SqlSessionFactoryBuilder().build(in);
        } catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
}
