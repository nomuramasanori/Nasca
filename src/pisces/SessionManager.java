package pisces;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class SessionManager {
	private static SqlSessionFactory sessionFactory;
	
	protected SqlSessionFactory getSessionFactory() {
		return sessionFactory;
	}

	protected SessionManager() {
		if(sessionFactory != null) return;
		
        try (InputStream in = SessionManager.class.getResourceAsStream("/resources/mybatis-config.xml")) {
            //設定ファイルを元に、 SqlSessionFactory を作成する
            sessionFactory = new SqlSessionFactoryBuilder().build(in);
        } catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
	
	public static SqlSession createSession(){
		return sessionFactory.openSession();
	}
}
