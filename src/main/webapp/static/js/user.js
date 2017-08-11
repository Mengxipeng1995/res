var userStore = Ext.create('Ext.data.Store', {
					fields:['userId','userName','nickName','roleName'],
					autoLoad: false,
					pageSize : 20,
					proxy : {
						type : 'ajax',
						url : 'ugmc/outline',
						reader : {
							root : 'content',
							totalProperty : 'totalElements'
						},
						simpleSortMode : true
					}
						});
var updateUserGroupTreeStore = Ext.create('Ext.data.TreeStore', {
	autoLoad: false,
	proxy:{
		type : 'ajax',
		url : 'group/getGroupWithUserId'
	},
	root:{
		nodeType : 'async', 
		expanded : true,
		name:'机械出版社',
		flag:true,
		id:'1'
	},
	fields: ['id','text'],
	nodeParam:'id',
	listeners:{
		load:function( store , records , successful , eOpts ) {
			Ext.getCmp('updateUserGroupTreeStore').expandAll();
	}
		
	}
});
//用户分类树数据
var userGroupTreeStore = Ext.create('Ext.data.TreeStore', {
	autoLoad: false,
	proxy:{
		type : 'ajax',
		url : 'group/getSons'
	},
	root:{
		nodeType : 'async', 
		expanded : true,
		id:'1',
		text:'机械出版社'
	},
	fields: ['id','text'],
	nodeParam:'id',
	listeners:{
		load:function( store , records , successful , eOpts ) {
			if(successful){
				Ext.Array.each(records, function(record, index, countriesItSelf) {
					record.data.text=record.data.name;
					record.commit();
				});


			}
	}
		
	}
});
//用户未拥有角色
var userRoleLeftStore = Ext.create('Ext.data.Store', {
	fields: ['roleId', 'roleName','roleDesc'],
	proxy : {
		type : 'ajax',
		url : 'role/getUnHaveRole',
		simpleSortMode : true
	}
	});
//用户已拥有角色
var userRoleRightStore = Ext.create('Ext.data.Store', {
	fields: ['roleId', 'roleName','roleDesc'],
	proxy : {
		type : 'ajax',
		url : 'role/getHaveRole',
		simpleSortMode : true
	}
	});


//用户组织机构
var userGroupWestPanel = Ext.create('Ext.tree.Panel', {
    title: '用户机构',
    width: 200,
    heigth: 'auto',
    split: true, //显示分隔条
    region: 'west',
    collapsible: true,
    store : userGroupTreeStore,
    rootVisible: true,
    listeners : {
	       'itemclick' : function(view,record,item,index,e,eOpts){
	    	   userStore.proxy.extraParams={groupid:record.id};
	    	   userStore.loadPage(1);
	       }
    }
});


var columns=[
{
	header:"roleId",
	dataIndex:"roleId",
	align : 'center',
	width : '3%',
	hidden:true
},
{
	header:"角色名",
	dataIndex:"roleName",
	align : 'center',
	width : '30%'
},
{
	header:"说明",
	dataIndex:"roleDesc",
	align : 'center',
	width : '67%'
}];

