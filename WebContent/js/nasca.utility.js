/**
 * 
 */

nasca.utility = nasca.utility || {};

$(function(){
	nasca.utility = (function(){
		
		//初期化処理
		(function(){})();
		
		var showModal = function(html, functionOK, functionCancel){
			$("body").append('<div id="modal-main"></div>');
			$("#modal-main").append(html);
	        $("#modal-main").append('<br><input id="btnOK" type="button" value="OK">');
	        $("#modal-main").append('<input id="btnCancel" type="button" value="Cancel">');
	        $("body").append('<div id="modal-bg"></div>');
	 
	        //画面中央を計算する関数を実行
	        modalResize();
	 
	    	//モーダルウィンドウを表示
	        $("#modal-bg,#modal-main").fadeIn("fast");
	
	        $("#btnOK").click(function(){
	        	if(typeof(functionOK) == "function") functionOK();
	            $("#modal-main,#modal-bg").fadeOut("fast",function(){$('#modal-bg,#modal-main').remove();});
	        });
	        
	        $("#btnCancel").click(function(){
	        	if(typeof(functionCancel) == "function") functionCancel();
	        	$("#modal-main,#modal-bg").fadeOut("fast",function(){$('#modal-bg,#modal-main').remove();});
	        });
	 
	        //画面の左上からmodal-mainの横幅・高さを引き、その値を2で割ると画面中央の位置が計算できます
	        $(window).resize(modalResize);
		}
		
        var modalResize = function(){
 
            var w = $(window).width();
            var h = $(window).height();
 
            var cw = $("#modal-main").outerWidth();
            var ch = $("#modal-main").outerHeight();
 
            //取得した値をcssに追加する
            $("#modal-main").css({
                "left": ((w - cw)/2) + "px",
                "top": ((h - ch)/2) + "px"
            });
        }
        
		var ajaxPost = function(url, param, callback){
			$.ajax({
				type: "POST",
				async: false,
				url: url,
				dataType: "json",
				data : {parameter : JSON.stringify(param)},
				success: function(json, textStatus){}
			})
			
			if(typeof(callback) == "function") callback();
		}

		return{
			showModal : showModal,
			ajaxPost : ajaxPost
		}
	})();
});