<?php
	$filename = $_GET["filename"];
	$pid = $_GET["propId"];

	$xml_str = file_get_contents("files/$filename");
	$xml = simplexml_load_string($xml_str);
	
// 	<IADPInfo>
// 		<Properties>
// 			<Property id=$propId>
// 				<Variables>
// 					<Variable id="$P">AnyUserEvent</Variable>
// 					<Variable id="$S">onload</Variable>
// 				</Variables>
// 			</Property>
// 			...
// 		</Properties>
// 	</IADPInfo>
	
	$props = $xml->IADPInfo->Properties;
	
	$prop = $props->addChild("Property");
	$prop->addAttribute("id", $_GET["propId"]);
	$vars = $prop->addChild("Variables");
	
	while($item = current($_GET)) {
		if(key($_GET) == "filename" || key($_GET) == "propId") {
			// NOP
		} else {
			$var = $vars->addChild("Variable", $item);
			$var->addAttribute("id", key($_GET));
		}
		next($_GET);
	}
	
	file_put_contents("files/$filename", $xml->asXML());
	header("Location: ./?filename=$filename");
?>