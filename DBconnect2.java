package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconnect2 {

	public Connection conn;
	
	public Connection getconn() {
		
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String id = "cafe3";
		String password = "cafe3";
		
		try {
			Class.forName(driver);
			try {
				conn = DriverManager.getConnection(url, id, password);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.out.println("DB 접속 성공함");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("DB 접속 실패함");
		}
		return conn;
		
	}
}
