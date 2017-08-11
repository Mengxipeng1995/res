var  authorizeItems={};
function changeAuthorizeItems(id,flag){
	var pro='menu:'+id;
	
	if(flag){
		authorizeItems[pro]='';
	}else{
		delete authorizeItems[pro]; 
	}
	
}
function cascadeUpdate(node,flag){
	var chileren=node.childNodes;
	if(chileren){
	//级联勾选
	if(flag){
		for(var i=0;i<chileren.length;i++){
			
			chileren[i].set('checkFlag','true');
			changeAuthorizeItems(chileren[i].id,flag);
			chileren[i].commit();
			cascadeUpdate(chileren[i],flag)
		}
	}else{
	//级联取消	
		for(var i=0;i<chileren.length;i++){
			chileren[i].set('checkFlag',null);
			changeAuthorizeItems(chileren[i].id,flag);
			chileren[i].commit();
			cascadeUpdate(chileren[i],flag)
		}
	}
	}
	
}
var resourcesTreeStore = Ext.create('Ext.data.TreeStore', {
	autoLoad : false,
	proxy:{
		type : 'ajax',
		url : 'menu/getMenuByParentId'
	},
	root:{
		nodeType : 'async', 
		expanded : true,
		id:'-1'
	},
	fields: ['id','text','checkFlag'],
	listeners:{
		'load':function(){
			authorizePanel.expandAll();
		}
	}
    
});

var roleStore = Ext.create('Ext.data.Store', {
	fields:['id','roleName','roleDesc','roleLevel'],
	proxy : {
		type : 'ajax',
		url : 'role/getRoleList',
		simpleSortMode : true
	}
});

var authorizePanel = Ext.create('Ext.tree.Panel', {
    width: '100%',
    split: true, //显示分隔条
    region: 'center',
    store : resourcesTreeStore,
    rootVisible: false,
    scrollable:true,
    dockedItems: [{
        xtype: 'toolbar',
        dock: 'top',
        hidden:true,
        items: [{
            xtype: 'textfield',
            id:'authorizeRoleId'
        }]
    }],
//    listeners:{
//    	added:function(treePanel){
//    		treePanel.expandAll();
//    	}
//    },
    columns:[
			{  
			    xtype: 'treecolumn', //this is so we know which column will show the tree  
			    text: '资源',  
			    flex: 2,  
			    sortable: false, 
			    width: "50", 
			    dataIndex: 'text'
			   
			},{  
	            xtype: 'checkcolumn',  
	            header: '授权',  
	            dataIndex: 'checkFlag',  
	            width: "50%", 
	            renderer:function(value,cellmeta,record,rowIndex,columnIndex,stroe){ 
	            	
	            	return (new Ext.grid.column.CheckColumn).renderer(value);
	            },
	            stopSelection: false,
	            listeners:{
	            	checkchange: function (item, rowIndex, checked, eOpts) {
	                	var menu = resourcesTreeStore.getAt(rowIndex);
	                	var parentNode=menu.parentNode;
	                	//勾选父节点
	                	while(checked&&parentNode){
	                		parentNode.set('checkFlag',true);
	                		parentNode.commit();
	                			break;
	                	}
	                	cascadeUpdate(menu,checked);
	                	
	                	//changeAuthorizeItems(menu.id,checked);
                    }
	            }
	        }
             ],
	bbar:{ 
		xtype: 'button', 
		text: '保存',
		handler:function(){
			var ids="";
//			for (var menu in authorizeItems) {
//				  ids+=menu.split(':')[1]+";";
//				}
			//遍历勾选的节点
			resourcesTreeStore.each(function(record){
				if(record.data.checkFlag){
					ids+=record.id+";";
				}
			});
				$.ajax({
					type: "POST",
				   url: "role/saveAuthorization",
				   data: {'roleId':Ext.getCmp('authorizeRoleId').getValue(),'resourcesIds':ids},
				   async:false,
				   dataType: 'json',
				   success: function(msg){
					   roleStore.load();
					   Ext.Msg.alert('提示', '保存成功');
					   
	              	  roleCenPanel.removeAll(false);
	              	  roleCenPanel.add(allRolesPanel);
					   
				   }
				});
			
		}
		
	}
});

