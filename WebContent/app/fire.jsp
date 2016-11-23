<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String ctxpath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7; IE=EmulateIE9" ; charset=UTF-8">
<link href="images/favicon.ico" rel="shortcut icon">
<link href="images/favicon.gif" type="image/gif" rel="icon">
<title>电力线路山火预警系统</title>
<!--[if IE]>
    <script type="text/javascript" src="app/excanvas.js"></script>
<![endif]-->
<link rel="stylesheet" type="text/css" href="dojo/dijit/themes/claro/claro.css" />
<link rel="stylesheet" type="text/css" href="dojo/dojox/grid/resources/claroGrid.css" />
<link rel="stylesheet" type="text/css" href="dojo/dojox/grid/enhanced/resources/claro/EnhancedGrid.css" />
<link rel="stylesheet" type="text/css" href="dojo/dojo/resources/dojo.css" />
<link rel="stylesheet" type="text/css" href="app/fire.css" /> 
<script type="text/javascript" src="dojo/dojo/dojo.js"
								djConfig="parseOnLoad: true, gfxRenderer:'svg,vml,silverlight'"></script>
<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=xGr7slTW1XWRrAQXgdPx8Au1"></script>								
<script type="text/javascript" src="app/dygraph-combined.js"></script>	
<script type="text/javascript" src="app/comfirm.js"></script>	
<script type="text/javascript" src="app/timer.js"></script>	
<script type="text/javascript" src="app/fire.js"></script>
<script type="text/javascript" src="app/bdmap.js"></script>
<script type="text/javascript" src="app/company.js"></script>	
<script type="text/javascript" src="app/device.js"></script>
<script type="text/javascript" src="app/user.js"></script>					
<script type="text/javascript" >
 
//工程主目录
var ctxpath = '<%=ctxpath%>';
//登录用户
var loginname='${requestScope.loginname}';

dojo.require("dojo/_base/json");
dojo.require("dojo/parser");

dojo.require("dojo.data.ItemFileWriteStore");

dojo.require("dijit/dijit-all");

dojo.require("dojox.grid.DataGrid");
dojo.require("dojox.grid.EnhancedGrid");
dojo.require("dojox.grid.enhanced.plugins.IndirectSelection");

dojo.require("dojox.gfx");

//Require the basic 2d chart resource: Chart2D
dojo.require("dojox.charting.widget.Chart2D");
dojo.require("dojox.charting.DataChart");
dojo.require("dojox.charting.DataSeries");

//Require the theme of our choosing
//"Claro", new in Dojo 1.6, will be used
dojo.require("dojox.charting.themes.Claro");

function formatterStatus(value){
	if (value=='正常运行')
	 	return '<div class="status_on" >' + value+'</div>'; 
	else
		return '<div class="status_off" >' + value+'</div>';
} 

function formatterRadar(value){
	
	var radar='<div><div class="device-control-start">'
			 + '<a href="javascript:void(0);" onclick="deviceRadarStart(' + value+');">'
			 + '通电</a></div>'
			 + '<div class="device-control-stop">'
			 + '<a href="javascript:void(0);" onclick="deviceRadarStop(' + value+');">'
			 + '断电</a></div></div>';
	
	return radar;
	
 /*   var w = new dijit.form.Button({
    	label: "Click me!",
        onClick: function(){
            // Do something:
            //dom.byId("result1").innerHTML += "Thank you! ";
        }
	});
    w._destroyOnRemove = true;
    return w;*/
}

function deviceRadarStop(value){
	
//	alert("deviceRadarStop");
	
	
	dojo.xhrPost({ 
	    //The URL of the request 
		url: "DataAction!deviceRadarStop.action", 
	    // 要处理内容的格式
	    //handleAs: "json", 
	    handleAs: "text",
	    // 请求参数
	    content: { 				
	    	id : value,
	    	user:loginname
	    }, 
	    load: function(data){
	        //dojo.byId("response2").innerHTML = "Message posted.";
	        var obj = dojo.fromJson(data); 
			if (obj.success==true){
	        	
	    		//alert("雷达断电操作成功！");
	    		
				dojoAlert("雷达断电操作成功！请刷新确认。",function(){ 
	    			
	    		});
	    		
	        } else {
	        	//alert("雷达断电操作失败！");
		        
				dojoAlert("雷达断电操作失败！",function(){ 
	    			
	    		});
	        }
	        	
	      },
	    // 错误处理函数
	    error: function(error, ioArgs) { 
	         alert(error.message); 
		} 
	}); 
}

function deviceRadarStart(value){
	dojo.xhrPost({ 
	    //The URL of the request 
		url: "DataAction!deviceRadarStart.action", 
	    // 要处理内容的格式
	    //handleAs: "json", 
	    handleAs: "text",
	    // 请求参数
	    content: { 				
	    	id : value,
	    	user:loginname
	    }, 
	    load: function(data){
	        //dojo.byId("response2").innerHTML = "Message posted.";
	        var obj = dojo.fromJson(data); 
			if (obj.success==true){
	        	
	    		//alert("雷达通电操作成功！");
	    		
	    		dojoAlert("雷达通电操作成功！请刷新确认。",function(){ 
	    			
	    		});
	    		
	        } else{
	        	//alert("雷达通电操作失败！");
		        
				dojoAlert("雷达通电操作失败！",function(){ 
	    			
	    		});
	        }
	        	
	      },
	    // 错误处理函数
	    error: function(error, ioArgs) { 
	         alert(error.message); 
		} 
	}); 
}

