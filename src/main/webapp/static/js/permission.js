var permissionGridStore=Ext.create('Ext.data.Store', {
	fields:['id','applicantUserName','applicantNickName','createDate','approverUserName','approverNickName','validDate','describeInfo','responseText','status'],
	proxy : {
		type : 'ajax',
		url : 'apply/list',
		reader : {
			root : 'content',
			totalProperty : 'totalElements'
		},
		simpleSortMode : true
	}

});
//var resourceList=null;
//$.ajax({
//	   url: "resource/getAllResource",
//	   async:false,
//	   dataType: 'json',
//	   success: function(data){
//		   resourceList=data;
//	   }});

var permissionGridPanel=Ext.create('Ext.panel.Panel',{
    	title:'申请列表',
    	width:'85%',
    	height:'100%',
    	autoWidth:true,
    	region : 'center',
    	layout:{
    		type : "border"
    	},
    	autoScroll:true,
		items:[{
			xtype:'grid',
			layout : 'fit',
			region : 'center',
			height:'100%',
			widht:'100%',
			autoWidth:true,
			columnLines:true,
			store: permissionGridStore,
			selModel:Ext.create('Ext.selection.CheckboxModel'),
			listeners:{
				'itemdblclick':function(view,record, item, index, e,eOpts){
					$.ajax({
             		   url: "apply/getApply?id="+record.id,
             		   async:false,
             		   dataType: 'json',
             		   success: function(data){
             			   var permissionStr='';
             			   if(data.arms){
             				  
             				  $.each(data.arms,function(key,value){  
             					 permissionStr+=value.typeName+":"+value.resourceName+";";
             				  });
             			   }
             			   //
             			  var hiddenFlag=!(systemAuditorPermission||$userName=='admin');
             			   
             			  var window=Ext.create("Ext.window.Window",{
         						autoWidth : true,
         						bodyStyle:'background-color:#ffffff',
         						modal:true,
         						closable:true,//右上角关闭按钮
         						height : 550,
         						title:'用户详细信息',
         						resizable:false,//调整窗口大小
         						width: 650,
         						constrain:true,//True： 限制窗口在窗口包含的元素里面，false：允许允许顶部超出它包含的元素。
         						autoScroll:true,
         						items:[
         							{
         								xtype:'form',
         								width:550,
         								height:'100%',
         								x:50,
         								style:'border-width:0 0 0 0;',
         								y:30,
         								id:'passApplyForm',
         								buttonAlign: 'center',
         								items:[
         									{ 
         										xtype:'fieldset', 
         										title:'申请信息', 
         										collapsible:false,
         										width : 550,
         										items:[
         											{	
      												xtype: "displayfield",
      												fieldLabel:'申请人',
      												labelAlign:'left',
      												labelWidth:150,
      												value:data.apply.applicantUserName
      											},
      											{	
      												xtype: "displayfield",
      												fieldLabel:'申请权限',
      												labelAlign:'left',
      												labelWidth:150,
      												value:permissionStr
      											},{
      												xtype: "textfield",
      												hidden:true,
      												name:'id',
      												value:record.id
      											},
      											{
      										        xtype: 'radiogroup',
      										        fieldLabel: '批准',
      										        // Arrange radio buttons into two columns, distributed vertically
      										        columns: 2,
      										        hidden:hiddenFlag,
      										        labelWidth:150,
      										        width:240,
      										       // vertical: true,
      										        items: [
      										            { boxLabel: '是', name: 'status', inputValue: '1', checked: true},
      										            { boxLabel: '否', name: 'status', inputValue: '2'}
      										        ]
      										    },
      											{
      												xtype: "textarea",
      												fieldLabel:'理由',
      												labelAlign:'left',
      												 hidden:hiddenFlag,
      												name:'responseText',
      												labelWidth:150//,
      												//value:
      											}]
         									}],
         									dockedItems: [{
         									    xtype: 'toolbar',
         									    dock: 'bottom',
         									    ui: 'footer',
         									   hidden:hiddenFlag,
         									    items: [
         									    	{
             											xtype:'button',
             											text:'提交',
             											style: {
             									            marginLeft: '200px'
             									        },
             											handler:function(){
             												Ext.getCmp("passApplyForm").submit({
    				                		                	url:$ctx+'/apply/passApply',
    				                		                    success: function(form, action) {
    				                		                    	permissionGridStore.loadPage(1);
    				                		                    	window.close();
    				                		                    },
    				                		                    failure: function(form, action) {
    				                		                        Ext.Msg.alert('提示', action.result.msg);
    				                		                    }
    				                		                });
             											}
             									
             										}
         									    ]
         									}]//,
//         									buttons:[
//         										{
//         											xtype:'button',
//         											text:'提交',
//         											 hidden:hiddenFlag,
//         											handler:function(){
//         												Ext.getCmp("passApplyForm").submit({
//				                		                	url:$ctx+'/apply/passApply',
//				                		                    success: function(form, action) {
//				                		                      
//				                		                    },
//				                		                    failure: function(form, action) {
//				                		                        Ext.Msg.alert('提示', action.result.msg);
//				                		                    }
//				                		                });
//         											}
//         									
//         										}
//         									]
         							}]
      					}).show();
             		   }});
					
					
					
				}
			},
			dockedItems:[
				{
	      			xtype : 'toolbar',
	      			//hidden:true,
	      			enableOverflow:true,
	      			items :[
	      			       {
					        xtype: 'button',
					        text:'权限申请',
					        listeners:{
					        	'click':function(){

		                    		 var form=Ext.getCmp("bookEntryForm");
		                        	 form.reset();
		                    		 Ext.getCmp('bookEntryTitle').setTitle('授权信息');
		                    		var window=Ext.create('Ext.window.Window',
											{
												modal : true,
												height : 400,
											   	width : 380,
											   	region : 'center',
											   	autoWidth:true,
											   	layout: "border",
												items:[
													{
													xtype:'panel',
													width:'85%',
												   	height:'100%',
												   	region : 'center',
												   	autoWidth:true,
												   	autoScroll:true,
													items:[{
														xtype:'form',
														id:'applyForm',
														width:'100%',
														style:'border-width:0 0 0 0;margin:auto',
														buttonAlign: 'center',
														items:[
															{
																xtype:'fieldset', 
																title:'申请表单', 
																collapsible:false,
																id:'resourceInfo',
																//width : 550,
																items:[
																	
																]
															}
														
														],
														buttons:[
														    {
														    
																text: '保存',
																handler:function(){
																	
																	var form=Ext.getCmp("applyForm");
																	
																	form.submit({
									        		                	url:$ctx+'/apply/launch',
									        		                    success: function(form, action) {
									        		                    	permissionGridStore.loadPage(1);
									        		                    	window.close();
									        		                    },
									        		                    failure: function(form, action) {
									        		                        Ext.Msg.alert('提示', action.result.msg);
									        		                    }
									        		                });
																	
																}
															},
															{ 
																text: '重置',
																handler:function(){
																	
																}
															}
														]
													
													}]
													}
											   	],
												listeners:{
													'close':function(win){
														//win.removeAll(false);
													}
												}
												
											}
					   					).show();

		                    		$.ajax({
		                    		   url: "resource/getAllResource",
		                    		   async:false,
		                    		   dataType: 'json',
		                    		   success: function(data){
		                    			   Ext.getCmp('resourceInfo').add({
		                    		        xtype: 'datefield',
		                    		        fieldLabel: '有效期',
		                    		        name: 'validDate',
		                    		        format: 'Y-m-d'
		                    		   });
		                    			   
		                    			   Ext.getCmp('resourceInfo').add({
			                    		        xtype: 'textareafield',
			                    		        fieldLabel: '描述',
			                    		        name: 'describe'
			                    		   });
		                    			   
		                    			
		                    			   
		                    			   
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
		                    				   
		                    				   Ext.getCmp('resourceInfo').add(info);
		                    				   
		                    				   }
		                    		   }});
		                    		
		                    	
					        	}
					        }
					       
					    }
	      			   ]
	      		}
				
			],
			//'id','applicantUserName','applicantNickName','createDate','approverUserName','approverNickName','validDate','describeInfo','responseText','status'
			columns : [
				{
				header:"序号",
				xtype : "rownumberer",
				align : 'center',
				hideable:false,
				width : '4%'
			},{
					header:"申请者",
					dataIndex:"applicantNickName",
					align : 'center',
					width : '20%'
			    },{
					header:"审核者",
					dataIndex:"approverNickName",
					align : 'center',
					width : '20%'
			    },{
					header:"创建时间",
					dataIndex:"createDate",
					align : 'center',
					width : '20%'
			    },{
					header:"有效期",
					dataIndex:"validDate",
					align : 'center',
					width : '20%'
			    },{
					header:"状态",
					dataIndex:"status",
					align : 'center',
					width : '13%',
					renderer: function(value){
					    if(value=="1"){
					    	return "通过";
					    }else if(value=="2"){
					    	return "拒绝";
					    }else{
					    	return "待审核";
					    }
					}
			    }
			   ],
		bbar : [
		    {
				xtype : 'pagingtoolbar',
				store:permissionGridStore,
				displayInfo : true,
				width : '100%',
				height : 30,
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
}]});