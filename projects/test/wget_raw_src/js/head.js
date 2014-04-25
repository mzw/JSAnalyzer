function validateForm ( form_obj )
	{
		var returnValue = true ;
		$("#"+form_obj+" input,select,textarea").each ( function ( )
							{
								if ( $(this).attr("sch_req") == "1" && returnValue )
								{
									if ( $(this).val() == "" )
									{
										alert ( $(this).attr("sch_msg")+" cannot be empty" ) ;
										$(this).focus();
										returnValue = false ;
									}
								}
							}
					
		 ) ;
		 return returnValue ;

	}
	
	function copy_coupon_and_go_to_site ( coupon_id )
	{
	       //var child = window.open ( "/load/"+coupon_id+"/" , "_blank" ) ;
	       var child = window.open ( "/coupon.php?cId="+coupon_id, "_blank" ) ;
	       //var child = window.open ( "http://www.coupones.net/load/"+coupon_id+"/" , "_blank" ) ;
		//child.blur();
		//window.focus();
	}
	
	function animate_area ( div_id , direction )
	{
		if ( direction == 0 )
			$("#"+div_id).animate ( { scrollLeft : "-=530" } , "slow" ) ;
		else
			$("#"+div_id).animate ( { scrollLeft : "+=530" } , "slow" ) ;
	}
	
	function show_tool_tip ( copon_id )
	{
		$("#coupon_Tool_tip_action_"+copon_id).show() ;
	}
	
	function hide_tool_tip ( copon_id )
	{
		$("#coupon_Tool_tip_action_"+copon_id).hide() ;
	}
	/*
	function reveal_all_coupons ( ) 
	{
		$('.coupon_code_text').each(function(i) { $(this).text($(this).attr('code')); });
	}
	*/
	
	
	function set_copy_command ( text_to_copy , control_id , coupon_id, website_name, coupon_code )
	{
		var revertcolor = $("#"+control_id).css('border-color');
		$("#"+control_id).parent().mouseout(function() {
			$(this).css('cursor', 'pointer');
			$("#"+control_id).parent().unbind('mouseover');
			$("#"+control_id).css('border-color', revertcolor);
			$("#"+control_id).parent().mouseover(function() {
				show_tool_tip ( coupon_id ) ;
				$("#"+control_id).css('border-color', '#000');
			});
			hide_tool_tip ( coupon_id ) ;
		});
		
		$("#"+control_id).parent().mouseover(function() {
			show_tool_tip ( coupon_id ) ;
			$("#"+control_id).css('border-color', '#000');
			
			if ( 0 && navigator.userAgent.indexOf("MSIE") > -1 ) 
			{
				$("#"+control_id).click ( function ( ) {
					window.clipboardData.setData("Text",text_to_copy);
					//_gaq.push(['_trackPageview', '/merchant-goal.php']);
					_gaq.push(['_trackEvent', 'CouponClick', website_name]);
					//setTimeout("copy_coupon_and_go_to_site ( " + coupon_id +")", 0) ;
					copy_coupon_and_go_to_site(coupon_id);
					//reveal_all_coupons();
				});
			}
			else
			{
				show_tool_tip ( coupon_id ) ;
				var clip = new ZeroClipboard.Client();

				ZeroClipboard.setMoviePath ( "js/zeroclipboard/ZeroClipboard.swf" ) ;
				
				clip.setText( text_to_copy ); 
				clip.setHandCursor( true );
			
				clip.addEventListener( 'mouseOver', function(client) {
					show_tool_tip ( coupon_id ) ;
					this.style.cursor = "pointer";
					$("#"+control_id).css('border-color', '#000');
				} );
			
				clip.addEventListener( 'mouseOut', function(client) { 
					hide_tool_tip ( coupon_id ) ;
				});
			
				clip.addEventListener( 'complete', function(client, text) {
					$("#"+control_id).html(text_to_copy);
					$("#"+control_id).css("background" , "#EEE");
					$("#"+control_id).css("line-height" , "29px");
					$("#"+control_id).css("padding" , "0 10px");
					
					$("#coupon_Tool_tip_action_"+coupon_id).css("width","60px").text("Copied ! ") ;
					//_gaq.push(['_trackPageview', '/merchant-goal.php']);
					_gaq.push(['_trackEvent', 'CouponClick', website_name]);
					//setTimeout("copy_coupon_and_go_to_site ( " + coupon_id +")", 0) ;
					copy_coupon_and_go_to_site(coupon_id);
					//reveal_all_coupons();
				} );
			
				clip.glue( control_id, $("#"+control_id).parent().attr("id") );
				show_tool_tip ( coupon_id ) ;	
			}
		});
	}
	
	function findPosX(obj, posDiv)
	{
	  var objDiv = document.getElementById(posDiv);
	  var curleft = 0;
	  if(obj.offsetParent)
	      while(1) 
	      {
	        curleft += obj.offsetLeft;
	        if(!obj.offsetParent)
	          break;
	        obj = obj.offsetParent;
	      }
	  else if(obj.x)
	      curleft += obj.x;
		
	  	objDiv.style.left = curleft+'px'; 
	  	return true;
	}

	function findPosY(obj, posDiv)
	{
	  var objDiv = document.getElementById(posDiv);
	  var curtop = 0;
	  if(obj.offsetParent)
	      while(1)
	      {
	        curtop += obj.offsetTop;
	        if(!obj.offsetParent)
	          break;
	        obj = obj.offsetParent;
	      }
	  else if(obj.y)
	      curtop += obj.y;
	  
	  objDiv.style.top = (curtop+36)+'px';
	  return true;
	}
	
	function setCookieFunction(){
		window.location.href = "setsession.php?red=<?php echo $_SERVER['REQUEST_URI']; ?>";
		//window.location.href = "setsession.php";
	}

	function getPageScroll(){

		var yScroll;

		if (self.pageYOffset) {
			yScroll = self.pageYOffset;
		} else if (document.documentElement && document.documentElement.scrollTop){	 // Explorer 6 Strict
			yScroll = document.documentElement.scrollTop;
		} else if (document.body) {// all other Explorers
			yScroll = document.body.scrollTop;
		}

		arrayPageScroll = new Array('',yScroll) 
		return arrayPageScroll;
	}

	function getPageSize(){
		
		var xScroll, yScroll;
		
		if (window.innerHeight && window.scrollMaxY) {	
			xScroll = document.body.scrollWidth;
			yScroll = window.innerHeight + window.scrollMaxY;
		} else if (document.body.scrollHeight > document.body.offsetHeight){ // all but Explorer Mac
			xScroll = document.body.scrollWidth;
			yScroll = document.body.scrollHeight;
		} else { // Explorer Mac...would also work in Explorer 6 Strict, Mozilla and Safari
			xScroll = document.body.offsetWidth;
			yScroll = document.body.offsetHeight;
		}
		
		var windowWidth, windowHeight;
		if (self.innerHeight) {	// all except Explorer
			windowWidth = self.innerWidth;
			windowHeight = self.innerHeight;
		} else if (document.documentElement && document.documentElement.clientHeight) { // Explorer 6 Strict Mode
			windowWidth = document.documentElement.clientWidth;
			windowHeight = document.documentElement.clientHeight;
		} else if (document.body) { // other Explorers
			windowWidth = document.body.clientWidth;
			windowHeight = document.body.clientHeight;
		}	
		
		// for small pages with total height less then height of the viewport
		if(yScroll < windowHeight){
			pageHeight = windowHeight;
		} else { 
			pageHeight = yScroll;
		}

		// for small pages with total width less then width of the viewport
		if(xScroll < windowWidth){	
			pageWidth = windowWidth;
		} else {
			pageWidth = xScroll;
		}
		arrayPageSize = new Array(pageWidth,pageHeight,windowWidth,windowHeight);
		return arrayPageSize;
	}

	function showLightbox(){
		document.body.scrollTop = 0;
		var lightboxWidth='450';
		var objOverlay = document.getElementById('shadowoverlay');
		var objLightbox = document.getElementById('left_right1');
		
		var arrayPageSize = getPageSize();
		var arrayPageScroll = getPageScroll();
		objOverlay.style.height = (arrayPageSize[1] + 'px');
		objOverlay.style.display = 'block';
		objOverlay.style.zIndex = '9990';
		objLightbox.style.display = 'block';
		var lightboxHeight = objLightbox.offsetHeight;
		var lightboxTop = arrayPageScroll[1] + ((arrayPageSize[3] - 35 - lightboxHeight) / 2);
		var lightboxLeft = ((arrayPageSize[0] - lightboxWidth) / 2);
		objLightbox.style.top = (lightboxTop < 0) ? "0px" : lightboxTop + "px";
		objLightbox.style.left = (lightboxLeft < 0) ? "0px" : lightboxLeft + "px";
		objLightbox.style.zIndex = '9999';
		arrayPageSize = getPageSize();
		objOverlay.style.height = (arrayPageSize[1] + 'px');
	}
	
	function hideLightbox(){
		objOverlay = document.getElementById('shadowoverlay');
		objLightbox = document.getElementById('left_right1');
		objOverlay.style.display = 'none';
		objLightbox.style.display = 'none';
		setCookieFunction();
		return;
	}
	
	function $(selector) {
        return document.querySelectorAll(selector)[0];
	}

	function play(example, fn) {
	    $('#example-' + example + ' .play').addEventListener('click', function(e){
	      e.preventDefault();
	      fn();
	    }, false);
	}
	
	function getAlpha(obj){	
		if(obj.id=='int'){obj.id = '0-9';}
		//var leftpos = (obj.offsetLeft)-312;
		var par =document.getElementById('dot');
		var childs=par.getElementsByTagName('a');
		var boxModel = document.getElementById('resultBox');
		boxModel.innerHTML = '';
		for(i=0;i<childs.length;i++)
		{	
			if (childs[i].id==obj.id){
				//par.style.backgroundPosition=leftpos+'px';
				childs[i].className = "selected";
				
				$.ajax({
					type: 'GET',
					url: 'getStoreByAlpha.php?alpha='+obj.id,
					//data: myArray, 
					beforeSend: function() {
						boxModel.innerHTML = '<div align="center" style="margin-top:60px">Loading...</div>';
					},
					success: function(data) {
						boxModel.innerHTML = data;
					},
				});
				
			} else {
				childs[i].className = "";
			}
		}
	}
	
	function getSelectionTab(obj){
		var par =document.getElementById('tab');
		var childs=par.getElementsByTagName('a');
		for(i=0;i<childs.length;i++){
			if (childs[i].id==obj.id){
				childs[i].className = "open";
			} else {
				childs[i].className = "selected";
			}
		}
	}
	
     function eTabSelected(Obj){
	  var parentElement = document.getElementById('countrytabs');	  
	  var childs=parentElement.getElementsByTagName('a');
	  for(i=0; i<childs.length; i++){
	       if (childs[i].id == Obj.id){
		    childs[i].className = 'selected';
		    document.getElementById(childs[i].id+'_div').style.display = '';
	       } else {
		    childs[i].className = '';
		    document.getElementById(childs[i].id+'_div').style.display = 'none';
	       }
	  }
     }
