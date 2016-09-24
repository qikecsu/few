package hive.fire.action;

import hive.fire.bean.User;
import hive.fire.biz.fireService;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import org.apache.struts2.ServletActionContext;

import util.ExtHelper;
import util.MyDBUtil;

import com.opensymphony.xwork2.ActionSupport;


public class LoginAction extends ActionSupport {
	private String loginname;
	private String password;
	private String cidString;
	
	public String getCidString() {
		return cidString;
	}
	public void setCidString(String cidString) {
		this.cidString = cidString;
	}

	private fireService service = new fireService();
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	
	public String Login() throws Exception{
		   
		System.out.println(loginname);
		HttpServletRequest request = ServletActionContext.getRequest();
		User user=service.getUserByName(loginname);
		if(user==null)
		{
			System.out.println("error");
			
			service.logging(0, 0, "user", "用户不存在", "");
			
			return "error";
		}
		else if(user.getPassword().equals(password))
		{
			System.out.println(password);
			
			service.logging(user.getId(), 0, "user", "用户登录成功", "");
			
			if (user.getType().equals("超级管理员"))
				return "super";
			else if (user.getType().equals("管理员"))
				return "admin";
			else {
				if (user.getType().equals("维护"))
					return "success";
				else{
					request.setAttribute("user_cid", user.getUser_cid());
					System.out.println("当前用户所属公司："+user.getUser_cid());
					return "readonly";
				}
					
			}
		}
		else {
			service.logging(user.getId(), 0, "user", "密码错误，登录失败", "");
			return "error";
		}			
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////
	// 用户 
	public String getUsers() throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();	
		HttpServletRequest request = ServletActionContext.getRequest();
		
		response.setContentType("text/html;charset=UTF-8");
				
		List<User> users = new ArrayList<User>();
		Connection conn = MyDBUtil.getConnection();
		Statement st = conn.createStatement();
		String sql = "select UserID, UserNAME, LOGINNAME, PASSWORD, type, MOBILENO, Note,cid from DMS_User where type<>'super' order by UserID";
		ResultSet rs = st.executeQuery(sql);
		while(rs.next()){
			int id=rs.getInt(1);
			String name="";
			String loginname="";
			String password="";
			String permissions="";
			String mobile="";
			String note="";
			String user_cid="";
			if (rs.getString(2)!=null) name = rs.getString(2);
			if (rs.getString(3)!=null) loginname = rs.getString(3);
			if (rs.getString(4)!=null) password = rs.getString(4);
			if (rs.getString(5)!=null) permissions = rs.getString(5);
			if (rs.getString(6)!=null) mobile = rs.getString(6);
			if (rs.getString(7)!=null) note = rs.getString(7);
			if (rs.getString(8)!=null) user_cid = rs.getString(8);
			
			User user = new User(id, name, loginname, password, permissions, mobile, note,user_cid);
			
			users.add(user);
		}
		rs.close();
		st.close();
		conn.close();
				
		System.out.println("   records="+users.size());
		
		StringBuffer json = new StringBuffer();
		json.append("{");
		json.append("results : "+users.size()+",");
		json.append("rows : [");
		int length = users.size();
		for(int i = 0 ; i < length ; i++){
			User user=users.get(i);			
			json.append(ExtHelper.getJsonFromBean(user));
			if(i < length - 1){
				json.append(",");
			}
		};
		json.append("]");
		json.append("}");
		response.getWriter().write(json.toString());
		
		System.out.println(json.toString());
		
		//return SUCCESS;
		return null;
	}
	
	public String addUser() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		
		//int unitId = Integer.parseInt(request.getParameter("unitid"));
	//	System.out.println("addPerson");
		String name=request.getParameter("name");	
		String loginname=request.getParameter("loginname");	
		String password=request.getParameter("password");							//
		String type=request.getParameter("type");	
		String mobile=request.getParameter("mobile");								//
		String note=request.getParameter("note");	
		String cid=request.getParameter("cid");	
		
//		System.out.println(name);
		
		Connection conn = MyDBUtil.getConnection();
		
		String sql = "insert into DMS_User (USERNAME, LOGINNAME,PASSWORD, TYPE,MOBILENO,Note,cid) values(";
		sql += ("'"+name+"',");
		sql += ("'"+loginname+"',");
		sql += ("'"+password+"',");
		sql += ("'"+type+"',");
		sql += ("'"+mobile+"',");		
		sql += ("'"+note+"')");
		sql += ("'"+cid+"')");
		
		System.out.println(sql);
		
		Statement ps=conn.createStatement();   
		ps.executeUpdate(sql);
		ps.close();
		
		int id=-1;
		Statement st = conn.createStatement();
		sql = "select max(UserID) from DMS_User";
		ResultSet rs = st.executeQuery(sql);
		if (rs.next()){
			id = rs.getInt(1);
		}
		rs.close();
		st.close();
			
		conn.close();
		
		boolean isSuccess = true;
		if(id == -1){
			isSuccess = false;
		}
		response.setContentType("text/json;charset=UTF-8");
		response.getWriter().write("{success:"+isSuccess+",id:"+id+"}");
		
		return null;
	}	
	
	public String changePassword() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		
		String loginname=request.getParameter("name");	
		String password=request.getParameter("password");												
		String oldpass=request.getParameter("oldpass");	
		
		System.out.println(loginname);
		
		response.setContentType("text/json;charset=UTF-8");
		
		User user=service.getUserByName(loginname);
		if (user==null)
		{			
			response.getWriter().write("{success:"+false+",why:'用户("+loginname+")不存在'}");			
		}
		else
		{
			if (!oldpass.equals(user.getPassword())){
				response.getWriter().write("{success:"+false+",why:'旧密码错误'}");	
			}
			else{
				Connection conn = MyDBUtil.getConnection();
				
				try {		
					Statement ps=conn.createStatement();   
					String sql="update DMS_User set Password='"+password+"' where LoginName='"+loginname+"'";				
					ps.executeUpdate(sql);
					ps.close();
					
					boolean isSuccess = true;
					response.getWriter().write("{success:"+isSuccess+",name:'"+loginname+"'}");
				
				} catch (Exception e) {
					response.getWriter().write("{success:"+false+",why:'"+e.toString()+"'}");	
								
				}
				
				conn.close();;
							
			}
		}
		
		return null;
	}
	
	public String userType() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		
		String loginname=request.getParameter("name");	
		
		System.out.println(loginname);
		
		response.setContentType("text/json;charset=UTF-8");
		
		User user=service.getUserByName(loginname);
		if (user==null)
		{			
			response.getWriter().write("{success:"+false+",why:'用户("+loginname+")不存在'}");			
		}
		else
		{
			boolean isSuccess = true;
			response.getWriter().write("{success:"+isSuccess+",type:'"+user.getType()+"'}");
		}
		
		return null;
	}
	
	public String appLogin() throws Exception{
		   
		System.out.println(loginname);
		
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		
		response.setContentType("text/html;charset=UTF-8");
		
		User user=service.getUserByName(loginname);
		if(user==null)
		{
			response.getWriter().write("error");
		}
		else if(user.getPassword().equals(password))
		{
			if (user.getType().equals("超级管理员"))
				response.getWriter().write("super");
			else if (user.getType().equals("管理员"))
				response.getWriter().write("admin");
			else {
				if (user.getType().equals("维护"))
					response.getWriter().write("success");
				else
					response.getWriter().write("readonly");
			}
		}
		else 
			response.getWriter().write( "error");
		
		return null;
	}
}