function deviceClearFireAlarm(value){
	dojo.xhrPost({ 
	    //The URL of the request 
		url: "DataAction!deviceClearFireAlarm.action", 
	    // 要处理内容的格式
	    //handleAs: "json", 
	    handleAs: "text",
	    // 请求参数
	    content: { 				
	    	id : value,
	    	user:loginname
	    }, 
	    load: function(data){
	        //dojo.byId("response2").innerHTML = "Message posted.";
	        var obj = dojo.fromJson(data); 
			if (obj.success==true){
	        	
	    		//alert("雷达通电操作成功！");
	    		
	    		dojoAlert("清除火警操作成功！请刷新确认。",function(){ 
	    			
	    		});
	    		
	        } else{
	        	//alert("雷达通电操作失败！");
		        
				dojoAlert("清除火警操作失败！",function(){ 
	    			
	    		});
	        }
	        	
	      },
	    // 错误处理函数
	    error: function(error, ioArgs) { 
	         alert(error.message); 
		} 
	}); 
}

function formatterFire(value){
	
	var camera='<div class="device-fire-clean">'
			 + '<a href="javascript:void(0);" onclick="deviceClearFireAlarm(' + value+');">'
			 + '清除火警</a></div>'
			 + '</div>';
	
	return camera;
}

function formatterCamera(value){
	
	var camera='<div><div class="device-control-start">'
			 + '<a href="javascript:void(0);" onclick="deviceCameraStart(' + value+');">'
			 + '通电</a></div>'
			 + '<div class="device-control-stop">'
			 + '<a href="javascript:void(0);" onclick="deviceCameraStop(' + value+');">'
			 + '断电</a></div></div>'
	
	return camera;
}



function btnClick() {
   // alert("我被点击了");
}

//初始化
dojo.ready(function(){	
	
	dojo.byId("login_user").innerHTML = "登录用户："+loginname;
	
	var grid=dijit.byId('gridDeviceList');
	
	dojo.connect(grid.selection, 'onSelected', function(rowIndex){
	//	alert("selected "+ rowIndex);
	});
	
	dojo.connect(grid.selection, 'onDeselected', function(rowIndex){
	//	alert("deselected "+ rowIndex);
	});

	// when Select All checkbox is changed
	dojo.connect(grid.rowSelectCell, 'toggleAllSelection', function(newValue){
	//	alert("toggleAllSelection "+ newValue);
	})
	
	//!!!!!! You can check whether a certain row is selected with the following code.
	//dijit.byId('grid').selection.isSelected(rowIndex) // returns true or false
			
	
	createUserGrid();
		
	push_socket.connect();
		
	
	
	/////////////////////////////////////////////////////////////////////////////////////////
	///////////事件初始化
	
	var today=new Date(); // 获取今天时间
	today.setDate(today.getDate() - 2); // 系统会自动转换
	
	var date1 = dijit.byId('event_start_date1');
	date1.setValue(today);
	
	today.setDate(today.getDate() + 1); // 系统会自动转换
	
	var date2 = dijit.byId('event_end_date1');
	date2.setValue(today);
	
/////////////////////////////////////////////////////////////////////////////////////////

	window.setInterval(timer //要执行的代码                      
		,1000);
		
/*	// 百度地图API功能
	var map = new BMap.Map("allmap");    // 创建Map实例
	map.centerAndZoom("南京",15);      // 初始化地图,用城市名设置地图中心点
	//map.centerAndZoom(new BMap.Point(118.858, 32.096), 11);  // 初始化地图,设置中心点坐标和地图级别
	//map.addControl(new BMap.MapTypeControl());   //添加地图类型控件
	//map.setCurrentCity("南京");          // 设置地图显示的城市 此项是必须设置的
	map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放
	*/
	
	// 百度地图API功能
	map = new BMap.Map("allmap_bd",{mapType: BMAP_HYBRID_MAP});    // 创建Map实例
	map.centerAndZoom("南京",15);      // 初始化地图,用城市名设置地图中心点
	map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放
	map.addControl(new BMap.MapTypeControl());
});

</script>
</head>

