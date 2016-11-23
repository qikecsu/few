package hive.fire.dao;

import hive.fire.bean.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import util.MyDBUtil;


/**
 * 监控平台数据访问类
 */

public class fireDao {
		
	public User getUserByName(String name) throws SQLException{
		
	//	System.out.println("ok");
		
		Connection conn = MyDBUtil.getConnection();
		
	//	System.out.println("ok1");
		
		String sql = "select id, User,Name,Password,Type,Mobile, Note,cid from User where User=?";
		PreparedStatement pst = conn.prepareStatement(sql);
		
	//	System.out.println("ok2");
		
		pst.setString(1, name);
		ResultSet rs = pst.executeQuery();
		
		System.out.println(name);
		
		User user = null;
		if(rs.next()){
			String user1="";
			String name1 = "";
			String pwd  = "";
			String type = "";
			String mobile = "";
			String note = "";
			String cid = "";
			if(rs.getString(2)!=null) user1 = rs.getString(2);
			if(rs.getString(3)!=null) name1 = rs.getString(3);
			if(rs.getString(4)!=null) pwd  = rs.getString(4);
			if(rs.getString(5)!=null) type = rs.getString(5);
			if(rs.getString(6)!=null) mobile = rs.getString(6);
			if(rs.getString(7)!=null) note = rs.getString(7);
			if(rs.getString(8)!=null) cid = rs.getString(8);
			user = new User(rs.getInt(1),user1,name1,pwd,type,mobile, note,cid);
		}
		rs.close();
		pst.close();
		conn.close();
		return user;
	}
	
	public void logging(int userid, int did, String type, String log, String note) throws SQLException{
		
		//	System.out.println("ok");
		Date now = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		//SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
		String today=df.format(now);// new Date()为获取当前系统时间
			
		Connection conn = MyDBUtil.getConnection();
		
		String sql = "insert into event (uid, time, event, type, note, did) values(?,?,?,?,?,?)";
		PreparedStatement pst = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
		
//		System.out.println("Prepared!!");
		
		pst.setInt(1, userid);
		pst.setString(2, today);
		pst.setString(3, log);
		pst.setString(4, type);
		pst.setString(5, note);
		pst.setInt(6, did);
		
		pst.execute();		
		
		conn.close();
							
		System.out.println("execute!!");
	}
	
	
	
}






