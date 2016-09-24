package hive.fire.bean;

public class User {
	private int id;
	private String user;
	private String name;
	private String password;
	private String type;		//超级管理员：系统管理;管理员：用户管理;	一般用户：维护、浏览
	private String mobile;	
	private String note;
	private String user_cid;
	
	public User(int id,String user,String name,String pw,String type, String mobile, String note,String user_cid){
		this.id = id;
		this.user = user;
		this.name = name;
		this.password = pw;
		this.type = type;
		this.mobile = mobile;
		this.note = note;
		this.user_cid=user_cid;
	}
	public String getUser_cid() {
		return user_cid;
	}
	public void setUser_cid(String user_cid) {
		this.user_cid = user_cid;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
}
