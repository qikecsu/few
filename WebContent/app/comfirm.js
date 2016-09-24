/** 
  * 使用dojo dialog模拟confirm<br> 
  * 返回true/false<br> 
  * 前提是已经引用了相应的dojo.js<br> 
  * @author: DFH<br> 
  * @since: 2012-05-16 
*/  
function dojoConfirm(content,callback){  
    require([ "dijit/Dialog","dijit/form/Button"], function(Dialog) {  
        content=content+  
            "<br/><br/><br/><button dojoType='dijit.form.Button' id='yesButton'>确认</button>" +  
            "<button dojoType='dijit.form.Button' id='noButton'>取消</button>";  
          
        var confirmDialog = new Dialog({  
            id:"confirmDialog",  
            title : '电力线路山火预警系统',  
            content : content,  
            onHide : function() {  
                this.destroyRecursive();  
            }  
        });  
        confirmDialog.startup();  
          
        var yesButton = dijit.byId('yesButton');  
        var noButton = dijit.byId('noButton');  
        //如果点击确定按钮  
        dojo.connect(yesButton, 'onClick', function(mouseEvent) {  
        confirmDialog.hide();  
        callback();  
        });  
        //如果点击取消按钮  
        dojo.connect(noButton, 'onClick', function(mouseEvent) {  
        confirmDialog.hide();});  
          
        confirmDialog.show();  
    });  
}  


function dojoAlert(content,callback){  
    require([ "dijit/Dialog","dijit/form/Button"], function(Dialog) {  
        content=content+  
            "<br/><br/><br/><div class='dijitDialogPaneActionBar'><button dojoType='dijit.form.Button' id='yesButtonAlert'>确认</button>" +  
            "</div>";  
          
        var alertDialog = new Dialog({  
            id:"alertDialog",  
            title : '电力线路山火预警系统',  
            content : content,  
            onHide : function() {  
                this.destroyRecursive();  
            }  
        });  
        alertDialog.startup();  
          
        var yesButton = dijit.byId('yesButtonAlert');  
          
        //如果点击确定按钮  
        dojo.connect(yesButton, 'onClick', function(mouseEvent) {  
        alertDialog.hide();  
        callback();  
        });  
        
          
        alertDialog.show();  
    });  
}  
