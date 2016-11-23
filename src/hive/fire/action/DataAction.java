package hive.fire.action;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Vector;
import java.util.Date;
import java.util.HashMap;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import sms.DingdongCloudApis;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

import hive.fire.bean.User;
import hive.fire.biz.fireService;
import util.InitListener;
import util.MyDBUtil;
import ws.chat.ChatAnnotation;

public class DataAction extends ActionSupport {
	
	private fireService service = new fireService();

	//对应供电公司
	public String getCompanyTree() throws Exception {
		
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		
		response.setContentType("text/html;charset=UTF-8");
		
		System.out.println("getCompanyTree");
		
		/////////////////////////////////////////////////////////////////
		//// !!!!!!!!!!!!整个JSON的ID值唯一
		/////////////////////////////////////////////////////////////////
		
		
		StringBuffer json = new StringBuffer();
		json.append("{identifier:'id',");
		json.append("label:'description',");
		json.append("items:[{'id':'company"+0+"','description':'"+"全部"+"','idx':'"+0+"','style':'"+"style"+"'},");
		
		Connection conn = MyDBUtil.getConnection();
		Statement st = conn.createStatement();
		String sql="select ID,NAME,TYPE from company order by ID";
		ResultSet rs = st.executeQuery(sql);
		while(rs.next()){
			String id=rs.getString(1);
            String name=rs.getString(2);
            String style=rs.getString(3);
            
            String obj="{'id':'company"+id+"','description':'"+name+"','idx':'"+id+"','style':'"+style+"'},";
                 
            json.append(obj);
		}
		rs.close();
		st.close();
		conn.close();
		
		json.replace(json.length()-1,json.length(),"]");
        json.append("}");
		
		response.getWriter().write(json.toString());
		
			System.out.println(json.toString());
	
		return null;
	}
	
	public String addCompany() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		
		//int unitId = Integer.parseInt(request.getParameter("unitid"));
		//	System.out.println("addPerson");
		//String name=request.getParameter("name");	
		//String note=request.getParameter("note");	
		
		//System.out.println(name);
		
		Connection conn = MyDBUtil.getConnection();
		

		String sql = "insert into company (name, note) values(?,?)";
		PreparedStatement pst = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
		
//		System.out.println("Prepared!!");
		
//		pst.setInt(1, -1);
		pst.setString(1, "11");
		pst.setString(2, "");
		
		try {		
			pst.execute();		
		} catch (Exception e) {
			System.out.println(e.toString());
//			logger.error(e,e);		
			
			conn.close();
			
			boolean isSuccess = false;
			response.setContentType("text/json;charset=UTF-8");
			response.getWriter().write("{success:"+isSuccess+"}");
			
			return null;
		}
				
		System.out.println("execute!!");
		
		ResultSet rs = pst.getGeneratedKeys();
		rs.next();
		int id = rs.getInt(1);//"ID");
		rs.close();
		pst.close();	
				
		String name="公司"+id;
		
		sql = "update company set name='"+name+"' where id="+id;
		Statement ps=conn.createStatement();   
		ps.executeUpdate(sql);
		ps.close();
		
		conn.close();
		
		boolean isSuccess = true;
		if(id < 1){
			isSuccess = false;
		}
		response.setContentType("text/json;charset=UTF-8");
		response.getWriter().write("{success:"+isSuccess+",id:"+id+",name:'"+name+"'}");
		
		return null;
	}
	
	public String deleteCompanies() throws Exception {
		
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		
//		logger.debug(">>>deleteLines");
		String ids = request.getParameter("ids");
		//sql=java.net.URLDecoder.decode(sql, "UTF-8");  
		
		System.out.println(ids);
		
		String result = "{success:true}";
		
		Connection conn = MyDBUtil.getConnection();
		
		try {	
			
			String ids_[]=ids.split(",");
			for (int i=0; i<ids_.length; i++){			
				Statement ps=conn.createStatement();   
				String sql="delete from company where id="+ids_[i];
				ps.executeUpdate(sql);
				ps.close();
			}
		
		} catch (Exception e) {
			result = "{success:false}";
//			logger.error(e,e);			
		}
		
		conn.close();;

		response.setContentType("text/json;charset=UTF-8");		
//		logger.debug("result="+result);
		response.getWriter().write(result);
//		logger.debug("<<<deleteLines");
		
		return null;
	}
	
	public String getCompanies() throws Exception {
		
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		
		System.out.println("getCompanies");
				
		response.setContentType("text/html;charset=UTF-8");
		
		/////////////////////////////////////////////////////////////////
		//// !!!!!!!!!!!!整个JSON的ID值唯一
		/////////////////////////////////////////////////////////////////
						
		StringBuffer json = new StringBuffer();
		json.append("{\"identifier\":\"id\",");
		json.append("\"label\":\"name\",");
		json.append("\"items\":[");
		
		Connection conn = MyDBUtil.getConnection();
		Statement st = conn.createStatement();
		String sql="select id, name, note from company order by id";
		
		ResultSet rs = st.executeQuery(sql);
		
		System.out.println(sql);
		
		int row=0;
		while(rs.next()){
			row++;
			
			String id="";
			if (rs.getString(1)!=null) id = rs.getString(1);
			String name="";
            if (rs.getString(2)!=null) name = rs.getString(2);
            String note="";
            if (rs.getString(3)!=null) note = rs.getString(3);
            
            String obj="{\"rowId\":"+id+",\"id\":"+id+",\"number\":"+row+",\"name\":\""+name+"\",\"note\":\""+note+"\"}";
            if (row>1)
            	obj=",{\"rowId\":"+id+",\"id\":"+id+",\"number\":"+row+",\"name\":\""+name+"\",\"note\":\""+note+"\"}";
            json.append(obj);
            
  		}
		rs.close();
		st.close();
		conn.close();
		
//		if (row>1)
//			json.replace(json.length()-1,json.length(),"]");
//		else
			json.append("]");
        json.append("}");       
		
		response.getWriter().write(json.toString());
		
		System.out.println(json.toString());
	
		return null;
	}
	
	public String addDevice() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		
		//int unitId = Integer.parseInt(request.getParameter("unitid"));
		//	System.out.println("addPerson");
		//String name=request.getParameter("name");	
		//String note=request.getParameter("note");	
		
		String cid = request.getParameter("cid");
		
		//System.out.println(name);
		
		Connection conn = MyDBUtil.getConnection();
		

		String sql = "insert into device (name, cid, note,run,fire) values(?,?,?,?,?)";
		PreparedStatement pst = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
		
//		System.out.println("Prepared!!");
		
