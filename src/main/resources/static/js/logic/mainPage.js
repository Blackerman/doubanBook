/**
 * 
 */
layui.use(['jquery', 'layer'], function() {
    var $ = layui.jquery;
	
	FrameWH();

	function FrameWH() {
	    var h = $(window).height()-130;
	    $("iframe").css("height", h+"px");
	}

	$(window).resize(function () {
	    FrameWH();
	});

	
	/**
	 * 初始化登陆人信息
	 */
	var initLoginUser = function() {
		$.post('/douban/user/currentUser', {},
				function(result) {
					$('#spanNickName').html(result.name);
		})
	};
	initLoginUser();

});