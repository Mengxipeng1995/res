Ext.Ajax.on('requestcomplete',function(conn,response,options) {  
	   if(response.responseText.indexOf("xhsgg_login_jsp_tag_trs_20161016")!=-1){    
	       Ext.Msg.alert('提示', '请您登录后访问!', function(){    
	           window.location = 'res_login_jsp_tag_trs_20161114';     
	       });    
	   } 
	});
//获取表格被选中的数据
function getGridSelectionRecords(gridId){
	return Ext.getCmp(gridId).getSelectionModel().getSelection();
}


function cascadeUpdateTree(node,flag){
	var chileren=node.childNodes;
	if(chileren){
	//级联勾选
	if(flag){
		for(var i=0;i<chileren.length;i++){
			
			chileren[i].set('flag','true');
			chileren[i].commit();
			cascadeUpdate(chileren[i],flag)
		}
	}else{
	//级联取消	
		for(var i=0;i<chileren.length;i++){
			chileren[i].set('flag',null);
			chileren[i].commit();
			cascadeUpdate(chileren[i],flag)
		}
	}
	}
	
}