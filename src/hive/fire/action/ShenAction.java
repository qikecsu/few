package hive.fire.action;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Vector;
import java.util.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar; 

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.mysql.jdbc.Blob;
import com.opensymphony.xwork2.ActionSupport;

import util.InitListener;
import util.MyDBUtil;
import ws.chat.ChatAnnotation;

public class ShenAction extends ActionSupport{
	///获取所有装置信息
public String getallCompany() throws Exception {
		
		HttpServletResponse response = ServletActionContext.getResponse();
		System.out.println("getallCompany");
		response.setContentType("text/html;charset=UTF-8");
		
		Date now = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String today=df.format(now);
				
		StringBuffer json = new StringBuffer();
		json.append("{identifier:'id',");
		json.append("label:'name',");
		json.append("items:[");
		
		Connection conn = MyDBUtil.getConnection();
		Statement st = conn.createStatement();
		String sql="select company.id,company.name,company.type from company";
		ResultSet rs = st.executeQuery(sql);
		
		System.out.println(sql);
		
		int row=0;
		while(rs.next()){
			row++;
			String id="";
			if (rs.getString(1)!=null) id = rs.getString(1);
            String name="";
            if (rs.getString(2)!=null) name = rs.getString(2);
            String type="";
            if(rs.getString(3)!=null) type = rs.getString(3);
                      
            String obj="{rowId:"+id+",id:"+id+",name:'"+name+"',type:'"+type+"'},";
            
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
public String getDevicesfromcompany() throws Exception {
	
	HttpServletRequest request = ServletActionContext.getRequest();
	HttpServletResponse response = ServletActionContext.getResponse();
	
	System.out.println("getDevicesfromcompany");
	
	String cid = request.getParameter("cid");
	
	response.setContentType("text/html;charset=UTF-8");
	
	/////////////////////////////////////////////////////////////0////
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
	String sql="select device.id, device.name, device.run, device.fire, device.longitude,device.latitude,device.deviceid from device where device.cid='"+cid+"' order by device.id";
	
	ResultSet rs = st.executeQuery(sql);
	
	System.out.println(sql);
	
	int row=0;
	while(rs.next()){
		row++;
		
		String id="";
		if (rs.getString(1)!=null) id = rs.getString(1);
		String name="";
        if (rs.getString(2)!=null) name = rs.getString(2);
        String status="";
        if (rs.getInt(3)!=0) status = "正常运行";
        else status = "停止运行";
        String fire="";
        if (rs.getInt(4)!=0) fire = "有火灾报警";
        else fire = "无";
        String longitude="";
        if (rs.getString(5)!=null) longitude = rs.getString(5);
        String latitude="";
        if (rs.getString(6)!=null) latitude = rs.getString(6);
        String deviceid="";
        if (rs.getString(7)!=null) deviceid = rs.getString(7);
        
        String obj="{\"rowId\":"+id+",\"id\":"+id+",\"name\":\""+name+"\",\"status\":\""+status+"\",\"fire\":\""+fire+"\",\"time\":\""+today+"\",\"longitude\":\""+longitude+"\",\"latitude\":\""+latitude+"\",\"deviceid\":\""+deviceid+"\"},";
        
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

public String getPicture() throws Exception {
	HttpServletRequest request = ServletActionContext.getRequest();
	HttpServletResponse response = ServletActionContext.getResponse();
	
	String did = request.getParameter("did");
	String which = request.getParameter("which");
	response.setContentType("text/html;charset=UTF-8");
	System.out.println("getpicture:   "+which);
	
	InputStream is = null;
	InputStream is_new = null;
	OutputStream os=null;
	
	Connection conn = MyDBUtil.getConnection();
	Statement st = conn.createStatement();
	String sql;
	String up="up";
	if(which.equals(up))
		sql="select scan_image_up from device where id = '"+did+"' order by id";
	else
		sql="select scan_image_down from device where id = '"+did+"' order by id";
	ResultSet rs = st.executeQuery(sql);
	
	System.out.println(sql);
	 if (rs.next()) {
		    is = rs.getBinaryStream(1);
		    is_new=mergeImage(is,did,which);
		    response.setContentType("image/jpg");
		    os = response.getOutputStream();
		   
		    int num;
		    byte buf[] = new byte[1024];
		   
		    while((num=is_new.read(buf))!=-1){
		     os.write(buf, 0, num);
		    }
		    is.close();
		    os.close();
		    rs.close();
		    }
	 else { 
		 rs.close(); 
		 response.sendRedirect("./images/logo.png"); 
		 } 
	return null;
	}


private InputStream  mergeImage(InputStream old_in,String did,String which) throws Exception {

	//创建BufferedImage
	//String imgpath="/few/WebContent/images/logo.png";
	BufferedImage image=ImageIO.read(old_in);
	//得到Graphics2D 对象
	Graphics2D g2d=(Graphics2D)image.getGraphics();
	//设置颜色和画笔粗细
	g2d.setColor(Color.RED);
	g2d.setStroke(new BasicStroke(10));
	String strdot="*";
	Connection conn = MyDBUtil.getConnection();
	Statement st = conn.createStatement();
	String sql="select warming._x,warming._y from warming where warming.did='"+did+"'";
	
	ResultSet rs = st.executeQuery(sql);
	
	System.out.println(sql);
	String up="up";
	String down="down";
	int row=0;
	int x_int,y_int;
	while(rs.next()){
		row++;
		String x="";
		if (rs.getString(1)!=null) x = rs.getString(1);
        String y="";
        if (rs.getString(2)!=null) y = rs.getString(2);
        x_int=Integer.parseInt(x);
        y_int=Integer.parseInt(y);
        //写入图像内容
        System.out.println(x+" "+y);
        if(y_int<=360){//应该标在上图
        	if(which.equals(up)){//传来的是上图
        		g2d.setColor(Color.RED);	//画笔颜色，火警点用红色
        		g2d.setStroke(new BasicStroke(10)); //画笔粗细
        		g2d.drawString(strdot,x_int, y_int);
        		g2d.setColor(Color.GREEN);	//圆圈用绿色
        		g2d.setStroke(new BasicStroke(2));
        		g2d.drawOval((x_int-8), (y_int-16), 20, 20);
            }
        }
        if(y_int>=360){//应该标在下图
        	if(which.equals(down)){//传来的是下图
            	y_int=720-y_int;
            	g2d.setColor(Color.RED);
            	g2d.setStroke(new BasicStroke(10));
            	g2d.drawString(strdot,x_int, y_int);
            	g2d.setColor(Color.GREEN);
            	g2d.setStroke(new BasicStroke(2));
            	g2d.drawOval((x_int-8), (y_int-16), 20, 20);
            }
        }
      }
	rs.close();
	st.close();
	conn.close();
	g2d.dispose();
	//输出拼接后的图像
	InputStream is = null; 
	ByteArrayOutputStream bs=new ByteArrayOutputStream();
	ImageOutputStream imOut; 
	imOut = ImageIO.createImageOutputStream(bs); 
    ImageIO.write(image, "jpg",imOut); 
    is= new ByteArrayInputStream(bs.toByteArray()); 
    
	//ImageIO.write(image, "jpeg", out);
	return  is;
}
}

