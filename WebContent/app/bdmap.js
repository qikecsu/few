function AutoResizeImage(maxWidth,maxHeight,objImg){
	var img = new Image();
	img.src = objImg.src;
	var hRatio;
	var wRatio;
	var Ratio = 1;
	var w = img.width;
	var h = img.height;
	wRatio = maxWidth / w;
	hRatio = maxHeight / h;
	if (maxWidth ==0 && maxHeight==0){
	Ratio = 1;
	}else if (maxWidth==0){//
	if (hRatio<1) Ratio = hRatio;
	}else if (maxHeight==0){
	if (wRatio<1) Ratio = wRatio;
	}else if (wRatio<1 || hRatio<1){
	Ratio = (wRatio<=hRatio?wRatio:hRatio);
	}
	if (Ratio<1){
	w = w * Ratio;
	h = h * Ratio;
	}
	objImg.height = h;
	objImg.width = w;
	}

function overview(item,map){
	dojo.xhrGet({
		url: ctxpath+"/ShenAction!getDevicesfromcompany.action",

	    //handleAs: "json", 
	    handleAs: "text",
	    content: { 				
	    	cid : item.id
	    }, 
	    load: function(response, ioArgs){
	    	var obj = dojo.fromJson(response);//alert(obj);
	    	dojo.forEach(obj.items, function(items, inx){
	    		point = new BMap.Point(items.longitude,items.latitude);
	    		var marker = new BMap.Marker(point); 
	    		map.addOverlay(marker);              
	    		map.panTo(point);
	    		var label = new BMap.Label(items.name,{offset:new BMap.Size(20,-10)});
	    		marker.setLabel(label);
	    		marker.name = items.name;
	    		marker.status = items.status;
	    		marker.fire = items.fire;
	    		marker.deviceid = items.deviceid;
	    		marker.id = items.id;
	    		label.addEventListener("click",getPic);
	    		marker.addEventListener("click",getPic);
	    		function getPic(){
	    			var p = marker.getPosition();
	    			var opts = {
 							width : 490,    
 							height: 480,    
 							title:marker.name + "  状态：" + marker.status +"  火情：" + marker.fire
 						}
	    			var sContent = "<div><img src= "+ctxpath+"/ShenAction!getPicture.action?which=up&did="+marker.id+" onload='AutoResizeImage(450,0,this)'><br />" +
	    			"" +"<img src="+ctxpath+"/ShenAction!getPicture.action?which=down&did="+marker.id+" onload='AutoResizeImage(450,0,this)'>" +
					""+	"</div>";
	    			var infoWindow = new BMap.InfoWindow(sContent, opts);
					map.openInfoWindow(infoWindow, p);      
	    		}
	    	})
	    	
	    	return response;
	    },
	    error: function(response, ioArgs){
	    	alert(response.message); 
	        return response;
	    }
	    	});
}
function overviewdevice(item,map){
	point = new BMap.Point(item.longitude,item.latitude);
	var marker = new BMap.Marker(point); 
	map.addOverlay(marker);              
	map.panTo(point);
	var label = new BMap.Label(item.name,{offset:new BMap.Size(20,-10)});
	marker.setLabel(label);
	marker.name = item.name;
	marker.status = item.status;
	marker.fire = item.fire;
	marker.deviceid = item.deviceid;
	marker.id = item.id;
	label.addEventListener("click",getPics);
	marker.addEventListener("click",getPics);
	function getPics(){
		var p = marker.getPosition();
		var opts = {
					width : 490,    
					height: 480,    
					title:marker.name + "  状态：" + marker.status +"  火情：" + marker.fire
				}
		var sContent = "<div><img src= "+ctxpath+"/ShenAction!getPicture.action?which=up&did="+marker.id+" onload='AutoResizeImage(450,0,this)'><br />" +
		"" +"<img src="+ctxpath+"/ShenAction!getPicture.action?which=down&did="+marker.id+" onload='AutoResizeImage(450,0,this)'>" +
		""+	"</div>";
		var infoWindow = new BMap.InfoWindow(sContent, opts);
		map.openInfoWindow(infoWindow, p);  
	}
}