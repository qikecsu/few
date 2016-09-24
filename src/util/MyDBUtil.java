package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

public class MyDBUtil {
	
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(MyDBUtil.class);
	
	//驱动程序名 
	static String driverName="com.mysql.jdbc.Driver"; 
	//数据库用户名 
	static String userName="root"; 
	//密码 
	static String userPasswd="123456"; 
	//数据库名 
	static String dbName="fire_ew"; 
	
	//联结字符串 
	static String dbURL="jdbc:mysql://localhost/"+dbName+"?user="+userName+"&password="+userPasswd; 
		
	static{
		try {
			Class.forName(driverName);
		} catch (ClassNotFoundException e) {
			logger.error(e,e);
		}
	}
	
	public static Connection getConnection() throws SQLException {
		
		try {
			//File directory = new File(".");
			//System.out.println(directory.getCanonicalPath()); //得到的是C:\test
			
			//File file = new File("c:\\patrol");
			//file.mkdir();
		} catch (Exception e) {
			logger.error(e,e);
		}
		
		Connection conn = DriverManager.getConnection(dbURL);
		
		return conn;
	}
	/**
	 * 获取数据库连接对象
	 * @return 数据库连接对象
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static Connection getConnection(String url) throws SQLException {
		
		try {
			//File directory = new File(".");
			//System.out.println(directory.getCanonicalPath()); //得到的是C:\test
			
			//File file = new File("c:\\patrol");
			//file.mkdir();
		} catch (Exception e) {
			logger.error(e,e);
		}
		
	
		return DriverManager.getConnection(url);
	}
/*	
	public static Connection recordConnection() throws SQLException {			
		return DriverManager.getConnection(recURL);
	}*/
}
