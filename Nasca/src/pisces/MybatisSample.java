package pisces;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class MybatisSample {
	public MybatisSample(){
	}
	
	public void Access() throws IOException{
        // ★ルートとなる設定ファイルを読み込む
        try (InputStream in = MybatisSample.class.getResourceAsStream("/resources/mybatis-config.xml")) {
            // ★設定ファイルを元に、 SqlSessionFactory を作成する
            SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(in);

            // ★SqlSessionFactory から SqlSession を生成する
            try (SqlSession session = factory.openSession()) {
                // ★SqlSession を使って SQL を実行する
                List<Map<String, Object>> result = session.selectList("sample.mybatis.selectTest");

                result.forEach(row -> {
                    System.out.println("---------------");
                    row.forEach((columnName, value) -> {
                        System.out.printf("columnName=%s, value=%s%n", columnName, value);
                    });
                });
            }

            try (SqlSession session = factory.openSession()) {
            	Map<String, Object> param = new HashMap<>();
                param.put("objectID", "PROCEDURE1");
                List<DataFlowNode> result = session.selectList("sample.mybatis.selectTest2", param);
                System.out.println(result.get(0).getRemark());
            }
        }
	}
}