//用户管理左侧菜单
var userWestMenu = new Ext.Panel({
	     title:"功能列表",
	     region:"west",
	     height:200,
	     split:true,
	     collapsible:true,
	     width:120,
	     layout:{
	    	 	type: 'vbox',
	     },
	     items:[
	           {
 	    	   xtype:'button', 
               // id:'allUsersBtn', 
                text:'用户列表',
                width:'100%',
                handler:function(){
              	  userStore.loadPage(1);
              	  userCenPanel.removeAll(false);
              	  userCenPanel.add(userGroupWestPanel); 
              	userCenPanel.add(userListPanel);
                }
 	       },
 	       {
 	    	   xtype:'button', 
                //id:'addUserBtn', 
                text:'新增用户',
                width:'100%',
                handler:function(){
                	updateUserGroupTreeStore.proxy.extraParams={
						    };
					updateUserGroupTreeStore.load();
                	
              	  userCenPanel.removeAll(false);
              	  userCenPanel.add(addUserPanel);
                }
 	       }
 	       
 	      
	     ]
});
var userLoginNameValidated=false,updateUserFlag=false;
var addUserPanel=Ext.create('Ext.panel.Panel',{
   	id:'addUserPanel',
   	title:'新增/修改用户',
	width:'85%',
   	height:'100%',
   	region : 'center',
   	autoWidth:true,
   	autoScroll:true,
   	listeners:{
   		'added':function(){
   			if(updateUserFlag){
   				updateUserFlag=false;
   			}else{
   				Ext.getCmp("addUserForm").reset();
   			}
   		},
   		'removed':function(){
   			Ext.getCmp('userPasswordFormpassWord').show();
			Ext.getCmp('addUserConfirmPassword').show();
   		}
   	},
   	items: [
		{
			xtype:'form',
			width:550,
			buttonAlign: 'center',
			style:'margin:auto;'
			
		},
		{
			xtype:'form',
			id:'addUserForm',
			width:550,
			style:'border-width:0 0 0 0;margin:auto',
			buttonAlign: 'center',
			items:[
				{
					xtype:'fieldset', 
					title:'用户信息', 
					collapsible:false,
					width : 550,
					items:[
						{
							xtype: "textfield",
							id:'updateUserId',
							name:'userId',
							hidden:true
						},
						{	
							xtype: "textfield",
							fieldLabel:'登录名<font color="red">*</font>',
							labelAlign:'left',
							labelWidth:150,
							regex :/^[a-zA-Z0-9]{6,10}$/,
							regexText:"请输入6-10位的数字、大小写字母作为用户名",
							id:'addUserFormloginname',
							name:'userName',
							allowBlank: false,
							listeners:{
								'blur':function(field){
									var value=field.getValue();
									$.ajax({
										   url: "user/chekUserInfoConflict",
										   data: {'id':Ext.getCmp('updateUserId').getValue(),'attributeName':'userName','attributeValue':value},
										   async:false,
										   dataType: 'json',
										   success: function(msg){
											   console.info(msg);
											   if(msg.success){
												   Ext.Msg.alert('提示', '用户登录名重复');
												   userLoginNameValidated=false;
											   }else{
												   userLoginNameValidated=true;
											   }
											  
										   }
										});
								}
								
							}
						},
						{	
							xtype: "textfield",
							fieldLabel:'用户密码<font color="red">*</font>',
							inputType:'password',
							labelAlign:'left',
							labelWidth:150,
							id:'userPasswordFormpassWord',
							name:'password',
							minLength : 8,
		                    minText: '密码必须大于8位',
							allowBlank: false,
							blankText: '密码为必填项',
							regex :/^[0-9A-Za-z!@#$%^&*]{8,20}$/,
							regexText:"请输入8-20的数字、大小写字母、特殊符号(!@#$%^&*)作为密码",
							msgTarget:'side',
						},
						{	
							xtype: "textfield",
							fieldLabel:'确认密码<font color="red">*</font>',
							inputType:'password',
							labelAlign:'left',
							labelWidth:150,
							id:'addUserConfirmPassword',
							name:'confirmPassword',
							allowBlank: false,
							//vtype: 'password',
							password : 'userPasswordFormpassWord',
							msgTarget:'side',
						},
						{	
							xtype: "textfield",
							fieldLabel:'用户昵称<font color="red">*</font>',
							labelAlign:'left',
							labelWidth:150,
							allowBlank: false,
							id:'addUserFormusername',
							name:'nickName'
						},{	
							xtype: "textfield",
							id:'addUserFormusergroupid',
							hidden:true,
							name:'groupids'
						},
						{
							xtype:'treepanel',
							split: true, //显示分隔条
		        	        autoScroll:true,
		        	        rootVisible: true,
		        	        id:'updateUserGroupTreeStore',
		        	        store : updateUserGroupTreeStore,
		        	        columns:[
		    	    			{  
		    	    			    xtype: 'treecolumn', //this is so we know which column will show the tree  
		    	    			    text: '分类：',  
		    	    			    flex: 2,  
		    	    			    sortable: false, 
		    	    			    width: "50", 
		    	    			    dataIndex: 'name'
		    	    			},{  
		    	    	            xtype: 'checkcolumn',  
		    	    	            header: '授权',  
		    	    	            dataIndex: 'flag',  
		    	    	            width: "50%", 
		    	    	            renderer:function(value,cellmeta,record,rowIndex,columnIndex,stroe){
		    	    	            	if(value==true){
		    	    	            		changeAuthorizeItems(record.id,true);
		    	    	            	}else{
		    	    	            		changeAuthorizeItems(record.id,false);
		    	    	            	}
		    	    	            	return (new Ext.grid.column.CheckColumn).renderer(value);
		    	    	            	
		    	    	            	
		    	    	            },
		    	    	            stopSelection: false,
		    	    	            listeners:{
		        	    				checkchange:function(item, rowIndex, checked, eOpts){
		        	    					var category = updateUserGroupTreeStore.getAt(rowIndex);
		        	    					var rootid=updateUserGroupTreeStore.getRoot().id;
		        	    					if(checked){
		        	    						category.set('flag',true);
		        	    						if(category.id!=rootid)
		        	    						cascadeUpdateTree(category,true);
		        	    					}else{
		        	    						//根节点必须勾选
		        	    						
		        	    						if(category.id==rootid){
		        	    							//Ext.Msg.alert('提示', '根节点必须勾选');
		        	    							category.set('flag',true);
		        	    						}else{
		        	    							category.set('flag',false);
		        	    							cascadeUpdateTree(category,false);
		        	    						}
		        	    						
		        	    					}
		        	    					
		        	    					category.commit();
		        	    					
		        	    				}
		        	    			}
		    	    	            }
		    	    			]
						}
						
					]
				}
			
			],
			buttons:[
			    {
			    
					text: '保存',
					handler:function(){
						var form=Ext.getCmp("addUserForm");
						var ids="";
						//遍历勾选的节点
						updateUserGroupTreeStore.each(function(record){
							if(record.data.flag){
								ids+=record.id+";";
							}
						});
						form.getForm().findField('addUserFormusergroupid').setValue(ids);
						
						
						
						if(form.isValid()&&userLoginNameValidated){
							form.submit({
								clientValidation:true,
								url:'user/add',
								success : function(form, action) {
									userStore.loadPage(1);
									userCenPanel.removeAll(false);
									
									 userCenPanel.add(userGroupWestPanel); 
						              userCenPanel.add(userListPanel);
								},
								failure : function(form, action) {
									Ext.Msg.alert('提示', '添加失败,原因:'+action.result.msg);
								}
								});
						}else{
							Ext.Msg.alert('提示', '请输入合法的用户信息');
						}
					}
				},
				{ 
					text: '重置',
					handler:function(){
						var userid=Ext.getCmp('updateUserId').getValue();
						Ext.getCmp("addUserForm").reset();
						Ext.getCmp('updateUserId').setValue(userid);
					}
				},
				{ 
					text: '返回',
					handler:function(){
						userCenPanel.removeAll(false);
						
						 userCenPanel.add(userGroupWestPanel); 
			              userCenPanel.add(userListPanel);
					}
				}
			]
		
		}
   	]
});
var allUsersPanel=Ext.create('Ext.panel.Panel',{
   	title:'用户列表',
   	width:'85%',
   	height:'100%',
   	autoWidth:true,
   	region : 'center',
   	layout:{
   		type : "border"
   	},
   	dockedItems:[
		{
			xtype : 'toolbar',
			id : 'allUsersPanelTool',
			enableOverflow:true,
			items :[
				{
					xtype : 'button',
					text:'修改信息',
					handler:function(){
						var records =getGridSelectionRecords('allUserGrid');
						if (records.length != 1){
							Ext.Msg.alert('提示', '请选择一名用户修改信息');
						} else {
							updateUserFlag=true;
							userLoginNameValidated=true;
							
							
							updateUserGroupTreeStore.proxy.extraParams={
			        				userId:records[0].data.userId
								    };
							updateUserGroupTreeStore.load();
							
							Ext.getCmp('addUserForm').getForm().setValues(records[0].data);
							
							userCenPanel.removeAll(false);
							//因此密码输入框
							Ext.getCmp('userPasswordFormpassWord').hide();
							Ext.getCmp('addUserConfirmPassword').hide();
							var password=Ext.getCmp('userPasswordFormpassWord'),confirmPassword=Ext.getCmp('addUserConfirmPassword');
							password.setValue("trsadmin123");
							password.hide();
							confirmPassword.setValue("trsadmin123");
							confirmPassword.hide();
			              	userCenPanel.add(addUserPanel);
							
						}
					}
				},
				{
					xtype : 'button',
					text:'修改密码',
					handler:function(){
						var records =getGridSelectionRecords('allUserGrid');
						if (records.length != 1){
							Ext.Msg.alert('提示', '请选择一名用户修改信息');
							return;
						}
						var showOldPasswordFlag=false;
						if($userName!=records[0].data.userName){
							showOldPasswordFlag=true;
						}

							var passswordUpdateWindow=Ext.create("Ext.window.Window",{
		                    		autoWidth : true,
		                    		modal:true,
		                    		title:'修改密码',
		                    		resizable:false,
		                    		width: 500,
		                    		height:320,
		                    		constrain:true,
		                    		overflowY:'scroll',
		                    		overflowX:'hidden',
		                    		items:[
										{
										   	xtype:'panel',
											width:'100%',
										   	height:'100%',
										   	region : 'center',
										   	autoWidth:true,
										   	autoScroll:true,
										   	items: [
												{
													xtype:'form',
													id:'updatePasswordForm',
													width:'100%',
													style:'border-width:0 0 0 0;margin:auto',
													autoScroll : true,
													buttonAlign: 'center',
													items:[

														{	
															xtype: "textfield",
															name:'userName',
															labelWidth:150,
															fieldLabel: '用户名',
															readOnly:true,
															value:records[0].data.userName
															
														},
														{	
															xtype: "textfield",
															fieldLabel:'当前密码<font color="red">*</font>',
															inputType:'password',
															labelAlign:'left',
															labelWidth:150,
															id:'updatePasswordOldPassword',
															name:'oldPassword',
															minLength : 8,
															hidden:showOldPasswordFlag,
										                    minText: '密码必须大于8位',
															allowBlank: true,
															blankText: '密码为必填项',
															regex :/^[0-9A-Za-z!@#$%^&*]{8,20}$/,
															regexText:"请输入8-20的数字、大小写字母、特殊符号(!@#$%^&*)作为密码",
															msgTarget:'side'
														},
														{	
															xtype: "textfield",
															fieldLabel:'新密码<font color="red">*</font>',
															inputType:'password',
															labelAlign:'left',
															labelWidth:150,
															id:'updatePasswordNewPassword',
															name:'newPassword',
															minLength : 8,
										                    minText: '密码必须大于8位',
															allowBlank: false,
															blankText: '密码为必填项',
															regex :/^[0-9A-Za-z!@#$%^&*]{8,20}$/,
															regexText:"请输入8-20的数字、大小写字母、特殊符号(!@#$%^&*)作为密码",
															msgTarget:'side'
														}
													],
													buttons:[{
														text: '保存',
														handler:function(){
															var form=Ext.getCmp("updatePasswordForm");
															if (form.isValid()) {
																 form.submit({
								                		                	url:$ctx+'/user/updateUserPwdV2',
								                		                    success: function(form, action) {
								                		                       
								                		                    	passswordUpdateWindow.close();
								                		                    },
								                		                    failure: function(form, action) {
								                		                        Ext.Msg.alert('提示', action.result.msg);
								                		                    }
								                		                });
															}else{
																 Ext.Msg.alert('提示', '表单信息有误,请核查');
															}
															
															
															
															
															
														}
													},{ 
																	text: '返回',
																	handler:function(){
																		
																		passswordUpdateWindow.close();
																	}
																}
															]
												
												}
										   	]
										}
		                    		       ],
		                        	listeners:{
		                        		'close':function(panel){
		                        			
		                        		}
		                        	}
		                        }).show();
							
                     
						
					}
				},
				{
					xtype : 'button',
					text:'修改角色',
					handler:function(){
						var records = Ext.getCmp('allUserGrid').getSelectionModel().getSelection();
						if (records.length != 1){
							Ext.Msg.alert('提示', '请选择一名用户修改信息');
						} else {
							Ext.getCmp('updateUserRoleUserId').setValue(records[0].data.userId);
							userRoleLeftStore.proxy.extraParams={
									userId:records[0].data.userId
								    };
							userRoleLeftStore.load();
							userRoleRightStore.proxy.extraParams={
									userId:records[0].data.userId
								    };
							userRoleRightStore.load();
							userCenPanel.removeAll(false);
							userCenPanel.add(modifyUserRolePanel);
						}
						
					
						
					}
				},
				{
					xtype : 'button',
					text:'删除用户',
					hidden:true,
					handler:function(){
						
					}
				}		
			]
		}
   	],
   
		items:[{
			xtype:'grid',
			layout : 'fit',
			region : 'center',
			height:'100%',
			widht:'100%',
			store:userStore,
			id:'allUserGrid',
			autoWidth:true,
			columnLines:true,
			selModel:Ext.create('Ext.selection.CheckboxModel'),
			listeners : {
				'itemdblclick' : function(view,record, item, index, e,eOpts) {
					
				},
				'itemcontextmenu' : function(view,record,item,index,e,eOpts){
					
				}
			},
			columns : [
			{
				header:"userId",
				dataIndex:"userid",
				align : 'center',
				width : '3%',
				hidden:true
			},
			{
				header:"登录名",
				dataIndex:"userName",
				align : 'center',
				width : '30%'
			},
			{
				header:"用户名",
				dataIndex:"nickName",
				align : 'center',
				width : '33%'
			},
			{
				header:"用户角色",
				dataIndex:"roleName",
				align : 'center',
				width : '34%',
				renderer:function(value,cellmeta,record,rowIndex,columnIndex,stroe){
					return value;
				}
			}
		]
		}],
		bbar : [
		    {
			xtype : 'pagingtoolbar',
			displayInfo : true,
			width : '100%',
			height : 30,
			store:userStore,
			anchorSize : 100,
			displayMsg : '显示 {0} - {1} 条，共计 {2} 条',
			beforePageText : '第',
			afterPageText : '页-共{0}页',
			emptyMsg : "没有内容可以显示",
			nextText : '',
			prevText : '',
			firstText : '',
			lastText : ''
		    } 
		]
}); 

var userRoleLeftGrid = Ext.create('Ext.grid.Panel', {
	   width : 500,
	   height:500,
 multiSelect: true,  
 viewConfig: {  
     plugins: {  
         ptype: 'gridviewdragdrop',  
         dragGroup: 'userRoleLeftGrid',
         dropGroup: 'userRoleRightGrid'
     },  
     listeners: {  
         drop: function(node, data, dropRec, dropPosition) {
             var dropOn = dropRec ? ' ' + dropPosition + ' ' + dropRec.get('name') : ' on empty view';  
         }  
     }  
 },  
 store : userRoleLeftStore,  
 columns:columns,  
 stripeRows : true,  
 title : '未授权角色',
 tools: [
     {
	    	   type : 'right',
	    	   tooltip: '全选',
	           scope: this,
	           hidden:true,
	           handler: function(){
	           }
     }
     
 ],
 border:false,
 margin: '5 0 0 0'
});

var userRoleRightGrid = Ext.create('Ext.grid.Panel', {
	   width : 500,
	   height:500,
multiSelect: true,  
viewConfig: {  
  plugins: {  
      ptype: 'gridviewdragdrop',  
      dragGroup: 'userRoleRightGrid',
      dropGroup: 'userRoleLeftGrid'
  },  
  listeners: {  
      drop: function(node, data, dropRec, dropPosition) {
          var dropOn = dropRec ? ' ' + dropPosition + ' ' + dropRec.get('name') : ' on empty view';  
      }  
  }  
},  
store : userRoleRightStore,  
columns:columns,  
stripeRows : true,  
title : '已拥有角色',
tools: [
  {
	    	   type : 'right',
	    	   tooltip: '全选',
	    	   hidden:true,
	           scope: this,
	           handler: function(){
	           }
  }
  
],
border:false,
margin: '5 0 0 0'
});



var modifyUserRolePanel=Ext.create('Ext.panel.Panel',{
   	title:'用户角色修改',
	width:'85%',
   	height:'100%',
   	region : 'center',
   	layout:'column',
   	buttonAlign: 'center',
   	layout:{
		type: 'table',
		columns: 3
	},
   	autoWidth:true,
   	autoScroll:true,
   	dockedItems: [{
   	    xtype: 'toolbar',
   	    dock: 'top',
   	    hidden:true,
   	    items: [
   	        {
		        xtype: 'textfield',
		        id:'updateUserRoleUserId',
		        allowBlank: false  // requires a non-empty value
		    }
   	    ]
   	}],
   	items: [ 
		{
   		    	width : 200,
   		    	border:false
   		    },
			userRoleLeftGrid,
			userRoleRightGrid
            ],
            buttons:[
            		    
         			{
         				text:'保存',
         				handler:function(){
         					var roleIds="";
         					userRoleRightStore.each(function(record){
         						roleIds+=record.id+";";
         					});
         					var userId=Ext.getCmp('updateUserRoleUserId').getValue();
         					$.ajax({
     						   url: "user/saveUserRole",
     						   data: {'id':userId,'roleIds':roleIds},
     						   async:false,
     						   dataType: 'json',
     						   success: function(msg){
     							 userStore.loadPage(1);
     							 Ext.Msg.alert('提示', '保存成功');
     							  
     							userCenPanel.removeAll(false);
     							userCenPanel.add(userGroupWestPanel); 
   				             userCenPanel.add(userListPanel);
     							   
     						   }
     						}); 
         				}
         			},
         			
         			{
         				text:'返回',
         				handler:function(){
							userCenPanel.removeAll(false);
							userCenPanel.add(userGroupWestPanel); 
				             userCenPanel.add(userListPanel);
         				}
         			}
         			
         	   	]
});




var userListPanel=new Ext.Panel({
   	id:'userCenPanel',
   	height : "85%",
   	width : "100%",
   	region : 'center',
   	autoWidth:true,
   	layout: "border",
   	items : [ allUsersPanel ]
});

var userCenPanel = new Ext.Panel({
	height : "85%",
	width : "100%",
	border : 5,
	region : 'center',
	autoWidth:true,
	layout: "border",
	items : [userGroupWestPanel,  userListPanel]
});


var userMainPanel=new Ext.Panel({
	height : "85%",
	width : "100%",
	region : 'center',
	autoWidth:true,
	layout: "border",
	items : [ userWestMenu, userCenPanel ]
});