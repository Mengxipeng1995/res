//词条分类树数据
var categoryTreeStore = Ext.create('Ext.data.TreeStore', {
	autoLoad: false,
	proxy:{
		type : 'ajax',
		url : 'category/getSons'
	},
	root:{
		nodeType : 'async', 
		expanded : true,
		id:'1'
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
var categoryWestPanel = Ext.create('Ext.tree.Panel', {
    title: '分类',
    width: 200,
    heigth: 'auto',
    split: true, //显示分隔条
    region: 'west',
    collapsible: true,
    store : categoryTreeStore,
    rootVisible: false,
    listeners : {
 	   //Ext.view.View this, Ext.data.Model record, HTMLElement item, Number index, Ext.EventObject e, Object eOpts
	       'itemclick' : function(view,record,item,index,e,eOpts){
	    	   
	    	   $.ajax({
				   url: "category/findById",
				   data: {id:record.data.id},
				   async:false,
				   dataType: 'json',
				   success: function(data){
					   Ext.getCmp('categoryMagageTitle').setTitle('修改分类信息');
					   Ext.getCmp('categoryMagageForm').reset();
			    	   Ext.getCmp('categoryMagageFormId').setValue(data.id);
			    	   Ext.getCmp('categoryMagageFormName').setValue(data.name);
			    	   Ext.getCmp('categoryMagageFormParentId').setValue(data.parentid);
				   }
 		});
	    	  
	       },
	       'load':function( store , records , successful , operation , node , eOpts ) {
				if(successful&&node.id=='1'&&records.length>0){
					
					categoryWestPanel.getSelectionModel().select(records[0]);
					categoryWestPanel.fireEvent('itemclick',categoryWestPanel,records[0]);
					categoryWestPanel.fireEvent('expand',categoryWestPanel,records[0]);
					this.getRootNode().getChildAt(0).expand();
					
				}
				
			},
	       'itemcontextmenu' : function(view,record,item,index,e,eOpts){
	        	 if(Ext.getCmp('rightClickMenu')!= null){
	        		 Ext.destroy(Ext.getCmp('rightClickMenu'));
	        	 }
	        	e.preventDefault();  
           e.stopEvent();
           
           var flag=!categoryDownloadPermission;
           
           var nodemenu = new Ext.menu.Menu({  
               floating:true,
               id:'rightClickMenu',
               align:'center',
               items:[
                   {  
	                        text:'新增',
	                        icon : $ctx+'/static/images/btn/add_0906.png',
	                        handler:function(){
	                        	 var form=Ext.getCmp("categoryMagageForm");
	                        	 form.reset();
	                        	 Ext.getCmp('categoryMagageTitle').setTitle('新增分类信息');
	                        	 var id = record.data.id; //节点id
	                        	 Ext.getCmp('categoryMagageFormParentId').setValue(id);
	              	    	  

	                        }  
	                    },
	                    {  
	                        text:'删除',
	                        icon : $ctx+'/static/images/btn/delete_0906.png',
	                        handler:function(){
	                            var tipstr = "是否删除？";											
								Ext.Msg.confirm("提示：",	tipstr,function(btn) {
									if (btn == "yes") {
										var id = record.data.id; //节点id
			                            Ext.Ajax.request({
											url : $ctx+"/category/delete",
											params : { "id" : id },
											disableCaching : false,
											success: function(resp,opts){  
												Ext.Msg.alert("提示：" ,"删除成功！");
												 categoryTreeStore.proxy.extraParams={};
	                		              		  categoryTreeStore.load();
												
					                        },  
					                        failure: function(resp,opts){  
					                        	Ext.Msg.alert("提示：" ,"删除失败,其重试");
					                        }
										});
									}
								});
	                        }  
	                    },{  
	                        text:'导出',
	                        hidden:flag,
	                        icon : $ctx+'/static/images/btn/add_0906.png',
	                        handler:function(){
	                        	var form = $("<form>");//定义一个form表单
		        				form
		        						.attr(
		        								"style",
		        								"display:none");
		        				form
		        						.attr(
		        								"target",
		        								"");
		        				form
		        						.attr(
		        								"method",
		        								"post");
		        				form
		        						.attr(
		        								"action",
		        								"category/export?id="+record.data.id);
		        				$("body")
		        						.append(
		        								form);//将表单放置在web中
		        				form.submit();//表单提交
	                        	
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
var categoryCenterPanel=Ext.create('Ext.panel.Panel',{
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
				id:"categoryMagageForm",
				style:'border-width:0 0 0 0;',
				buttonAlign: 'center',
				margin  : '15 0 0 0',
				items:[
					{ 
						xtype:'fieldset',
						id:'categoryMagageTitle',
						title:'新增分类信息', 
						collapsible:false,
						width : 350,
						items:[
							{	
								xtype: "textfield",
								fieldLabel:'分类名称<font color="red">*</font>',
								id:'categoryMagageFormName',
								name:'name',
								allowBlank: false,
								blankText: '分类名称为必填项'
							},
							{
								xtype: "textfield",
								fieldLabel:'分类Id<font color="red">*</font>',
								id:'categoryMagageFormId',
								hidden:true,
								name:'id',
								blankText: '分类Id为必填项'
								
							},{
								xtype: "textfield",
								fieldLabel:'父分类Id<font color="red">*</font>',
								id:'categoryMagageFormParentId',
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
								var form=Ext.getCmp("categoryMagageForm");
								if (form.isValid()) {
									 form.submit({
	                		                	url:$ctx+'/category/save',
	                		                    success: function(form, action) {
	                		                       Ext.Msg.alert('提示', '修改成功');
	                		                       categoryTreeStore.proxy.extraParams={};
	                		              		  categoryTreeStore.load();
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

var categoryPanel = Ext.create('Ext.panel.Panel', {
	height : "85%",
	width : "100%",
	region : 'center',
	autoWidth:true,
	layout: "border",
	activeTab : 0,// 默认激活第一个tab页
	items : [categoryWestPanel, categoryCenterPanel ],
	listeners : {
   		tabchange : function(tab, newc, oldc) {}
   	},
   	activate:function(){
	     this.getActiveTab().refresh(); 
	}
 
});