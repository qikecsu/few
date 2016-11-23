//全局变量定义
var xxx=-1;									//为IE提交不同的参数，迫使IE向后台

//控制台输出对象
var socket_console = {log : function(text) {/*infoPanel.innerHTML += text + "<br>";*/}};
// WebSocket演示对象
var push_socket = {
	socket : null, 	// WebSocket连接对象
	host : '',		// WebSocket连接 url
	connect : function() { 	// 连接服务器
		window.WebSocket = window.WebSocket || window.MozWebSocket;
		if (!window.WebSocket) {	// 检测浏览器支持
			socket_console.log('Error: WebSocket is not supported .');
			alert('请使用现代浏览器。');
			return;
		}
		this.socket = new WebSocket(this.host); // 创建连接并注册响应函数
		this.socket.onopen = function(){socket_console.log("websocket is opened .");};
		this.socket.onmessage = function(message) {
		//	socket_console.log(message.data);
			
/*			var reg=new RegExp("t","g"); //创建正则RegExp对象    
	    	var newstr1=message.data.replace(reg,"tableid");  
	    	reg = new RegExp("f","g"); //创建正则RegExp对象    
	    	var newstr2=newstr1.replace(reg,"fieldid");
	    	reg = new RegExp("v","g"); //创建正则RegExp对象    
	    	var newstr3=newstr2.replace(reg,"value");
	    	var newstr=newstr3.replace("datableidas","datas");
			*/
			alert(message.data);
						
		};
		this.socket.onclose = function(){
			socket_console.log("websocket is closed .");
			push_socket.socket = null; // 清理
		};
	},
	send : function(message) {	// 发送消息方法
		if (this.socket) {
			this.socket.send(message);
			return true;
		}
		socket_console.log('please connect to the server first !!!');
		return false;
	}
};
// 初始化WebSocket连接 url
push_socket.host=(window.location.protocol == 'http:') ? 'ws://' : 'wss://' ;
push_socket.host += window.location.host + '/few/websocket/chat';
//push_socket.host += window.location.host + '/monitor/chat';

//var URL = "ws://localhost:8080/monitor/websocket/web-client"; 

//push_socket.host = "ws://localhost:8080/monitor/websocket/web-client";


var timer = function(){
	var dt = new Date();
	var iYear = dt.getFullYear();
	var iMonth = ("0"+ (dt.getMonth() +1)).slice(-2);
	var iDate = ("0"+ dt.getDate()).slice(-2);
//	document.write(iYear + "-" + iMonth + "-" + iDate);	
	var iHour = ("0"+ dt.getHours()).slice(-2); //获取当前小时数(0-23)
	var iMinute = ("0"+ dt.getMinutes()).slice(-2); //获取当前分钟数(0-59)
	var iSecond = ("0"+ dt.getSeconds()).slice(-2); //获取当前秒数(0-59)
	//console.log("current time:", iYear + "-" + iMonth + "-" + iDate + " "+iHour+":"+iMinute+":"+iSecond);
	//dojo.byId('timer').innerHTML = ""+iYear + "-" + iMonth + "-" + iDate + " "+iHour+":"+iMinute+":"+iSecond;
	window.parent.document.getElementById('timer').innerHTML = ""+iYear + "-" + iMonth + "-" + iDate + " "+iHour+":"+iMinute+":"+iSecond;
	
	xxx++;
	
	return;
	
};

function resizeWindow(){
    if (window.screen) {//判断浏览器是否支持window.screen判断浏览器是否支持screen     
      var myw = screen.availWidth;   //定义一个myw，接受到当前全屏的宽     
      var myh = screen.availHeight;  //定义一个myw，接受到当前全屏的高     
      window.moveTo(0, 0);           //把window放在左上脚     
      window.resizeTo(100, 100);     //把当前窗体的长宽跳转为myw和myh  
      window.resizeTo(myw, myh);     //把当前窗体的长宽跳转为myw和myh     
    } 
}

function showDojoDialog(content, title) {
	require([ "dijit/Dialog" ], function(Dialog) {
		new Dialog({
			title : title = title ? title : '',
			content : content + '',
			style: "width: 300px",
			onHide : function() {
				this.destroyRecursive();
			}
		}).show();
	});
}

function tooltip (node,content) { 
	/*require(["dijit/Tooltip",  "dojo/on", "dojo/mouse" ], function(Tooltip, on,mouse) {
		Tooltip.show(content, node, ['below']); 
		Tooltip.showDelay = 2000;
		//on.once(node, mouse.leave, function(){
		//      Tooltip.hide(node);
		//});

	});*/
	dijit.Tooltip.show(content, node, ['below']);
	setTimeout(function () {
		require([ "dojo/on" ], function(on) {
			on.once(document.body, "click", function(){   
				//showDojoDialog("hide", "tooltip");
				dijit.Tooltip.hide(node);  
			}); 
		});
	}, 
	500);
}