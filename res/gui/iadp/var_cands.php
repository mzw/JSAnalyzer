<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN">


<?php
	$filename = $_GET["filename"];
	
	$xml_str = file_get_contents("files/$filename");
	$xml = simplexml_load_string($xml_str);
?>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>IADP Info Repository</title>
	<link rel="stylesheet" href="css/base.css" type="text/css" />
	<script type="text/javascript" src="js/prototype.js"></script>
	<script type="text/javascript" src="js/myscript.js"></script>
<script type="text/javascript"><!--//
window.onload = init;
function init() {
	$("func_filter").onkeyup = filterFunc;
	$("event_filter").onkeyup = filterEvent;
}
function filterFunc() {
	var filter = $("func_filter").value;
	for(var i = 0; i < $("func").options.length; i++) {
		var opt = $("func").options[i];
		
		if(opt.text.search(filter)) {
			opt.disabled = true;
		} else {
			opt.disabled = false
		}
	}
}

function filterEvent() {
	var filter = $("event_filter").value;
	for(var i = 0; i < $("event").options.length; i++) {
		var opt = $("event").options[i];
		
		if(opt.text.search(filter)) {
			opt.disabled = true;
		} else {
			opt.disabled = false
		}
	}
}
//--></script>
</head>

<body>
<div id="header">
<h1>IADP Info Repository</h1>
</div>

<div id="content">

<h2>Variable Candidates</h2>
<dl>
<dt>Functions</dt>
<dd>
<select id="func">
<?php
	foreach($xml->FSMData->State as $state) {
		foreach($state->Abstracted as $func) {
			print("<option value=\"". $state["id"] . "\">" . $func["func"] . " (lineno=" . $func["lineno"] . ", pos=" . $func["pos"] . ")" . "</option>");
		}
	}
?>
</select>
</dd>
<dd>Filter: <input type="text" id="func_filter"></input></dd>
<dt>Events</dt>
<dd>
<select id="event">
<?php
	foreach($xml->FSMData->Event as $event) {
		print("<option value=\"". $event["id"] . "\">" . $event["name"] . " (lineno=" . $event["lineno"] . ", pos=" . $event["pos"] . ")" . "</option>");
	}
?>
</select>
</dd>
<dd>Filter: <input type="text" id="event_filter"></input></dd>
</dl>
</div>

<div id="footer">
<label>IADP Info Repository, 2014.</label>
</div>


</body>
</html>
