package hive.fire.biz;

import hive.fire.bean.User;
import hive.fire.dao.fireDao;

import java.net.Socket;
import java.sql.SQLException;

import org.apache.log4j.Logger;

/**
 * 监控平台业务类
 */

public class fireService {
	
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(fireService.class);
	
	private static Socket socket=null;
	
	private fireDao dao = new fireDao();
	
	public Socket getDynamicSeivice(){
		try {
			if (socket==null){
				socket = new Socket("127.0.0.1", 4505);
			}
			else if (socket.isClosed()){				
				socket = new Socket("127.0.0.1", 4505);
			}
		} catch (Exception e) {
			logger.error(e,e);
			
			socket = null;			
		}			
		
	/*	
		
		try {
			socket = new Socket("127.0.0.1", 4505);
		} catch (Exception e) {
			logger.error(e,e);
			
			socket = null;			
		}
*/
		return socket;
	}
	
	public void closeSocket(){
		try {
			if (socket!=null){
				socket.close();;
			}
		} catch (Exception e) {
			logger.error(e,e);
			
			socket = null;			
		}
		
		socket = null;
	}
	
	public User getUserByName(String name){
		User user = null;
		try{
			user = dao.getUserByName(name);
		} catch (SQLException e){
			logger.error(e, e);
		}
		return user;
	}
	
	public void logging(int userid, int did, String type, String log, String note){
		try{
			dao.logging(userid, did, type, log, note);
		} catch (SQLException e){
			logger.error(e, e);
		}
	}
}
