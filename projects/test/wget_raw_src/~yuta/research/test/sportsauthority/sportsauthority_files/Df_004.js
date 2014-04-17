if(Df){
}else{
	var Df = {}
}
Df.DropNav = function(el){
	//BEGIN constructor
	
	var ele = this.ele = $(el)
	
	var pars = this.pars = {
		animate: false,
		pause:100
		
	}
	
	var status = false;
	var displayStatus = false;
	var list, animation;
	
	this.version = function(){
		return 1.2;
	}
	
	this.requires = function(){
		return [
			'../js/Df.js',
			'../js/prototype1_5.js',
			'../js/prototype1_5_extend.js',
			'../js/Df.Animate.js'
			];
	}

	
	this.set = function(para){
		if(para){
			pars = Object.extend(pars,para)
		}

		list = ele.getElementsByTagName("UL")[0];
		
		if(list){
		
			if(pars.animate){
				animation = new Df.Animate(list);
				if(pars.animate){
					animation.pars = Object.extend(animation.pars,pars.animate);
				}
			}
			
			Event.observe(list,'mouseout',hide,false);
			
			Event.observe(ele, 'mouseover', display ,false)
			
			Event.observe(ele, 'mouseout', hide ,false)
		}
		
	}
	
	var display = this.display = function(event){
		status = true;
		setTimeout(waintToDisplay,pars.pause);
	}
	
	var hide = this.hide = function(event){
		status = false;
		setTimeout(waintToHide,pars.pause);
	}
	
	var waintToDisplay = function(event){
		if(status && !displayStatus){
		
			displayStatus = true
			
			list.style.display = "block";
			
			ele.addClassName("active");
			
			if(animation){
				if(animation.getHistoryCount() == 0){
					animation.run();	
				}else{
					animation.last();
				}
				
			}
			
			if((Df.browser()).ie6){
				showIframe();
			}
		}
	}
	
	var waintToHide = function(event){
		if(!status){
			
			displayStatus = false;
			ele.removeClassName("active");
			if(animation){
				if(animation.getHistoryCount() > 0){
					animation.first({onComplete: function(){
							list.style.display = "none";
						}
					});
				}
			}else{
				list.style.display = "none";
			}
			
			if((Df.browser()).ie6){
				hideIframe();
			}
		}
	}
	
	var showIframe = function(){
		var oDiv = ele.down('.oDiv',0);
		//var oDiv = ele.getElementsByTagName("div")[0];
		if(oDiv){
			oDiv.style.display = "block";
		}else{
			var html = '<iframe class="oDiv" style="filter:progid:DXImageTransform.Microsoft.Alpha(style=0,opacity=0);top:25px;position:absolute;" scrolling="no" src="javascript:false;" frameborder="0" height="300px" width="315px"></iframe>';
			new Insertion.Top(ele, html);
		}
	}
	
	var hideIframe = function(){
		var oDiv = ele.down('.oDiv',0);
		if(oDiv){
			oDiv.style.display = "none";
		}
	}
}
