//**********************************************************************************************************
function openDialog(src, width, height, leftPos, topPos, scrolling, closeBtn, bkgrnd){
	if(closeBtn==undefined) closeBtn='';
	if(bkgrnd==undefined) bkgrnd='';
	$.post('../include/openInLineDialog.php', { mtd: 'openDialog', src: src, width: width, height: height, scrolling: scrolling, closeBtn: closeBtn, bkgrnd: bkgrnd},
	function(data){
		if(leftPos==''){
			leftPos = ($(window).width()-parseInt(width))/2;
		}
		if(topPos==''){
			topPos = ($(window).height()-parseInt(height))/2+$(window).scrollTop();
		}
		$('#inLineDialog').append(data);
		$('#inLineDialog').css('width', width+'px');
		$('#inLineDialog').css('height', height+'px');
		$('#inLineDialog').css('left', leftPos+'px');
		$('#inLineDialog').css('top', topPos+'px');
		var h = $('body').height(); //document.height;
		$('#GreyoutDiv').css('height', h);
		$('body').css('overflow', 'hidden');
		$('#GreyoutDiv').css('visibility', 'visible');
		$('#GreyoutDiv').show();
		$('#inLineDialog').css('visibility', 'visible');
		$('#inLineDialog').show();
	});
	return false;
}
function getDocWidth() {
    var D = document;
    return Math.max(
        Math.max(D.body.scrollWidth, D.documentElement.scrollWidth),
        Math.max(D.body.offsetWidth, D.documentElement.offsetWidth),
        Math.max(D.body.clientWidth, D.documentElement.clientWidth)
    );
}
function getDocHeight() {
    var D = document;
    return Math.max(
        Math.max(D.body.scrollHeight, D.documentElement.scrollHeight),
        Math.max(D.body.offsetHeight, D.documentElement.offsetHeight),
        Math.max(D.body.clientHeight, D.documentElement.clientHeight)
    );
}
//**********************************************************************************************************
function closeDialog(){
	$('#inLineDialog').hide();
	$('#inLineDialog').css('visibility', 'hidden');
	$('#GreyoutDiv').css('visibility', 'hidden');
	$('#GreyoutDiv').hide();
	$('#inLineDialog').empty();
	$('body').css('overflow', 'auto');
}
//**********************************************************************************************************
function showResponse(msg){
	$('.response').remove();	
	$('#msg').append('<div class="response">'+msg+'</div>');
	$('.response').fadeIn('fast');
	var t=setTimeout(function(){ $('.response').fadeOut('slow'); }, 7000);
	return false;
}
//**********************************************************************************************************
function addLoader(objID){
	remLoader(objID);
	$(objID).append('<img src="../graphics/ajax-loader.gif" width="16" height="16" class="loader" />');
	return false;
}
//**********************************************************************************************************
function fadeOutLoader(objID){
	$(objID+' img.loader').fadeOut(700,function(){$(this).remove()});
	return false;
}
//**********************************************************************************************************
function remLoader(objID){
	$(objID+' img.loader').remove();
	return false;
}
//**********************************************************************************************************
function checkNum(){
	var carCode = event.keyCode;
	if ((carCode < 46) || (carCode > 57) || (carCode == 47)){
		event.cancelBubble = true
		event.returnValue = false;
	}
}
//**********************************************************************************************************
function editSettings(prefix, name, label){
	var names = name.split(':');
	var ename = prefix+name;
	if(names.length>1){
		ename = prefix+names[0];
	}
	if($('#'+ename+'Btn').attr('innerHTML')=="Edit"){
		$('#'+ename+'Div').slideDown(200);
		$('#'+ename+'Btn').attr('innerHTML', 'Save');
		$('#'+ename+'CopyFrom').show();
	}else{
		$('#'+ename+'CopyFrom').hide();
		var update = false;
		var val = '';
		if(names.length>1){
			val = 'set:|:';
			for(var i=1; i < names.length; i++){
				val += $('#'+prefix+names[i]).val()+':|:';
				if($('#'+prefix+names[i]+'Changed').val()=="yes"){
					update = true;
				}
			}
		}else{
			val = $('#'+ename).val();
			if($('#'+ename+'Changed').val()=="yes"){
				update = true;
			}
		}
		$('#'+ename+'Div').slideUp(200);
		$('#'+ename+'Btn').attr('innerHTML', 'Edit');
		//$('#content').prepend(name+'|||'+val);
		if(update == true){
			$.post('include/eBaySettingsModule.php', { mtd: 'saveSetting', prefix: prefix, name: name, valu: val  },
			function(data){ $('#content').prepend(data);
				showResponse('Saved '+label);
				$('#'+name+'Changed').val()="no";
			});
		}
	}
	return false;
}
//**********************************************************************************************************
function copyFrom(from, to){
	$('#'+to).val($('#'+from).val());
	$('#'+to+'Changed').val('yes');
}
//**********************************************************************************************************
function setSelectedCat(hiddenBoxID, selectedCatID){ //alert('set');
	$('#'+hiddenBoxID).val(selectedCatID);
	switch(hiddenBoxID){
		case 'eBayCatsHTB':
		case 'StoreCategoryID':
		case 'StoreSecondCategoryID':
			validateEbayGenInfo('yes');
		break;
		case 'iVenditaCatID':
		case 'eComCatID1':
		case 'eComCatID2':
			validateEcomTab('yes');
		break;
	}
}
//**********************************************************************************************************
function setCatContainerHeight(ContainerID, toHeight){
	$('#'+ContainerID).css('height', toHeight);
}
//**********************************************************************************************************
function cmToInch(cm){
	return Math.round((cm*0.3937008)*10)/10;
}
//**********************************************************************************************************
function inchToCm(inch){
	return Math.round((inch*2.54)*10)/10;
}
//**********************************************************************************************************
function kgToLbs(kg){
	return Math.round((kg/2.2)*100)/100;
}
//**********************************************************************************************************
function lbsToKg(lbs){
	return Math.round((lbs*2.2)*100)/100;
}
//**********************************************************************************************************
function scaleImgDims(w, h, maxw, maxh){
	var ow = 0;
	var oh = 0;
	var ratio = 0;
	while(w > maxw || h > maxh){
		ow = w;
		oh = h;
		if (h > maxh){
			h = maxh;
			ratio = oh / h;
			w = ow / ratio;
		}else if (w > maxw){
			w = maxw;
			ratio = ow / w;
			h = oh / ratio;
		}
	}
	return w+':'+h;
}
//**********************************************************************************************************
function CharCounter(boxID, maxLength, lblID){
	$('#'+lblID).attr('innerHTML', ' '+(maxLength-$('#'+boxID).val().length)+' characters remaining');
}
//***************************************************************************
function addCat(tbl, newCatTxt, pid, uid){
	$.post('../include/cats.php', { mtd: 'addcat', type: tbl, newcat: newCatTxt, pid: pid, uid: uid },
	function(data){
		var dat = data.split(':');
		switch(dat[0]){
			case 'Added':
				showResponse('Added Category: '+newCatTxt);
			break;
			case 'NoNewCat':
				showResponse('No Category was entered.');
			break;
			case 'AlreadyExists':
				showResponse('Category: '+newCatTxt+' Already Exists');
			break;
			default:
				showResponse('There was an error while saving Category: '+newCatTxt+' : '+data);
			break;
		}
		$('#newCatIDbox').val(dat[1]);
	});
	return false;
}
//***************************************************************************
function renameCat(tbl, newCatTxt, cid, uid){
	$.post('../include/cats.php', { mtd: 'updcat', type: tbl, newtitle: newCatTxt, cid: cid, uid: uid },
	function(data){
		switch(data){
			case 'Updated':
				showResponse('Changed Selected Category to: '+newCatTxt);
			break;
			case 'BlankTitle':
				showResponse('No Category was entered.');
			break;
			case 'AlreadyExists':
				showResponse('Category: '+newCatTxt+' Already Exists');
			break;
			default:
				showResponse('There was an error while Changing Category to: '+newCatTxt+' : '+data);
			break;
		}
		return data;
	});
	return false;
}
//***************************************************************************
function remCat(tbl, cid, CatTxt, uid){
	$.post('../include/cats.php', { mtd: 'remcat', type: tbl, cid: cid, uid: uid },
	function(data){
		switch(data){
			case 'Removed':
				showResponse('Removed Category: '+CatTxt);
			break;
			case 'HasSubCats':
				showResponse('Category: '+CatTxt+' has Sub-Categories. Remove these and try again.');
			break;
			case 'NoCatSelected':
				showResponse('No Category is selected.');
			break;
			default:
				showResponse('There was an error Removing Category: '+CatTxt);
			break;
		}
		return data;
	});
	return false;
}
//***************************************************************************
function createRootCat(catTree){
	//Move the selection to the top level
	for(var i = 0; i < 8; i++){
		$.tree_reference(catTree).get_left();
	}
	$.tree_reference(catTree).create('', '', 'after');
}
//**********************************************************************************************************
function clickCheckBox(boxID){
	if($('#'+boxID).attr('checked') == false){
		$('#'+boxID).attr('checked', 'checked');
	}else{
		$('#'+boxID).attr('checked', '');
	}
	return false;
}
//**********************************************************************************************************
function checkAll(){
	var lidAry = $('#lids').val().split(':');
	if($('#checkallclicktop').attr('innerHTML') == 'Select All'){
		$('#checkallclicktop').attr('innerHTML', 'De-Select All');
		$('#checkallclickbtm').attr('innerHTML', 'De-Select All');
		for(var i = 0; i < lidAry.length; i++){
			$('#check'+lidAry[i]).attr('checked', 'checked');
		}
	}else{
		$('#checkallclicktop').attr('innerHTML', 'Select All');
		$('#checkallclickbtm').attr('innerHTML', 'Select All');
		for(var i = 0; i < lidAry.length; i++){
			$('#check'+lidAry[i]).attr('checked', '');
		}
	}
	return false;
}
//**********************************************************************************************************
var isNav4 = ((navigator.appName == "Netscape") && (parseInt(navigator.appVersion) >= 4))
// resizes window to occupy all available screen real estate
function maximize() {
	// fill missing IE properties
    if (!window.outerWidth) {
        window.outerWidth = document.body.clientWidth
        window.outerHeight = document.body.clientHeight + 30
    }
    // fill missing IE4 properties
    if (!screen.availWidth) {
        screen.availWidth = 640
        screen.availHeight = 480
    }
    window.moveTo(0,0)
    window.resizeTo(screen.availWidth, screen.availHeight)
}
