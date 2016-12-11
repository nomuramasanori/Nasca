/**
 * 
 */

nasca.nodeTree = nasca.nodeTree || {};

$(function(){
	nasca.nodeTree = (function(){
		var jstree;
		var currentJson;
		
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
				refresh();
			});
			
			//イベント追加
			$('#jstree_demo_div').on('open_node.jstree', function (e, data) {
				//無駄があるのでそのうち改善する
				setBackground(currentJson.nodes);
			});
		})();
		
		var refresh = function(){
			//選択されたオブジェクトを"/"区切りで連結しデータ取得のパラメータを作成します。
			var nodesString = "";
			for(i = 0, j = jstree.get_selected().length; i < j; i++) {
				nodesString = nodesString + jstree.get_selected()[i] + "/"
			} 
			
			var param = nodesString.slice(0,-1);
			
			$.ajax({
				type: "POST",
				url: "DataFlowInfomation",
				dataType: "json",
				data : {parameter : param},
				success: function(json, textStatus){
					currentJson = json;
					setBackground(currentJson.nodes);
					nasca.dataFlow.draw(currentJson);
				}
			});
		};
		
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
		
		var select = function(nodeID){
			jstree.select_node(nodeID);
		}
		
		var selectChild = function(nodeID){
			//複数の子ノードが存在する場合ひとつずつchangedイベントが発生し不具合となるため第2引数にtrueを指定しイベントの発生を抑止します
			jstree.select_node(jstree.get_node(nodeID).children, true);
			jstree.deselect_node(nodeID);
		};
		
		var escapePeriod = function(str){
			return str.split(".").join("\\.");
		}
		
		return{
			select : select,
			selectChild : selectChild,
			refresh : refresh
		}
	})();
});