//		pst.setInt(1, -1);
		pst.setString(1, "11");
		pst.setString(2, cid);
		pst.setString(3, "");
		pst.setInt(4, 0);
		pst.setInt(5, 0);
		
		try {		
			pst.execute();		
		} catch (Exception e) {
			System.out.println(e.toString());
//			logger.error(e,e);		
			
			conn.close();
			
			boolean isSuccess = false;
			response.setContentType("text/json;charset=UTF-8");
			response.getWriter().write("{success:"+isSuccess+"}");
			
			return null;
		}
				
		System.out.println("execute!!");
		
		ResultSet rs = pst.getGeneratedKeys();
		rs.next();
		int id = rs.getInt(1);//"ID");
		rs.close();
		pst.close();	
				
		String name="设备"+id;
		
		sql = "update device set name='"+name+"' where id="+id;
		Statement ps=conn.createStatement();   
		ps.executeUpdate(sql);
		ps.close();
		
		String company="";
		Statement st1 = conn.createStatement();
		sql = "select name from company where id="+cid;
		
		ResultSet rs1 = st1.executeQuery(sql);
		
		if (rs1.next()){
			if (rs1.getString(1)!=null) company = rs1.getString(1);
  		}
		rs1.close();
		st1.close();
				
		conn.close();
		
		boolean isSuccess = true;
		if(id < 1){
			isSuccess = false;
		}
		response.setContentType("text/json;charset=UTF-8");
		response.getWriter().write("{success:"+isSuccess+",id:"+id+",company:'"+company+"',name:'"+name+"'}");
		
		return null;
	}
	
	public String deleteDevices() throws Exception {
		
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		
//		logger.debug(">>>deleteLines");
		String ids = request.getParameter("ids");
		//sql=java.net.URLDecoder.decode(sql, "UTF-8");  
		
		System.out.println(ids);
		
		String result = "{success:true}";
		
		Connection conn = MyDBUtil.getConnection();
		
		try {	
			
			String ids_[]=ids.split(",");
			for (int i=0; i<ids_.length; i++){			
				Statement ps=conn.createStatement();   
				String sql="delete from device where id="+ids_[i];
				ps.executeUpdate(sql);
				ps.close();
			}
		
		} catch (Exception e) {
			result = "{success:false}";
//			logger.error(e,e);			
		}
		
		conn.close();;

		response.setContentType("text/json;charset=UTF-8");		
//		logger.debug("result="+result);
		response.getWriter().write(result);
//		logger.debug("<<<deleteLines");
		
		return null;
	}
	
	public String getDevices() throws Exception {
		
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		
		System.out.println("getDevices");
		
		String cid = request.getParameter("cid");
		
		response.setContentType("text/html;charset=UTF-8");
		
		/////////////////////////////////////////////////////////////////
		//// !!!!!!!!!!!!整个JSON的ID值唯一
		/////////////////////////////////////////////////////////////////
		
		Date now = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		//SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
		String today=df.format(now);// new Date()为获取当前系统时间
				
		StringBuffer json = new StringBuffer();
		json.append("{\"identifier\":\"id\",");
		json.append("\"label\":\"name\",");
		json.append("\"items\":[");
		
		Connection conn = MyDBUtil.getConnection();
		Statement st = conn.createStatement();
		String sql="select device.id, company.name,device.name, device.run, device.fire, device.type, device.deviceid, device.towerid, device._com, device.note, company.id,device.sms_mobiles  from company, device where device.cid=company.id and company.id='"+cid+"' order by device.id";
		if (Integer.parseInt(cid)<1)
			sql="select device.id, company.name,device.name, device.run, device.fire, device.type, device.deviceid, device.towerid, device._com, device.note, company.id ,device.sms_mobiles from company, device where device.cid=company.id order by device.id";
		
		ResultSet rs = st.executeQuery(sql);
		
		System.out.println(sql);
		
		int row=0;
		while(rs.next()){
			row++;
			
			String id="";
			if (rs.getString(1)!=null) id = rs.getString(1);
			String company="";
            if (rs.getString(2)!=null) company = rs.getString(2);
            String name="";
            if (rs.getString(3)!=null) name = rs.getString(3);
            String status="";
            if (rs.getInt(4)!=0) status = "正常运行";
            else status = "停止运行";
            String fire="";
            if (rs.getInt(5)!=0) fire = "worning";
            else fire = "ok";
            String type="";
            if (rs.getString(6)!=null) type = rs.getString(6);
            String deviceid="";
            if (rs.getString(7)!=null) deviceid = rs.getString(7);
            String towerid="";
            if (rs.getString(8)!=null) towerid = rs.getString(8);
            String _com="";
            if (rs.getString(9)!=null) _com = rs.getString(9);
            String note="";
            if (rs.getString(10)!=null) note = rs.getString(10);
            if (rs.getString(11)!=null) cid = rs.getString(11);
            String sms_mobiles="";
            if (rs.getString(12)!=null) sms_mobiles = rs.getString(12);
            
            String obj="{\"rowId\":"+id+",\"id\":"+id+",\"cid\":"+cid+",\"number\":"+row+",\"company\":\""+company+"\",\"name\":\""+name+"\",\"status\":\""+status+"\",\"fire\":\""+fire+"\",\"type\":\""+type+"\",\"time\":\""+today+"\",\"radar\":\"\", \"camera\":\"\", \"note\":\""+note+"\",\"com\":\""+_com+"\", \"deviceid\":\""+deviceid+"\",\"towerid\":\""+towerid+"\",\"sms_mobiles\":\""+sms_mobiles+"\"}";
            if (row>1)
            	obj=",{\"rowId\":"+id+",\"id\":"+id+",\"cid\":"+cid+",\"number\":"+row+",\"company\":\""+company+"\",\"name\":\""+name+"\",\"status\":\""+status+"\",\"fire\":\""+fire+"\",\"type\":\""+type+"\",\"time\":\""+today+"\",\"radar\":\"\", \"camera\":\"\", \"note\":\""+note+"\",\"com\":\""+_com+"\", \"deviceid\":\""+deviceid+"\",\"towerid\":\""+towerid+"\",\"sms_mobiles\":\""+sms_mobiles+"\"}";
            
            
            json.append(obj);
            
  		}
		rs.close();
		st.close();
		conn.close();
		
	//	if (row>1)
	//		json.replace(json.length()-1,json.length(),"]");
	//	else
			json.append("]");
        json.append("}");       
		
		response.getWriter().write(json.toString());
		
		System.out.println(json.toString());
	
		return null;
	}
	
	public String getDeviceStatus() throws Exception {
		
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		
		System.out.println("getDeviceStatus");
		
		String device = request.getParameter("device");
		
		response.setContentType("text/html;charset=UTF-8");
		
		/////////////////////////////////////////////////////////////////
		//// !!!!!!!!!!!!整个JSON的ID值唯一
		/////////////////////////////////////////////////////////////////
		
		Date now = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		//SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
		String today=df.format(now);// new Date()为获取当前系统时间
				
		StringBuffer json = new StringBuffer();
		json.append("{items:[");
		
		Connection conn = MyDBUtil.getConnection();
		Statement st = conn.createStatement();
		String sql="select device.id, company.name,device.name, device.run, device.type from company, device where device.cid=company.id and device.fire=1 order by device.id";
		if (device.equals("normal"))
			sql="select device.id, company.name,device.name, device.run, device.type from company, device where device.cid=company.id and device.run=1 order by device.id";
		if (device.equals("unnormal"))
			sql="select device.id, company.name,device.name, device.run, device.type from company, device where device.cid=company.id and device.run<>1 order by device.id";
		
		ResultSet rs = st.executeQuery(sql);
		
		System.out.println(sql);
		
		int row=0;
		while(rs.next()){
			row++;
			
			String id="";
			if (rs.getString(1)!=null) id = rs.getString(1);
			String company="";
            if (rs.getString(2)!=null) company = rs.getString(2);
            String name="";
            if (rs.getString(3)!=null) name = rs.getString(3);
            String status="";
            if (rs.getInt(4)!=0) status = "正常运行";
            else status = "停止运行";
            String type="";
            if (rs.getString(5)!=null) type = rs.getString(5);
            
            String obj="{rowId:"+id+",id:"+id+",number:"+row+",company:'"+company+"',name:'"+name+"',status:'"+status+"',type:'"+type+"',time:'"+today+"',radar:'', camera:'', note:''},";
            
            json.append(obj);
            
  		}
		rs.close();
		st.close();
		conn.close();
		
		if (row>1)
			json.replace(json.length()-1,json.length(),"]");
		else
			json.append("]");
        json.append("}");       
		
		response.getWriter().write(json.toString());
		
		System.out.println(json.toString());
	
		return null;
	}
	
	public String getDeviceStatus_() throws Exception {
		
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		
		System.out.println("getDeviceStatus");
		
		String device = request.getParameter("device");
		String cid = request.getParameter("cid");
		
		response.setContentType("text/html;charset=UTF-8");
		
		/////////////////////////////////////////////////////////////////
		//// !!!!!!!!!!!!整个JSON的ID值唯一
		/////////////////////////////////////////////////////////////////
		
		Date now = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		//SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
		String today=df.format(now);// new Date()为获取当前系统时间
				
		StringBuffer json = new StringBuffer();
		json.append("{items:[");
		
		Connection conn = MyDBUtil.getConnection();
		Statement st = conn.createStatement();
		String sql="select device.id, company.name,device.name, device.run, device.type from company, device where device.cid=company.id and device.fire=1 and company.id="+cid+" order by device.id";
		if (device.equals("normal"))
			sql="select device.id, company.name,device.name, device.run, device.type from company, device where device.cid=company.id and device.run=1 and company.id="+cid+" order by device.id";
		if (device.equals("unnormal"))
			sql="select device.id, company.name,device.name, device.run, device.type from company, device where device.cid=company.id and device.run<>1 and company.id="+cid+" order by device.id";
		
		ResultSet rs = st.executeQuery(sql);
		
		System.out.println(sql);
		
		int row=0;
		while(rs.next()){
			row++;
			
			String id="";
			if (rs.getString(1)!=null) id = rs.getString(1);
			String company="";
            if (rs.getString(2)!=null) company = rs.getString(2);
            String name="";
            if (rs.getString(3)!=null) name = rs.getString(3);
            String status="";
            if (rs.getInt(4)!=0) status = "正常运行";
            else status = "停止运行";
            String type="";
            if (rs.getString(5)!=null) type = rs.getString(5);
            
            String obj="{rowId:"+id+",id:"+id+",number:"+row+",company:'"+company+"',name:'"+name+"',status:'"+status+"',type:'"+type+"',time:'"+today+"',radar:'', camera:'', note:''},";
            
            json.append(obj);
            
  		}
		rs.close();
		st.close();
		conn.close();
		
		if (row>1)
			json.replace(json.length()-1,json.length(),"]");
		else
			json.append("]");
        json.append("}");       
		
		response.getWriter().write(json.toString());
		
		System.out.println(json.toString());
	
		return null;
	}
	
	public String putDeviceStatus() throws Exception {
		
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		
		System.out.println("putDeviceStatus");
		
		
//		String status = request.getHeader("Status"); 
		
//		System.out.println(status);
		
//		String status = request.getParameter("status");
		
//		System.out.println(status);
		
		
		 request.setCharacterEncoding("UTF-8");
         int size = request.getContentLength();
         System.out.println(size);

         InputStream is = request.getInputStream();
         byte[] reqBodyBytes = readBytes(is, size);
         System.out.println(Arrays.toString(reqBodyBytes));
         String res = new String(reqBodyBytes);
         System.out.println(res);
        // String teststr = "[{fire: '1', id: '1', power: '1'}]";
         JSONArray jsons = JSONArray.fromObject(res );
  //       ChatAnnotation.broadcast(res);
		
		response.setContentType("text/html;charset=UTF-8");
		System.out.println(jsons.size());
		
		Connection conn = MyDBUtil.getConnection();
		
		if(jsons.size()>0){
			  for(int i=0;i<jsons.size();i++){
			    JSONObject job = jsons.getJSONObject(i);  // 遍历 jsonarray 数组，把每一个对象转成 json 对象
			    			    
			    try {		
					Statement ps=conn.createStatement();   
					String sql="update device set run="+job.get("power")+", fire="+job.get("fire")+" where id="+job.get("id");
					ps.executeUpdate(sql);
					ps.close();
				
				} catch (Exception e) {
		
				}
			    
			    int fire=job.getInt("fire");
			    if (fire==1){
			    	String name = "";
			    	Statement st = conn.createStatement();
					String sql="select company.name,device.name,device.sms_mobiles from company, device where device.cid=company.id and device.id="+job.get("id");									
					ResultSet rs = st.executeQuery(sql);
					
					System.out.println(sql);
					//String mobile = "13182813303,13605175556,13770515415,15050527529";
					String mobile = "";
					if(rs.next()){
						String company="";
			            if (rs.getString(1)!=null) company = rs.getString(1);
			            if (rs.getString(2)!=null) name = rs.getString(2);	
			            if (rs.getString(3)!=null) mobile = rs.getString(3);	
			            name = company+name;
			  		}
					rs.close();
					st.close();
			    				    	
			    	SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");//设置日期格式
			    	String strTime=df.format(new Date());// new Date()为获取当前系统时间
			    	
				    String apikey = "4cc12a6dc4747224bc875722e9e6b8dd";
				    
				    /**************** 发送通知短信 *****************/
			        // 设置您要发送的内容
			        String tzContent = "【山火预警设备】通知："+name+"，在"+strTime+"报警，请处理！";
	
			        // 发短信调用示例
			        System.out.println(DingdongCloudApis.sendTz(apikey, mobile, tzContent));
			    }
			    
			    System.out.println("id="+job.get("id")) ;  // 得到 每个对象中的属性值
			  }
			}
		
		conn.close();
		
		response.getWriter().write("{success:true}");
			
		return null;
	}
	
	public String putWarming() throws Exception {
		
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		
		System.out.println("putWarming");
		
		
//		String status = request.getHeader("Status"); 
		
//		System.out.println(status);
		
//		String status = request.getParameter("status");
		
//		System.out.println(status);
		
		
		 request.setCharacterEncoding("UTF-8");
         int size = request.getContentLength();
         System.out.println(size);

         InputStream is = request.getInputStream();

         byte[] reqBodyBytes = readBytes(is, size);

         String res = new String(reqBodyBytes);

         System.out.println(res);
         
         JSONArray jsons = JSONArray.fromObject(res );
         
  //       ChatAnnotation.broadcast(res);
		
		response.setContentType("text/html;charset=UTF-8");
		
		
		Connection conn = MyDBUtil.getConnection();
		
		if(jsons.size()>0){
			  for(int i=0;i<jsons.size();i++){
			    JSONObject job = jsons.getJSONObject(i);  // 遍历 jsonarray 数组，把每一个对象转成 json 对象
			    
			    if (i==0){
			    	try {		
						Statement ps=conn.createStatement();   
						String sql="delete from warming where did="+job.get("id");
						ps.executeUpdate(sql);
						ps.close();
					
					} catch (Exception e) {
			
					}
			    }
			    
			    try {		
			    	String sql = "insert into warming (did, _x, _y, value) values(?,?,?,?)";
					PreparedStatement pst = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
					
//					System.out.println("Prepared!!");
					
//					pst.setInt(1, -1);
					pst.setInt(1, (int)job.get("id"));
					pst.setInt(2, (int)job.get("x"));
					pst.setInt(3, (int)job.get("y"));
					pst.setInt(4, (int)job.get("value"));
					
					pst.execute();		
					
				
				} catch (Exception e) {
		
				}
			    
			    
			    System.out.println("id="+job.get("id")) ;  // 得到 每个对象中的属性值
			  }
			}
		
		conn.close();
		
		response.getWriter().write("{success:true}");
			
		return null;
	}
	
	public String putCleanWarming() throws Exception {
		
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		
		System.out.println("putCleanWarming");
			
		 request.setCharacterEncoding("UTF-8");
         int size = request.getContentLength();
         System.out.println(size);

         InputStream is = request.getInputStream();

         byte[] reqBodyBytes = readBytes(is, size);

         String id = new String(reqBodyBytes);

         System.out.println(id);
         		
		response.setContentType("text/html;charset=UTF-8");
		
		
		Connection conn = MyDBUtil.getConnection();
		
		try {		
			Statement ps=conn.createStatement();   
			String sql="delete from warming where did="+id;
			ps.executeUpdate(sql);
			ps.close();
		
		} catch (Exception e) {

		}
		
		conn.close();
		
		response.getWriter().write("{success:true}");
			
		return null;
	}
	
	public String getMask() throws Exception {
		
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		
		System.out.println("getMask");
		
		String did = request.getParameter("did");
		
		response.setContentType("text/html;charset=UTF-8");
		
		/////////////////////////////////////////////////////////////////
		//// !!!!!!!!!!!!整个JSON的ID值唯一
		/////////////////////////////////////////////////////////////////
		
		Date now = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		//SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
		String today=df.format(now);// new Date()为获取当前系统时间
				
		StringBuffer json = new StringBuffer();
		json.append("{\"identifier\":\"id\",");
		json.append("\"label\":\"id\",");
		json.append("\"items\":[");
		
		Connection conn = MyDBUtil.getConnection();
		Statement st = conn.createStatement();
		String sql="select id, _x,_x_delta, _y, _y_delta, value, bcolor, fcolor from mask where did='"+did+"' order by id";
		
		ResultSet rs = st.executeQuery(sql);
		
		System.out.println(sql);
		
		int row=0;
		while(rs.next()){
			row++;
			
			String id="";
			if (rs.getString(1)!=null) id = rs.getString(1);
			String _x="";
            if (rs.getString(2)!=null) _x = rs.getString(2);
            String _x_delta="";
            if (rs.getString(3)!=null) _x_delta = rs.getString(3);
            String _y="";
            if (rs.getString(4)!=null) _y = rs.getString(4);
            String _y_delta="";
            if (rs.getString(5)!=null) _y_delta = rs.getString(5);
            String value="";
            if (rs.getString(6)!=null) value = rs.getString(6);
            String bcolor="";
            if (rs.getString(7)!=null) bcolor = rs.getString(7);
            String fcolor="";
            if (rs.getString(8)!=null) fcolor = rs.getString(8);
            
            String obj="{\"rowId\":"+id+",\"id\":"+id+",\"did\":"+did+",\"number\":"+row+",\"x\":\""+_x+"\",\"deltax\":\""+_x_delta+"\",\"y\":\""+_y+"\",\"deltay\":\""+_y_delta+"\",\"value\":\""+value+"\",\"bcolor\":\""+bcolor+"\",\"fcolor\":\""+fcolor+"\"}";
            if (row>1)
            	obj=",{\"rowId\":"+id+",\"id\":"+id+",\"did\":"+did+",\"number\":"+row+",\"x\":\""+_x+"\",\"deltax\":\""+_x_delta+"\",\"y\":\""+_y+"\",\"deltay\":\""+_y_delta+"\",\"value\":\""+value+"\",\"bcolor\":\""+bcolor+"\",\"fcolor\":\""+fcolor+"\"}";
            json.append(obj);
            
  		}
		rs.close();
		st.close();
		conn.close();
		
		//if (row>1)
		//	json.replace(json.length()-1,json.length(),"]");
		//else
			json.append("]");
        json.append("}");       
		
		response.getWriter().write(json.toString());
		
		System.out.println(json.toString());
	
		return null;
	}
	
	public String putMask() throws Exception {
		
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		
		System.out.println("putMask");
		
		
//		String status = request.getHeader("Status"); 
		
//		System.out.println(status);
		
//		String status = request.getParameter("status");
		
//		System.out.println(status);
		
		
		 request.setCharacterEncoding("UTF-8");
         int size = request.getContentLength();
         System.out.println(size);

         InputStream is = request.getInputStream();

         byte[] reqBodyBytes = readBytes(is, size);

         String res = new String(reqBodyBytes);

         System.out.println(res);
         
         JSONArray jsons = JSONArray.fromObject(res );
         
  //       ChatAnnotation.broadcast(res);
		
		response.setContentType("text/html;charset=UTF-8");
		
		
		Connection conn = MyDBUtil.getConnection();
		
		if(jsons.size()>0){
			  for(int i=0;i<jsons.size();i++){
			    JSONObject job = jsons.getJSONObject(i);  // 遍历 jsonarray 数组，把每一个对象转成 json 对象
			    
			    if (i==0){
			    	try {		
						Statement ps=conn.createStatement();   
						String sql="delete from mask where did="+job.get("id");
						ps.executeUpdate(sql);
						ps.close();
					
					} catch (Exception e) {
			
					}
			    }
			    
			    try {		
			    	String sql = "insert into mask (did, _x, _x_delta, _y, _y_delta, value, bcolor, fcolor) values(?,?,?,?,?,?,?,?)";
					PreparedStatement pst = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
					
//					System.out.println("Prepared!!");
					
//					pst.setInt(1, -1);
					pst.setInt(1, (int)job.get("id"));
					pst.setInt(2, (int)job.get("x"));
					pst.setInt(3, (int)job.get("deltax"));
					pst.setInt(4, (int)job.get("y"));
					pst.setInt(5, (int)job.get("deltay"));
					pst.setInt(6, (int)job.get("value"));
					pst.setInt(7, (int)job.get("bcolor"));
					pst.setInt(8, (int)job.get("fcolor"));
					
					pst.execute();		
					
				
				} catch (Exception e) {
		
				}
			    
			    
			    System.out.println("id="+job.get("id")) ;  // 得到 每个对象中的属性值
			  }
			}
		
		conn.close();
		
		response.getWriter().write("{success:true}");
			
		return null;
	}
	
	public static final byte[] readBytes(InputStream is, int contentLen) {
        if (contentLen > 0) {
                int readLen = 0;

                int readLengthThisTime = 0;

                byte[] message = new byte[contentLen];

                try {

                        while (readLen != contentLen) {

                                readLengthThisTime = is.read(message, readLen, contentLen
                                                - readLen);

                                if (readLengthThisTime == -1) {// Should not happen.
                                        break;
                                }

                                readLen += readLengthThisTime;
                        }

                        return message;
                } catch (IOException e) {
                        // Ignore
                        // e.printStackTrace();
                }
        }

        return new byte[] {};
	}
	
	public String deviceRadarStart() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
				
		String id = request.getParameter("id");
		String username = request.getParameter("user");
		
		System.out.println("deviceRadarStart");
		
		boolean isSuccess = true;
		
		try {
            // 指定端口号，避免与其他应用程序发生冲突

			byte[] sendDataByte = new byte[1024];
            String sendStr = "{\"function\":\"radar start\",\"id\":"+id+"}";
            sendDataByte = sendStr.getBytes();
            DatagramPacket dataPacket = new DatagramPacket(sendDataByte, sendDataByte.length,
                    InetAddress.getByName("127.0.0.1"), InitListener.PORT-1);
            InitListener.dataSocket.send(dataPacket);
            
            
            User user=service.getUserByName(username);
    		if(user!=null)
    		{
    			service.logging(user.getId(), Integer.parseInt(id), "device", "通电操作", username);

    		}

                      
        } catch (SocketException se) {
        	isSuccess = false;
            se.printStackTrace();
        } catch (IOException ie) {
        	isSuccess = false;
            ie.printStackTrace();
        }
		
		response.setContentType("text/json;charset=UTF-8");
		response.getWriter().write("{success:"+isSuccess+"}");
		
		return null;
	}
	
	public String deviceRadarStop() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		
		//int unitId = Integer.parseInt(request.getParameter("unitid"));
		String id = request.getParameter("id");
		String username = request.getParameter("user");
		
		System.out.println("deviceRadarStop");
		
		boolean isSuccess = true;
		
		try {
            // 指定端口号，避免与其他应用程序发生冲突

			byte[] sendDataByte = new byte[1024];
            String sendStr = "{\"function\":\"radar stop\",\"id\":"+id+"}";
            sendDataByte = sendStr.getBytes();
            DatagramPacket dataPacket = new DatagramPacket(sendDataByte, sendDataByte.length,
                    InetAddress.getByName("127.0.0.1"), InitListener.PORT-1);
            InitListener.dataSocket.send(dataPacket);
            
            User user=service.getUserByName(username);
    		if(user!=null)
    		{
    			service.logging(user.getId(), Integer.parseInt(id), "device", "断电操作", username);

    		}
            
            
            
        } catch (SocketException se) {
        	isSuccess = false;
            se.printStackTrace();
        } catch (IOException ie) {
        	isSuccess = false;
            ie.printStackTrace();
        }
		
		response.setContentType("text/json;charset=UTF-8");
		response.getWriter().write("{success:"+isSuccess+"}");
		
		return null;
	}
	
	public String deviceClearFireAlarm() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
				
		String id = request.getParameter("id");
		String username = request.getParameter("user");
		
		System.out.println("deviceClearFireAlarm");
		
		boolean isSuccess = true;
		
		try {
            // 指定端口号，避免与其他应用程序发生冲突

			byte[] sendDataByte = new byte[1024];
            String sendStr = "{\"function\":\"clear fire alarm\",\"id\":"+id+"}";
            sendDataByte = sendStr.getBytes();
            DatagramPacket dataPacket = new DatagramPacket(sendDataByte, sendDataByte.length,
                    InetAddress.getByName("127.0.0.1"), InitListener.PORT-1);
            InitListener.dataSocket.send(dataPacket);
            
            
            User user=service.getUserByName(username);
    		if(user!=null)
    		{
    			service.logging(user.getId(), Integer.parseInt(id), "device", "清除火警", username);

    		}

                      
        } catch (SocketException se) {
        	isSuccess = false;
            se.printStackTrace();
        } catch (IOException ie) {
        	isSuccess = false;
            ie.printStackTrace();
        }
		
		response.setContentType("text/json;charset=UTF-8");
		response.getWriter().write("{success:"+isSuccess+"}");
		
		return null;
	}
	
	public String fireAlarm() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		
		request.setCharacterEncoding("UTF-8");
        int size = request.getContentLength();
        System.out.println(size);

        InputStream is = request.getInputStream();

        byte[] reqBodyBytes = readBytes(is, size);

        String res = new String(reqBodyBytes);

        System.out.println(res);
         
        JSONObject obj = JSONObject.fromObject(res); 
        
        response.setContentType("text/html;charset=UTF-8");
        
        if (obj.getString("fire").equals("fire"))
        {
        	service.logging(0, obj.getInt("did"), "fire", obj.getString("name")+"火警", obj.getString("name"));
 			response.getWriter().write("{success:true}");
        }else{
        	Connection conn = MyDBUtil.getConnection();
    		
    		try {		
    			Statement ps=conn.createStatement();   
    			String sql="delete from warming where did="+obj.getInt("did");
    			ps.executeUpdate(sql);
    			ps.close();
    		
    		} catch (Exception e) {

    		}
    		
    		conn.close();
    		
	        User user=service.getUserByName(obj.getString("user"));
	 		if(user!=null)
	 		{
	 			service.logging(user.getId(), obj.getInt("did"), "fire", obj.getString("name")+"火警清除", "操作员"+obj.getString("user"));
	 			response.getWriter().write("{success:true}");
	 		}
	 		else response.getWriter().write("{success:false}"); 
 		}

		return null;

	}
	
	public String addUser() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		
		//int unitId = Integer.parseInt(request.getParameter("unitid"));
		//	System.out.println("addPerson");
		//String name=request.getParameter("name");	
		//String note=request.getParameter("note");	
		
		String cid = request.getParameter("cid");
		
		//System.out.println(name);
		
		Connection conn = MyDBUtil.getConnection();
		

		String sql = "insert into user (user, cid, password, permission, note) values(?,?,?,?,?)";
		PreparedStatement pst = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
		
