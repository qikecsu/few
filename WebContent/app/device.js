/**
 * 设备管理
 */

function addDevice()
{
	var tree=dijit.byId("ordTreeDeviceOption");
	var items=tree.selectedItems;
//	alert(items.length)
	var cid=0;
	if (items.length>0) cid = items[0].idx;
	
	if (cid<1){
		alert("请先选择供电公司！");
		return;
	}
	
	dojo.xhrPost({ 
	    //The URL of the request 
		url: "DataAction!addDevice.action", 
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
	        	
	        	var grid=dijit.byId("gridDeviceOption");
	        	var store=grid.store;
	        	
	        	//创建item  
	        	var item = {id:obj.id,company:obj.company,name:obj.name,number:grid.rowCount+1,towerid:'',deviceid:'',note:''};  
	        	store.newItem(item);  
	        	store.save();  
	        	
	    		alert("新建设备操作成功！");
	        } else
	        	alert("新建设备操作失败！");
	      },
	    // 错误处理函数
	    error: function(error, ioArgs) { 
	         alert(error.message); 
		} 
	}); 
}

function updateDevice(value)
{
//	alert(value);
	
	var grid=dijit.byId("gridDeviceOption");
	var store=grid.store;

//	alert(grid.rowCount);		//ok
	
//	alert(grid.store.getValue(grid.getItem(0), 'name'));

	//根据identity拿到item，两种方法  
	var item = store._itemsByIdentity[value]; //Object { id=[1], name=[1], type=[1], more...}  
//	alert(store.getValue(item, 'name'));
	var name=store.getValue(item, 'name');
	var towerid=store.getValue(item, 'towerid');
	var deviceid=store.getValue(item, 'deviceid');
	var sms_mobiles=store.getValue(item, 'sms_mobiles');
	
	var sql="update device set name='"+name+"',towerid='"+towerid+"',deviceid='"+deviceid+"',sms_mobiles='"+sms_mobiles+"' where id="+value;
	
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
	    		alert("更新设备操作成功！");
	        } else
	        	alert("更新设备操作失败！");
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

function deleteDevice(value)
{
	var grid=dijit.byId("gridDeviceOption");
	var store=grid.store;
	var item = store._itemsByIdentity[value];
	
	dojoConfirm("您确认要删除"+store.getValue(item, 'name')+"吗？",function(){  
        //删除功能...  
		var sql="delete from device where id="+value;
		
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
			        
			    	alert("删除设备操作成功！");
		        } else 
		        	alert("删除设备操作失败！");
		        
		      },
		    // 错误处理函数
		    error: function(error, ioArgs) { 
		         alert(error.message); 
			} 
		}); 

	});

}

function deleteDevices()
{
	var grid=dijit.byId("gridDeviceOption");
	/* 选择被选中的行 */ 
    var items = grid.selection.getSelected(); 		
    if(items.length<1){ 
    	alert("请选择要删除的设备。");
    	return;
    }
    
    var store=grid.store;
    dojoConfirm("您确认要删除选择的设备？",function(){ 
    	// 循环遍历所选择的行  
    	var ids='';
    	dojo.forEach(items, function(selectedItem){ 
            if(selectedItem !== null){ 
            	if (ids!='') ids = ids+",";
            	ids = ids+store.getValue(selectedItem, 'id');
            	
            	//alert(store.getValue(selectedItem, 'name'));
                // Delete the item from the data store:  
       //         store.deleteItem(selectedItem); 
            } // end if 
        }); // end forEach 
    	
    	if (ids=='') return;
    	
    	dojo.xhrPost({ 
		    //The URL of the request 
			url: "DataAction!deleteDevices.action", 
		    // 要处理内容的格式
		    //handleAs: "json", 
		    handleAs: "text",
		    // 请求参数
		    content: { 				
		        ids : ids 
		    }, 
		    load: function(data){
		        //dojo.byId("response2").innerHTML = "Message posted.";
		        var obj = dojo.fromJson(data); 
		        if (obj.success==true){
					//删除item  
					dojo.forEach(items, function(selectedItem){ 
			            if(selectedItem !== null){ 
			            	store.deleteItem(selectedItem);  
			            } // end if 
			        }); // end forEach 
					store.save(); 
			        
			    	alert("批量删除设备操作成功！");
		        } else 
		        	alert("批量删除设备操作失败！");
		        
		      },
		    // 错误处理函数
		    error: function(error, ioArgs) { 
		         alert(error.message); 
			} 
		}); 
    	
    });
    
}

function formatterDevice(value){
	
	var camera='<div><div class="company-update">'
			 + '<a href="javascript:void(0);" onclick="updateDevice(' + value+');">'
			 + '更新</a></div>'
			 + '<div class="company-delete">'
			 + '<a href="javascript:void(0);" onclick="deleteDevice(' + value+');">'
			 + '删除</a></div>'
			 + '<div class="device-eye">'
			 + '<a href="javascript:void(0);" onclick="dutyDevice(' + value+');">'
			 + '值班人员</a></div>'
			 + '<div class="device-infra-red">'
			 + '<a href="javascript:void(0);" onclick="redDevice(' + value+');">'
			 + '红外成像</a></div>'
			 + '<div class="device-photo">'
			 + '<a href="javascript:void(0);" onclick="photoDevice(' + value+');">'
			 + '实景照片</a></div></div>'
	
	return camera;
}