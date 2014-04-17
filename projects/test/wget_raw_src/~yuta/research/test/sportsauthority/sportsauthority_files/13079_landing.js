// Copyright Channel Intelligence, Inc. 2002-2008
var ci_vid= 13079;
var ci_cookieDomain=".sportsauthority.com";
var ci_refDomain=".sportsauthority.com";
var ci_imgs=[];

function ci_FP(ci_pix_url,protocol){var ci_pic=document.createElement('img');ci_pic.src=(protocol!==undefined ? protocol + '://' : (window.location.protocol.toLowerCase() == 'http:' ? 'http://' : 'https://'))+ci_pix_url;ci_imgs[ci_imgs.length]=ci_pic;}

function ci_RQV(name,dValue){
    var qArg=new RegExp('[\\?&]'+name+'=?([^&#]*)','i').exec(window.document.location);
    if(qArg===null){return dValue===undefined?null:dValue;}else if(qArg.length<2){return '';}else{return qArg[1];}
}
function ci_CC(name,value,daysTillExpire){
	if (daysTillExpire){
		var exDate=new Date();
		exDate.setTime(exDate.getTime()+(daysTillExpire*24*60*60*1000));
		document.cookie=name+'='+value+'; expires='+exDate.toGMTString()+'; domain='+ci_cookieDomain+'; path=/';
	}
}
function CI_GetValue(ci_vName,ci_dValue) {
	if (typeof(window[ci_vName])!=="undefined"){return window[ci_vName];}else{return ci_dValue===undefined?null:ci_dValue;}
}
function CI_ReadCookie(ci_cookieName) {
	var ci_cookieParts = document.cookie.split(';');
	ci_cookieName += '=';
	for (var ci_cookiePartIndex=0;ci_cookiePartIndex<ci_cookieParts.length;ci_cookiePartIndex++)
	{
		var ci_cookiePart=ci_cookieParts[ci_cookiePartIndex];
		while (ci_cookiePart.charAt(0)===' '){ci_cookiePart=ci_cookiePart.substring(1,ci_cookiePart.length);}
		if (ci_cookiePart.indexOf(ci_cookieName)===0){return ci_cookiePart.substring(ci_cookieName.length,ci_cookiePart.length);}
	}
	return null;
}
function ci_UID(value){
	var today=new Date();
	var UID=ci_vid+"-"+value+"-"+Math.floor(Math.random()*9999999999)+today.getFullYear().toString()+today.getMonth().toString()+ today.getDay().toString()+today.getHours().toString()+today.getMinutes().toString()+today.getSeconds().toString()+today.getMilliseconds().toString();
	return UID;
}
function ci_PIX(loc,eid,tid,src,sku,tag){
	var url='';
	//if (loc===1){url='origin.channelintelligence.com/log.asp?';}
	if (loc===2){url='cts-log.channelintelligence.com?';}
	url+='vid='+ci_vid+'&eid='+eid+'&tid='+tid;
	if(src!==null){url+='&src='+src;}
	if(sku!==null){url+='&sku='+sku;}
	if(tag!==null){url+='&tag='+tag;}
	url += "&ref="+escape(document.referrer);
	return ci_FP(url, 'http');
}
try {
	var ci_cpncode=ci_RQV('cpncode');
	var ci_srccode=ci_RQV('srccode');
	var ci_src=ci_RQV('ci_src');
	var ci_sku=ci_RQV('ci_sku');
	var ci_tag=ci_RQV('ci_tag');
	var ci_tid=ci_UID(ci_sku);
	var ci_pagetype=CI_GetValue('CI_PageType',null);
	var ci_itemid=CI_GetValue('CI_ItemID',null);
	var ci_catid=CI_GetValue('CI_CatID',null);
	var ci_catname=CI_GetValue('CI_CatName',null);
	
	
	//LinkShare tracking start
	var ci_lscookie = CI_ReadCookie('LinkShare_Referral');
	var ci_cilscookie = CI_ReadCookie('ci_cilscookie');
		
  	if(ci_lscookie!==null&&ci_cilscookie===null){                                                   
        ci_CC('ci_cilscookie',ci_lscookie,30);
        ci_tag=14110925;
    }
   	else if((ci_lscookie!==null&&ci_cilscookie!==null)&&(ci_lscookie!==ci_cilscookie)){
	    ci_CC('ci_cilscookie',ci_lscookie,30);
	    ci_tag=14110925;
    }
	//LinkShare tracking end
	
	if(ci_tag===null&&ci_src===null&&ci_cpncode===null){
		var ci_cid=ci_RQV('cid');
		if(ci_cid!==null&&ci_cid!==''){
			ci_tag=ci_cid;
		}else if (ci_cid!==null&&ci_cid===''){
			ci_tag=30520923;
		}	
	}
	 
	var ci_origkw=ci_RQV('origkw');
	if(ci_origkw!==null){ci_FP('truetag.channelintelligence.com?eid=32&v='+ci_vid+'&search='+ci_origkw,'http');}
	
	//Google Code for Remarketing Tag
	try{
		if(ci_catid!=null&&ci_catname!=null){
			var google_tag_params = {
			ecomm_prodid:ci_itemid,
			ecomm_pagetype:'category',
			ecomm_category:ci_catname,
			ecomm_totalvalue:''
			};
		}else{
			var google_tag_params = {
			ecomm_prodid:ci_itemid,
			ecomm_pagetype:ci_pagetype,
			ecomm_totalvalue:''
			};
		}
		var google_conversion_id = 979920549;
		var google_custom_params = window.google_tag_params;
		var google_remarketing_only = true;
		document.write('<scr'+'ipt type="text/javascript" src="//www.googleadservices.com/pagead/conversion.js"><\/scr'+'ipt>');
	}catch(err){}
	
	//DataLogix
	try{
		(function(){
			var pixelUrl = "//h.nexac.com/e/a-1189/s-2132/c-301/g-876.xgi?pkey=ak930d7lx38dp";
			var query = window.location.search.substr(1);
			if (query.length > 0) {
			  var params = query.split("&");
			  for (var i=0; i<params.length; i++) {
				var pos = params[i].indexOf("=");
				var name = params[i].substring(0, pos);
				if (name == "gclid") {
				  var gclid = params[i].substring(pos + 1);
				  createPixel(pixelUrl,gclid);
				}
			  }
			}
			function createPixel(pixelUrl,gclid) {
				var img=document.createElement("img");
				img.src= pixelUrl + "&chpth=" + gclid;
				img.width='1';
				img.height='1';
				img.border='0';
				var na_tag = document.getElementById("na_tag");
				na_tag.appendChild(img);
			}
    	}());
	}catch(err){}
	
	//Floodlight
	try{
		ci_FP('ad.doubleclick.net/activity;src=4363898;type=Sport-;cat=V7j_3-;ord=1?');
	}catch(err){}

	if(ci_cpncode!==null){
		ci_CC('ci_cpncode',ci_cpncode,30);
		ci_CC('ci_src',ci_srccode,30);
		ci_CC('ci_tid',"",-1);
	} else if(ci_src!==null && ci_sku!==null) {
		ci_CC('ci_cpncode',"",-1);
		ci_CC('ci_tid',ci_tid,30);
		ci_CC('ci_src',ci_src,30);
		ci_PIX(2,23,ci_tid,ci_src,ci_sku,null);
	} else if(ci_tag!==null) {
		ci_CC('ci_cpncode',"",-1);
		ci_CC('ci_tid',ci_tid,30);
		ci_CC('ci_tag',ci_tag,30);
		ci_PIX(2,7,ci_tid,null,null,ci_tag);
	} else if(document.referrer.toLowerCase().indexOf(ci_refDomain)===-1&&document.referrer!==''){
	    //ci_PIX(2,13,null,null,null,null);
	}
}    
catch(err){}
