/**
 * 
 */

nasca.nodeTree = nasca.nodeTree || {};

$(function(){
	nasca.nodeTree = (function(){
		var jstree;
		var currentJson = {};
		
		//初期化処理
		(function(){
			//ツリーデータ取得
			$.ajax({
				type: "GET",
				url: "NodeList",
				dataType: "json",
				success: function(data, textStatus){
					create(data);
				}
			});
			
			//イベント追加
			$('#jstree_demo_div').on('changed.jstree', function (e, data) {			
				refresh();
			});
			
			//イベント追加
			$('#jstree_demo_div').on('open_node.jstree', function (e, data) {
				//無駄があるのでそのうち改善する				
				if(typeof currentJson.nodes !== "undefined") setBackground(currentJson.nodes);
			});
			
			//イベント追加
			$('#jstree_demo_div').on('loaded.jstree', function() {
				jstree.open_node("#root");
			});
		})();
		
		var create = function(data){
			//ツリー生成
			$("#jstree_demo_div").jstree({
				"core" : {
					"data" : data,
					"dblclick_toggle" : false
				},
			    "plugins" : [ "checkbox", "sort", "types", "contextmenu"],
			    checkbox : {
			    	three_state : false,
			    	cascade : ""
			    },
				"contextmenu":{         
				    "items": function($node) {
				        var tree = $("#tree").jstree(true);
				        return {
				        		"Create": {
				        		"separator_before": false,
				        		"separator_after": false,
				        		"label": "Create",
				        		"action": function (obj) {
				        			nasca.utility.showModal(
			                    		generateHtmlNodeRegister("Create", $node),
			                    		function(){
											nasca.utility.ajaxPost(
												"NodeRegister/insert",
												{
													"parentid": $("#parentid").val(),
													"id": $("#id").val(),
													"name": $("#name").val(),
													"type": $("#type  option:selected").val(),
													"remark": $("[name=remark]").val()
												},
												refreshJstree
											);
										},
			                    		null
			                    	);
				                }
				            },
				            "Edit": {
				            	"_disabled" : function(){if($node.id == "root") return true; else return false;},
				                "separator_before": false,
				                "separator_after": false,
				                "label": "Edit",
				                "action": function (obj) { 
				                	nasca.utility.showModal(
				                    		generateHtmlNodeRegister("Edit", $node),
				                    		function(){
												nasca.utility.ajaxPost(
													"NodeRegister/update",
													{
														"parentid": $node.parent,
														"originalid": $node.id,
														"id": $("#id").val(),
														"name": $("#name").val(),
														"type": $("#type  option:selected").val(),
														"remark": $("[name=remark]").val()
													},
													refreshJstree
												);
											},
				                    		null
				                    	);
				                }
				            },                         
				            "Remove": {
				            	"_disabled" : function(){if($node.id == "root") return true; else return false;},
				                "separator_before": false,
				                "separator_after": false,
				                "label": "Remove",
				                "action": function (obj) { 
				                	nasca.utility.showModal(
				                    		"Are you sure?",
				                    		function(){
												nasca.utility.ajaxPost(
													"NodeRegister/delete",
													{
														"parentid": $node.parent,
														"id": $node.id.replace($node.parent, "")
													},
													refreshJstree
												);
											},
				                    		null
				                    	);
				                }
				            }
				        };
				    }
				}
			});
			
			$.jstree.defaults.checkbox.three_state = false;
			$.jstree.defaults.checkbox.cascade = "";
			
			jstree = $.jstree.reference('#jstree_demo_div');
		};
		
		var refreshJstree = function(){
			//ツリーデータ取得
			$.ajax({
				type: "GET",
				url: "NodeList",
				dataType: "json",
				success: function(data, textStatus){
					jstree.settings.core.data = data;
					jstree.refresh();
				}
			});
		}
		
		var refresh = function(){
			var i, j;
			
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
		};
		
		var generateHtmlNodeRegister = function(mode, node){
			var parentid, parentname, id, name, selectedValue, remark;
			
			if(mode === "Create"){
				parentid = node.id;
				parentname = node.text;
				id = "";
				name = "";
				selectedValue = null;
				remark = "input remark";
			}else if(mode === "Edit"){
				parentid = node.parent;
				parentname = node.original.parentText;
				id = node.id.replace(node.parent + ".", "");
				name = node.text;
				selectedValue = node.original.type;
				remark = node.original.remark;
			}
			
			var html = 
				'<input type="text" id="parentid" size="20" value="' + parentid + '" maxlength="20" disabled>' +
				'<input type="text" id="parentname" size="40" value="' + parentname + '" maxlength="20" disabled><br>' +
				'<input type="text" id="id" size="20" value="' + id + '" maxlength="20">' +
				'<input type="text" id="name" size="40" value="' + name + '" maxlength="20"><br>' +
				'<div id="type"></div>' +
				'<textarea name="remark" rows="4" cols="40">' + remark + '</textarea><br>' +
				//アイコン付きドロップダウンリストの発動
				'<script>nasca.nodeTree.startMsDropDown("' + selectedValue + '");</script>';
			
			console.log(node);
			
			return html;
		};
		
		var startMsDropDown = function(selectedValue){
			$.ajax({
				type: "GET",
				url: "NodeType",
				dataType: "json",
				success: function(data, textStatus){
					var dd = $("#type").msDropdown({byJson:{data:data}}).data("dd");
					dd.setIndexByValue(selectedValue);
				}
			});
		};
		
		var debug = function(){};
		
		return{
			select : select,
			selectChild : selectChild,
			refresh : refresh,
			startMsDropDown : startMsDropDown,
			debug : debug
		}
	})();
});