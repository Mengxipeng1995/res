Ext.onReady(function () {
	Ext.QuickTips.init();
	
	

//    {
//    	title:'<font style="font-size:10pt;cursor:pointer">资源管理</font>',
//    	collapsed:false,
//    	height:15,
//    	overflowHandler:'scroller',
//    	autoScroll:true,
//    	items:[
//    	       {
//			xtype:'button', 
//		    text:'条目库',
//		    width:'100%',
//		    listeners:{
//         	   'click':function(){
//         		   mainPanel.removeAll(false);
//         		   mainPanel.add(itemMainPanel);
//         	   }
//            }
//			},
//				{
//					xtype:'button', 
//				    text:'产品库',
//				    width:'100%',
//				    handler:function(){
//				    	mainPanel.removeAll(false);
//                 		mainPanel.add(productMainPanel);
//				    }
//				    
//				},
//    	       {
//    	    	   xtype:'button', 
//                   text:'图书库',
//                   width:'100%',
//                   listeners:{
//                	   'click':function(){
//                		   mainPanel.removeAll(false);
//                		   mainPanel.add(bookMainPanel);
//                	   }
//                   }
//    	       }, {
//    	    	   xtype:'button',
//                   text:'图片库',
//                   width:'100%',
//                   listeners:{
//                	   'click':function(){
//                		   mainPanel.removeAll(false);
//                		   mainPanel.add(picMainPanel);
//                	   }
//                   }
//    	       },
//    	       {
//    	    	   xtype:'button',
//                   text:'视频库',
//                   width:'100%',
//                   listeners:{
//                	   'click':function(){
//                		   mainPanel.removeAll(false);
//                		   mainPanel.add(videoMainPanel);
//                	   }
//                   }
//    	       },
//        	      {
//        	    	   xtype:'button',
//                       text:'专题库',
//                       width:'100%',
//                       listeners:{
//                    	   'click':function(){
//                    		   mainPanel.removeAll(false);
//                    		   mainPanel.add(subjectMainPanel);
//                    	   }
//                       }
//        	       },
//        	       {
//        	    	   xtype:'button',
//                       text:'期刊库',
//                       width:'100%',
//                       listeners:{
//                    	   'click':function(){
//                    		   mainPanel.removeAll(false);
//                    		   mainPanel.add(magazineMainPanel);
//                    	   }
//                       }
//        	       },
//				{
//					xtype:'button',
//				    text:'篇章库',
//				    width:'100%',
//                   listeners:{
//                	   'click':function(){
//                		   mainPanel.removeAll(false);
//                		   mainPanel.add(chapterMainPanel);
//                	   }
//                   }
//				},
//				{
//					xtype:'button',
//				    text:'案例库',
//				    width:'100%',
//				    listeners:{
//                    	   'click':function(){
//                    		   mainPanel.removeAll(false);
//                    		   mainPanel.add(caseMainPanel);
//                    	   }
//                       }
//				}
//    	       
//    	]
//    },
//    {
//    	title:'<font style="font-size:10pt;cursor:pointer">用户管理</font>',
//    	collapsed:true,
//    	items:[
//				{
//					xtype:'button', 
//				    text:'用户管理',
//				    width:'100%',
//				    listeners:{
//                    	   'click':function(){
//                    		   userStore.loadPage(1);
//                    		   mainPanel.removeAll(false);
//                    		   mainPanel.add(userMainPanel);
//                    	   }
//                       }
//				},
//				{
//					xtype:'button', 
//				    text:'角色管理',
//				    width:'100%',
//				    listeners:{
//                    	   'click':function(){
//                    		   roleStore.load();
//                    		   mainPanel.removeAll(false);
//                    		   mainPanel.add(roleMainPanel);
//                    	   }
//                       }
//				}
//    	       
//    	]
//    },
//    {
//    	title:'<font style="font-size:10pt;cursor:pointer">系统管理</font>',
//    	collapsed:true,
//    	items:[
//				{
//					xtype:'button',
//				    text:'入库管理',
//				    width:'100%'
//				},
//				{
//					xtype:'button',
//				    text:'出库管理',
//				    width:'100%'
//				},
//				{
//					xtype:'button',
//				    text:'统计管理',
//				    width:'100%'
//				},
//				{
//					xtype:'button',
//				    text:'流程管理',
//				    width:'100%'
//				},
//				{
//					xtype:'button',
//				    text:'接口管理',
//				    width:'100%'
//				},
//				{
//					xtype:'button', 
//				    text:'参数设置',
//				    width:'100%',
//				    handler:function(){
//				    	
//				    }
//				},
//				{
//					xtype:'button',
//				    text:'系统日志',
//				    width:'100%'
//				}
//    	       
//    	]
//    },
//    {
//    	title:'<font style="font-size:10pt;cursor:pointer">知识体系</font>',
//    	collapsed:true,
//    	items:[
//				{
//					xtype:'button', 
//				    text:'分类体系维护',
//				    width:'100%'
//				},
//				{
//					xtype:'button', 
//				    text:'关键词库维护',
//				    width:'100%'
//				},
//				{
//					xtype:'button', 
//				    text:'索引词库维护',
//				    width:'100%'
//				}
//    	       
//    	]
//    }

	//菜单个体
	/*************************************资源管理***********************************/
	 var menu0={
     	title:'<font style="font-size:10pt;cursor:pointer">资源管理</font>',
     	collapsed:false,
     	height:15,
     	overflowHandler:'scroller',
     	autoScroll:true,
     	 items:[
	            ]
	 };
	 var menu1={
			xtype:'button', 
		    text:'条目库',
		    width:'100%',
		    listeners:{
      	   'click':function(){
      		 itemCategoryTreeStore.proxy.extraParams={};
      		itemCategoryTreeStore.load();
      		 itemGridStore.proxy.url='icmsc/findByCid';
      		 itemGridStore.proxy.extraParams={catid:3};
      		itemGridStore.loadPage(1);
      		   mainPanel.removeAll(false);
      		   mainPanel.add(itemMainPanel);
      	   }
         }
		};
	 var menu2={
			xtype:'button', 
		    text:'产品库',
		    width:'100%',
		    handler:function(){
		    	productGridStore.loadPage(1);
		    	mainPanel.removeAll(false);
      		mainPanel.add(productMainPanel);
		    }
		    
		};
	 
	var menu3={
  	   xtype:'button', 
         text:'图书库',
         width:'100%',
         listeners:{
      	   'click':function(){
      		 bookCategoryTreeStore.load();
      		 bookGridStore.proxy.extraParams={};
      		bookGridStore.loadPage(1);
      		   mainPanel.removeAll(false);
      		   mainPanel.add(bookMainPanel);
      	   }
         }
     };
	
	var menu4={
 	   xtype:'button',
        text:'图片库',
        width:'100%',
        listeners:{
     	   'click':function(){
     		  picGridStore.loadPage(1);
     		  picCategoryTreeStore.load();
     		   mainPanel.removeAll(false);
     		   mainPanel.add(picMainPanel);
     	   }
        }
    };
    
	var menu5={
 	   xtype:'button',
        text:'视频库',
        width:'100%',
        listeners:{
     	   'click':function(){
     		  videoGridStore.proxy.extraParams={};
     		 videoGridStore.loadPage(1);
     		  videoCategoryTreeStore.load();
     		   mainPanel.removeAll(false);
     		   mainPanel.add(videoMainPanel);
     	   }
        }
    };
	
	var menu6={
	    	   xtype:'button',
               text:'专题库',
               width:'100%',
               listeners:{
            	   'click':function(){
            		   subjectGridStore.proxy.extraParams={};
            		   subjectGridStore.loadPage(1);
            		   subjectCategoryTreeStore.proxy.extraParams={};
						subjectCategoryTreeStore.load();
            		   mainPanel.removeAll(false);
            		   mainPanel.add(subjectMainPanel);
            	   }
               }
	       };
	
	var menu7={
	    	   xtype:'button',
               text:'期刊库',
               width:'100%',
               listeners:{
            	   'click':function(){
            		   magazineGridStore.proxy.extraParams={};
            		   magazineGridStore.loadPage(1);
            		   mainPanel.removeAll(false);
            		   mainPanel.add(magazineMainPanel);
            	   }
               }
	       };
	var menu8={
			xtype:'button',
		    text:'篇章库',
		    width:'100%',
           listeners:{
        	   'click':function(){
        		   chapterGridStore.proxy.extraParams={};
        		   chapterGridStore.loadPage(1);
        		   mainPanel.removeAll(false);
        		   mainPanel.add(chapterMainPanel);
        	   }
           }
		};
	
	var menu9={
			xtype:'button',
		    text:'标准库',
		    width:'100%',
           listeners:{
        	   'click':function(){
        		   standardStore.proxy.extraParams={};
        		   standardStore.loadPage(1);
        		   mainPanel.removeAll(false);
        		   mainPanel.add(standardGridPanel);
        	   }
           }
		};
	
    
	 /*************************************资源管理***********************************/
	
	 /*************************************用户管理***********************************/
	var menu100={
        	title:'<font style="font-size:10pt;cursor:pointer">用户管理</font>',
        	collapsed:true,
        	 items:[
    	            ]
        	};
	
	var menu101={
			xtype:'button', 
		    text:'用户管理',
		    width:'100%',
		    listeners:{
            	   'click':function(){
            		   userStore.loadPage(1);
            		   mainPanel.removeAll(false);
            		   mainPanel.add(userMainPanel);
            	   }
               }
		};
	var menu102={
			xtype:'button', 
		    text:'角色管理',
		    width:'100%',
		    listeners:{
            	   'click':function(){
            		   roleStore.load();
            		   mainPanel.removeAll(false);
            		   mainPanel.add(roleMainPanel);
            	   }
               }
		};
	
	var menu103={
			xtype:'button', 
		    text:'机构管理',
		    width:'100%',
		    listeners:{
            	   'click':function(){
            		   groupTreeStore.load();
            		   mainPanel.removeAll(false);
            		   mainPanel.add(groupPanel);
            	   }
               }
		};

	
	/*************************************用户管理***********************************/
	
	/*************************************系统管理***********************************/
	var menu200={
        	title:'<font style="font-size:10pt;cursor:pointer">系统管理</font>',
        	collapsed:true,
        	 items:[
    	            ]
        	};
	
	
	var menu201={
		xtype:'button',
	    text:'入库管理',
	    width:'100%',
	    handler:function(){
	    	mainPanel.removeAll(false);
	  		mainPanel.setHtml(
	    	'<CENTER>'+
	    	 '<form action="/res/test/bookImp" method="post" target="_blank" >'+
	    	    '<table border="1" >'+
	    	       '<tr><td colspan="2">图书入库</td></tr>'+
	    	       '<tr><td width="100">路径</td><td><input name="path" type="text" /></td></tr>'+
	    	       '<tr><td width="100">cid</td><td><input name="cid" type="text" /></td></tr>'+
	    	      '<tr><td colspan="2"> <input type="submit" value="提交"></td></tr>'+
	    	    '</table>'+
	    	   '</form>'+
	    	  '</CENTER>');
	    	
	    }
	};
	var menu202={
		xtype:'button',
	    text:'统计管理',
	    width:'100%',
	    handler:function(){
	    	
	    	mainPanel.removeAll(false);
	  		mainPanel.add(statisticsMainPanel);
	  		Ext.getCmp('statisticsPieButton').fireEvent('click');
	    }
	};
	var menu203={
		xtype:'button',
	    text:'流程管理',
	    width:'100%',
	    handler:function(){
	    	permissionGridStore.loadPage(1);
	    	mainPanel.removeAll(false);
	  		   mainPanel.add(permissionGridPanel);
	    }
	};
	var menu204={
		xtype:'button',
	    text:'接口管理',
	    width:'100%',
	    handler:function(){
		       mainPanel.removeAll(false);
	  		   mainPanel.add(interfaceGridPanel);
		    }
	    
	};
	var menu205={
		xtype:'button', 
	    text:'参数设置',
	    width:'100%',
	    handler:function(){
	    	mainPanel.removeAll(false);
	  		mainPanel.add(sysParGridPanel);
	    }
	};
	var menu206={
		xtype:'button',
	    text:'系统日志',
	    width:'100%',
	    handler:function(){
	    	logsGridStore.loadPage(1);
	    	mainPanel.removeAll(false);
	  		   mainPanel.add(logsGridPanel);
	    }
	};
	
	
	/*************************************系统管理***********************************/
	
	/*************************************知识体系***********************************/
	var menu300={
        	title:'<font style="font-size:10pt;cursor:pointer">知识体系</font>',
        	collapsed:true,
        	 items:[
    	            ]
        	};
	
	var menu301={
		xtype:'button', 
	    text:'分类体系维护',
	    width:'100%',
	    listeners:{
     	   'click':function(){
     		  categoryTreeStore.proxy.extraParams={};
     		  categoryTreeStore.loadPage(1);
     		   mainPanel.removeAll(false);
     		   mainPanel.add(categoryPanel);
     	   }
        }
	    
	};
	var menu302={
		xtype:'button', 
	    text:'关键词库维护',
	    width:'100%',
	    listeners:{
	    	'click':function(){
	    		keywordGridStore.proxy.extraParams={};
	    		keywordGridStore.loadPage(1);
	     		   mainPanel.removeAll(false);
	     		   mainPanel.add(keywordGridPanel);
	    	}
	    }
	};
	var menu303={
		xtype:'button', 
	    text:'索引词库维护',
	    width:'100%',
	    handler:function(){
	    	indexGridStore.proxy.extraParams={};
	    	indexGridStore.load();
	    	mainPanel.removeAll(false);
  		   mainPanel.add(indexGridPanel);
	    }
	};
	/*************************************知识体系***********************************/
	
	
	//菜单
	var westMenu = new Ext.Panel({
	     title:'<font style="font-size:10pt"><b>功能菜单</b></font>',
	     region:"west",
	     height:1000,
	     split:true,
	     collapsible:true,
	     width:150,
	     layout:{
	    	 	type: 'accordion',
	    	 	overflowHandler:'scroller',
	            activeOnTop : false,             //设置打开的子面板置顶
	            fill : true,                     //子面板充满父面板的剩余空间
	            hideCollapseTool: true,         //显示“展开收缩”按钮
	            titleCollapse : false,     //允许通过点击子面板的标题来展开或收缩面板
	            animate:true,          //使用动画效果
	            autoWidth:true
	           
	     },
	     items:[
	            ]
  });
	
	//菜单权限判断
	if(menuPermissions['menu0']||'admin'==$userName){
		 if(menuPermissions['menu1']||'admin'==$userName){
			 menu0.items.push(menu1);
		 }
		 if(menuPermissions['menu2']||'admin'==$userName){
			 menu0.items.push(menu2);
		 }
		 
		 if(menuPermissions['menu3']||'admin'==$userName){
			 menu0.items.push(menu3);
		 }
		 if(menuPermissions['menu4']||'admin'==$userName){
			 menu0.items.push(menu4);
		 }
		 if(menuPermissions['menu5']||'admin'==$userName){
			 menu0.items.push(menu5);
		 }
		 
		 if(menuPermissions['menu6']||'admin'==$userName){
			 menu0.items.push(menu6);
		 }
		 if(menuPermissions['menu7']||'admin'==$userName){
			 menu0.items.push(menu7);
		 }
		 if(menuPermissions['menu8']||'admin'==$userName){
			 menu0.items.push(menu8);
		 }
		 if(menuPermissions['menu9']||'admin'==$userName){
			 menu0.items.push(menu9);
		 }
		 westMenu.add(menu0);
	}
	
	if(menuPermissions['menu100']||'admin'==$userName){
		if(menuPermissions['menu101']||'admin'==$userName){
			menu100.items.push(menu101);
		 }
		 if(menuPermissions['menu102']||'admin'==$userName){
			 menu100.items.push(menu102);
		 }
		 
		 if(menuPermissions['menu103']||'admin'==$userName){
			 menu100.items.push(menu103);
		 }
		 westMenu.add(menu100);
	}
	
	if(menuPermissions['menu200']||'admin'==$userName){
		
		 if(menuPermissions['menu201']||'admin'==$userName){
			 menu200.items.push(menu201);
		 }
		
		 if(menuPermissions['menu202']||'admin'==$userName){
			 menu200.items.push(menu202);
		 }
		 if(menuPermissions['menu203']||'admin'==$userName){
			 menu200.items.push(menu203);
		 }
		 if(menuPermissions['menu204']||'admin'==$userName){
			 menu200.items.push(menu204);
		 }
		 if(menuPermissions['menu205']||'admin'==$userName){
			 menu200.items.push(menu205);
		 }
		 if(menuPermissions['menu206']||'admin'==$userName){
			 menu200.items.push(menu206);
		 }
		 
		 
		 
		 westMenu.add(menu200);
	}
	
	if(menuPermissions['menu300']||'admin'==$userName){
		if(menuPermissions['menu301']||'admin'==$userName){
			menu300.items.push(menu301);
		 }
		if(menuPermissions['menu302']||'admin'==$userName){
			menu300.items.push(menu302);
		 }
		if(menuPermissions['menu303']||'admin'==$userName){
			menu300.items.push(menu303);
		 }
		westMenu.add(menu300);
	}
	
	var northPanel = new Ext.Panel({
	       id: 'northPanel',
	       autoWidth: true,
	       heigth: 180,
	       frame: true,
	       bodyStyle:"background-image:url("+$ctx+"/static/images/top3.jpg);background-size:cover;",
	       region: 'north',
	       html: 
	       		 '<div style="text-align:right;font-size:13px;color:#000;float:right;width:35%;height:58px">'+
					 '<div style="float:right;margin-right:8px;margin-top:25px;color:white;letter-spacing:1px;">'+
						'当前用户:'+$userNickName+'&nbsp; -><a style="color:white" href="'+$ctx+'/logout" target="_parent">退出</a>'+ 
					 '</div>'+
					 '<div style="float:right;height:58px;">'+
						'<img src="'+$ctx+'/static/images/loginuser.png" width="37" height="32" style="margin-top:15px;"/>'+
					 '</div>'+
			     '</div>'
			     
	    });
	
	//版权
	var southPanel = new Ext.Panel({
	       id: 'southPanel',
	       autoWidth: true,
	       heigth: 200,
	       frame: true,
	       region: 'south',
	       bodyStyle: 'border-width:0 0 1px 0;',
	       html: '<div style="margin:0 auto;text-align:center"><span >Copyright © 2016 机械工业出版社 All rights reserved.</span></div>'
	   });
	//欢迎面板
	var welcomePanel=new Ext.Panel({
	   	id:'systemLink',
	   	height : "100%",
	   	width : "100%",
	   	region : 'center',
	   	autoWidth:true,
	   	layout: "border",
	   	bodyStyle: {  
            //background: '#ffc',  
           // background: 'url('+$ctx+'/static/image/welcome.jpg) no-repeat #00FFFF',  
           // padding: '10px'  
        },  
	   	html:'<div style="background-image:url('+$ctx+'/static/images/welcome.jpg );background-repeat: no-repeat;background-position:0 center;background-size:cover;width:100%;height:100%"></div>'
  });
	
	mainPanel=new Ext.Panel({
	   	id:'mainPanel',
	   	height : "85%",
	   	width : "100%",
	   	region : 'center',
	   	autoWidth:true,
	   	layout: "border",
	   	items : [ welcomePanel ]
   });

 var vp = new Ext.Viewport({
       layout: "border",
       renderTo: 'mainPanel',//Ext.getBody(),
       listeners:{
    	   'afterrender':function(){}
       },
       items: [northPanel,westMenu,mainPanel,southPanel]
   });
	
	
	
	
	
	});