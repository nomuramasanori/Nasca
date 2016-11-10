/**
 * 
 */

var nasca = nasca || {};
nasca.frame = nasca.frame || {};

$(function(){
	//画面のフレーム情報
	nasca.frame = (function(){
		var wWindow = $(window).innerWidth();
		var hWindow = $(window).innerHeight();
		var wNodeList = $("#nodeList").width();    
		var wMain = wWindow - wNodeList -1;
		var hMain = hWindow;
		
		return {
			wWindow: wWindow,
			hWindow: hWindow,
			wNodeList: wNodeList,
			wMain: wMain,
			hMain: hMain
		};
	})();

	nasca.nodeTree.initialize();
	nasca.dataFlow.initialize();
	
	//画面リサイズイベント登録
	$(window).resize(function(){
		$("#drawingPaper").attr("width", nasca.frame.wMain);
	});
});
