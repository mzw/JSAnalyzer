(function(window){
/*!*/
var racOpts={version:"1.5",base:"http://sportsauthority.resultspage.com",onsubmit:undefined};
/*!*/
if(document.location.protocol=="https:"){racOpts.base=racOpts.base.replace(/^https?:/i,"https:")}if(document.location.href.match(/\.local|resultsdemo/)){racOpts.base="http://"+document.domain}racOpts.onsubmit=function(param){try{var racType="";var cType="";if(param.url.match(/rt=racsug/)){racType="&ractype=suggestion";cType="racsug"}else{racType="&ractype=product";ctype="racclick"}var urlToTrack="/search?w="+param.query+"&ts=rac"+racType;
_gaq.push(["_trackPageview",urlToTrack])}catch(err){}};Function.prototype.slibind=function(obj){var method=this,temp=function(){return method.apply(obj,arguments)};return temp};var sliAutocomplete={opts:{version:"",path:"http://assets.resultspage.com/js/rac/sli-rac.stub",ext:"js",https:true},init:function(opts){for(var i in opts){this.opts[i]=opts[i]}this.load()},load:function(){var obj=this;if(obj.oScript){obj.stubInit()}else{obj.oScript=document.createElement("script");obj.oScript.type="text/javascript";var path=obj.opts.path+".";
if(obj.opts.version!=""){path+=obj.opts.version+"."}path+=obj.opts.ext;if(obj.opts.https&&document.location.protocol=="https:"){path=path.replace(/^https?:/i,"https:")}obj.oScript.src=path;document.body.appendChild(obj.oScript)}},extend:function(obj){if(obj.extend){obj.extend(this)}else{for(var i in obj){this[i]=obj[i]}}}};window.sliAutocomplete=sliAutocomplete;if(jQuery.ready){jQuery(document).ready(function(){window.sliAutocomplete.init(racOpts)})}else{window.sliAutocomplete.init(racOpts)}})(window);jQuery(window).load(function(){var hostname=window.location.hostname;
var protocol=window.location.protocol;if(hostname!="shop.sportsauthority.com"&&protocol=="https:"){jQuery("#headerSearchForm #search-button").click(function(event){event.preventDefault();var search=encodeURIComponent(jQuery("#sli_search_1").val());document.activeElement.blur();window.location="http://shop.sportsauthority.com/search?w="+search;return false});jQuery("#headerSearchForm #sli_search_1").keypress(function(event){var keycode=(event.keyCode?event.keyCode:event.which);if(keycode=="13"){var search=encodeURIComponent(jQuery(this).val());
document.activeElement.blur();window.location="http://shop.sportsauthority.com/search?w="+search;return false}})}var iefix='<!--[if gt IE 5]><style type="text/css">#header #utility #sli_search_1 { margin-top: 7px; }</style><![endif]-->';jQuery("head").append(iefix)});
