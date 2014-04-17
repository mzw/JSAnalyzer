
    var USIqs = "";
    var USIsiteID = "";
    var USI_key = "";
    if ((location.href.indexOf("/cart/") != -1 || location.href.indexOf("/checkout/") != -1) && location.href.indexOf("process=thanks") == -1) {
        USIqs = "220244250211314314307326336300306341295306278280276279311314";
        USIsiteID = "5839";
    }
	   
    if (USIqs != "") {
        var USI_headID = document.getElementsByTagName("head")[0];
        var USI_dynScript = document.createElement("script");
        USI_dynScript.setAttribute("type","text/javascript");
        USI_dynScript.setAttribute("src","//www.upsellit.com/upsellitJS4.jsp?qs="+USIqs+"&siteID="+USIsiteID+"&keys="+USI_key);
        USI_headID.appendChild(USI_dynScript);
    }