//		System.out.println("Prepared!!");
		
//		pst.setInt(1, -1);
		pst.setString(1, "11");
		pst.setString(2, cid);
		pst.setString(3, "1234554321");
		pst.setString(4, "虚拟用户");
		pst.setString(5, "");
		
		try {		
			pst.execute();		
		} catch (Exception e) {
			System.out.println(e.toString());
//			logger.error(e,e);		
			
			conn.close();
			
			boolean isSuccess = false;
			response.setContentType("text/json;charset=UTF-8");
			response.getWriter().write("{success:"+isSuccess+"}");
			
			return null;
		}
				
		System.out.println("execute!!");
		
		ResultSet rs = pst.getGeneratedKeys();
		rs.next();
		int id = rs.getInt(1);//"ID");
		rs.close();
		pst.close();	
				
		String user="用户"+id;
		
		sql = "update user set user='"+user+"' where id="+id;
		Statement ps=conn.createStatement();   
		ps.executeUpdate(sql);
		ps.close();
		
		conn.close();
		
		boolean isSuccess = true;
		if(id < 1){
			isSuccess = false;
		}
		response.setContentType("text/json;charset=UTF-8");
		response.getWriter().write("{success:"+isSuccess+",id:"+id+",user:'"+user+"'}");
		
		return null;
	}
	
	public String getUsers() throws Exception {
		
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		
		System.out.println("getUsers");
		
		String cid = request.getParameter("cid");
		
		response.setContentType("text/html;charset=UTF-8");
		
		/////////////////////////////////////////////////////////////////
		//// !!!!!!!!!!!!整个JSON的ID值唯一
		/////////////////////////////////////////////////////////////////
				
		StringBuffer json = new StringBuffer();
		json.append("{\"identifier\":\"id\",");
		json.append("\"label\":\"name\",");
		json.append("\"items\":[");
		
		Connection conn = MyDBUtil.getConnection();
		Statement st = conn.createStatement();
		String sql="select id, user, name, password, mobile, permission, note from user where cid='"+cid+"' order by id";
		if (Integer.parseInt(cid)<1)
			sql="select id, user, name, password, mobile, permission, note from user order by id";
		
		ResultSet rs = st.executeQuery(sql);
		
		System.out.println(sql);
		
		int row=0;
		while(rs.next()){
			row++;
			
			String id="";
			if (rs.getString(1)!=null) id = rs.getString(1);
			String user="";
            if (rs.getString(2)!=null) user = rs.getString(2);
            String name="";
            if (rs.getString(3)!=null) name = rs.getString(3);
            String password="";
            if (rs.getString(4)!=null) password = rs.getString(4);
            String mobile="";
            if (rs.getString(5)!=null) mobile = rs.getString(5);
            String permission="";
            if (rs.getString(6)!=null) permission = rs.getString(6);
            String note="";
            if (rs.getString(7)!=null) note = rs.getString(7);
            
            String obj="{\"rowId\":"+id+",\"id\":"+id+",\"number\":"+row+",\"user\":\""+user+"\",\"name\":\""+name+"\",\"password\":\""+password+"\",\"mobile\":\""+mobile+"\",\"permission\":\""+permission+"\",\"note\":\""+note+"\",\"radar\":\"\", \"camera\":\"\"}";
            if (row>1)
            	obj=",{\"rowId\":"+id+",\"id\":"+id+",\"number\":"+row+",\"user\":\""+user+"\",\"name\":\""+name+"\",\"password\":\""+password+"\",\"mobile\":\""+mobile+"\",\"permission\":\""+permission+"\",\"note\":\""+note+"\",\"radar\":\"\", \"camera\":\"\"}";
            
            json.append(obj);
            
  		}
		rs.close();
		st.close();
		conn.close();
		
	//	if (row>1)
	//		json.replace(json.length()-1,json.length(),"]");
	//	else
			json.append("]");
        json.append("}");       
		
		response.getWriter().write(json.toString());
		
		System.out.println(json.toString());
	
		return null;
	}
		
	public String getJsonUsers() throws Exception {
		
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		
//		System.out.println("getJsonStations");
		
		response.setContentType("text/html;charset=UTF-8");
		
		/////////////////////////////////////////////////////////////////
		//// !!!!!!!!!!!!整个JSON的ID值唯一
		/////////////////////////////////////////////////////////////////
				
		StringBuffer json = new StringBuffer();
		json.append("{items:[");
		
		Connection conn = MyDBUtil.getConnection();
		Statement st = conn.createStatement();
		String sql="select UserID,LoginName,UserName,TYPE,BT_MAC from DMS_User order by UserID";
		ResultSet rs = st.executeQuery(sql);
		int row=1;
		while(rs.next()){
			String id=rs.getString(1);
			String login=rs.getString(2);
            String name=rs.getString(3);
            String type=rs.getString(4);
            String addr=rs.getString(5);
            
            String obj="{id:"+id+",login:'"+login+"',name:'"+name+"',type:'"+type+"',addr:'"+addr+"'}";
            if (row>1)
            	obj=",{id:"+id+",login:'"+login+"',name:'"+name+"',type:'"+type+"',addr:'"+addr+"'}";
            	
            json.append(obj);
            
            row++;
		}
		rs.close();
		st.close();
		conn.close();
		
	//	if (row>1)
	//		json.replace(json.length()-1,json.length(),"]");
	//	else
			json.append("]");
        json.append("}");
		
		response.getWriter().write(json.toString());
		
//		System.out.println(json.toString());
	
		return null;
	}
	
	public String getCompanyId() throws Exception {
		
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		
		System.out.println("getCompanyId");
		
		String user = request.getParameter("user");
		
		response.setContentType("text/html;charset=UTF-8");
		
		int id=0;
		
		Connection conn = MyDBUtil.getConnection();
		Statement st = conn.createStatement();
		String sql="select cid from User where User='"+user+"'";
		ResultSet rs = st.executeQuery(sql);
		if(rs.next()){
			id=rs.getInt(1);
		}
		rs.close();
		st.close();
		conn.close();
		
		boolean isSuccess = true;
		if(id < 1){
			isSuccess = false;
		}
		response.setContentType("text/json;charset=UTF-8");
		response.getWriter().write("{success:"+isSuccess+",id:"+id+",user:'"+user+"'}");
		
//		System.out.println(json.toString());
	
		return null;
	}
	
	
	//事件记录
	public String getEvents() throws Exception {
		
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		
//		System.out.println("getJsonUnits");
		
		String cid = request.getParameter("cid");
		String type = request.getParameter("type");
		String startdate = request.getParameter("startdate");
		String enddate= request.getParameter("enddate");
		
		System.out.println(startdate);
		System.out.println(enddate);
		
		response.setContentType("text/html;charset=UTF-8");
		
		/////////////////////////////////////////////////////////////////
		//// !!!!!!!!!!!!整个JSON的ID值唯一
		/////////////////////////////////////////////////////////////////
				
		StringBuffer json = new StringBuffer();
		json.append("{items:[");
		
		Connection conn = MyDBUtil.getConnection();
		
		HashMap companies = new HashMap(); 
		Statement st = conn.createStatement();
		String sql="select id,name from company order by id";
		ResultSet rs = st.executeQuery(sql);
		while (rs.next()){
			if (rs.getString(1)!=null&&rs.getString(2)!=null){
				String name=rs.getString(2);
				if (!name.equals("")){
					companies.put(rs.getString(1), name);   
				}
			}           
		}
		rs.close();
		st.close();
		
		int row=1;
		
		if (type.equals("user")){
		
			st = conn.createStatement();
			
			sql = "select event.id, user.cid,user.name, event.time,event.event,event.note from user, event where event.type='user' and event.time>='"+startdate+" 00:00:00' and event.time<='"+enddate+" 23:59:59' and event.uid=user.id order by event.id";
	
			rs = st.executeQuery(sql);
			
			while(rs.next()){
				String id=rs.getString(1);
				String company=rs.getString(2);
				
				if (Integer.parseInt(cid)>0&&(!cid.equals(company)))
					continue;
				
				company = (String)companies.get(company);
				String name=rs.getString(3);
	            String time=rs.getString(4);
	            String event=rs.getString(5);
	            String note=rs.getString(6);
	            String value=rs.getString(6);
	            
	            String obj="{id:"+row+",number:"+row+",company:'"+company+"',time:'"+time+"',event:'"+event+"',note:'"+note+"',value:'"+value+"'},";
	            
	            json.append(obj);
	            
	            row++;
			}
			rs.close();
			st.close();
		
		}
		
		if (type.equals("device")){
			
			st = conn.createStatement();
			
			sql = "select event.id, device.cid,device.name, event.time,event.event,event.note from device, event where event.type='device' and event.time>='"+startdate+" 00:00:00' and event.time<='"+enddate+" 23:59:59' and event.did=device.id order by event.id";
	
			rs = st.executeQuery(sql);
			
			while(rs.next()){
				String id=rs.getString(1);
				String company=rs.getString(2);
				
				if (Integer.parseInt(cid)>0&&(!cid.equals(company)))
					continue;
				
				company = (String)companies.get(company);
				String name=rs.getString(3);
	            String time=rs.getString(4);
	            String event=rs.getString(5);
	            String note=rs.getString(6);
	            String value=rs.getString(6);
	            
	            String obj="{id:"+row+",number:"+row+",company:'"+company+"',time:'"+time+"',event:'"+name+event+"',note:'操作员 "+note+"',value:'"+value+"'},";
	            
	            json.append(obj);
	            
	            row++;
			}
			rs.close();
			st.close();
		
		}
		
		if (type.equals("fire")){
			
			st = conn.createStatement();
			
			sql = "select event.id, device.cid,device.name, event.time,event.event,event.note from device, event where event.type='fire' and event.time>='"+startdate+" 00:00:00' and event.time<='"+enddate+" 23:59:59' and event.did=device.id order by event.id";
	
			rs = st.executeQuery(sql);
			
			while(rs.next()){
				String id=rs.getString(1);
				String company=rs.getString(2);
				
				if (Integer.parseInt(cid)>0&&(!cid.equals(company)))
					continue;
				
				company = (String)companies.get(company);
				String name=rs.getString(3);
	            String time=rs.getString(4);
	            String event=rs.getString(5);
	            String note=rs.getString(6);
	            String value=rs.getString(6);
	            
	            String obj="{id:"+row+",number:"+row+",company:'"+company+"',time:'"+time+"',event:'"+event+"',note:'"+note+"',value:'"+value+"'},";
	            
	            json.append(obj);
	            
	            row++;
			}
			rs.close();
			st.close();
		
		}
		
		conn.close();
		
		if (row>1)
			json.replace(json.length()-1,json.length(),"]");
		else
			json.append("]");
        json.append("}");
		
		response.getWriter().write(json.toString());
		
		System.out.println(json.toString());
	
		return null;
	}
	
	/**
	* 执行SQL语句
	* @param con
	* @param SQL语句
	* @throws SQLException
	*/
	public String executeSQL() throws Exception {
		
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		
//		logger.debug(">>>executeSQL");
		String sql = request.getParameter("sql");
		//sql=java.net.URLDecoder.decode(sql, "UTF-8");  
		
		System.out.println(sql);
		
		String result = "{success:true}";
		
		Connection conn = MyDBUtil.getConnection();
		
		try {		
			Statement ps=conn.createStatement();   
			ps.executeUpdate(sql);
			ps.close();
		
		} catch (Exception e) {
			result = "{success:false}";
//			logger.error(e,e);			
		}
		conn.close();;
		
		response.setContentType("text/json;charset=UTF-8");		
//		logger.debug("result="+result);
		response.getWriter().write(result);
//		logger.debug("<<<executeSQL");
		
		return null;
	}
	
	public String updateUserPassword() throws Exception {
		
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		
//		logger.debug(">>>updateUserPassword");
		String user = request.getParameter("user");
		String oldpwd = request.getParameter("oldpwd");
		String newpwd = request.getParameter("newpwd").trim();
		String repwd = request.getParameter("repwd");
		//sql=java.net.URLDecoder.decode(sql, "UTF-8");  
		
		System.out.println(user);
		
		String result = "{success:true}";
		
		if (newpwd.equals("")) result = "{success:false, why:'the new is empty'}";
		else if (!newpwd.equals(repwd)) 
			result = "{success:false, why:'is not the same'}";
		else {

			Connection conn = MyDBUtil.getConnection();
			
			Statement st = conn.createStatement();
			String sql="select id,Password from user where user='"+user+"'";
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()){
				String pwd="";
				if (rs.getString(2)!=null) pwd = rs.getString(2);
	            if (!oldpwd.equals(pwd)) 
	    			result = "{success:false, why:'is wrong'}";
	            else {
	            	Statement ps=conn.createStatement();   
	    			ps.executeUpdate("update user set password='"+newpwd+"' where user='"+user+"'");
	    			ps.close();
	            }
			} else {
				result = "{success:false, why:'is not exist'}";
			}
			
			rs.close();
			st.close();
			
			conn.close();
		}

		response.setContentType("text/json;charset=UTF-8");		
