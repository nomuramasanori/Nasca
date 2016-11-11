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
		      		[255,255,0], [0,255,0], [0,0,255], [255,64,0],
		      		[64,255,0], [0,64,255],　[0,255,64],　[64,0,255],
		      		[255,0,64],　[160,32,32],　[196,128,64],　[128,196,64],
		      		[64,128,196],　[64,196,128],　[96,96,96], [0,0,0]
		];
		
		//初期化処理
		(function(){
			force = d3.layout.force()
				.size([nasca.frame.wMain, nasca.frame.hMain])
				.nodes(nodes)
				.links(links)
				.linkDistance(60)
				.charge(-8000)
				.gravity(0.4)
				.friction(0.7);
			
			svg = d3.select("body")
				.append("svg")
				.attr("id", "drawingPaper")
				.attr("width", nasca.frame.wMain)
		        .attr("preserveAspectRatio", "xMidYMid meet")
				.attr("pointer-events", "all")
				.append("g")
				.call(d3.behavior.zoom().scaleExtent([0.3, 6]).on("zoom", function(){
					svg.attr("transform", "translate(" + d3.event.translate + ")scale(" + d3.event.scale + ")");
				}))
				.append("g")
				.attr("id", "test");
			
			svg.append("rect")
				.attr("id", "background")
				.attr("width",nasca.frame.wMain)
				.attr("height",nasca.frame.hMain)
				.attr("fill","white");
			
			// build the arrow.
			svg.append("svg:defs").selectAll("marker")
			    .data(colors)      // Different link/path types can be defined here
			    .enter().append("svg:marker")    // This section adds in the arrows
			    .attr("id", function(d,i){
			    	return "marker-end-" + ("0000" + i.toString(2)).slice(-4);
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
			
			svg.append("svg:defs").selectAll("marker")
			    .data(colors)      // Different link/path types can be defined here
			    .enter().append("svg:marker")    // This section adds in the arrows
			    .attr("id", function(d,i){
			    	return "marker-start-" + ("0000" + i.toString(2)).slice(-4);
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
	
			svg.append("svg:g");
		})();
		
		var draw = function(json){
			//d3にバインドされているデータを更新します。
			updateData(json);
			
			//リンク描画
			var link = svg.select("g").selectAll("path").data(links, function(d,i){return d.source.id + '-' + d.target.id;});
		    link.enter().append("svg:path")
		    	.style("fill", "none")
		    	.style("stroke", function(d){
		    		var colorIndex = parseInt(d["crud"],2);
		    		var color = colors[colorIndex];
		    		return "rgb(" + color[0] + "," + color[1] + "," + color[2] + ")";
		    	})
		    	.style("stroke-width", "1.5px")
		    	.attr("marker-start", function(d){
		    		if(d["io"] === "I" || d["io"] === "IO"){
		    			return "url(#marker-start-" + d["crud"] + ")";
		    		}
		    	})
		    	.attr("marker-end", function(d){
		    		if(d["io"] === "O" || d["io"] === "IO"){
		    			return "url(#marker-end-" + d["crud"] + ")";
		    		}
		    	});
	
			link.exit().remove();
			
			//ノード背景描画
			var node = svg.selectAll("circle").data(nodes, function(d,i){return d.id;});
			node.enter().append("circle")
				.attr({r: 20, opacity: 1.0})
				.attr("fill", "white");
			node.exit().remove();
	
			//ノード描画
			var img = svg.selectAll("image").data(nodes, function(d,i){return d.id;});
			img.enter().append("image")
			.attr("xlink:href", function(d){
				return "./img/" + d["svg-file"];
			}) //ノード用画像の設定
			.attr("x", "-16px")
			.attr("y", "-16px")
			.attr("width", "32px")
			.attr("height", "32px")
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
			.on("click", function(d){
				d3.event.stopPropagation();
				$('#jstree_demo_div').jstree('select_node', d.id);
			})
			.call(force.drag);
			img.exit().remove();
			
			//オブジェクト名称描画
			var text = svg.selectAll("text.nodeName").data(nodes, function(d,i){return d.id;});
			text.enter().append("text")
				.attr("class", "nodeName")
			    .text( function (d) { return d.name; })
			    .attr("font-family", "sans-serif")
			    .attr("font-size", "20px")
			    .attr("fill", "darkgray");
			text.exit().remove();	
			
			force.on("tick", function() {
				link.attr("d", function(d) {
					var dr;
					if(d["duplex"]){
						var dx = d.target.x - d.source.x,
			            dy = d.target.y - d.source.y;
			            dr = Math.sqrt(dx * dx + dy * dy);
					}else{
				        dr = 0;
					}
					return "M" + 
		            d.source.x + "," + 
		            d.source.y + "A" + 
		            dr + "," + dr + " 0 0,1 " + 
		            d.target.x + "," + 
		            d.target.y;
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
			var length;
			var target,source;
	
			//ノードを追加・削除
			//変更のないデータを削除⇒追加するとD3への束縛に不具合が発生するので変更分のみ追加・削除を行う
			j = json["nodes"].length;
			for(i=0; i<j ; i++){
				if(!contain(json["nodes"][i]["id"], nodes, function(d){return d.id;})){
					nodes.push(json["nodes"][i]);
				}
			}
			j = nodes.length;
			for(i=j-1; 0<=i ; i--){
				if(!contain(nodes[i]["id"], json["nodes"], function(d){return d.id;})){
					nodes.splice(i, 1);
				}
			}
			
			//リンクを追加・削除
			//変更のないデータを削除⇒追加するとD3への束縛に不具合が発生するので変更分のみ追加・削除を行う
			j = json["links"].length;
			for(i=0; i<j ; i++){
				if(!contain(json["links"][i]["source"] + json["links"][i]["target"], links, function(d){return d.source.id + d.target.id;})){
					target = nodes.filter(function(item, index){
						if (item.id == json["links"][i]["target"]) return true;
					});
					
					source = nodes.filter(function(item, index){
						if (item.id == json["links"][i]["source"]) return true;
					});
					
					links.push({source: source[0], target: target[0], crud: json["links"][i]["crud"], remark: json["links"][i]["remark"], io: getIO(json["links"][i]["crud"])});
				}
			}
			j = links.length;
			for(i=j-1; 0<=i ; i--){
				if(!contain(links[i]["source"]["id"] + links[i]["target"]["id"], json["links"], function(d){return d.source + d.target;})){
					links.splice(i, 1);
				}
			}
		};
		
		//引数に渡した値が配列に含まれているかどうかを検索します
		var contain = function(val, arr, func){
			var i,j;
			j=arr.length;
			for(i=0; i<j; i++){
				if(val === func(arr[i])){
					return true;
				}
			}
			return false;
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
		
		var getIO = function(crud){
			var result;
			var r,cud;
			r = crud.substr(1, 1);
			cud = crud.substr(0, 1) + crud.substr(2, 2);
			
			if(r === "1" && cud !== "000"){
				result = "IO";
			} else if(r === "0" && cud !== "000"){
				result = "O";
			} else{
				result = "I";
			}
			return result;
		};
		
		return{
			draw : draw
		};
	})();
});
