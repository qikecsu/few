/**
 * 	基本函数
 */



function updateUserPassword()
{
	
	var oldPwd=dijit.byId("oldPwd").value;
	var newPwd=dijit.byId("newPwd").value;
	var rePwd=dijit.byId("rePwd").value;
	
	dojo.xhrPost({ 
	    //The URL of the request 
		url: "DataAction!updateUserPassword.action", 
	    // 要处理内容的格式
	    //handleAs: "json", 
	    handleAs: "text",
	    // 请求参数
	    content: { 				
	        user : loginname,
	        oldpwd:oldPwd,
	        newpwd:newPwd,
	        repwd :rePwd
	    }, 
	    load: function(data){
	        //dojo.byId("response2").innerHTML = "Message posted.";
	        var obj = dojo.fromJson(data); 
	        if (obj.success==true){
	        	modifyPasswordDialog.hide();
	    		alert("修改用户密码操作成功！");
	        } else {
	        	if (obj.why=='the new is empty')
	        		alert("新密码不能为空。修改用户密码操作失败！");
	        	else if (obj.why=='is not the same')
	        		alert("输入的两次密码不一致。修改用户密码操作失败！");
	        	else if (obj.why=='is wrong')
	        		alert("旧密码输入错误，修改用户密码操作失败！");
	        	else if (obj.why=='is not exist')
	        		alert("用户不存在，修改密码操作失败！");
	        	else 
	        		alert("修改用户密码操作失败！");
	        }	        	
	      },
	    // 错误处理函数
	    error: function(error, ioArgs) { 
	    	modifyPasswordDialog.hide();
	         alert(error.message); 
		} 
	}); 
	//alert(oldPwd);
}