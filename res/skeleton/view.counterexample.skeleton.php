<?php
	$projName = $_GET["projname"];
	$specId = $_GET["specid"];
	$stepnum = $_GET["stepnum"];
?>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Counterexample Viewer</title>
<link rel="stylesheet" href="../../css/base.css" type="text/css" />
<script type="text/javascript"><!--//
var step = 0;
var projName = "<?php echo $projName;?>";
var specId = <?php echo $specId;?>;
window.onload = function() {
	step = 0;
	document.getElementById("prev").onclick = goPrev;
	document.getElementById("next").onclick = goNext;
	
	document.getElementById("prev").disabled = true;
}
function goPrev() {
	if(0 < step) {
		document.getElementById("view").src = "counterexample/" + projName + ".spec." + specId + ".step." + --step + ".dot.png";
		document.getElementById("next").disabled = false;
	} else {
		document.getElementById("prev").disabled = true;
	}
}
function goNext() {
	if(step < <?php echo $stepnum?> - 1) {
		document.getElementById("view").src = "counterexample/" + projName + ".spec." + specId + ".step." + ++step + ".dot.png";
		document.getElementById("prev").disabled = false;
	} else {
		document.getElementById("next").disabled = true;
	}
}
//--></script>
</head>

<body>
<div id="header">
<h1>Counterexample Viewer</h1>
</div>

<div id="content">

<div>
<button id="prev">prev</button>
<button id="next">next</button>
</div>

<img id="view" width="750px" src="counterexample/<?php echo $projName;?>.spec.<?php echo $specId;?>.step.0.dot.png" />

</div>

<div id="footer">
<label>JSVerifier, 2014.</label>
</div>

</body>
</html>
