// SetFocus();
// ***************************************************************************
// ***************************************************************************
// ***************************************************************************
function SubmitIt(Actions)
{
document.forms[0].Actions.value = Actions;
document.forms[0].submit();
}
function SubmitPage(ASPHandler, Actions)
{
document.forms[0].action = ASPHandler;
document.forms[0].Actions.value = Actions;
document.forms[0].submit();
}

// ***************************************************************************
function ConfirmIt(Actions) {
if (!confirm ("Are you sure you want to " + Actions + " this?")) {return};
document.forms[0].Actions.value = Actions;
document.forms[0].submit();
}

function ConfirmPage(ASPHandler, Actions) {
if (!confirm ("Are you sure you want to " + Actions + " this?")) {return};
document.forms[0].action = ASPHandler;
document.forms[0].Actions.value = Actions;
document.forms[0].submit();
}

// ***************************************************************************
function calpopup(obj) 
{ 
var url = "CalPopUp.asp";
var parm = "scrollbars=no,height=170,width=200,left=400,top=200";
url += "?Element=" + obj;
url += "&date=" + document.forms[0].elements[obj].value;
window.open(url, "CalPopUp", parm);
}

// ****************************************************************************
function SortAsc(obj, col) {
var siz = obj.rows.length - 1;
var tb = FindTBody(obj);
for (a = 1; a < siz; a++) {
	for (b = a + 1; b <= siz; b++) {
		if (obj.childNodes(tb).childNodes(a).childNodes(col).innerText.toUpperCase() > obj.childNodes(tb).childNodes(b).childNodes(col).innerText.toUpperCase())
			{	obj.childNodes(tb).children(a).swapNode(obj.childNodes(tb).children(b));	}
		}
	}
}
// ****************************************************************************
function SortDesc(obj, col) {
var siz = obj.rows.length - 1;
var tb = FindTBody(obj);
for (a = 1; a < siz; a++) {
	for (b = a + 1; b <= siz; b++) {
		if (obj.childNodes(tb).childNodes(a).childNodes(col).innerText.toUpperCase() < obj.childNodes(tb).childNodes(b).childNodes(col).innerText.toUpperCase())
			{	obj.childNodes(tb).children(a).swapNode(obj.childNodes(tb).children(b));	}
		}
	}
}
// ****************************************************************************
function FindTBody (obj) {
var x = 0;
while (obj.childNodes(x).tagName != "TBODY") { x++ };
return (x);
}

// ***************************************************************************
function SetFocus() {
for (var dex=0; dex < document.forms[0].elements.length; dex++) {
	if (document.forms[0].elements[dex].type != "hidden") {
		if (!document.forms[0].elements[dex].disabled) {
			document.forms[0].elements[dex].focus();
			return;
			}
		}
	}
}
// ****************************************************************************
function EnterKey(code) {
if (window.event.keyCode == 13) {
	window.event.returnValue = false;
	eval(code);	
	}
}
	
// ****************************************************************************
function grapher(title, idx) {
var txt = " ", tot = 0;
var from = idx * Months; 
var thru = from + Months;

for (dex = from; dex < thru; dex++) { tot += Results[dex]; }

txt += "<table cellspacing=10 cellpadding=0 border=0>";
txt += "<caption>" + title + "</caption>";
txt += "<tr><td><img src=images/blank.gif width=10 height=300></td>";
for (dex = from; dex < thru; dex++) {
	txt += "<td class=graphbar>";
	txt += FormatNumber(Results[dex]);
	txt += "<br>";
	txt += "<img src=images/Bar.gif width=50 height=";
	txt += Results[dex] * 300 / tot;
	txt += "></td>";
	}
txt += "</tr><tr><td>&nbsp;</td>";
for (dex = 0; dex < Periods.length; dex++) {
	txt += "<td class=graphlbl>";
	txt += Periods[dex];
	txt += "</td>";
	}
txt += "</tr></table>";

if (document.getElementById("graph")) { 
	document.getElementById("graph").innerHTML = txt;
	}
}

// ****************************************************************************
function FormatNumber(num) {
	var delimiter = ","; // replace comma if desired
	var i = parseInt(num);
	if(isNaN(i)) { return '0'; }
	i = Math.abs(i);
	var n = new String(i);
	var a = [];
	while(n.length > 3)	{
		var nn = n.substr(n.length-3);
		a.unshift(nn);
		n = n.substr(0,n.length-3);
	}
	if(n.length > 0) { a.unshift(n); }
	n = a.join(delimiter);
	return n;
}


// ****************************************************************************

function Toggle(obj) {
if (obj.className == "short") {
	obj.className = "long";
	} else {
	obj.className = "short";
	}
}

// **************************************************************************** 

function showPhoto(i) {
	var obj = window.event.srcElement;
	var x = window.event.offsetX;
	var y = window.event.offsetY;
	while (obj.offsetParent.tagName != "BODY") {
		y += obj.offsetTop;
		x += obj.offsetLeft;
		obj = obj.offsetParent;
		}
	obj = document.getElementById("preview");

	obj.border = 1;
	obj.style.top = y;
	obj.style.left = x;
	obj.style.filter="progid:DXImageTransform.Microsoft.Pixelate(MaxSquare=30, Duration=2, Enabled=false)";
    obj.filters[0].Apply();
	obj.src = i;
    obj.style.display="block";
    obj.filters[0].Play();
}

// ****************************************************************************