<body class="claro">
	<div dojoType="dijit.layout.BorderContainer" gutters="false" id="borderContainer" style="width: 100%; height: 100%;padding:0px; border : 1px;"> 
	 		
	 	<!-- 页面头 -->
		<div dojoType="dijit.layout.ContentPane" region="top"  id="header_bar" style="padding:0px;" > 
			<div dojoType="dijit.layout.BorderContainer" design="sidebar" gutters="false" style="width: 100%; height: 36px;padding:0px;"> 
	 			<div dojoType="dijit.layout.ContentPane" region="center" >
		 			<div id="prj_title" >电力线路山火预警系统
					</div>
	 			</div>
	 			<div dojoType="dijit.layout.ContentPane" region="trailing" style="width: 880px;overflow:hidden;padding:0px;" >
	 			<div style="margin:0; padding:4px;"><font id="login_user" style="color:white;padding:0;" >administrator</font>
	 			<button data-dojo-type="dijit.form.Button" data-dojo-props=" "  >					
					设备列表					
					 <script type="dojo/on" data-dojo-event="click" data-dojo-args="evt">
       		 			var stack = dijit.byId("maincontainer");
						stack.selectChild("device_list");
						//getPicture(surface, rootid);
    				</script>					
				</button>
				<button data-dojo-type="dijit.form.Button" data-dojo-props=" "  >					
					山火报警	
					<script type="dojo/on" data-dojo-event="click" data-dojo-args="evt">
       		 			var stack = dijit.byId("maincontainer");
						stack.selectChild("realtimemonitoring");
    				</script>										 				
				</button>
				<button data-dojo-type="dijit.form.Button" data-dojo-props=" "  >					
					事件分析					
					<script type="dojo/on" data-dojo-event="click" data-dojo-args="evt">
       		 			var stack = dijit.byId("maincontainer");
						stack.selectChild("historyrecord");

    				</script>	 				
				</button>
				<button data-dojo-type="dijit.form.Button" data-dojo-props=" "  >					
					地图展示					
					<script type="dojo/on" data-dojo-event="click" data-dojo-args="evt">
       		 			var stack = dijit.byId("maincontainer");
						stack.selectChild("allmap");
    				</script>	 					
				</button>
				<button data-dojo-type="dijit.form.Button" data-dojo-props=" "  >					
					公司管理					
					<script type="dojo/on" data-dojo-event="click" data-dojo-args="evt">
       		 			var stack = dijit.byId("maincontainer");
						stack.selectChild("companyEditor");
    				</script>	 					
				</button>
				<button data-dojo-type="dijit.form.Button" data-dojo-props=" "  >					
					设备管理					
					<script type="dojo/on" data-dojo-event="click" data-dojo-args="evt">
       		 			var stack = dijit.byId("maincontainer");
						stack.selectChild("device_option");
    				</script>	 					
				</button>
				<button data-dojo-type="dijit.form.Button" data-dojo-props=" "  >					
					用户管理					
					<script type="dojo/on" data-dojo-event="click" data-dojo-args="evt">
       		 			var stack = dijit.byId("maincontainer");
						stack.selectChild("user_option");
    				</script>	 					
				</button>
				<button data-dojo-type="dijit.form.Button" type="button" onClick="modifyPasswordDialog.show();"  >					
					修改密码					
					<script type="dojo/on" data-dojo-event="click" data-dojo-args="evt">
       		 			//var stack = dijit.byId("maincontainer");
						//stack.selectChild("allmap");
    				</script>	 					
				</button>
				<button data-dojo-type="dijit.form.Button" data-dojo-props=" "  >					
					注销					
					 <script type="dojo/on" data-dojo-event="click" data-dojo-args="evt">
       		 			//require(["dojo/dom"], function(dom){
            			//	dom.byId("login_user").innerHTML = "Thank you! ";
        				//});
						//if (confirm("您确实要离开电力线路山火预警系统吗？"))
						//	location.href = 'login.html';
						dojoConfirm("您确认要离开电力线路山火预警系统吗？",function(){  
						            //删除功能...  
							

							
		dojo.xhrPost({ 
		    //The URL of the request 
			url: "DataAction!logoff.action", 
		    // 要处理内容的格式
		    //handleAs: "json", 
		    handleAs: "text",
		    // 请求参数
		    content: { 				
		        user : loginname 
		    }, 
		    load: function(data){
		        //dojo.byId("response2").innerHTML = "Message posted.";
		        var obj = dojo.fromJson(data); 
		        if (obj.success==true){
					
			    	//alert("删除公司操作成功！");
		        } else {
		        	//alert("删除公司操作失败！");
		        }
		      },
		    // 错误处理函数
		    error: function(error, ioArgs) { 
		         alert(error.message); 
			} 
		}); 

						location.href = 'index.jsp';


						}); 

    				</script>					
				</button>
				<font id="timer" color="white" style="height: 10px;padding:0px;"> 2013-06-18  22:12:12</font>
				</div>				
	 			</div>			
	 		</div> 
 		</div>  
 		 
 		<!-- 页面体 -->  
 		<div dojoType="dijit.layout.StackContainer" id="maincontainer" region="center" style="margin:0; padding:0;overflow:hidden;" > 
 			
 			<!-- 设备列表----------------------------------------------------------->
		 	<div dojoType="dijit.layout.BorderContainer" id="device_list" design="sidebar" title="<b class='tabtitle'>雷达设备列表</b>"  style="background:#180053;padding:0px;" >
		 	
		 		<div dojoType="dijit.layout.ContentPane" data-dojo-props='title:"Tree"' splitter="true" region="leading" style="width: 200px;"> 
	 				<!-- tree widget -->
	 				<div dojoType="dojo.data.ItemFileReadStore" url="DataAction!getCompanyTree.action" jsId="ordStoreDevice"></div>
					<div dojoType="dijit.tree.ForestStoreModel" rootLabel="探山火设备列表" store="ordStoreDevice"  jsId="ordModelDevice"></div>
					<div data-dojo-type="dijit.Tree" id="ordTreeDevice"  data-dojo-props="model: ordModelDevice, openOnClick:true"  >
						<script type="dojo/method" event="onClick" args="item">
				//		alert(item.station_+' '+item.rack_+' '+item.slot_);
						var store = new dojo.data.ItemFileReadStore({'url':ctxpath+"/DataAction!getDevices.action?cid="+item.idx});				
						var grid=dijit.byId("gridDeviceList");
						grid.setStore(store, {id:"*"}, "*");
						grid.startup(); 
					</script>
					</div>
	 			</div>     
	
 				<div dojoType="dijit.layout.ContentPane" region="top"  gutters="false" id="query_bar_device"  >
 					<div class="div-normal" >探山火设备列表</div>
 					<!--div class="div-normal" >正常运行</div> 
					<div class="div-abnormal" >停止运行</div --> 
					<!--  input id="mycheck" name="mycheck" data-dojo-type="dijit/form/CheckBox" value="agreed" checked onChange="alert('onChange called with parameter = ' + arguments[0] + ', and widget value = ' + this.get('value'))"> <label for="mycheck">全选</label-->
				</div>
				<div dojoType="dijit.layout.ContentPane" splitter="true" region="center" > 
					
					<div dojoType="dojo.data.ItemFileReadStore" url="DataAction!getDevices.action?cid=0" jsId="ordStoreDeviceList"></div>
		 			<table dojoType="dojox.grid.EnhancedGrid" store="ordStoreDeviceList" id="gridDeviceList" jsId="gridDeviceList" data-dojo-props="plugins:{indirectSelection: {headerSelector:true, width:'40px', styles:'text-align: center;'}}"  
						loadingMessage="请等待，数据正在加载中......" errorMessage="对不起，你的请求发生错误!"
						>
  
			     	 <!--thead定义布局，这是不可缺少的！否则widget将不能工作-->
				    	<thead>
					        <tr>
					           <!--表头，field属性的值为json对象中由items指定的json对象数组中的属性明-->
					           
					           <!-- th width="30px" field="rowId" styles="text-align:center;" 
						             formatter="checkBoxFormatter" sortable="false" > 
					             <div> 
					               <input type="checkBox" id="gridId.checkBox" name="gridId.checkBox" 
					                  onclick="globalSelection();"/> 
					            </div> 
						       </th--> 
					           
					           <th field="number" editable="false" width="50px" styles="text-align:center;">序号</th>
					           <th field="company" editable="false" width="150px">供电公司</th>
					           <th field="name" editable="false" width="200px">设备名称</th>
					           <th field="fire" editable="false" width="80px">火警状态</th>
					           <th field="status" formatter="formatterStatus" editable="false" width="100px" styles="text-align:center;">运行状态
					           	
					           </th>
					           <th field="id" formatter="formatterRadar" editable="false" width="160px">雷达控制</th>	
					           <th field="id" formatter="formatterFire" editable="false" width="160px">火警处理</th>
					       </tr>
				      	</thead>
				    </table>	
					
					
				</div>	
				
 			</div>
 							 		
	 		<!-- 山火报警----------------------------------------------------------->
		 	<div dojoType="dijit.layout.BorderContainer" id="realtimemonitoring" gutters="false" title="<b class='tabtitle'>实时监控</b>"  style="padding:0px;" >
		 	   
				<div dojoType="dijit.layout.ContentPane" region="top"  id="query_bar_event"  >
 					<!--  div class="div-label" >图例：</div-->
 					<div class="div-normal" >山火报警</div> 
					<!--  div class="div-abnormal" >过高或过低</div> 
					<div class="div-fault" >故障</div--> 
					<!-- button data-dojo-type="dijit.form.Button" data-dojo-props=" "  >					
					发现山火					
						<script type="dojo/on" data-dojo-event="click" data-dojo-args="evt">
       		 			alert("发现山火");
    					</script>					
					</button>
					<button data-dojo-type="dijit.form.Button" data-dojo-props=" "  >					
					正常运行					
						<script type="dojo/on" data-dojo-event="click" data-dojo-args="evt">
       		 			alert("正常运行");
    					</script>					
					</button>
					<button data-dojo-type="dijit.form.Button" data-dojo-props=" "  >					
					停止运行					
						<script type="dojo/on" data-dojo-event="click" data-dojo-args="evt">
       		 			alert("停止运行");
    					</script>					
					</button-->
					<form id="myform"  style="float:left;">
					    <input type="radio" data-dojo-type="dijit.form.RadioButton" name="device" id="radioOne" checked value="fire" /> <label for="radioOne">发现山火</label>
					    <input type="radio" data-dojo-type="dijit.form.RadioButton" name="device" id="radioTwo" value="normal" /> <label for="radioTwo">正常运行</label>
					    <input type="radio" data-dojo-type="dijit.form.RadioButton" name="device" id="radioThree" value="unnormal" /> <label for="radioThree">停止运行</label>
					</form>
					<button data-dojo-type="dijit.form.Button" data-dojo-props=" "  >					
					刷新					
						<script type="dojo/on" data-dojo-event="click" data-dojo-args="evt">
       		 			//alert("刷新");
					//		with(dojo.byId('myform')) with(elements[0]) with(elements[checked?0:1])
					//			alert(name+'='+value);

							var f = dojo.byId("myform");
            				var s = "";
							var val="";
            				for(var i = 0; i < f.elements.length; i++){
                				var elem = f.elements[i];
                				if(elem.name == "button"){ continue; }
                				if(elem.type == "radio" && !elem.checked){ continue; }
                				s += elem.name + ": " + elem.value + "\n";
								val = elem.value;
            				}
            				//alert("The selected value is :\n" + s);

							var store = new dojo.data.ItemFileReadStore({'url':ctxpath+"/DataAction!getDeviceStatus.action?device="+val});				
							var grid=dijit.byId("gridRealtime");
							grid.setStore(store, {id:"*"}, "*");
							grid.startup(); 


    					</script>					
					</button>
				</div>
				<div dojoType="dijit.layout.ContentPane" region="center" > 

		 			<table dojoType="dojox.grid.EnhancedGrid" id="gridRealtime" jsId="gridRealtime" data-dojo-props="plugins:{indirectSelection: {headerSelector:true, width:'40px', styles:'text-align: center;'}}"  
						loadingMessage="请等待，数据正在加载中......" errorMessage="对不起，你的请求发生错误!">
  
			     	 <!--thead定义布局，这是不可缺少的！否则widget将不能工作-->
				    	<thead>
					        <tr>
					           <!--表头，field属性的值为json对象中由items指定的json对象数组中的属性明-->
					           <th field="number" editable="false" width="50px" styles="text-align:center;">序号</th>
					           <th field="company" editable="false" width="150px">供电公司</th>
					           <th field="name" editable="false" width="150px">设备名称</th>
					           <th field="fire" editable="false" width="70px">火警状态</th>
					           <th field="fire" editable="false" width="120px">报警时间</th>
					           
					           <th field="status" formatter="formatterStatus" editable="false" width="100px" styles="text-align:center;">运行状态
					           	
					           </th>
					           <th field="id" formatter="formatterRadar" editable="false" width="140px">雷达控制</th>	
					           <th field="id" formatter="formatterFire" editable="false" width="120px">火警处理</th>	
					           <th field="id" editable="false" width="100px">短信上报人</th>
					           <th field="id" editable="false" width="80px">号码</th>
					           <th field="id" editable="false" width="80px">上报状态</th>				           
					       </tr>
				      	</thead>
				    </table>	

				</div>
				
 			</div>
	 		
	 		<!-- 历史记录----------------------------------------------------------->
		 	<div dojoType="dijit.layout.BorderContainer" id="historyrecord" design="sidebar" title="<b class='tabtitle'>实时监控</b>"  style="background:#180053;padding:0px;" >
		 	
		 		<div dojoType="dijit.layout.ContentPane" data-dojo-props='title:"Tree"' splitter="true" region="leading" style="width: 200px;"> 
	 				<!-- tree widget -->
	 				<div dojoType="dojo.data.ItemFileReadStore" url="DataAction!getCompanyTree.action" jsId="ordStoreHistory"></div>
					<div dojoType="dijit.tree.ForestStoreModel" rootLabel="探火报警日志" store="ordStoreHistory" jsId="ordModelHistory"></div>
					<div data-dojo-type="dijit.Tree" id="ordTreeHistory"  data-dojo-props="model: ordModelHistory, openOnClick:true"	>
						<script type="dojo/method" event="onClick" args="item">
						//alert(item.description);
						
					</script>
					</div>
	 			</div>     
	
 				<div dojoType="dijit.layout.ContentPane" region="top"  gutters="false" id="history_bar"  >
 					<label for="event_start_date" >开始</label>
					<input id="event_start_date1"  name="event_start_date" type="text" data-dojo-type="dijit.form.DateTextBox" data-dojo-props='value:"2008-12-25", disabled:false'>
 					<label for="event_end_date" >结束</label>
					<input id="event_end_date1"  name="event_end_date" type="text" data-dojo-type="dijit.form.DateTextBox" data-dojo-props='value:"2008-12-25", disabled:false'>
					
		 			<div data-dojo-type="dijit.form.DropDownButton" data-dojo-props="iconClass:'dijitIconEdit'">
					<span>查询</span>
					<div data-dojo-type="dijit.Menu" id="editMenu11" style="display: none;">
						<div data-dojo-type="dijit.MenuItem" data-dojo-props=" iconClass:'dijitIconTask'">火警事件
							<script type="dojo/on" data-dojo-event="click" args="events">
								var tree=dijit.byId("ordTreeHistory");
								var items=tree.selectedItems;
								//	alert(items.length)
								var cid=0;
								if (items.length>0) cid = items[0].idx;
								//	alert(cid);

								startdate = dijit.byId("event_start_date1");
								enddate  = dijit.byId("event_end_date1");
								var dateX = dojo.date.locale.format(startdate.getValue(), {selector:"date", datePattern:'yyyyMMdd' } );
								var dateY = dojo.date.locale.format(enddate.getValue(),{selector:"date",datePattern:'yyyyMMdd'});
								if(dateX>=dateY){
									alert("你输入的日期错误！");
								}
								var store_event = new dojo.data.ItemFileReadStore({'url':ctxpath+"/DataAction!getEvents.action?type=fire&startdate="+startdate+"&enddate="+enddate+"&cid="+cid});				
								var gridevent=dijit.byId("gridEventList");
								gridevent.setStore(store_event, {id:"*"}, "*");
								gridevent.startup(); 
							</script>		
						
						</div>

						<div data-dojo-type="dijit.MenuItem" data-dojo-props=" iconClass:'dijitIconTask'">操作记录
							<script type="dojo/on" data-dojo-event="click" args="events">
						//		alert("操作记录!");

								var tree=dijit.byId("ordTreeHistory");
								var items=tree.selectedItems;
								//	alert(items.length)
								var cid=0;
								if (items.length>0) cid = items[0].idx;
								//	alert(cid);

								startdate = dijit.byId("event_start_date1");
								enddate  = dijit.byId("event_end_date1");
								var dateX = dojo.date.locale.format(startdate.getValue(), {selector:"date", datePattern:'yyyyMMdd' } );
								var dateY = dojo.date.locale.format(enddate.getValue(),{selector:"date",datePattern:'yyyyMMdd'});
								if(dateX>=dateY){
									alert("你输入的日期错误！");
								}
								var store_event = new dojo.data.ItemFileReadStore({'url':ctxpath+"/DataAction!getEvents.action?type=device&startdate="+startdate+"&enddate="+enddate+"&cid="+cid});				
								var gridevent=dijit.byId("gridEventList");
								gridevent.setStore(store_event, {id:"*"}, "*");
								gridevent.startup(); 
							</script>	
						</div>

						<div data-dojo-type="dijit.MenuItem" data-dojo-props=" iconClass:'dijitIconTask'">用户信息
							<script type="dojo/on" data-dojo-event="click" args="events">
						//		alert("用户信息!");

								var tree=dijit.byId("ordTreeHistory");
								var items=tree.selectedItems;
								//	alert(items.length)
								var cid=0;
								if (items.length>0) cid = items[0].idx;
								//	alert(cid);

								startdate = dijit.byId("event_start_date1");
								enddate  = dijit.byId("event_end_date1");
								var dateX = dojo.date.locale.format(startdate.getValue(), {selector:"date", datePattern:'yyyyMMdd' } );
								var dateY = dojo.date.locale.format(enddate.getValue(),{selector:"date",datePattern:'yyyyMMdd'});
								if(dateX>=dateY){
									alert("你输入的日期错误！");
								}
								var store_event = new dojo.data.ItemFileReadStore({'url':ctxpath+"/DataAction!getEvents.action?type=user&startdate="+startdate+"&enddate="+enddate+"&cid="+cid});				
								var gridevent=dijit.byId("gridEventList");
								gridevent.setStore(store_event, {id:"*"}, "*");
								gridevent.startup(); 
							</script>	
						</div>
						
						
					</div>
					</div>
				</div>
				<div dojoType="dijit.layout.ContentPane"  splitter="true" region="center" > 

					<!--  div id="demodiv" style="width: 100%;height: 100%;"></div> -->
					
					<div dojoType="dojo.data.ItemFileReadStore"  url="DataAction!getEvents.action?type=user&startdate=2012-01-01&enddate=2012-01-02&cid=0" jsId="ordStoreEventList"></div>
		 			<table dojoType="dojox.grid.EnhancedGrid" store="ordStoreEventList" id="gridEventList" jsId="gridEventList" data-dojo-props="plugins:{indirectSelection: {headerSelector:true, width:'40px', styles:'text-align: center;'}}"  
						loadingMessage="请等待，数据正在加载中......" errorMessage="对不起，你的请求发生错误!"
						>
  
			     	 <!--thead定义布局，这是不可缺少的！否则widget将不能工作-->
				    	<thead>
					        <tr>
					           <th field="number" editable="false" width="50px" styles="text-align:center;">序号</th>
					           <th field="company" editable="false" width="150px">供电公司</th>
					           <th field="time" editable="false" width="150px">时间</th>
					           <th field="event" editable="false" width="300px">事件</th>
					           <th field="note" editable="false" width="200px">备注</th>
					           
					       </tr>
				      	</thead>
				    </table>	
				</div>
				
 			</div>
 			
	 		<!-- 地图展示----------------------------------------------------------->
	 		<div dojoType="dijit.layout.BorderContainer" id="allmap" design="sidebar"  style="background:#180053;padding:0px;" >
		 		<div dojoType="dijit.layout.ContentPane" data-dojo-props='title:"Tree"' splitter="true" region="leading" style="width: 180px;"> 
	 				<!-- tree widget -->
	 				<div dojoType="dojo.data.ItemFileReadStore" url="ShenAction!getallCompany.action" jsId="ordStoreDeviceOption_bd"></div>
					<div dojoType="dijit.tree.ForestStoreModel" rootLabel="设备浏览" store="ordStoreDeviceOption_bd"  jsId="ordModelDeviceOption_bd"></div>
					<div data-dojo-type="dijit.Tree" id="ordTreeDeviceOption_bd"  data-dojo-props="model: ordModelDeviceOption_bd, openOnClick:true"  >
						<script type="dojo/method" event="onClick" args="item">
							overview(item,map);
					</script>
					</div>
	 			</div>     
				<div dojoType="dijit.layout.ContentPane" id="allmap_bd" region="center" > 
					
				</div>	
			</div>
	 		
	 		<!-- 公司管理----------------------------------------------------------->
	 		<div dojoType="dijit.layout.BorderContainer" id="companyEditor" gutters="false" style="padding:0px;"> 
				<div dojoType="dijit.layout.ContentPane" region="top"  id="company_bar"  >
 					<div class="div-normal" >公司管理</div> 
 					<button type="button" data-dojo-type="dijit.form.Button"  iconClass="buttonEditorIconNew" data-dojo-props=" "  >					
					<div style="width:66px;">新建</div>					
						<script type="dojo/on" data-dojo-event="click" data-dojo-args="evt">
       		 			//alert("新建");

							addCompany();
							
    					</script>					
					</button>
					<button data-dojo-type="dijit.form.Button" iconClass="dijitEditorIcon dijitEditorIconCut" data-dojo-props=" "  >					
					<div style="width:66px;">批量删除</div>					
						<script type="dojo/on" data-dojo-event="click" data-dojo-args="evt">
       		 			//alert("批量删除    to do ...");
						deleteCompanies();

