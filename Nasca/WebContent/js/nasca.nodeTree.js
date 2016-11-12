/**
 * 
 */

nasca.nodeTree = nasca.nodeTree || {};

$(function(){
	nasca.nodeTree = (function(){
		//初期化処理
		(function(){
			var i, j;

			//ツリーデータ取得
			$.ajax({
				type: "GET",
				url: "HelloWorld",
				dataType: "json",
				success: function(data, textStatus){
					//ツリー生成
					$("#jstree_demo_div").jstree({
						"core" : {"data" : data},
					    "plugins" : [ "checkbox", "sort","types" ]
					});
				}
			});
			
			//イベント追加
			$('#jstree_demo_div').on('changed.jstree', function (e, data) {			
				//選択されたオブジェクトを"/"区切りで連結しデータ取得のパラメータを作成します。
				var nodesString = "";
				for(i = 0, j = data.selected.length; i < j; i++) {
					nodesString = nodesString + data.instance.get_node(data.selected[i]).id + "/"
				} 
				
				var param = nodesString.slice(0,-1);
				
				$.ajax({
					type: "POST",
					url: "IO",
					dataType: "json",
					data : {parameter : param},
					success: function(json, textStatus){
						nasca.dataFlow.draw(json);
					}
				});
			});
		})();
	})();
});