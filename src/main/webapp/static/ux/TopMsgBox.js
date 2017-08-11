Ext.TopMsgBox = function(){
	var msgCt;
    function createBox(t, s){
        return ['<div class="msg">',
                '<div class="x-box-tl"><div class="x-box-tr"><div class="x-box-tc"></div></div></div>',
                '<div class="x-box-ml"><div class="x-box-mr"><div class="x-box-mc" style="font-size=12px;"><h3>', t, '</h3>', s, '</div></div></div>',
                '<div class="x-box-bl"><div class="x-box-br"><div class="x-box-bc"></div></div></div>',
                '</div>'].join('');
    };
    return {
        msg : function(title, message,autoHide,pauseTime){
            if(!msgCt){
                msgCt = Ext.DomHelper.insertFirst(document.body, {id:'msg-div2012',style:'position:absolute;top:10px;width:300px;margin:0 auto;z-index:20000;'}, true);
                msgCt.alignTo(document, 't-t');
            }
            //给消息框右下角增加一个关闭按钮
            message+='<br><span style="text-align:right;font-size:12px;display:block;">' +
              '<font color="blank"><u style="cursor:pointer;" onclick="Ext.TopMsgBox.hide(this);">关闭</u></font></span>'
            var m = Ext.DomHelper.append(msgCt, {html:createBox(title, message)}, true);
            m.hide();
            m.slideIn('t');
            pauseTime = autoHide ? pauseTime:1000;
            m.ghost("tr", {delay:pauseTime,remove:true});
        },
        hide:function(v){
	        var msg=Ext.get(v.parentElement.parentElement.parentElement.parentElement.parentElement.parentElement);
	        msg.ghost("tr", {remove:true});
        },
        init:function(){
            if(!msgCt){
                msgCt = Ext.DomHelper.insertFirst(document.body, {id:'msg-div2012',style:'position:absolute;top:10px;width:300px;margin:0 auto;z-index:20000;'}, true);
                msgCt.alignTo(document, 't-t');
            }
        }
    };
}();