/* 选择被选中的行 */ 
/*        var items = grid.selection.getSelected(); 		
        if(items.length){ 
           if(!confirm("Are you sure to delete?")){ 
              return false; 		
			 } 
            // 循环遍历所选择的行  
            array.forEach(items, function(selectedItem){ 
                if(selectedItem !== null){ 
                    // Delete the item from the data store:  
                    store.deleteItem(selectedItem); 
                } // end if 
            }); // end forEach 
        }else{ 
           alert("Please select row(s)"); 
 } 
*/




    					</script>					
					</button>
 				</div>
 				
				<div dojoType="dijit.layout.ContentPane"  splitter="false" region="center" > 

					<div dojoType="dojo.data.ItemFileWriteStore" url="DataAction!getCompanies.action?cid=0" jsId="ordStoreCompanies"></div>
		 			<table dojoType="dojox.grid.EnhancedGrid" store="ordStoreCompanies" id="gridCompanies" jsId="gridCompanies" data-dojo-props="plugins:{indirectSelection: {headerSelector:true, width:'40px', styles:'text-align: center;'}}"  
						loadingMessage="请等待，数据正在加载中......" errorMessage="对不起，你的请求发生错误!"
						>
  
			     	 <!--thead定义布局，这是不可缺少的！否则widget将不能工作-->
				    	<thead>
					        <tr>
					           <!--表头，field属性的值为json对象中由items指定的json对象数组中的属性明-->
					           
					           <th field="number" editable="false" width="50px" styles="text-align:center;">序号</th>
					           <th field="name" editable="true" width="200px">公司名称</th>
					           <th field="note" editable="true" width="200px">备注</th>
					           <th field="id" formatter="formatterCompnay" editable="false" width="160px">编辑</th>	
					       </tr>
				      	</thead>
				    </table>	
				    
				</div>
	 		
	 		
	 		</div>
	 		
	 		<!-- 设备管理----------------------------------------------------------->
		 	<div dojoType="dijit.layout.BorderContainer" id="device_option" design="sidebar" title="<b class='tabtitle'>雷达设备管理</b>"  style="background:#180053;padding:0px;" >
		 	
		 		<div dojoType="dijit.layout.ContentPane" data-dojo-props='title:"Tree"' splitter="true" region="leading" style="width: 200px;"> 
	 				<!-- tree widget -->
	 				<div dojoType="dojo.data.ItemFileReadStore" url="DataAction!getCompanyTree.action" jsId="ordStoreDeviceOption"></div>
					<div dojoType="dijit.tree.ForestStoreModel" rootLabel="探山火设备管理" store="ordStoreDeviceOption"  jsId="ordModelDeviceOption"></div>
					<div data-dojo-type="dijit.Tree" id="ordTreeDeviceOption"  data-dojo-props="model: ordModelDeviceOption, openOnClick:true"  >
						<script type="dojo/method" event="onClick" args="item">
				//		alert(item.station_+' '+item.rack_+' '+item.slot_);
						var store = new dojo.data.ItemFileWriteStore({'url':ctxpath+"/DataAction!getDevices.action?cid="+item.idx});				
						var grid=dijit.byId("gridDeviceOption");
						grid.setStore(store, {id:"*"}, "*");
						grid.startup(); 
					</script>
					</div>
	 			</div>     
	
 				<div dojoType="dijit.layout.ContentPane" region="top"  gutters="false" id="query_bar_device_Option"  >
 					<div class="div-normal" >探山火设备管理</div>
 					<!--div class="div-normal" >正常运行</div> 
					<div class="div-abnormal" >停止运行</div --> 
					<button type="button" data-dojo-type="dijit.form.Button"  iconClass="buttonEditorIconNew" data-dojo-props=" "  >					
					<div style="width:66px;">新建</div>					
						<script type="dojo/on" data-dojo-event="click" data-dojo-args="evt">
       		 			//alert("新建");

							addDevice();
							
    					</script>					
					</button>
					<button data-dojo-type="dijit.form.Button" iconClass="dijitEditorIcon dijitEditorIconCut" data-dojo-props=" "  >					
					<div style="width:66px;">批量删除</div>					
						<script type="dojo/on" data-dojo-event="click" data-dojo-args="evt">
       		 			//alert("批量删除    to do ...");
						deleteDevices();
    					</script>					
					</button>
				</div>
				<div dojoType="dijit.layout.ContentPane" splitter="true" region="center" > 
					
					<div dojoType="dojo.data.ItemFileWriteStore" url="DataAction!getDevices.action?cid=0" jsId="ordStoreDeviceOption"></div>
		 			<table dojoType="dojox.grid.EnhancedGrid" store="ordStoreDeviceOption" id="gridDeviceOption" jsId="gridDeviceOption" data-dojo-props="plugins:{indirectSelection: {headerSelector:true, width:'30px', styles:'text-align: center;'}}"  
						loadingMessage="请等待，数据正在加载中......" errorMessage="对不起，你的请求发生错误!"
						>
  
			     	 <!--thead定义布局，这是不可缺少的！否则widget将不能工作-->
				    	<thead>
					        <tr>
					           <!--表头，field属性的值为json对象中由items指定的json对象数组中的属性明-->
					           
					           <!-- th width="30px" field="rowId" styles="text-align:center;" 
						             formatter="checkBoxFormatter" sortable="false" > 
					             <div> 
					               <input type="checkBox" id="gridId.checkBox" name="gridId.checkBox" 
					                  onclick="globalSelection();"/> 
					            </div> 
						       </th--> 
					           
					           <th field="number" editable="false" width="40px" styles="text-align:center;">序号</th>
					           <th field="company" editable="false" width="150px">供电公司</th>
					           <th field="name" editable="true" width="150px">铁塔名称</th>
					           <th field="towerid" editable="true" width="150px">铁塔标识</th>
					           <th field="deviceid" editable="true" width="80px">设备标识</th>
					           <th field="sms_mobiles" editable="true" width="150px">报警号码</th>
					   		   <th field="id" formatter="formatterDevice" editable="false" width="400px">编辑</th>
					       </tr>
				      	</thead>
				    </table>	
					
					
				</div>	
				
 			</div>
 			
 			<!-- 用户管理----------------------------------------------------------->
		 	<div dojoType="dijit.layout.BorderContainer" id="user_option" design="sidebar" title="<b class='tabtitle'>用户管理</b>"  style="background:#180053;padding:0px;" >
		 	
		 		<div dojoType="dijit.layout.ContentPane" data-dojo-props='title:"Tree"' splitter="true" region="leading" style="width: 200px;"> 
	 				<!-- tree widget -->
	 				<div dojoType="dojo.data.ItemFileReadStore" url="DataAction!getCompanyTree.action" jsId="ordStoreUserOption"></div>
					<div dojoType="dijit.tree.ForestStoreModel" rootLabel="探山火用户管理" store="ordStoreUserOption"  jsId="ordModelUserOption"></div>
					<div data-dojo-type="dijit.Tree" id="ordTreeUserOption"  data-dojo-props="model: ordModelUserOption, openOnClick:true"  >
						<script type="dojo/method" event="onClick" args="item">
				//		alert(item.station_+' '+item.rack_+' '+item.slot_);
						var store = new dojo.data.ItemFileWriteStore({'url':ctxpath+"/DataAction!getUsers.action?cid="+item.idx});				
						var grid=dijit.byId("usergrid");
						grid.setStore(store, {id:"*"}, "*");
						grid.startup(); 
					</script>
					</div>
	 			</div>     
	
 				<div dojoType="dijit.layout.ContentPane" region="top"  gutters="false" id="query_bar_User_Option"  >
 					<div class="div-normal" >探山火用户管理</div>
 					<!--div class="div-normal" >正常运行</div> 
					<div class="div-abnormal" >停止运行</div --> 
					<!--  input id="mycheck" name="mycheck" data-dojo-type="dijit/form/CheckBox" value="agreed" checked onChange="alert('onChange called with parameter = ' + arguments[0] + ', and widget value = ' + this.get('value'))"> <label for="mycheck">全选</label-->
					<button type="button" data-dojo-type="dijit.form.Button"  iconClass="buttonEditorIconNew" data-dojo-props=" "  >					
						<div style="width:66px;">新建</div>					
							<script type="dojo/on" data-dojo-event="click" data-dojo-args="evt">
       		 				//alert("新建");

							addUser();
							
    						</script>					
					</button>
					<button data-dojo-type="dijit.form.Button" iconClass="dijitEditorIcon dijitEditorIconCut" data-dojo-props=" "  >					
						<div style="width:66px;">批量删除</div>					
						<script type="dojo/on" data-dojo-event="click" data-dojo-args="evt">
       		 			alert("批量删除    to do ...");
						
						return;

