$(function(){
	$("#mytabs").on("shown.bs.tab",function(e){
		//获取当前的tab内容
		var txt=$(e.target).attr('href');
		//获取上一个tab标签的内容
		var prev=$(e.relatedTarget).attr('href');
		$(txt).html($(prev).html());
	})
})