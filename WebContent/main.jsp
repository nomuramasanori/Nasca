<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<!-- <meta httpequiv="Content-Type" content="text/html; charset=ISO-8859-1"> -->
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Expires" content="Thu, 01 Dec 1994 16:00:00 GMT">
<script type="text/javascript" src="js/jquery-3.1.0.min.js" charset="utf-8"></script>
<script type="text/javascript" src="js/jstree.min.js" charset="utf-8"></script>
<script src="http://d3js.org/d3.v3.js" charset="utf-8"></script>
<script type="text/javascript" src="js/d3-context-menu.js" charset="utf-8"></script>
<script type="text/javascript" src="js/msdropdown/jquery.dd.js" charset="utf-8"></script>
<script type="text/javascript" src="js/nasca.js" charset="utf-8"></script>
<script type="text/javascript" src="js/nasca.nodeTree.js" charset="utf-8"></script>
<script type="text/javascript" src="js/nasca.dataFlow.js" charset="utf-8"></script>
<script type="text/javascript" src="js/nasca.utility.js" charset="utf-8"></script>
<link rel="shortcut icon" href="img/favicon128.ico" >
<link href="https://fonts.googleapis.com/css?family=Dancing+Script" rel="stylesheet">
<link href="https://fonts.googleapis.com/earlyaccess/notosansjapanese.css" rel="stylesheet" />
<link rel="stylesheet" href="js/themes/default/style.min.css" />
<link rel="stylesheet" href="css/d3-context-menu.css" />
<link rel="stylesheet" href="css/msdropdown/dd.css" />
<link rel="stylesheet" href="css/pisces.css" />
<title>Nasca main</title>
</head>
<body>
	<div id="header">
		<div id="logoArea">
			<object id="logo" type="image/svg+xml" data="./img/nasca.svg"></object>
		</div>
		<div id="title">
			<h1>Nasca</h1>
		</div>
	</div>
	<div id="nodeList">
		<div id="jstree_demo_div"></div>
	</div>
</body>
</html>