/* 选择被选中的行 */ 
/*        var items = grid.selection.getSelected(); 		
        if(items.length){ 
           if(!confirm("Are you sure to delete?")){ 
              return false; 		
			 } 
            // 循环遍历所选择的行  
            array.forEach(items, function(selectedItem){ 
                if(selectedItem !== null){ 
                    // Delete the item from the data store:  
                    store.deleteItem(selectedItem); 
                } // end if 
            }); // end forEach 
        }else{ 
           alert("Please select row(s)"); 
 } 
*/

 /* Get all selected items from the Grid: */
             var items = grid.selection.getSelected();
             if(items.length){
                 /* Iterate through the list of selected items.
                    The current item is available in the variable
                    "selectedItem" within the following function: */
                 dojo.forEach(items, function(selectedItem){
                     if(selectedItem !== null){
                         /* Iterate through the list of attributes of each item.
                            The current attribute is available in the variable
                            "attribute" within the following function: */
                         dojo.forEach(grid.store.getAttributes(selectedItem), function(attribute){
                             /* Get the value of the current attribute:*/
                             var value = grid.store.getValues(selectedItem, attribute);
                             /* Now, you can do something with this attribute/value pair.
                                Our short example shows the attribute together
                                with the value in an alert box, but we are sure, that
                                you'll find a more ambitious usage in your own code:*/
                             alert('attribute: ' + attribute + ', value: ' + value);
                         }); /* end forEach */
                     } /* end if */
                 }); /* end forEach */
             } /* end if */


    					</script>					
					</button>
 				</div>
 				
				<div dojoType="dijit.layout.ContentPane" splitter="true" region="center" > 					
					<div id="gridUserOption" ></div>
				</div>	
			
 			</div>
	 					
 		</div> 
           
        <!-- 页面脚 --> 
 		<div dojoType="dijit.layout.ContentPane" region="bottom" style="background:#21695A;padding:0px;" > 
 			<div dojoType="dijit.layout.BorderContainer" design="sidebar" gutters="false" id="footer" >
 				<div dojoType="dijit.layout.ContentPane" region="center" gutters="false" href="app/copyright.txt" style="padding:1px;">
		 		</div>
	 			<div dojoType="dijit.layout.ContentPane" region="trailing" gutters="false" style="width: 330px;height:20px;margin:0px  auto;padding:1px;"  > 
 					南京炬名电力科技有限公司
 				</div>
 			</div>
		</div> 		
		
 	</div> 
 	
 	
 	<!-- 修改密码对话框 --> 
	<div data-dojo-type="dijit/Dialog" data-dojo-id="modifyPasswordDialog" title="修改用户密码">
	    <table class="dijitDialogPaneContentArea">
	        <tr>
	            <td><label for="oldPwd">旧密码:</label></td>
	            <td><input type="password" data-dojo-type="dijit/form/TextBox" name="oldPwd" id="oldPwd"></td>
	        </tr>
	        <tr>
	            <td><label for="newPwd">新密码:</label></td>
	            <td><input type="password" data-dojo-type="dijit/form/TextBox" name="newPwd" id="newPwd"></td>
	        </tr>
	        <tr>
	            <td><label for="rePwd">再输一次:</label></td>
	            <td><input type="password" data-dojo-type="dijit/form/TextBox" name="rePwd" id="rePwd"></td>
	        </tr>
	    </table>

	    <div class="dijitDialogPaneActionBar">
	        <button data-dojo-type="dijit/form/Button" type="button" data-dojo-props="onClick:function(){updateUserPassword();}" id="okUser">确认</button>
	        <button data-dojo-type="dijit/form/Button" type="button" data-dojo-props="onClick:function(){modifyPasswordDialog.hide();}"
	                id="cancelUser">取消</button>
	    </div>
	</div>
</body>
</html>