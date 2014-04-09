<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN">

<?php
$from = $_GET["from"];
?>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>IADP Info Repository</title>
		<link rel="stylesheet" href="css/base.css" type="text/css" />
		<script type="text/javascript"><!--//
window.onload = function() {
	document.getElementById("select").onclick = select_file;		
};
function select_file() {
	var select = document.getElementById("existing");
	var options = select.options;
	var value = options.item(select.selectedIndex).value;
	location.href = "./<?php echo $from;?>?filename=" + value + "";
};
		//--></script>
	</head>

	<body>

<div id="header">
<h1>IADP Info Repository</h1>
</div>

<div id="content">

<h2>Create New Repository File</h2>
<ol>
<li>Run JSAnalyzer: jp.mzw.jsanalyzer.verifier.Verifier#setup</li>
<li>JSAnalyzer generates a repository file "YOUR_FILE"</li>
<li>Simultaneously, it lets you know a URL "http://path/to/iadp/files/YOUR_FILE"</li>
<li>Directedly access the URL</li>
<li>Or select the options below where the "YOUR_FILE" will appear</li>
</ol>

<h2>Select Your Repository File</h2>

<select id="existing">
<?php
if ($dir = opendir("files/")) {
	while (($file = readdir($dir)) !== false) {
		if ($file != "." && $file != "..") {
			echo "<option value=\"$file\">$file</option>\n";
		}
		}
		closedir($dir);
}
?>
</select>
<input id="select" type="submit" value="select"></input>
</div>

<div id="footer">
<label>IADP Info Repository, 2014.</label>
</div>

</body>
</html>