//角色管理左侧菜单
var roleWestMenu = new Ext.Panel({
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
                text:'角色列表',
                width:'100%',
                handler:function(){
              	  roleStore.load();
              	  roleCenPanel.removeAll(false);
              	  roleCenPanel.add(allRolesPanel);
                	
                }
 	       },
 	       {
 	    	   xtype:'button', 
                text:'新增角色',
                width:'100%',
                handler:function(){
                	roleCenPanel.removeAll(false);
                	roleCenPanel.add(addRolePanel);
                }
 	       }
 	       
 	      
	     ]
});
var roleNameValidated=false,updateRoleFlag=false;
var addRolePanel=Ext.create('Ext.panel.Panel',{
   	id:'addRolePanel',
   	title:'新增/修改角色',
	width:'85%',
   	height:'100%',
   	region : 'center',
   	autoWidth:true,
   	autoScroll:true,
   	listeners:{
   		'added':function(){
   			if(updateRoleFlag){
   				updateRoleFlag=false;
   			}else{
   				Ext.getCmp("addRoleForm").reset();
   			}
   			
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
			id:'addRoleForm',
			width:550,
			style:'border-width:0 0 0 0;margin:auto',
			buttonAlign: 'center',
			items:[
				{
					xtype:'fieldset', 
					title:'角色信息', 
					collapsible:false,
					width : 550,
					items:[
						{
							xtype: "textfield",
							id:'updateRoleId',
							name:'id',
							hidden:true
						},
						{	
							xtype: "textfield",
							fieldLabel:'角色名称<font color="red">*</font>',
							labelAlign:'left',
							labelWidth:150,
							regex :/^[a-zA-Z0-9]{4,20}$/,
							regexText:"请输入4-20位的数字、大小写字母作为角色名",
							name:'roleName',
							allowBlank: false,
							listeners:{
								'blur':function(field){
									var value=field.getValue();
									var roleId=Ext.getCmp('updateRoleId').getValue();
									$.ajax({
										   url: "role/checkRoleName",
										   data: {'roleId':roleId,'roleName':value},
										   async:false,
										   dataType: 'json',
										   success: function(msg){
											   console.info(msg);
											   if(msg){
												   Ext.Msg.alert('提示', '角色名重复');
												   roleNameValidated=false;
											   }else{
												   roleNameValidated=true;
											   }
											  
										   }
										});
								}
								
							}
						},
						{
					        xtype: 'numberfield',
					        name: 'roleLevel',
					        labelWidth:150,
					        fieldLabel: '角色等级',
					        value:999999,
					        maxValue: 999999,
					        minValue: 0
					    },
						{
					        xtype     : 'textareafield',
					        grow      : true,
					        labelAlign:'left',
							labelWidth:150,
					        name      : 'roleDesc',
					        fieldLabel: '角色描述'
					       // anchor    : '100%'
					    }
						
					]
				}
			
			],
			buttons:[
			    {
			    
					text: '保存',
					handler:function(){
						var form=Ext.getCmp("addRoleForm");
						if(form.isValid()&&roleNameValidated){
							form.submit({
								clientValidation:true,
								url:'role/addRole',
								success : function(form, action) {
									roleNameValidated=false;
									roleStore.loadPage(1);
									roleCenPanel.removeAll(false);
									roleCenPanel.add(allRolesPanel);
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
						var roleid=Ext.getCmp('updateRoleId').getValue();
						Ext.getCmp("addRoleForm").reset();
						Ext.getCmp('updateRoleId').setValue(roleid);
					}
				},
				{ 
					text: '返回',
					handler:function(){
						roleCenPanel.removeAll(false);
						roleCenPanel.add(allRolesPanel);
					}
				}
			]
		
		}
   	]
});

var tabPanel213=Ext.create('Ext.TabPanel', {
	width:'85%',
   	height:'100%',
   	autoWidth:true,
   	scrollable:true,
   	region : 'center',
   	layout:{
   		type : "border"
   	},
    items: [
        {
            title: '访问资源',
            items:[authorizePanel]
        },
        {
            title: '操作资源',
            items:[
            	{
					xtype:'panel',
					width:'85%',
				   	height:'100%',
				   	region : 'center',
				   	//autoWidth:true,
				   	style:'margin:0 auto',
				   	autoScroll:true,
					items:[{
						xtype:'form',
						width:'100%',
						region : 'center',
						id:'roleResourceForm',
						buttonAlign: 'center',
						items:[
							{
								xtype:'fieldset', 
								title:'资源表单', 
								collapsible:false,
								id:'roleResourceInfo',
								//width : 550,
								items:[
									
								]
							}
						
						],
						buttons:[
						    {
						    
								text: '保存',
								handler:function(){
									
									var form=Ext.getCmp("roleResourceForm");
									
									form.submit({
	        		                	url:$ctx+'/apply/launch',
	        		                    success: function(form, action) {
	        		                    	permissionGridStore.loadPage(1);
	        		                    },
	        		                    failure: function(form, action) {
	        		                        Ext.Msg.alert('提示', action.result.msg);
	        		                    }
	        		                });
									
								}
							},
							{ 
								text: '返回',
								handler:function(){
									roleCenPanel.removeAll(false);
									roleCenPanel.add(allRolesPanel);
								}
							}
						]
					
					}]
					}
            	
            ]
        }
    ]
});
var allRolesPanel=Ext.create('Ext.panel.Panel',{
   	title:'角色列表',
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
			enableOverflow:true,
			items :[
				{
					xtype : 'button',
					text:'更新角色',
					handler:function(){
						var records = getGridSelectionRecords('roleListGrid');
						if (records.length != 1){
							Ext.Msg.alert('提示', '请选择一个角色进行修改');
						}else{
							updateRoleFlag=true;
							roleNameValidated=true;
							Ext.getCmp('addRoleForm').getForm().setValues(records[0].data);
							roleCenPanel.removeAll(false);
		                	roleCenPanel.add(addRolePanel);
						}
						
					}
				},
				{
					xtype : 'button',
					text:'删除角色',
					hidden:true,
					handler:function(){
						
					}
				},
				{
					xtype : 'button',
					text:'角色授权',
					handler:function(){
						var records = Ext.getCmp('roleListGrid').getSelectionModel().getSelection();
						if (records.length != 1){
							Ext.Msg.alert('提示', '请选择一个角色进行授权');
						}else{
							authorizeItems={};
							Ext.getCmp('authorizeRoleId').setValue(records[0].id);
							resourcesTreeStore.proxy.extraParams={
									roleId:records[0].id
								    };
							resourcesTreeStore.load();
							roleCenPanel.removeAll(false);
							//roleCenPanel.add(authorizePanel);
							roleCenPanel.add(tabPanel213);
							
							//加载操作权限
							$.ajax({
	                    		   url: "resource/getAllResource",
	                    		   async:false,
	                    		   dataType: 'json',
	                    		   success: function(data){
	                    			   Ext.getCmp('roleResourceInfo').removeAll();
	                    			   for(var title in data){
	                    				   var info={
	   		                                xtype: 'fieldcontainer',
	   		                                fieldLabel: title,
	   		                                defaultType: 'checkboxfield',
	   		                                items:[]
	   		                                };
	                    				   
	                    				   for(var i=0;i<data[title].length;i++){
	                    					   var checkBox={
			                                        boxLabel  : data[title][i].name,
			                                        name      : 'resourceId',
			                                        inputValue: data[title][i].id,
			                                    };
	                    					   info.items.push(checkBox);
	                    				   }
	                    				   
	                    				   Ext.getCmp('roleResourceInfo').add(info);
	                    				   
	                    				   }
	                    		   }});
						}
						
						
						
					}
				}
				
			]
		}
   	],
   
		items:[{
			xtype:'grid',
			layout : 'fit',
			id:'roleListGrid',
			region : 'center',
			height:'100%',
			widht:'100%',
			store:roleStore,
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
				header:"id",
				dataIndex:"id",
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
				header:"描述",
				dataIndex:"roleDesc",
				align : 'center',
				width : '33%'
			},
			{
				header:"角色等级",
				dataIndex:"roleLevel",
				align : 'center',
				width : '34%'
			}
		]
		}]
}); 

var roleCenPanel=new Ext.Panel({
   	height : "85%",
   	width : "100%",
   	region : 'center',
   	autoWidth:true,
   	scrollable:true,
   	layout: "border",
   	items : [ allRolesPanel ]
});

var roleMainPanel=new Ext.Panel({
	height : "85%",
	width : "100%",
	region : 'center',
	autoWidth:true,
	layout: "border",
	items : [ roleWestMenu, roleCenPanel ]
});