/**
 * 
 */

nasca.dataFlow = nasca.dataFlow || {};

$(function(){
	nasca.dataFlow = (function(){
		var force;
		var svg;
		var nodes = [];    //ノードを収める配列
		var links = [];    //ノード間のリンク情報を収める配列
		var comments = [];
		var colors = [
		      		[255,255,0], [0,255,0], [0,0,255], [255,200,0],
		      		[150,80,90], [0,64,255],　[100,255,100],　[64,160,255],
		      		[255,0,64],　[160,32,32],　[196,128,64],　[128,196,64],
		      		[64,128,196],　[64,196,128],　[96,96,96], [0,0,0],
		      		[230,230,230]	//半透明リンク用
		];
		
		//初期化処理
		(function(){
			var linearGradient;
			
			force = d3.layout.force()
				.size([nasca.frame.wMain(), nasca.frame.hMain()])
				.nodes(nodes)
				.links(links)
				.linkDistance(60)
				.charge(-8000)
				.gravity(0.4)
				.friction(0.8);
			
			svg = d3.select("body")
				.append("svg")
				.attr("id", "drawingPaper")
				.attr("width", nasca.frame.wMain())
		        .attr("preserveAspectRatio", "xMidYMid meet")
				.attr("pointer-events", "all")
				.append("g")
				.call(d3.behavior.zoom().scaleExtent([0.3, 6]).on("zoom", function(){
					svg.attr("transform", "translate(" + d3.event.translate + ")scale(" + d3.event.scale + ")");
				}))
				.on("dblclick.zoom", null)
				.append("g")
				.attr("id", "test");
			
			//領域全体をドラッグアンドドロップするためのプレースホルダ
			svg.append("rect")
				.attr("id", "background")
				.attr("width",nasca.frame.wMain())
				.attr("height",nasca.frame.hMain())
				.attr("fill","white");

			//矢印定義（終端）
			svg.append("svg:defs").selectAll("marker")
			    .data(colors)      // Different link/path types can be defined here
			    .enter().append("svg:marker")    // This section adds in the arrows
			    .attr("id", function(d,i){
			    	return "marker-end-" + i;
			    })
			    .attr("viewBox", "0 -5 10 10")
			    .attr("refX", 32)	//矢印の位置
			    .attr("refY", 0.0)	//矢印の位置
			    .attr("markerWidth", 6)
			    .attr("markerHeight", 6)
			    .attr("orient", "auto")
			    .append("svg:path")	//矢印の形
			    .attr("d", "M0,-5L10,0L0,5")	//矢印の形
				.attr("fill", function(d){
					return "rgb(" + d[0] + "," + d[1] + "," + d[2] + ")";
				});
			
			//矢印定義（始端）
			svg.append("svg:defs").selectAll("marker")
			    .data(colors)      // Different link/path types can be defined here
			    .enter().append("svg:marker")    // This section adds in the arrows
			    .attr("id", function(d,i){
			    	return "marker-start-" + i;
			    })
			    .attr("viewBox", "0 -5 10 10")
			    .attr("refX", -22)	//矢印の位置
			    .attr("refY", 0)	//矢印の位置
			    .attr("markerWidth", 6)
			    .attr("markerHeight", 6)
			    .attr("orient", "auto")
			    .append("svg:path")	//矢印の形
			    .attr("d", "M10,-5L0,0L10,5")	//矢印の形
				.attr("fill", function(d){
					return "rgb(" + d[0] + "," + d[1] + "," + d[2] + ")";
				});
			
			//線のグラデーション定義（終端）
			linearGradient = svg.append("svg:defs")
				.append("radialGradient")
				.attr("id", "fadeout-end")
				.attr("cx", "80%")
			linearGradient.append("stop").attr("offset","50%").attr("stop-color","rgb(0,0,180)");
			linearGradient.append("stop").attr("offset","80%").attr("stop-color","rgb(250,150,0)");
			
			//線のグラデーション定義（始端）
			linearGradient = svg.append("svg:defs")
				.append("linearGradient")
				.attr("id", "fadeout-start");
			linearGradient.append("stop").attr("offset","50%").attr("stop-color","rgb(0,220,0)");
			linearGradient.append("stop").attr("offset","80%").attr("stop-color","rgb(255,0,255)");
			
	
			svg.append("svg:g");
		})();
		
		var draw = function(json){
			//d3にバインドされているデータを更新します。
			updateData(json);
			
			//リンク描画
			var link = svg.select("g").selectAll("path").data(links, function(d,i){return d.source.id + '-' + d.target.id;});
			setLinkStyleAndAttribute(setLinkStyleAndAttribute(link).enter().append("svg:path"));
			link.exit().remove();
			
			//ノード背景描画
			var node = svg.selectAll("circle").data(nodes, function(d,i){return d.id;});
			node.enter().append("circle")
				.attr({r: 20, opacity: 1.0})
				.attr("fill", "white");
			node.exit().remove();
	
			//ノード描画
			var img = svg.selectAll("image").data(nodes, function(d,i){return d.id;});
			img
				.attr("xlink:href", function(d){if(d.visible) return "./img/" + d["svg-file"]; else return null;}) //ノード用画像の設定
				.attr("width", function(d){if(d.visible) return "32px"; else return "0px";})
				.attr("height", function(d){if(d.visible) return "32px"; else return "0px";})
				.attr("x", function(d){if(d.visible) return "-16px"; else return "0px";})
				.attr("y", function(d){if(d.visible) return "-16px"; else return "0px";})
				.enter().append("image")
				.attr("xlink:href", function(d){if(d.visible) return "./img/" + d["svg-file"]; else return null;}) //ノード用画像の設定
				.attr("width", function(d){if(d.visible) return "32px"; else return "0px";})
				.attr("height", function(d){if(d.visible) return "32px"; else return "0px";})
				.attr("x", function(d){if(d.visible) return "-16px"; else return "0px";})
				.attr("y", function(d){if(d.visible) return "-16px"; else return "0px";})
				.on("mouseenter", function(d){
					var i,j;
					var flg = false;
					j = comments.length;
					for(i=0; i<j; i++){
						if(comments[i]["id"] === d.id){
							comments.splice(i,1);
							flg = true;
							break;
						}
					}
					if(!flg){
						comments.push(d);
					}
					drawComment();
				})
				.on("mouseleave", function(d){
					var i,j;
					var flg = false;
					j = comments.length;
					for(i=0; i<j; i++){
						if(comments[i]["id"] === d.id){
							comments.splice(i,1);
							flg = true;
							break;
						}
					}
					if(!flg){
						comments.push(d);
					}
					drawComment();
				})
				.on("mousedown", function(d){
					d3.event.stopPropagation();
				})
				.on("dblclick", function(d){
					d3.event.stopPropagation();
					$('#jstree_demo_div').jstree('select_node', d.id);
				})
				.call(force.drag);
			img.exit().remove();
			
			//オブジェクト名称描画
			var text = svg.selectAll("text.nodeName").data(nodes, function(d,i){return d.id;});
			text
				.text(function(d){if(d.visible) return d.name; else return null;})
				.enter().append("text")
				.attr("class", "nodeName")
			    .text(function(d){if(d.visible) return d.name; else return null;})
			    .attr("font-family", "sans-serif")
			    .attr("font-size", "20px")
			    .attr("fill", "darkgray");
			text.exit().remove();	
			
			force.on("tick", function() {
				link.attr("d", function(d) {
					//2段階目の点線描画のため「ソースノードが非表示」かつ「ターゲットノードが表示」の場合のみ線の向きを反転します。
					if(!d.source.visible && d.target.visible){
						return "M" + d.target.x + "," + d.target.y + " " + d.source.x + "," + d.source.y;
					}else{
						return "M" + d.source.x + "," + d.source.y + " " + d.target.x + "," + d.target.y;
					}
			    });
			    
			    node
			   .attr({cx: function(d) { return d.x; },
				   	  cy: function(d) { return d.y; }});
			    img
				   .attr({x: function(d) { return d.x - 16; },
					   	  y: function(d) { return d.y - 16; }});
	
			    text.attr('x', function(d) { return d.x; })
		        .attr('y', function(d) { return d.y + 32; });
			});	
	
			force.start();
		};
		
		var updateData = function(json){
			var i,j;
			var countJsonNodes, countJsonLinks, countDataNodes, countDataLinks;
			var isExists;
			var target,source;
	
			//ノードを追加・削除
			//変更のないデータを削除⇒追加するとD3への束縛に不具合が発生するので変更分のみ追加・削除を行う
			countJsonNodes = json["nodes"].length;
			for(i=0; i<countJsonNodes ; i++){
				countDataNodes = nodes.length;
				isExists = false;
				for(j=0; j<countDataNodes; j++){
					//D3データに既に存在する場合はプロパティを更新します（nodeごと入れ替えると不具合が発生するため必要なプロパティを明示します）
					if(json["nodes"][i].id === nodes[j].id){
						isExists = true
						nodes[j].visible = json["nodes"][i].visible;
						break;
					}
				}
				//D3データに存在しない場合は追加します
				if(!isExists){
					nodes.push(json["nodes"][i]);
				}
			}
			countDataNodes = nodes.length;
			for(i=countDataNodes-1; 0<=i ; i--){
				countJsonNodes = json["nodes"].length;
				isExists = false;
				for(j=0; j<countJsonNodes; j++){
					if(nodes[i].id === json["nodes"][j].id){
						isExists = true
						break;
					}
				}
				//JSONに存在しなければD3データを削除します
				if(!isExists){
					nodes.splice(i, 1);
				}
			}
			
			//リンクを追加・削除
			//変更のないデータを削除⇒追加するとD3への束縛に不具合が発生するので変更分のみ追加・削除を行う
			countJsonLinks = json["links"].length;
			for(i=0; i<countJsonLinks ; i++){
				countDataLinks = links.length;
				isExists = false;
				for(j=0; j<countDataLinks; j++){
					//D3データに既に存在する場合はプロパティを更新します（linkごと入れ替えると不具合が発生するため必要なプロパティを明示します）
					if(json["links"][i].source === links[j].source.id && json["links"][i].target === links[j].target.id){
						isExists = true
						links[j].visible = json["links"][i].visible;
						break;
					}
				}
				//D3データに存在しない場合は追加します
				if(!isExists){
					//ID文字列だと不具合が発生するのでノードオブジェクトへの参照を取得します。
					target = nodes.filter(function(item, index){
						if (item.id == json["links"][i]["target"]) return true;
					});
					source = nodes.filter(function(item, index){
						if (item.id == json["links"][i]["source"]) return true;
					});
					
					links.push({
						source: source[0],
						target: target[0],
						remark: json["links"][i].remark,
						io: json["links"][i].io,
						colorIndex: json["links"][i].colorIndex,
						visible: json["links"][i].visible
					});
				}
			}
			countDataLinks = links.length;
			for(i=countDataLinks-1; 0<=i ; i--){				
				countJsonLinks = json["links"].length;
				isExists = false;
				for(j=0; j<countJsonLinks; j++){
					if(links[i].source.id === json.links[j].source && links[i].target.id === json.links[j].target){
						isExists = true
						break;
					}
				}
				//JSONに存在しなければD3データを削除します
				if(!isExists){
					links.splice(i, 1);
				}
			}
		};
		
		var drawComment = function(){
			var comment = svg.selectAll(".comment").data(comments);
			comment.enter().append("rect")
				.attr("class","comment")
				.attr("width",300)
				.attr("height",200)
				.attr("opacity",0.0)
				.attr("x",function(d){return d.x + 50})
				.attr("y",function(d){return d.y - 50});
			comment.exit().remove();
			
			var commenttext = svg.selectAll(".commentText").data(comments);
			commenttext.enter().append("text")
				.attr("class", "commentText")
				.attr("x", function(d) { return d.x + 70; })
			    .attr("y", function(d) { return d.y ; })
			    .text( function (d) { return d.remark; })
			    .attr("font-family", "sans-serif")
			    .attr("font-size", "20px")
			    .attr("fill", "white");
			commenttext.exit().remove();
			
			comment.transition().delay(300).duration(300).style("opacity", 0.5);
		};
		
		var setLinkStyleAndAttribute = function(selection){
			selection
		    	.style("fill", "none")
		    	.style("stroke-width", "1.5px")
		    	.style("stroke", function(d){
		    		if(d.visible){
		    			return "rgb(" + colors[d.colorIndex][0] + "," + colors[d.colorIndex][1] + "," + colors[d.colorIndex][2] + ")";
		    		}else{
		    			return "rgb(" + colors[16][0] + "," + colors[16][1] + "," + colors[16][2] + ")";
		    		}
		    	})
		    	.style("stroke-dasharray", function(d){
		    		if(d.visible || (d.source.visible && d.target.visible)){
		    			return "0";
		    		}else{
		    			return "40,1,4,2,3,3,2,4,1,9999";
		    		}
		    	})
		    	.attr("marker-start", function(d){
		    		if(!d.source.visible && d.target.visible){
		    			return getMarkerDefinition(d, true, true);
		    		} else{
		    			return getMarkerDefinition(d, false, true);
		    		}
		    	})
		    	.attr("marker-end", function(d){
		    		if(!d.source.visible && d.target.visible){
		    			return getMarkerDefinition(d, true, false);
		    		} else{
		    			return getMarkerDefinition(d, false, false);
		    		}
		    	});
			
			return selection;
		};
		
		var getMarkerDefinition = function(line, isReverse, isStart){
			var result;
			
			//着目するべきノード。
			//2段階目の点線描画のため線の向きを反転させている場合があるため着目すのはどちらかを設定します。
			var subjectNode = isReverse ? (isStart ? line.target : line.source) : (isStart ? line.source : line.target);
			
			//マーク（矢印）を表示するかどうかを示します。
			//着目するべきノードが可視かつ、IOがあればマークを表示します。
			var marked = subjectNode.visible && (line.io === "IO" || line.io === (isReverse ? (isStart ? "O" : "I") : (isStart ? "I" : "O")))

			if(marked){
				if(isStart){
					if(line.visible){
						result = "#marker-start-" + line.colorIndex;
	    			}else{
	    				result = "#marker-start-16";
	    			}
				}else{
					if(line.visible){
						result = "#marker-end-" + line.colorIndex;
	    			}else{
	    				result = "#marker-end-16";
	    			}
				}
				result = "url(" + result + ")";
			}else{
				result = null;
			}
			
			return result;
		};
		
		var debug = function(){
			nodes[0]["type"] = "EXCEL";
			nodes[0]["name"] = "debug";
			d3.selectAll('.nodeName').data(nodes, function(d,i){return d.id;}).text(function(d){return d["name"]});
			d3.selectAll("path").style("stroke", "url(#fadeout)");
		};
		
		return{
			draw : draw,
			debug : debug
		};
	})();
});
