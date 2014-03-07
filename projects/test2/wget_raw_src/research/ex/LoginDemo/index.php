<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>LoginDemo</title>
<link rel="stylesheet" href="css/base.css" type="text/css" />
<script type="text/javascript" src="js/prototype.js"></script>
<script type="text/javascript" src="js/jshash/md5.js"></script>
<script type="text/javascript"><!--//
var LOGIN_PREFIX = "loginProc.php";

var loggedIn;
var hasSeed;
window.onload = function() {
	/// login/out
	loggedIn = false;
	hasSeed = false;

	// User Action.User event registration
	var u = document.getElementById("username");
	u.onfocus = getSeed;
	u.onkeyup = validateInput;


	var p = document.getElementById("password");
	p.onfocus = getSeed;
	p.onkeyup = validateInput;

	var s = document.getElementById("submit");
	s.onclick = validateLogin;
	s.disabled = true;

	/// file access
	var o0 = document.getElementById("object_0");
	o0.onclick = readPublicDoc;
	o0.disabled = true;

	var o1 = document.getElementById("object_1");
	o1.onclick = readUserDoc;
	o1.disabled = true;

	var o2 = document.getElementById("object_2");
	o2.onclick = readPremierDoc;
	o2.disabled = true;
};

///// login/out
function getSeed() {
	if(!loggedIn && !hasSeed) {
		// Live Form.Process before submission?
		new Ajax.Request(LOGIN_PREFIX, {
			method: 'GET',
			parameters: 'task=getseed',
			onSuccess: handleHttpGetSeed
		});
	} else {
		// NOP
	}
};

var seed_id;
var seed;
function handleHttpGetSeed(request) {
	var results = request.responseText.split('|');
	seed_id = results[0];
	seed = results[1];
	
	hasSeed = true;
};

function validateInput() {
	var len_un = $("username").value.length;
	var len_pw = $("password").value.length;

	var s = document.getElementById("submit");
	if(0 < len_un && 0 < len_pw) {
		s.disabled = false;
	} else {
		s.disabled = true;
	}
}

function validateLogin() {
	var username = $("username").value;
	var password = $("password").value;

	hash = hex_md5(hex_md5(password).concat(seed));

	// Explicit Submission.User event before submission
	new Ajax.Request(LOGIN_PREFIX, {
		method: 'GET',
		parameters: 'task=checklogin&username='+username+'&id='+seed_id+'&hash='+hash,
		onSuccess: tryLogin
	});
};

function tryLogin(request) {
	var results = request.responseText.split('|');
	var result = results[0];
	if(result == "true") {
		successLogin(results[1], results[2]);
	} else {
		alert("Invalid username and/or password");
	}
};

function successLogin(fullname, sec_level) {
	$("message").innerText = "Logged in as ";
	var elm_fn = document.createElement("b");
	elm_fn.id = "fullname";
	elm_fn.innerText = fullname;
	$("message").appendChild(elm_fn);

	$("message").appendChild(document.createTextNode(" ["));
	
	var elm_logout = document.createElement("a");
	elm_logout.id = "logout";
	elm_logout.onclick = tryLogout;
	elm_logout.innerText = "logout";
	$("message").appendChild(elm_logout);

	$("message").appendChild(document.createTextNode("]"));

	disableLoginForm();
	changeDocumentAccess(sec_level);
	
	loggedIn = true;	
};

function disableLoginForm() {
	var u = document.getElementById("username");
	u.disabled = true;
	
	var p = document.getElementById("password");
	p.disabled = true;
	
	var s = document.getElementById("submit");
	s.disabled = true;
	document.getElementById("submit").style.display = "none";
};

function enableLoginForm() {
	var u = document.getElementById("username");
	u.disabled = false;
	u.value = "";
	
	var p = document.getElementById("password");
	p.disabled = false;
	p.value = "";
	
	var s = document.getElementById("submit");
	s.disabled = true;
	document.getElementById("submit").style.display = "inline";

	$("message").innerText = "Enter your username and password to log in.";
};

