var mailru = mailru || {};
mailru.loader = {
    modvers: {
        receiver: 1,
        proxy: 15,
        api: 104,
        api_dev: 2,
        xdm: 5
	},
    modreleases: {
		receiver: null,
		proxy: null,
		api: null,
        api_dev: null,
        xdm: null
    },
    _modulePath: 'http://my2.imgsmail.ru/mail/ru/images/js/connect/',
    _loaded: {},
    _readyCallbacks: {},
    require: function(module, onready, deferLoad){
        if(this._loaded[module]){
            onready();
        } else {
            if(!this._readyCallbacks[module]){
                if(typeof this._readyCallbacks[module] === 'undefined')
				    this._readyCallbacks[module] = [];
				this._readyCallbacks[module].push(onready);
				var modver = (this.modvers[module] && ('?'+ this.modvers[module])) || '';
                var modrelease = (this.modreleases[module] && ('/' + this.modreleases[module]) + '/') || '/';
                deferLoad = mailru.isIE && deferLoad? true : false;
				document.URL.match(/testmode=1/) && (modver = '');

				with(document.getElementsByTagName('head')[0].appendChild(document.createElement('script'))){
					type = 'text/javascript';
					src = this._modulePath+ module+ modrelease+ module+ '.js' +modver;
                    if(deferLoad){
                        defer = "defer";
                    }
				}
			} else {
				this._readyCallbacks[module].push(onready);
			}

		}
	},
	onready: function(module){
		if(this._readyCallbacks[module]){
			this._loaded[module] = true;
			var cbs = this._readyCallbacks[module];
            if(cbs.length)
                for(var i=0; i<cbs.length; i++){
                    try{
                        cbs[i]();
                    }catch(e){}
                }

		}
	}
};

(function(){
    'use strict';

    try {
        var url = document.location.search,
            branch = url.match(/__branch=([a-z0-9\-]*)/i),
            remote = url.match(/__remote=([a-z0-9]*)/i);

        if ((branch && branch.length > 1) && (remote && remote.length > 1)) {
            mailru.loader._modulePath = 'http://' + branch[1] + '.my.' + remote[1] + '.i.mail.ru/mail/ru/images/js/connect/';
        }
    } catch (e) {}
})();

mailru.isIE = (function () {
    var tmp = document.documentMode, e, isIE;
    try{document.documentMode = "";}
    catch(e){ };
    isIE = typeof document.documentMode == "number" ? !0 : eval("/*@cc_on!@*/!1");
    try{document.documentMode = tmp;}
    catch(e){ };
    return isIE;
})()
mailru.isOpera = !!window.opera;
mailru.isApp = false;

if (window.name.indexOf('app') !== -1 && window.name.indexOf('mailruapp') !== -1) {
    mailru.isApp = true;
}

mailru.intercomType = ( (window.postMessage && !mailru.isIE) || (mailru.isApp && mailru.isIE && window.postMessage))? 'event' : (((function(){var i,a,o,p,s="Shockwave",f="Flash",t=" 2.0",u=s+" "+f,v=s+f+".",rSW=RegExp("^"+u+" (\\d+)");if((o=navigator.plugins)&&(p=o[u]||o[u+t])&&(a=p.description.match(rSW)))return a[1];else if(!!(window.ActiveXObject))for(i=10;i>0;i--)try{if(!!(new ActiveXObject(v+v+i)))return i}catch(e){}return 0;})() < 10) ? 'hash' : 'flash');

mailru.init = function(onready, private_key, DOMFlashId){
	mailru.loader.require('api', function(){
		try{
			mailru.app.init(private_key);
		}catch(e){}
		var e;
		if(DOMFlashId && (e=document.getElementById(DOMFlashId))){
			setTimeout(onready, 1);
			mailru.events.listen('event', function(name, data){
				document.getElementById(DOMFlashId).mailruEvent(name, data);
			});
		}
	})
}
mailru.autoInit = (function(){
    if (!mailru.disableAutoInit) {
        var a = document.getElementsByTagName('a'), al = a.length;
        for(var i = 0; i < al; i++){
            if (typeof a[i] !== 'undefined' && a[i].className.indexOf('mrc__plugin') != -1) {
                mailru.loader.require('api', function(){
                    mailru.plugin.init();
                });
                break;
            }
        }    
    }
})();
