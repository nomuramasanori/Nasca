/**
 * 
 */

nasca.nodeTree = nasca.nodeTree || {};

$(function(){
	nasca.nodeTree = (function(){
		var jstree;
		
		//初期化処理
		(function(){
			var i, j;

			//ツリーデータ取得
			$.ajax({
				type: "GET",
				url: "NodeList",
				dataType: "json",
				success: function(data, textStatus){
					//ツリー生成
					$("#jstree_demo_div").jstree({
						"core" : {"data" : data},
					    "plugins" : [ "checkbox", "sort","types" ],
					    checkbox : {
					    	three_state : false,
					    	cascade : ""
					    }
					});
					
					$.jstree.defaults.checkbox.three_state = false;
					$.jstree.defaults.checkbox.cascade = "";
					
					jstree = $.jstree.reference('#jstree_demo_div');
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
					url: "DataFlowInfomation",
					dataType: "json",
					data : {parameter : param},
					success: function(json, textStatus){
						setBackground(json.nodes);
						nasca.dataFlow.draw(json);
					}
				});
			});
		})();
		
		var setBackground = function(nodes){
			var i;
			
			//独自に設定した背景スタイルを解除します
			for(i=0; i<jstree.settings.core.data.length; i++){
				$("#" + escapePeriod(jstree.settings.core.data[i].id) + "_anchor").css("background","");
				$("#" + escapePeriod(jstree.settings.core.data[i].id) + "_anchor").css("border-radius","");
				$("#" + escapePeriod(jstree.settings.core.data[i].id) + "_anchor").css("box-shadow","");
			}
			
			//表示かつ選択されていないノードに背景スタイルを設定します
			for(i=0; i<nodes.length; i++){
				if(nodes[i].visible && !jstree.is_selected(nodes[i].id)){
					$("#" + escapePeriod(nodes[i].id) + "_anchor").css("background","#beebff");
					$("#" + escapePeriod(nodes[i].id) + "_anchor").css("border-radius","2px");
					$("#" + escapePeriod(nodes[i].id) + "_anchor").css("box-shadow","inset 0 0 1px #999999");
				}
			}
		};
		
		var escapePeriod = function(str){
			return str.split(".").join("\\.");
		}
		
		return{}
	})();
});