/**
 * 
 */

var nasca = nasca || {};
nasca.frame = nasca.frame || {};

$(function(){
	//画面のフレーム情報
	nasca.frame = (function(){
		var wWindow;
		var hWindow;
		var wNodeList;    
		var wMain;
		var hMain;
		
		var initialize = function(){
			wWindow = $(window).innerWidth();
			hWindow = $(window).innerHeight();
			wNodeList = $("#nodeList").width();    
			wMain = wWindow - wNodeList -1;
			hMain = hWindow;
		};

		return {
			wWindow: function(){initialize(); return wWindow;},
			hWindow: function(){initialize(); return hWindow;},
			wNodeList: function(){initialize(); return wNodeList;},
			wMain: function(){initialize(); return wMain;},
			hMain: function(){initialize(); return hMain;}
		};
	})();
	
	//画面リサイズイベント登録
	$(window).resize(function(){
		$("#drawingPaper").attr("width", nasca.frame.wMain());
	});
});
