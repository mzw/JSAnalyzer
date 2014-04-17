

 var xhttp;
 var oldValue;

 var activeToolTip   = '';
 var activeToolTip_L = '';

 var isIEBr        = 0;
 var activePicId   = 0;

 var activeButton  = null;
 var activeSub     = null;
 var activeBar     = null;
 var overMenu      = null;

 var mainMenuLeft;
 var mainMenuTop;

 var oldOrt;


 // Messenger
 // ===========================================================================================================================


 function messenger(userid)
 {
	var winName = 'pmsg_'+userid;
	var wleft   = (screen.availWidth - 300) / 2;
	var wtop    = (screen.availHeight - 450) / 2;
	params      = ('width=300,height=450,left='+ wleft +',top='+ wtop +',toolbar=0,location=0,directories=0,status=0,menubar=0,scrollbars=no,resizable=0');
	F1          = open('messenger.php?id='+userid, winName, params);
 }


 function ajax_messenger1(id)
 {
	if( xhttp )
	{
		xhttp.open('GET', 'ajaxMessenger1.php?id='+id, true);

		if( isIEBr == 1 )
		{
			xhttp.onreadystatechange = ajax_messenger1_callback;	
		} else
		  {
			xhttp.onload             = ajax_messenger1_callback;
		  }

		xhttp.send(null);
	}
 }


 function ajax_messenger1_callback()
 {
	if( xhttp.readyState != 4 || xhttp.status != 200 )
	{
		return;
	}

	if( xhttp.responseText )
	{
		var neuerInhalt = xhttp.responseText;
	} else
	  {
		var neuerInhalt = xhttp.responseText;
	  }

	var sucheNeu = neuerInhalt.search(/##RELOAD##.+/);
	if( sucheNeu != -1 )
	{
		window.focus();
	}

	document.getElementById('chatContent').innerHTML = neuerInhalt;
	self.location.href = '#chatEnd';
 }


 function ajax_messenger2(id)
 {
	if( xhttp )
	{
		xhttp.open('GET', 'ajaxMessenger2.php?id='+id, true);

		if( isIEBr == 1 )
		{
			xhttp.onreadystatechange = ajax_messenger2_callback;	
		} else
		  {
			xhttp.onload             = ajax_messenger2_callback;
		  }

		xhttp.send(null);
	}
 }


 function ajax_messenger2_callback()
 {
	if( xhttp.readyState != 4 || xhttp.status != 200 )
	{
		return;
	}

	if( xhttp.responseText )
	{
		var neuerInhalt = xhttp.responseText;
	} else
	  {
		var neuerInhalt = xhttp.responseText;
	  }

	document.getElementById('archivContent').innerHTML = neuerInhalt;
	//self.location.href = '#chatEnd';
 }


 // ===========================================================================================================================


 // Chat
 // ===========================================================================================================================


 function ajax_chat(raumID)
 {
	if( xhttp )
	{
		xhttp.open('GET', 'ajaxChat.php?raumID='+raumID, true);

		if( isIEBr == 1 )
		{
			xhttp.onreadystatechange = ajax_chat_callback;	
		} else
		  {
			xhttp.onload             = ajax_chat_callback;
		  }

		xhttp.send(null);
	} else
	  {
		location.reload();
		self.location.href = '#chatEnd';
	  }
 }


 function ajax_chat_callback()
 {
	if( xhttp.readyState != 4 || xhttp.status != 200 )
	{
		return;
	}

	if( xhttp.responseText )
	{
		var neuerInhalt = xhttp.responseText;
	} else
	  {
		var neuerInhalt = xhttp.responseText;
	  }

	document.getElementById('chatContent').innerHTML = neuerInhalt;
	self.location.href = '#chatEnd';
 }


 var schalteZweiten = 0;

 function ajax_infos_init(raumID, mode)
 {
	if( mode == 1 )
	{
		ajax_infos1(raumID);
	} else
	  {
		ajax_infos1(raumID);
		schalteZweiten = raumID;
	  }
 }


 function ajax_infos1(raumID)
 {
	if( xhttp )
	{
		xhttp.open('GET', 'ajaxInfos1.php?raumID='+raumID, true);

		if( isIEBr == 1 )
		{
			xhttp.onreadystatechange = ajax_infos1_callback;	
		} else
		  {
			xhttp.onload             = ajax_infos1_callback;
		  }

		xhttp.send(null);
	}
 }


 function ajax_infos1_callback()
 {
	if( xhttp.readyState != 4 || xhttp.status != 200 )
	{
		return;
	}

	if( xhttp.responseText )
	{
		var neuerInhalt = xhttp.responseText;
	} else
	  {
		var neuerInhalt = xhttp.responseText;
	  }

	document.getElementById('1').innerHTML = neuerInhalt;

	if( schalteZweiten != 0 )
	{
		ajax_infos2(schalteZweiten);
	}
 }


 function ajax_infos2(raumID)
 {
	if( xhttp )
	{
		xhttp.open('GET', 'ajaxInfos2.php?raumID='+raumID, true);

		if( isIEBr == 1 )
		{
			xhttp.onreadystatechange = ajax_infos2_callback;	
		} else
		  {
			xhttp.onload             = ajax_infos2_callback;
		  }

		xhttp.send(null);
	}
 }


 function ajax_infos2_callback()
 {
	if( xhttp.readyState != 4 || xhttp.status != 200 )
	{
		return;
	}

	if( xhttp.responseText )
	{
		var neuerInhalt = xhttp.responseText;
	} else
	  {
		var neuerInhalt = xhttp.responseText;
	  }

	document.getElementById('2').innerHTML = neuerInhalt;
 }


 // ===========================================================================================================================


 function AjaxUpdateOrte()
 {
	if( xhttp )
	{
		var ort = document.editor.ort.options[document.editor.ort.selectedIndex].value;
		ort     = encodeURIComponent(ort);
		oldOrt  = ort;

		xhttp.open('GET', 'ajaxOrte.php?ort='+ort, true);

		if( isIEBr == 1 )
		{
			xhttp.onreadystatechange = AjaxCallbackOrte;	
		} else
		  {
			xhttp.onload             = AjaxCallbackOrte;
		  }

		xhttp.send(null);
	} else
	  {
		var noAjaxContent = document.getElementById('ajax2').innerHTML;
		document.getElementById('dynOrtsteile').innerHTML = noAjaxContent;
	  }
 }


 function AjaxCallbackOrte()
 {
	if( xhttp.readyState != 4 || xhttp.status != 200 )
	{
		return;
	}

	if( xhttp.responseText )
	{
		var teileSelect = xhttp.responseText;
	} else
	  {
		var teileSelect = xhttp.responseText;
	  }

	document.getElementById('dynOrtsteile').innerHTML = teileSelect;
 }


 function Browser()
 {
	var ua, s, i;

	this.isIE    = false;  // Internet Explorer
	this.isNS    = false;  // Netscape
	this.version = null;

	ua = navigator.userAgent;

	s = "MSIE";
	if ((i = ua.indexOf(s)) >= 0)
	{
		this.isIE = true;
		this.version = parseFloat(ua.substr(i + s.length));
		return;
	}

	s = "Netscape6/";
	if ((i = ua.indexOf(s)) >= 0)
	{
		this.isNS = true;
		this.version = parseFloat(ua.substr(i + s.length));
		return;
	}

	s = "Gecko";
	if ((i = ua.indexOf(s)) >= 0)
	{
		this.isNS = true;
		this.version = 6.1;
		return;
	}
 }


 var browser = new Browser();


 if(browser.isIE)
 {
	document.onmousedown = pageMousedown;
	document.onkeyup     = seitenklick;
	document.onmousemove = pageMouseMove;
 }
 if(browser.isNS)
 {
	document.addEventListener("mousedown", pageMousedown, true);
	document.addEventListener("keyup", seitenklick, true);
	document.addEventListener("mousemove", pageMouseMove, true);
 }


 function pageMouseMove(event)
 {
	if( browser.isIE )
	{
		x = window.event.clientX;
		y = window.event.clientY;
	}
	if( browser.isNS )
	{
		x = event.clientX;
		y = event.clientY;
	}

	if( activeToolTip )
	{
		if( activeToolTip != '' )
		{
			document.getElementById(activeToolTip).style.left       = ( x + 10 ) + "px";
			document.getElementById(activeToolTip).style.top        = ( y + 20 ) + "px";
		}
	}

	if( activeToolTip_L )
	{
		if( activeToolTip_L != '' )
		{
			document.getElementById(activeToolTip_L).style.left       = ( x - 220 ) + "px";
			document.getElementById(activeToolTip_L).style.top        = ( y +  20 ) + "px";
		}
	}
 }


 function ajax_an()
 {
	var subObject = document.getElementById('suggestBlock');
	subObject.style.visibility = "visible";

	if( document.getElementById('task') )
	{
		document.getElementById('task').style.visibility        = "hidden";
	}

	if( document.getElementById('taskSwitch') )
	{
		document.getElementById('taskSwitch').style.borderColor = "#EFEFEF #C0C0C0 #C0C0C0 #EFEFEF";
	}
 }


 function ajax_aus()
 {
	if( subObject = document.getElementById('suggestBlock') )
	{
		subObject.style.visibility = "hidden";
	}
 }


 function updateList()
 {
	if( !xhttp )
	{
		return;
	}
	var q = document.home.wort.value;
	if( q == oldValue )
	{
		return;
	}
	if( q.length < 2 )
	{
		ajax_aus();
		return;
	}

	oldValue = q;

	if( isIEBr == 1 )
	{
		xhttp.onreadystatechange = updateList_callback;	
	} else
	  {
		xhttp.onload = updateList_callback;
	  }

	q = encodeURIComponent(q);

	var mode = document.getElementById('wo').value;

	xhttp.open('GET', 'ajax.php?mode='+mode+'&q='+q, true);

	if( isIEBr == 1 )
	{
		xhttp.onreadystatechange = updateList_callback;	
	} else
	  {
		xhttp.onload = updateList_callback;
	  }

	xhttp.send(null);
 }


 function updateList_callback()
 {
	if( xhttp.readyState != 4 || xhttp.status != 200 )
	{
		return;
	}

	if( xhttp.responseText )
	{
		var neuerInhalt = xhttp.responseText;
	} else
	  {
		var neuerInhalt = xhttp.responseText;
	  }

	document.getElementById('suggestBlock').innerHTML = neuerInhalt;

	ajax_an();
 }


 function seitenklick(event)
 {
	if( !event )
	{
		event = window.event;
	}

	if( browser.isIE )
	{
		hotKey = window.event.keyCode;
		isAlt  = window.event.altKey;
	}
	if( browser.isNS )
	{
		hotKey = event.which;
		isAlt  = event.altKey;
	}

	// hotKeys
	if( isAlt == true )
	{

	}
 }


 function pageMousedown(event)
 {
	var el;

	// angeklicktes Element finden
	if( browser.isIE )
	{
		el = window.event.srcElement;
	}
	if( browser.isNS )
	{
		el = (event.target.className ? event.target : event.target.parentNode);
	}

	if( el.id != 'suggestLine' )
	{
		ajax_aus();
	}

	// Ab hier f�rs Men�
 	// Abbrechen wenn kein Men� aktiv ist
	if( !activeButton )
	{
		return;
	}
	if( overMenu )
	{
		return;
	}

	// If the active button was clicked on, exit.
	if( el == activeButton )
	{
		return;
	}

	// Aktives Hauptmen� schliessen
	if( el.className != "mainMenu" )
	{
		if( activeBar )
		{
			var deSetName    = "main_" + activeBar;
			var changeObject = document.getElementById(deSetName);

			changeObject.style.borderColor = "#ECE9D8 #C0C0C0 #C0C0C0 #ECE9D8";

			activeBar = null;
		}
	}

	if( activeButton )
	{
		document.getElementById(activeButton).style.visibility = "hidden";
	}

	if( activeSub )
	{
		document.getElementById(activeSub).style.visibility = "hidden";
	}

	if( activeBar )
	{
		var deSetName    = "main_" + activeBar;
		var changeObject = document.getElementById(deSetName);

		changeObject.style.borderColor = "#ECE9D8 #C0C0C0 #C0C0C0 #ECE9D8";
	}
 }


 function getObjectLeft(name)
 {
	var idName = "main_" + name;
	return document.getElementById(idName).offsetLeft;
 }


 function getObjectWidth(name)
 {
	var idName = "main_" + name;
	var u = document.getElementById(idName).offsetWidth;
	return u;
 }

 function getObjectHeight(name)
 {
	var idName = "main_" + name;
	var u = document.getElementById(idName).offsetHeight;
	return u;
 }


 function mainBarSet(name)
 {
	if( !activeBar )
	{
		activeBar = name;
		mainBar(name);
	} else
	  {
		activeBar = null;
	  }
 }


 function mainBarOut(name)
 {
	var changeName = "main_" + name;
	var changeObject = document.getElementById(changeName);

	if( !activeBar )
	{
		changeObject.style.borderColor = "#ECE9D8 #C0C0C0 #C0C0C0 #ECE9D8";
	}
 }


 function mainBar(name)
 {
	var idName     = "menu_" + name;
	var changeName = "main_" + name;

	var tempObject   = document.getElementById(idName);
	var changeObject = document.getElementById(changeName);

	if( activeButton )
	{
		document.getElementById(activeButton).style.visibility = "hidden";
	}

	if( activeSub )
	{
		document.getElementById(activeSub).style.visibility = "hidden";
	}

	activeButton = idName;

	if( activeBar )
	{
		var tBar = "main_" + activeBar;

		document.getElementById(tBar).style.borderColor = "#ECE9D8 #C0C0C0 #C0C0C0 #ECE9D8";

		activeBar = name;

		changeObject.style.borderColor = "#808080 #f0f0f0 #f0f0f0 #808080";

		// Button zeigen
		var oL = getObjectLeft(name);
		var oW = getObjectWidth(name);
		var oH = getObjectHeight(name);

		// Beim Opera das SubMen� nach rechts verschieben
		mn = navigator.userAgent;
		s = "Opera";
		if ((i = mn.indexOf(s)) >= 0)
		{
			oL += 12;
		}

		tempObject.style.position = "absolute";
		tempObject.style.left =  oL + "px";
		tempObject.style.top  =  ( mainMenuTop  + oH ) + "px";

		tempObject.style.visibility = "visible";
	} else
	  {
		changeObject.style.borderColor = "#f0f0f0 #808080 #808080 #f0f0f0";
	  }
 }


 function subColorOn(name)
 {
	var lineName   = "line_"  + name;
	var lineObject = document.getElementById(lineName);

	lineObject.style.backgroundColor = "#000080";
	lineObject.style.color = "#ffffff";

	overMenu = name;
 }


 function subColorOff(name)
 {
	var lineName   = "line_"  + name;
	var lineObject = document.getElementById(lineName);

	lineObject.style.backgroundColor = "#ffffff";
	lineObject.style.color = "#000000";

	overMenu = null;
 }


 function subColorOn2(name)
 {
	var lineName   = "subLine_"  + name;
	var lineObject = document.getElementById(lineName);

	lineObject.style.backgroundColor = "#000080";
	lineObject.style.color = "#ffffff";

	overMenu = name;
 }


 function subColorOff2(name)
 {
	var lineName   = "subLine_"  + name;
	var lineObject = document.getElementById(lineName);

	lineObject.style.backgroundColor = "#ffffff";
	lineObject.style.color = "#000000";

	overMenu = null;
 }


 function subArrowOn(name)
 {
	var subPic = "subImg_" + name;
	document.getElementById(subPic).src = "images/menu/subOff.gif";
 }


 function subArrowOff(name)
 {
	var subPic = "subImg_" + name;
	document.getElementById(subPic).src = "images/menu/sub.gif";
 }


 function subMenuOff(name)
 {
	if( activeSub )
	{
		document.getElementById(activeSub).style.visibility = "hidden";
	}

	activeSub = null;
 }


 function subMenuOn(name)
 {
	if( activeSub )
	{
		document.getElementById(activeSub).style.visibility = "hidden";
	}

	var idName  = "menu_" + name;
	var subName = "sub_"  + name;

	var tempObject = document.getElementById(idName);

	var oL = tempObject.offsetLeft;
	var oW = tempObject.offsetWidth;
	var oH = tempObject.offsetTop;

	var subObject = document.getElementById(subName);

	subObject.style.position = "absolute";
	subObject.style.left = ( oL + oW - 2 ) + "px";
	subObject.style.top  = oH  + "px";

	subObject.style.visibility = "visible";

	activeSub = subName;
 }


 function link(ziel)
 {
	self.location.href = ziel;
 }


 function linkExtern(ziel)
 {
	openTarget = open(ziel, 'newWindow');
 }


 function initAjax()
 {
	// Ajax
	if( window.ActiveXObject )
	{
		try
		{
			// IE 6.0
			xhttp = new ActiveXObject("Msxml2.XMLHTTP");
			isIEBr = 1;
		} catch(e)
		  {
			// IE 5.x
			try
			{
				xhttp = new ActiveXObject("Microsoft.XMLHTTP");
				isIEBr = 1;
			} catch(e)
			  {
				xhttp = false;
			  }
		  }
	} else
		if( window.XMLHttpRequest )
		{
			// Mozilla, Opera, Safari
			try
			{
				xhttp = new XMLHttpRequest();
			} catch(e)
			  {
				xhttp = false;
			  }
		}
 }


 function init()
 {
	// Men�
	mainMenuLeft = document.getElementById('menu').offsetLeft;
	mainMenuTop  = document.getElementById('menu').offsetTop;

	var initContainer   = new Array('menu_1', 'menu_2');

	for( iC = 0; iC < initContainer.length; iC++ )
	{
		var subObject = document.getElementById(initContainer[iC]);

		subObject.style.position = "absolute";
		subObject.style.left = 0 + "px";
		subObject.style.top  = 0 + "px";

		subObject.style.visibility = "hidden";
	}

	initAjax();
 }


 function tooltip_show(tooltip, event)
 {
	if( browser.isIE )
	{
		x = window.event.clientX;
		y = window.event.clientY;
	}
	if( browser.isNS )
	{
		x = event.clientX;
		y = event.clientY;
	}

	document.getElementById(tooltip).style.left       = ( x + 10 ) + "px";
	document.getElementById(tooltip).style.top        = ( y + 20 ) + "px";
	document.getElementById(tooltip).style.width      = "200px";
	document.getElementById(tooltip).style.display    = 'block';
	document.getElementById(tooltip).style.visibility = 'visible';

	activeToolTip = tooltip;
 }


 function tooltip_hide(tooltip)
 {
	document.getElementById(tooltip).style.display    = 'none';
	document.getElementById(tooltip).style.visibility = 'hidden';

	activeToolTip = '';
 }


 function tooltip_L_show(tooltip, event)
 {
	if( browser.isIE )
	{
		x = window.event.clientX;
		y = window.event.clientY;
	}
	if( browser.isNS )
	{
		x = event.clientX;
		y = event.clientY;
	}

	document.getElementById(tooltip).style.left       = ( x - 220 ) + "px";
	document.getElementById(tooltip).style.top        = ( y +  20 ) + "px";
	document.getElementById(tooltip).style.width      = "200px";
	document.getElementById(tooltip).style.display    = 'block';
	document.getElementById(tooltip).style.visibility = 'visible';

	activeToolTip_L = tooltip;
 }


 function tooltip_L_hide(tooltip)
 {
	document.getElementById(tooltip).style.display    = 'none';
	document.getElementById(tooltip).style.visibility = 'hidden';

	activeToolTip_L = '';
 }


 function editPicTitle(id, target)
 {
	var left = (screen.availWidth - 220) / 2;
	var top  = (screen.availHeight - 120) / 2;
	params   = ('width=220,height=120,left='+ left +',top='+ top +',toolbar=0,location=0,directories=0,status=0,menubar=0,scrollbars=no,resizable=0');
	F1       = open('editPic.php?id='+id+'&target='+target, 'editPic', params); 
 }


 function editImmoPicTitle(id, target)
 {
	var left = (screen.availWidth - 220) / 2;
	var top  = (screen.availHeight - 120) / 2;
	params   = ('width=220,height=120,left='+ left +',top='+ top +',toolbar=0,location=0,directories=0,status=0,menubar=0,scrollbars=no,resizable=0');
	F1       = open('editImmoPic.php?id='+id+'&target='+target, 'editPic', params); 
 }


 function imgClick(imgNr, imgPath)
 {
	document.mainPic.src = imgPath;

	document.getElementById(activePicId).style.borderColor = '#FFFFFF';
	document.getElementById(imgNr).style.borderColor       = 'green';

	activePicId = imgNr;
 }


 function sicher(ziel)
 {
	var Checkdelete = confirm("Sicher?");
	if( Checkdelete != false )
	{
		self.location.href = ziel;
	}
 }


 // ======================================================================================================================================
 // ======================================================================================================================================


 function sure(modus)
 {
	var Checkdelete1 = confirm("Achtung! Alle Eintr�ge werden unwiderruflich gel�scht! Wollen Sie weitermachen?");
	if( Checkdelete1 != false )
	{
		var Checkdelete2 = confirm("Wollen Sie wirklich alles l�schen?");
		if( Checkdelete2 != false )
		{
			self.location.href = modus;
		}
	}
 }


 function copyUrl(FeldID)
 {
	document.getElementById(FeldID).focus();
	document.getElementById(FeldID).select();
	if( document.all )
	{
		Bereich = document.getElementById(FeldID).createTextRange();
		Bereich.execCommand("Copy");
	}
 }


 function openarchiv(archivid)
 {
	var wleft = (screen.availWidth - 700) / 2;
	var wtop  = (screen.availHeight - 700) / 2;
	params    = ('width=700,height=700,left='+ wleft +',top='+ wtop +',scrollbars=1');
	F1        = open('showarchiv.php?id='+archivid, 'name', params);
 }


 function listemails()
 {
	var wleft = (screen.availWidth - 500) / 2;
	var wtop  = (screen.availHeight - 520) / 2;
	params    = ('width=500,height=520,left='+ wleft +',top='+ wtop +',scrollbars=0');
	F1        = open('listemails.php', 'name', params);
 }


 function galPicPreview()
 {
	if( document.editor.galleriebild.value != "" && document.editor.galleriebild.value != 0 )
	{
		var pfad = document.editor.galleriebild.value;
		var wleft = (screen.availWidth - 600) / 2;
		var wtop  = (screen.availHeight - 600) / 2;
		params    = ('width=600,height=600,left='+ wleft +',top='+ wtop +',scrollbars=1');
		F1        = open('gallerie_vorschau.php?pfad='+pfad, 'name', params);
	} else
	  {
		alert("W�hlen Sie bitte zuerst ein Bild aus.");
	  }
 }

 function addPicToForm()
 {
	if( document.editor.galleriebild.value != "" && document.editor.galleriebild.value != 0 )
	{
		var ir = '[img]' + document.editor.galleriebild.value + '[/img]';
		addcode(ir);
	} else
	  {
		alert("W�hlen Sie bitte zuerst ein Bild aus.");
	  }
 }


 function hinweis(theText)
 {
	document.editor.formHinweis.value = theText;
 }


 function storeCaret(textEl)
 {
	if (textEl.createTextRange) textEl.caretPos = document.selection.createRange().duplicate();
 }


 function addcode(hrc)
 {
	if ( document.editor.text.createTextRange && document.editor.text.caretPos )
	{
		var caretPos  = document.editor.text.caretPos;
		caretPos.text = caretPos.text.charAt(caretPos.text.length - 1) == ' ' ? hrc + ' ' : hrc;
	} else
	  {
		document.editor.text.value += hrc;
	  }

	document.editor.text.focus();
 }


 function promtCodeSingle(start, end)
 {
	var input = document.editor.text;

	var showedText;
	switch(start)
	{
		case "[b]":
		showedText = "Geben Sie bitte den Text ein, der fett dargestellt werden soll.";
			break;
		case "[u]":
		showedText = "Geben Sie bitte den Text ein, der unterstrichen dargestellt werden soll.";
			break;
		case "[i]":
		showedText = "Geben Sie bitte den Text ein, der kursiv dargestellt werden soll.";
			break;
		case "[center]":
		showedText = "Geben Sie bitte den Text ein, der zentriert dargestellt werden soll.";
			break;
		case "[img]":
		showedText = "Geben Sie bitte die url des Bildes ein";
			break;
		default:
		showedText = "Geben Sie bitte den Text ein.";
	}

	if( document.selection )
	{
		var Selection = document.selection.createRange().text;

		if( Selection.length > 0)
		{
			var newText = prompt(showedText, Selection);
			if( newText != null && newText != "" )
			{
				document.selection.createRange().text = start + Selection + end;
			}
		} else
		  {
			input.focus();

			var range   = document.selection.createRange();
			var subText = range.text;

			var newText = prompt(showedText, subText);

			if( newText != null && newText != "" )
			{
				range.text  = start + newText + end;

				if( newText.length == 0 )
				{
					range.move('character', -end.length);
				} else
				  {
					range.moveStart('character', start.length + newText.length + end.length);
				  }
			}

			range.select();
		  }
	} else
	  {
		if( window.getSelection )
		{
			var selLength = input.textLength;
			var selStart  = input.selectionStart;
			var selEnd    = input.selectionEnd;

			if(selEnd == 1 || selEnd == 2 )
			{
				selEnd = selLength;
			}

			var string1   = (input.value).substring(0,selStart);
			var string2   = (input.value).substring(selStart, selEnd)
			var string3   = (input.value).substring(selEnd, selLength);

			var Selection = string2;

			var newText = prompt(showedText, Selection);
			if( newText != null && newText != "" )
			{
				input.value   = string1 + start + newText + end + string3;
			}
		} else
		  {
			input.value = input.value + start + end;
		  }
	  }
 }


 function promtCodeMulti(start, end)
 {
	var input = document.editor.text;

	var showedText;
	switch(start)
	{
		case "[link]":
		showedText1 = "Geben Sie bitte einen Linknamen ein (optional).";
		showedText2 = "Geben Sie bitte die url des Links ein.";
		preText     = "http://";
			break;
		case "[mail]":
		showedText1 = "Geben Sie bitte einen Linknamen ein (optional).";
		showedText2 = "Geben Sie bitte die Emailadresse ein.";
		preText     = "";
			break;
		default:
		showedText1 = "Geben Sie bitte den Namen ein.";
		showedText2 = "Geben Sie bitte die url ein.";
		preText     = "";
	}

	if( document.selection )
	{
		var Selection = document.selection.createRange().text;

		if( Selection.length > 0)
		{
			var newText = prompt(showedText1, Selection);
			var newLink = prompt(showedText2, preText);
			if( newLink != null && newLink != "" && newLink != "http://" )
			{
				if( newText == "" )
				{
					newText = newLink;
				}

				if( start == "[link]" )
				{
					document.selection.createRange().text = '[link=' + newLink + ']' + newText + end;
				} else
				  {
					document.selection.createRange().text = '[mail=' + newLink + ']' + newText + end;
				  }
			}
		} else
		  {
			input.focus();

			var range   = document.selection.createRange();
			var subText = range.text;

			var newText = prompt(showedText1, subText);
			var newLink = prompt(showedText2, preText);
			if( newLink != null && newLink != "" && newLink != "http://" )
			{
				if( newText == "" )
				{
					newText = newLink;
				}

				if( start == "[link]" )
				{
					range.text = '[link=' + newLink + ']' + newText + end;

					if( newText.length == 0 )
					{
						range.move('character', -end.length);
					} else
					  {
						range.moveStart('character', start.length + newText.length + end.length);
					  }
				} else
				  {

					range.text = '[mail=' + newLink + ']' + newText + end;

					if( newText.length == 0 )
					{
						range.move('character', -end.length);
					} else
					  {
						range.moveStart('character', start.length + newText.length + end.length);
					  }
				  }
			}

			range.select();
		  }
	} else
	  {
		if( window.getSelection )
		{
			var selLength = input.textLength;
			var selStart  = input.selectionStart;
			var selEnd    = input.selectionEnd;

			if(selEnd == 1 || selEnd == 2 )
			{
				selEnd = selLength;
			}

			var string1   = (input.value).substring(0,selStart);
			var string2   = (input.value).substring(selStart, selEnd)
			var string3   = (input.value).substring(selEnd, selLength);

			var Selection = string2;

			var newText = prompt(showedText1, Selection);
			var newLink = prompt(showedText2, preText);
			if( newLink != null && newLink != "" && newLink != "http://" )
			{
				if( newText == "" )
				{
					newText = newLink;
				}

				if( start == "[link]" )
				{
					input.value = string1 + '[link=' + newLink + ']' + newText + end + string3;
				} else
				  {
					input.value = string1 + '[mail=' + newLink + ']' + newText + end + string3;
				  }
			}
		} else
		  {
			if( start == "[link]" )
			{
				input.value = input.value + '[link=]' + end;
			} else
			  {
				input.value = input.value + '[mail=]' + end;
			  }
		  }
	  }
 }


 function promtCodeFont(start, end)
 {
	var input = document.editor.text;

	var showedText;
	switch(start)
	{
		case "[size]":
		showedText1 = "Geben Sie den Text ein.";
		showedText2 = "Geben Sie bitte die Schriftgr��e ein (0-7).";
		preText     = "";
			break;
		case "[color]":
		showedText1 = "Geben Sie den Text ein.";
		showedText2 = "Geben Sie bitte die Schriftfarbe ein (z.B. red, blue, green, yellow).";
		preText     = "";
			break;
		default:
		showedText1 = "Geben Sie den Text ein.";
		showedText2 = "Geben Sie bitte die Schriftgr��e ein (0-7).";
		preText     = "";
	}

	if( document.selection )
	{
		var Selection = document.selection.createRange().text;

		if( Selection.length > 0)
		{
			var newText = prompt(showedText1, Selection);
			var newLink = prompt(showedText2, preText);
			if( newLink != null && newLink != "" && newLink != "http://" )
			{
				if( newText == "" )
				{
					newText = newLink;
				}

				if( start == "[size]" )
				{
					document.selection.createRange().text = '[size=' + newLink + ']' + newText + end;
				} else
				  {
					document.selection.createRange().text = '[color=' + newLink + ']' + newText + end;
				  }
			}
		} else
		  {
			input.focus();

			var range   = document.selection.createRange();
			var subText = range.text;

			var newText = prompt(showedText1, subText);
			var newLink = prompt(showedText2, preText);
			if( newLink != null && newLink != "" && newLink != "http://" )
			{
				if( newText == "" )
				{
					newText = newLink;
				}

				if( start == "[size]" )
				{
					range.text = '[size=' + newLink + ']' + newText + end;

					if( newText.length == 0 )
					{
						range.move('character', -end.length);
					} else
					  {
						range.moveStart('character', start.length + newText.length + end.length);
					  }
				} else
				  {

					range.text = '[color=' + newLink + ']' + newText + end;

					if( newText.length == 0 )
					{
						range.move('character', -end.length);
					} else
					  {
						range.moveStart('character', start.length + newText.length + end.length);
					  }
				  }
			}

			range.select();
		  }
	} else
	  {
		if( window.getSelection )
		{
			var selLength = input.textLength;
			var selStart  = input.selectionStart;
			var selEnd    = input.selectionEnd;

			if(selEnd == 1 || selEnd == 2 )
			{
				selEnd = selLength;
			}

			var string1   = (input.value).substring(0,selStart);
			var string2   = (input.value).substring(selStart, selEnd)
			var string3   = (input.value).substring(selEnd, selLength);

			var Selection = string2;

			var newText = prompt(showedText1, Selection);
			var newLink = prompt(showedText2, preText);
			if( newLink != null && newLink != "" && newLink != "http://" )
			{
				if( newText == "" )
				{
					newText = newLink;
				}

				if( start == "[size]" )
				{
					input.value = string1 + '[size=' + newLink + ']' + newText + end + string3;
				} else
				  {
					input.value = string1 + '[color=' + newLink + ']' + newText + end + string3;
				  }
			}
		} else
		  {
			if( start == "[link]" )
			{
				input.value = input.value + '[size=]' + end;
			} else
			  {
				input.value = input.value + '[color=]' + end;
			  }
		  }
	  }
 }


 function setCheckboxes(do_check)
 {
	var elts      = document.pn.elements['selected_pns[]'];
	var elts_cnt  = (typeof(elts.length) != 'undefined') ? elts.length : 0;
	if( elts_cnt )
	{ 
		for( var i = 0; i < elts_cnt; i++ )
		{   
			elts[i].checked = do_check;    
		}
	} else
	  {
		elts.checked = do_check;
	  }
 }

