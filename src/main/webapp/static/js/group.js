//词条分类树数据
var groupTreeStore = Ext.create('Ext.data.TreeStore', {
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

//分类管理 文字产品线路分类树
var groupWestPanel = Ext.create('Ext.tree.Panel', {
    title: '分类',
    width: 200,
    heigth: 'auto',
    split: true, //显示分隔条
    region: 'west',
    collapsible: true,
    store : groupTreeStore,
    rootVisible: true,
    listeners : {
 	   //Ext.view.View this, Ext.data.Model record, HTMLElement item, Number index, Ext.EventObject e, Object eOpts
	       'itemclick' : function(view,record,item,index,e,eOpts){
	    	   if(record.data.id==1)return;
	    	   $.ajax({
				   url: "group/findById",
				   data: {id:record.data.id},
				   async:false,
				   dataType: 'json',
				   success: function(data){
					   Ext.getCmp('groupMagageTitle').setTitle('修改分类信息');
					   Ext.getCmp('groupMagageForm').reset();
			    	   Ext.getCmp('groupMagageFormId').setValue(data.id);
			    	   Ext.getCmp('groupMagageFormName').setValue(data.name);
			    	   Ext.getCmp('groupMagageFormParentId').setValue(data.parentid);
				   }
 		});
	    	  
	       },
	       'load':function( store , records , successful , operation , node , eOpts ) {
				if(successful&&node.id=='1'&&records.length>0){
					
					groupWestPanel.getSelectionModel().select(records[0]);
					groupWestPanel.fireEvent('itemclick',groupWestPanel,records[0]);
					groupWestPanel.fireEvent('expand',groupWestPanel,records[0]);
					this.getRootNode().getChildAt(0).expand();
					
				}
				
			},
	       'itemcontextmenu' : function(view,record,item,index,e,eOpts){
	        	 if(Ext.getCmp('rightClickMenu')!= null){
	        		 Ext.destroy(Ext.getCmp('rightClickMenu'));
	        	 }
	        	e.preventDefault();  
           e.stopEvent();
           var rootFlag=record.data.id==1;
           var nodemenu = new Ext.menu.Menu({  
               floating:true,
               id:'rightClickMenu',
               align:'center',
               items:[
                   {  
	                        text:'新增',
	                        icon : $ctx+'/static/images/btn/add_0906.png',
	                        handler:function(){
	                        	 var form=Ext.getCmp("groupMagageForm");
	                        	 form.reset();
	                        	 Ext.getCmp('groupMagageTitle').setTitle('新增分类信息');
	                        	 var id = record.data.id; //节点id
	                        	 Ext.getCmp('groupMagageFormParentId').setValue(id);
	              	    	  

	                        }  
	                    },
	                    {  
	                        text:'删除',
	                        disabled:rootFlag,
	                        icon : $ctx+'/static/images/btn/delete_0906.png',
	                        handler:function(){
	                            var tipstr = "是否删除？";											
								Ext.Msg.confirm("提示：",	tipstr,function(btn) {
									if (btn == "yes") {
										var id = record.data.id; //节点id
			                            Ext.Ajax.request({
											url : $ctx+"/group/delete",
											params : { "id" : id },
											disableCaching : false,
											success: function(resp,opts){  
												Ext.Msg.alert("提示：" ,"删除成功！");
												groupTreeStore.proxy.extraParams={};
												groupTreeStore.load();
												
					                        },  
					                        failure: function(resp,opts){  
					                        	Ext.Msg.alert("提示：" ,"删除失败,其重试");
					                        }
										});
									}
								});
	                        }  
	                    },{
	                    	text:'关闭',
	                    	icon:$ctx+'/static/images/btn/close_0906.png',
	                    	handler:function(){
	                    		Ext.destroy(Ext.getCmp('rightClickMenu'));
	                    	}
	                    }
               ]  
                 
           });  
           nodemenu.showAt(e.getXY());
	        
  }
    }
});


//分类管理详细信息panel 
var groupCenterPanel=Ext.create('Ext.panel.Panel',{
	   	title:'详细信息',
	   	width:'85%',
	   	height:'100%',
	   	autoWidth:true,
	   	region : 'center',
	   	layout: {
		    type: 'hbox',
		    pack: 'center'
		},
	   	autoScroll:true,
	   	items: [
			{
				xtype:'form',
				width:350,
				id:"groupMagageForm",
				style:'border-width:0 0 0 0;',
				buttonAlign: 'center',
				margin  : '15 0 0 0',
				items:[
					{ 
						xtype:'fieldset',
						id:'groupMagageTitle',
						title:'新增分类信息', 
						collapsible:false,
						width : 350,
						items:[
							{	
								xtype: "textfield",
								fieldLabel:'分类名称<font color="red">*</font>',
								id:'groupMagageFormName',
								name:'name',
								allowBlank: false,
								blankText: '分类名称为必填项'
							},
							{
								xtype: "textfield",
								fieldLabel:'分类Id<font color="red">*</font>',
								id:'groupMagageFormId',
								hidden:true,
								name:'id',
								blankText: '分类Id为必填项'
								
							},{
								xtype: "textfield",
								fieldLabel:'父分类Id<font color="red">*</font>',
								id:'groupMagageFormParentId',
								hidden:true,
								name:'pid',
								allowBlank: false,
								blankText: '分类Id为必填项'
								
							}
							
					        
							
						]
					}
				],
				buttons:[
					{
						text: '保存',
						listeners:{
							click:function(){
								var form=Ext.getCmp("groupMagageForm");
								if (form.isValid()) {
									 form.submit({
	                		                	url:$ctx+'/group/save',
	                		                    success: function(form, action) {
	                		                       Ext.Msg.alert('提示', '修改成功');
	                		                       groupTreeStore.proxy.extraParams={};
	                		                       groupTreeStore.load();
	                		                    },
	                		                    failure: function(form, action) {
	                		                        Ext.Msg.alert('提示', action.result.msg);
	                		                    }
	                		                });
								}else{
									 Ext.Msg.alert('提示', '表单信息有误,请核查');
								}
							}
						}
						
					}
				]
			
			}
	   	]
});

var groupPanel = Ext.create('Ext.panel.Panel', {
	height : "85%",
	width : "100%",
	region : 'center',
	autoWidth:true,
	layout: "border",
	activeTab : 0,// 默认激活第一个tab页
	items : [groupWestPanel, groupCenterPanel ],
	listeners : {
   		tabchange : function(tab, newc, oldc) {}
   	},
   	activate:function(){
	     this.getActiveTab().refresh(); 
	}
 
});