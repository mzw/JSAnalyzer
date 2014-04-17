window.onload = function(e) {init();}

function init() {
	if(document.getElementById("availStatus")) {
		
		//Status indicator container
		var availStatus = document.getElementById("availStatus");
		
		//Shoe color dropdown
		if(document.getElementById('drpCustomTagProductColor_0')) {
			var shoeColorSelect = document.getElementById("drpCustomTagProductColor_0");
		}
		
		//Product color dropdown
		if(document.getElementById('prodDd') && sliceAvailStatus[0].color_code != "One Color") {
			var prodColorSelect = document.getElementById('prodDd');
		}
		//One Color
		if(sliceAvailStatus[0].color_code == "One Color" && sliceAvailStatus[0].storeAvailStatus != undefined) {
			availStatus.innerHTML = sliceAvailStatus[0].storeAvailStatus;
		}
		//Check to see if the shoe has multiple colors
		if($$("span.drpCustomTagProductColor_0") != "") {
			checkAvailLocation("noMultiColor");
		} else if($$("select#drpCustomTagProductColor_0") != "") {
			shoeColorSelect.observe("change", function() {
				checkAvailLocation("standardShoe");
			});	
		} else {
			if (prodColorSelect) {
				$(prodColorSelect).observe('change', function(){
					checkAvailLocation("standard");
				})
			}
			/*
				prodColorSelect.onchange = function() {
					checkAvailLocation("standard");
				}
			*/
		}
	}
	
	function checkAvailLocation(prodType) {
		if(prodType == "noMultiColor") {
			var currColor = sliceAvailStatus[0].storeAvailstatus;
		} else if(prodType == "standardShoe"){
			currColor = colorHandler(shoeColorSelect);
			
		} else if(prodType == "standard") {
			var currColor = colorHandler(prodColorSelect);
		}
		availStatus.innerHTML = currColor;
	}

	function colorHandler(dropdown) {
		var prodIndex = dropdown.selectedIndex;
		var	prodIndexValue = dropdown[prodIndex].text.replace("* ", "");
		var	colorValue = prodIndexValue.split(" :");
		var	currColor = colorValue[0].toLowerCase();
		if(currColor == "please select a color" || currColor == "select color" ) {
			var currColor = "";
		} else {
			for(i=0, len=sliceAvailStatus.length; i < len; i++){
			var colorCode = sliceAvailStatus[i].color_code.toLowerCase();
				if(colorCode == currColor){
					var currColor = (sliceAvailStatus[i].storeAvailstatus);
				}
			}
		}
		return currColor;
	}
}
