function getRequest(){
	if(location.search.length > 1) {
		var get = new Object();
		var ret = location.search.substr(1).split("&");
		for(var i = 0; i < ret.length; i++) {
			var r = ret[i].split("=");
			get[r[0]] = r[1];
		}
		return get;
	} else {
		return false;
	};
};


window.onload = init;
function init() {
	$("add").onclick = add;
	$("property").onchange = selectProperty
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

function selectProperty() {
	var pid = $("property").value;
	
	$$(".vars").each(function(obj){
		obj.style.display = "none";
	});
	
	if(pid == -1) {
		$("vars_prop_notselect").style.display = "inline";
		$("add").disabled = true;
	} else {
		$("vars_prop_" + pid).style.display = "inline"
		$("add").disabled = false
	}
	
}


function add() {
	var pid = $("property").value;
	
	var get = getRequest();
	var param = "?filename=" + get["filename"] + "&propId=" + pid;
	
	var propdiv = $("vars_prop_" + pid);
	var props = propdiv.childNodes;
	for(var i = 0; i < props.length; i++) {
		if(props[i].tagName == "INPUT") {
			var input = props[i];
			param += "&" + input.id + "=" + input.value;
		}
	}
	
	new Ajax.Request("add.php", {
		method: "GET", parameters: param,
		onSuccess: function(response) {
			location.href = "?filename=" + get["filename"];
		}
	});
}

