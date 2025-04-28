package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConfig {
	private final String CONNECTION = "jdbc:mysql://localhost:3306/pj_management?characterEncoding=latin1&useConfigs=maxPerformance";
	private final String USERNAME = "root";
	private final String PASSWORD = "";
	private static Connection con = null;
	
	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public Connection getConnection() throws SQLException{
		if(con==null||con.isClosed()) {
			con=DriverManager.getConnection(CONNECTION,USERNAME,PASSWORD);
		}
		return con;
	}

	public static void main(String[] args) {
		try {
			DBConfig dbConfig = new DBConfig();
			Connection connection = dbConfig.getConnection();
			if(connection!=null) {
				System.out.println("Connection established successfully");
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}

}
