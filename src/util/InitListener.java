package util;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;    
import javax.servlet.ServletContextListener;  

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

public class InitListener implements ServletContextListener {
	private static final Logger logger = Logger.getLogger(InitListener.class);
	
	public static int PORT = 4517;
	public static DatagramSocket dataSocket;

	public void contextDestroyed(ServletContextEvent sce) {  
		
		if (!dataSocket.isClosed())
			dataSocket.close();
		dataSocket = null;
		
        System.out.println("web exit ... ");    
    }    
    
    public void contextInitialized(ServletContextEvent sce) {    
        System.out.println("web init ... ");    
        //系统的初始化工作    
        // ...   
        try {
            // 指定端口号，避免与其他应用程序发生冲突
        	dataSocket = new DatagramSocket(PORT);
        	
        	System.out.println("The datagram socket is ok."); 
        } catch (SocketException se) {
            se.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
        
        ServletContext sct=sce.getServletContext();   
        String url=sct.getInitParameter("url")+"?useUnicode=true&characterEncoding=utf8&user=hive&password=123123";
        System.out.println(url);
        
        try {
			initDatabase(url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }   
    
    private void initDatabase(String url) throws Exception {
    	Connection conn = MyDBUtil.getConnection(url);
		
		System.out.println("The mysql is connected!");
		
  /*
		
		try {
			String sql="CREATE TABLE FINGERPRINT ("
                    //不需要   +  "ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                    +  "SAMPLING INTEGER,"				//采集指纹的人
                    +  "PID VARCHAR (50),"				// position id
                    +  "BSSID VARCHAR (50),"            // The address of the access point.
                    +  "SSID VARCHAR (50),"				// The network name.
                    +  "FREQ REAL,"						// The frequency in MHz of the channel over which the client is communicating with the access point.
                    +  "LEVEL REAL,"					// The detected signal level in dBm.
                    +  "STIME VARCHAR (50),"			//采样时间
                    +  "NOTE VARCHAR (50)"
                    +  ");";
			Statement ps=conn.createStatement();   
			ps.executeUpdate(sql);
			
		} catch (Exception e) {
			logger.error(e,e);
		}*/
		

    }
}
