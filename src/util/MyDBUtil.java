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
	
	//���������� 
	static String driverName="com.mysql.jdbc.Driver"; 
	//���ݿ��û��� 
	static String userName="hive"; 
	//���� 
	static String userPasswd="123123"; 
	//���ݿ��� 
	static String dbName="few"; 
	
	//�����ַ��� 
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
			//System.out.println(directory.getCanonicalPath()); //�õ�����C:\test
			
			//File file = new File("c:\\patrol");
			//file.mkdir();
		} catch (Exception e) {
			logger.error(e,e);
		}
		
		Connection conn = DriverManager.getConnection(dbURL);
		
		return conn;
	}
	/**
	 * ��ȡ���ݿ����Ӷ���
	 * @return ���ݿ����Ӷ���
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static Connection getConnection(String url) throws SQLException {
		
		try {
			//File directory = new File(".");
			//System.out.println(directory.getCanonicalPath()); //�õ�����C:\test
			
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
