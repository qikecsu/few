/**
 * 用户管理
 */

function createUserGrid(){
	
	// 使用dataGrid.txt中的数据作为填充DataGrid 的数据  
 	var store = new dojo.data.ItemFileWriteStore({ url:'DataAction!getUsers.action?cid=0'});    
  
 	var userLayout = [    
 		{field: 'number', name: '序号', editable:false, width:'50px', styles:'text-align:center;'},    
 		{field: 'user', name: '用户名', editable: true, width:'120px' },  //该列可编辑  
 		{field: 'name', name: '姓名', editable: true, width:'120px'  },  //该列可编辑  
 		{field: 'mobile', name: '手机号码（接收短信）', editable:true, width:'150px'  }, //编辑该列时使用下拉菜单  
 		{field: 'permission', name: '权限', editable:true,width:'150px', type:dojox.grid.cells.Select, options:['总公司管理员','总公司用户','地局级管理员','地局级用户','虚拟用户','超级用户']},    
 		{field: 'note', name: '备注', editable: true, width:'200px' },
 		{field: 'id', name: '编辑', editable: false, width:'160px',formatter: formatterUser }
 	];    
	
 	var grid = new dojox.grid.EnhancedGrid({    
 		 query: { id: '*' },  	//查询字符串  
 		 id: 'usergrid',            //DataGrid的 id    
 		 autoWidth:true,        	//自动调整宽度  
 		 autoHeight:true,        	//自动调整
 		 store: store,       	//使用jsonStore 对象  
 		 structure: userLayout,      // 使用layout对象定义的结构  
 		 plugins:{indirectSelection: {headerSelector:true, width:'40px', styles:'text-align: center;'}}
 		 });    
 	/*append the new grid to the div*/  
 	grid.placeAt("gridUserOption");  
 	grid.startup(); //grid生效  
 	
// 	return grid;
}

function addUser()
{
	var tree=dijit.byId("ordTreeUserOption");
	var items=tree.selectedItems;
//	alert(items.length)
	var cid=0;
	if (items.length>0) cid = items[0].idx;
//	alert(cid);
	
	dojo.xhrPost({ 
	    //The URL of the request 
		url: "DataAction!addUser.action", 
	    // 要处理内容的格式
	    //handleAs: "json", 
	    handleAs: "text",
	    // 请求参数
	    content: { 				
	    	cid : cid
	    }, 
	    load: function(data){
	        //dojo.byId("response2").innerHTML = "Message posted.";
	        var obj = dojo.fromJson(data); 
	        if (obj.success==true){
	        	
	        	var grid=tree=dijit.byId("usergrid");
	        	var store=grid.store;
	        	
	  //      	alert(store);
	        	
	        	//创建item  
	        	var item = {id:obj.id,user:obj.user,password:'1234554321',mobile:'',permission:'虚拟用户', number:grid.rowCount+1,name:'',note:''};  
	        	store.newItem(item);  
	        	store.save();  
	        	
	    		alert("新建用户操作成功！");
	        } else
	        	alert("新建用户操作失败！");
	      },
	    // 错误处理函数
	    error: function(error, ioArgs) { 
	         alert(error.message); 
		} 
	}); 
}

function updateUser(value)
{
//	alert(value);
	
	var grid=dijit.byId("usergrid");
	var store=grid.store;

//	alert(grid.rowCount);		//ok
	
//	alert(grid.store.getValue(grid.getItem(0), 'name'));

	//根据identity拿到item，两种方法  
	var item = store._itemsByIdentity[value]; //Object { id=[1], name=[1], type=[1], more...}  
//	alert(store.getValue(item, 'name'));
	var user=store.getValue(item, 'user');
	var name=store.getValue(item, 'name');
	var mobile=store.getValue(item, 'mobile');
	var permission=store.getValue(item, 'permission');
	var note=store.getValue(item, 'note');
	
	var sql="update user set name='"+name+"',note='"+note+"',user='"+user+"',mobile='"+mobile+"',permission='"+permission+"' where id="+value;
	
	dojo.xhrPost({ 
	    //The URL of the request 
		url: "DataAction!executeSQL.action", 
	    // 要处理内容的格式
	    //handleAs: "json", 
	    handleAs: "text",
	    // 请求参数
	    content: { 				
	        sql : sql 
	    }, 
	    load: function(data){
	        //dojo.byId("response2").innerHTML = "Message posted.";
	        var obj = dojo.fromJson(data); 
	        if (obj.success==true){
	    		alert("更新用户操作成功！");
	        } else
	        	alert("更新用户操作失败！");
	      },
	    // 错误处理函数
	    error: function(error, ioArgs) { 
	         alert(error.message); 
		} 
	}); 
	
	// 添加记录后自动刷新页面 ; 
	//setTimeout(function() {window.location.reload();},2000);
	
//	var item = store._getItemByIdentity("0");  
/*
//对item设置属性及值  
store.setValue(item, "description", "This is fruits"); //true  
  
//对item取消某个属性  
store.unsetAttribute(item, "description");  //true  
  
//拿到item的所有自定义属性  
store.getAttributes(item);  //["id", "name", "type", "children"]  
  
//拿到item的所有属性，包括系统属性  
Object.keys(item);  //["id", "name", "type", "children", "_RI", "_S", "_0", "_RRM"]  
*/

	
}

function deleteUser(value)
{
	var grid=dijit.byId("usergrid");
	var store=grid.store;
	var item = store._itemsByIdentity[value];
	
	dojoConfirm("您确认要删除"+store.getValue(item, 'user')+"吗？",function(){  
        //删除功能...  
		var sql="delete from user where id="+value;
		
		dojo.xhrPost({ 
		    //The URL of the request 
			url: "DataAction!executeSQL.action", 
		    // 要处理内容的格式
		    //handleAs: "json", 
		    handleAs: "text",
		    // 请求参数
		    content: { 				
		        sql : sql 
		    }, 
		    load: function(data){
		        //dojo.byId("response2").innerHTML = "Message posted.";
		        var obj = dojo.fromJson(data); 
		        if (obj.success==true){
					//删除item  
					store.deleteItem(item);  
					store.save(); 
			        
			    	alert("删除用户操作成功！");
		        } else 
		        	alert("删除用户操作失败！");
		        
		      },
		    // 错误处理函数
		    error: function(error, ioArgs) { 
		         alert(error.message); 
			} 
		}); 

	});

}

function formatterUser(value){
	
	var camera='<div><div class="company-update">'
			 + '<a href="javascript:void(0);" onclick="updateUser(' + value+');">'
			 + '更新</a></div>'
			 + '<div class="company-delete">'
			 + '<a href="javascript:void(0);" onclick="deleteUser(' + value+');">'
			 + '删除</a></div></div>'
	
	return camera;
}