(function($){
	$.fn.myPlugin=function(options){

		var settings = $.extend({
            
        }, options);
		
//		var imgs=['http://pic33.nipic.com/20130916/3420027_192919547000_2.jpg',
//				  'http://img05.tooopen.com/images/20140604/sy_62331342149.jpg',
//				  'http://pic55.nipic.com/file/20141208/19462408_171130083000_2.jpg'];

		return this.each(function(){
			var divContain=$(this);
			divContain.html("");
			var records=settings.store.data.items;
			var flag=settings.flag;
			var ul=$("<ul class='picHoverUl'></ul>")
			if(flag==1){
				//图片库概览展示
				for(var i=0;i<records.length;i++){
					var str=
				 	'<li class="picHoverLi">'+
			            	'<div class="port-1 effect-3">'+
			                	'<div class="imageBox">'+
			                    	'<img class="imgHover" src="photo/getImage?id='+records[i].data.photoid+'" alt="Image-3">'+
			                    '</div>'+
			                    '<div class="text-desc">'+
			                        '<p><input name="photoId" class="picOutline picHoverBorder" type="checkbox" value="'+records[i].data.photoid+'">标题:'+records[i].data.title+'</p>'+
			                    	'<a href="#" class="btn fullScreenPic" src="photo/getImage?id='+records[i].data.photoid+'">查看大图</a>'+
			                    '</div>'+
			                '</div>'+
			            '</li>';

			            $(str).appendTo(ul);  
				}

				
			}else if(flag==2){
				//混合产品
				var str="";
				//图片库概览展示
				for(var i=0;i<records.length;i++){
					if(records[i].data.type==1){//图片
					str=
				 	'<li class="picHoverLi">'+
			            	'<div class="port-1 effect-3">'+
			                	'<div class="imageBox">'+
			                    	'<img class="imgHover" src="photo/getImage?id='+records[i].data.itemid+'" alt="Image-3">'+
			                    '</div>'+
			                    '<div class="text-desc">'+
			                        '<p>标题:'+records[i].data.itemTitle+'</p>'+
			                    	'<a href="#" class="btn fullScreenPic" src="photo/getImage?id='+records[i].data.itemid+'">查看大图</a>'+
			                    '</div>'+
			                '</div>'+
			            '</li>';

			            
					}else if(records[i].data.type==0){//文字
						str='<li class="picHoverLi">'+
		            	'<div class="port-1 effect-3">'+
		                	'<div class="imageBox">'+
		                    	'<img class="imgHover" src="static/images/font.jpg" alt="Image-3">'+
		                    '</div>'+
		                    '<div class="text-desc">'+
		                        '<p>标题:'+records[i].data.itemTitle+'</p>'+
		                    '</div>'+
		                '</div>'+
		            '</li>';
						 
					}
					$(str).appendTo(ul);  
				}

				
			
			}else if(flag==3){
				var str="";
				//图片库概览展示
				for(var i=0;i<records.length;i++){
					if(records[i].data.type==1){//图片
					str=
				 	'<li class="picHoverLi">'+
			            	'<div class="port-1 effect-3">'+
			                	'<div class="imageBox">'+
			                    	'<img class="imgHover" src="photo/getImage?id='+records[i].data.itemid+'" alt="Image-3">'+
			                    '</div>'+
			                    '<div class="text-desc">'+
			                        '<p>标题:'+records[i].data.title+'</p>'+
			                    	'<a href="#" class="btn fullScreenPic" src="photo/getImage?id='+records[i].data.itemid+'">查看大图</a>'+
			                    '</div>'+
			                '</div>'+
			            '</li>';

			            
					}else if(records[i].data.type==0){//文字
						str='<li class="picHoverLi">'+
		            	'<div class="port-1 effect-3">'+
		                	'<div class="imageBox">'+
		                    	'<img class="imgHover" src="static/images/font.jpg" alt="Image-3">'+
		                    '</div>'+
		                    '<div class="text-desc">'+
		                        '<p>标题:'+records[i].data.title+'</p>'+
		                    '</div>'+
		                '</div>'+
		            '</li>';
						 
					}
					$(str).appendTo(ul);  
				}

				
			
			}
			ul.appendTo(divContain);
			if(flag==1){
				$(".picHoverBorder").on('click',function(){
					var check=$(this),liNode=check.parent().parent().parent().parent();
					if(check[0].checked){
						liNode.css('border-color','red');
					}else{
						liNode.css('border-color','black');
					}
					
				});
			}
			

		});

	};

})(jQuery)