//		logger.debug("result="+result);
		response.getWriter().write(result);
//		logger.debug("<<<updateUserPassword");
		
		return null;
	}
	
	public String logoff() throws Exception {
		
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		
//		logger.debug(">>>logoff");
		String username = request.getParameter("user");
		
		User user=service.getUserByName(username);
		if(user==null)
		{
			System.out.println("error");
			
			service.logging(0, 0, "user", "用户不存在", "");
			
			return "error";
		}
		else
		{
			service.logging(user.getId(), 0, "user", "用户注销", "");

		}

		response.setContentType("text/json;charset=UTF-8");	
		
		String result = "{success:true}";
		response.getWriter().write(result);
//		logger.debug("<<<logoff");
		
		return null;
	}
	
	public String updateScanImage() throws Exception {
		
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		
//		logger.debug(">>>updateScanImage");
		String id = request.getParameter("id");
		String path = request.getParameter("path");
		
		System.out.println(path);
		
		String result = "{success:true}";
		
		Connection conn = MyDBUtil.getConnection();
		
		try {
			InputStream in=new FileInputStream(path);
			PreparedStatement  ps=conn.prepareStatement("update device set scan_image=? where id=?");
			ps.setBinaryStream(1, in, in.available());
			ps.setInt(2,Integer.parseInt(id));			
			ps.executeUpdate();
			in.close();
		} catch (Exception e) {
			result = "{success:false}";
			System.out.println(e.toString());
//			logger.error(e,e);			
		}
		
		conn.close();

		response.setContentType("text/json;charset=UTF-8");		
//		logger.debug("result="+result);
		response.getWriter().write(result);
//		logger.debug("<<<updateScanImage");
		
		return null;
	}
	
	private ByteArrayOutputStream mergeImage(InputStream top, InputStream bottom) throws IOException {

		//创建BufferedImage
		BufferedImage t=ImageIO.read(top);
		BufferedImage b=ImageIO.read(bottom);
				
		int type = t.getType();
		int width = 720;	//t.getWidth();
		int height = 720;	//t.getHeight(); 

		//设置拼接后图的大小和类型
		BufferedImage finalImg = new BufferedImage(width, height, type);

		//写入图像内容
		finalImg.createGraphics().drawImage(t, width*0, height*0, null);
		finalImg.createGraphics().drawImage(b, width*0, (int)(height*0.5), null);
		
		//输出拼接后的图像
		ByteArrayOutputStream out=new ByteArrayOutputStream();
		ImageIO.write(finalImg, "jpeg", out);

		return out;
	}

	
	public String getScanImage() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		
		String id=request.getParameter("id");	
		
		byte data[] = {0x00, 0x00};
		response.setContentType("image/"+"jpg"); //设置返回的文件类型     
		OutputStream os = response.getOutputStream();    

		Connection conn = MyDBUtil.getConnection();
		
		Statement stmt = conn.createStatement();  
	    ResultSet rs = stmt.executeQuery("select scan_image_up, scan_image_down from device where id = "+id);  
      	if (rs.next()){  
      		Blob blob = rs.getBlob(1);  
		    InputStream t = blob.getBinaryStream();
		    Blob blob2 = rs.getBlob(2);  
		    InputStream b = blob2.getBinaryStream(); 
		    ByteArrayOutputStream out=mergeImage(t, b);
		    data = out.toByteArray(); 	
		      
      	}
      	else
      	{
      		
      	}
      	os.write(data); 	      
		os.flush();    
		os.close();   
		
		return null;
	}
	
	public String getScanImage_() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		
		String id=request.getParameter("id");	
		
		byte data[] = {0x00, 0x00};
		response.setContentType("image/"+"jpg"); //设置返回的文件类型     
		OutputStream os = response.getOutputStream();    

		Connection conn = MyDBUtil.getConnection();
		
		Statement stmt = conn.createStatement();  
	    ResultSet rs = stmt.executeQuery("select scan_image from device where id = "+id);  
      	if (rs.next()){  
      		Blob blob = rs.getBlob(1);  
		    InputStream in = blob.getBinaryStream();  
		    data = readInputStream(in); 	
		      
      	}
      	else
      	{
      		
      	}
      	os.write(data); 	      
		os.flush();    
		os.close();   
		
		return null;
	}

	public byte[] readInputStream(InputStream inStream) throws Exception{    
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();    
        byte[] buffer = new byte[2048];    
        int len = 0;    
        while( (len=inStream.read(buffer)) != -1 ){    
            outStream.write(buffer, 0, len);    
        }    
        inStream.close();    
        return outStream.toByteArray();    
	}  
	
	public String execute() throws Exception{
		
		return SUCCESS;
	}
}
