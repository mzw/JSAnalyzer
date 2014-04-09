<?php
	$filename = $_GET["filename"];
	
	$xml_str = file_get_contents("files/$filename");
	$xml = simplexml_load_string($xml_str);
?>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>IADP Info Repository</title>
<link rel="stylesheet" href="css/base.css" type="text/css" />
<script type="text/javascript" src="js/prototype.js"></script>
<script type="text/javascript" src="js/myscript.js"></script>
</head>

<body>
<div id="header">
<h1>IADP Info Repository</h1>
</div>

<div id="content">
<?php 
	if($filename == null) {
		echo "<span class=\"error\">Create or select your repository file <a href=\"new_or_select.php\">here</a>.</span>";
	}
?>

<h2 class="iadp">Project Information</h2>
<dl>
<dt>Name</dt>
<dd><?php print($xml->Project["name"]);?></dd>
<dt>URL</dt>
<dd><a href="<?php print($xml->Project["url"]);?>"><?php print($xml->Project["url"]);?></a></dd>
</dl>

<h2 class="iadp">Add Ajax Design Properties</h2>
<select id="property">
<option value="-1">--Please select--</option>
<?php
$p_xml_str = file_get_contents("properties.xml");
$p_xml = simplexml_load_string($p_xml_str);	
foreach($p_xml->Property as $property) {
	print("<option value=\"" . $property["id"] . "\">" . $property->AjaxDesignPattern . " - " . $property->Name . "</option>\n");
}
?>
</select>

<h2 class="iadp">Enter Variables for Template Formula</h2>

<span class="vars" id="vars_prop_notselect">Select your Ajax design property above</span>
<?php
foreach($p_xml->Property as $property) {
	print("<div class=\"vars\" style=\"display:none;\" id=\"vars_prop_" . $property["id"] . "\">\n");
	foreach($property->Variables->Variable as $var) {
		print("<input type=\"text\" id=\"" . $var["id"] ."\" value=\"" . $var["semantic"] . "\"></input>\n");
	}
	print("</div>\n");
}
?>
<!-- <p><a href="var_cands.php?filename=<?php print($filename);?>" target="_blank">(variable candidates)</a></p> -->

<p>
<input id="add" type="submit" value="Add IAPD info" disabled="true" />
</p>


<h2 class="iadp">Implemented Ajax Desing Patterns</h2>
<?php
	$prop_num = count($xml->IADPInfo->Properties->Property);
	if($prop_num == 0) {
		print("<p>Enter information about your implemented Ajax design patterns</p>");
	} else {
		print("<table cellspacing=1>");
		print("<thead>");
		print("<th>Property</th>");
		print("<th>Requirement</th>");
		print("<th>Ajax Design Pattern</th>");
		print("<th>Property Pattern</th>");
		print("<th>CTL Template</th>");
		print("<th>Variable (key)</th>");
		print("<th>Variable (key)</th>");
// 		print("<th>Variable (key)</th>");
		print("</thead>");
		foreach($xml->IADPInfo->Properties->Property as $prop) {

			print("<tr>");
			foreach($p_xml->Property as $p_prop) {

				if(!strcmp($p_prop["id"], $prop["id"])) {
					print("<td>" . $p_prop->NameAbbr . "</td>");
					print("<td>" . $p_prop->Requirement . "</td>");
					print("<td>" . $p_prop->AjaxDesignPattern . "</td>");
					print("<td>" . $p_prop->PropertyPattern . "</td>");
					print("<td>" . $p_prop->CTLTemplate . "</td>");
					break;
				}
			}

			foreach($prop->Variables->Variable as $var) {
				$key = $var["id"];
				print("<td>$var ($key)</td>");
			}
			print("</tr>");
		}
		print("</table>");
	}
?>

</div>

<div id="footer">
<label>IADP Info Repository, 2014.</label>
</div>

</body>
</html>