function tryLogout() {
	// User Action.User event handler singleton
	var elm_logout = document.getElementById("logout");
	elm_logout.disabled = true;
	
	enableLoginForm();
	
	loggedIn = false;
	hasSeed = false;

	// disable all objects
	var o0 = document.getElementById("object_0");
	o0.disabled = true;

	var o1 = document.getElementById("object_1");
	o1.disabled = true;

	var o2 = document.getElementById("object_2");
	o2.disabled = true;
};

///// file access
function setObjectReadHandler() {
	var o0 = document.getElementById("object_0");
	o0.onclick = readPublicDoc;

	var o1 = document.getElementById("object_1");
	o1.onclick = readUserDoc;
	o1.disabled = true;

	var o2 = document.getElementById("object_2");
	o2.onclick = readPremierDoc;
	o2.disabled = true;
}

function changeDocumentAccess(sec_level) {
	// hard coding
	if(sec_level == "0") {
		loginAsAnonymous();
	} else if(sec_level == "1") {
		loginAsUser();
	} else if(sec_level == "2") {
		loginAsSuperUser();
	}
}

/* @loginAsAnonymous.subject-level = "0"; */
function  loginAsAnonymous() {
	var o0 = document.getElementById("object_0");
	o0.disabled = false;
	
	var o1 = document.getElementById("object_1");
	o1.disabled = true;

	var o2 = document.getElementById("object_2");
	o2.disabled = true;
}
/* @loginAsUser.subject-level = "1"; */
function loginAsUser() {
	var o0 = document.getElementById("object_0");
	o0.disabled = false;
	
	var o1 = document.getElementById("object_1");
	o1.disabled = false;

	var o2 = document.getElementById("object_2");
	o2.disabled = true;
}
function loginAsSuperUser() {
	var o0 = document.getElementById("object_0");
	o0.disabled = false;
	
	var o1 = document.getElementById("object_1");
	o1.disabled = false;

	var o2 = document.getElementById("object_2");
	o2.disabled = false;
}

/* @readPublicDoc.object-level="0"; */
function readPublicDoc() {
	// hard coding due to bugs on JSAnalyzer
	var u = document.getElementById("username");
	u.disabled = true;
	
	var p = document.getElementById("password");
	p.disabled = true;
	
	var s = document.getElementById("submit");
	s.disabled = true;

	location.href = "http://maezawa.honiden.nii.ac.jp/yuta/research/cs/LoginDemo/doc/public";
}
/* @readUserDoc.object-level="1"; */
function readUserDoc() {
	// hard coding due to bugs on JSAnalyzer
	var u = document.getElementById("username");
	u.disabled = true;
	
	var p = document.getElementById("password");
	p.disabled = true;
	
	var s = document.getElementById("submit");
	s.disabled = true;
	
	location.href = "http://maezawa.honiden.nii.ac.jp/yuta/research/cs/LoginDemo/doc/user";
}
/* @readPremierDoc.object-level="2"; */
function readPremierDoc() {
	// hard coding due to bugs on JSAnalyzer
	var u = document.getElementById("username");
	u.disabled = true;
	
	var p = document.getElementById("password");
	p.disabled = true;
	
	var s = document.getElementById("submit");
	s.disabled = true;

	location.href = "http://maezawa.honiden.nii.ac.jp/yuta/research/cs/LoginDemo/doc/premier";
}
//--></script>
</head>

<body>

<h1>Demonstration</h1>

<div id="login" class="login" style="width:100px;">
<label for="username">Username: </label><br />
<input name="username" id="username" size="20" type="text" /><br />
<label for="password">Password: </label><br />
<input name="password" id="password" size="20" type="password" /><br />
<p id="message">Enter your username and password to log in.</p>
<input name="submit" id="submit" value="submit" type="submit" />
</div>

<div style='display:none;'>
<h2>Objects</h2>
<div id="objects" class="objects">
<input name="object_0" id="object_0" value="Public document" type="submit" />
<input name="object_1" id="object_1" value="User document" type="submit" />
<input name="object_2" id="object_2" value="Premier document" type="submit" />
</div>
</div>

</body>
