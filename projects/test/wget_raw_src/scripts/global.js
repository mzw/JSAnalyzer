$(document).ready(function() {

	//  Disable all form autocompletes.
	disableAutoComplete();

	//  Set the modals.
	setModals();

	//  IE7 for placeholder.
	fixPlaceholder();
});

function setModals() {

	if( typeof( $.fancybox ) != 'undefined' ) {

		//  Standard modals.
		$('.modal').fancybox();
		$('.modal-link').fancybox();

		//  Iframe modals.
		$('.modal-iframe').each(function(){

			//  Set the iframe width using the 'rel' attribute?
			if( $(this).attr( 'rel' ) != undefined ) {

				var rel = new String( $(this).attr( 'rel' ) );
				var regex = /^(\d+)x(\d+)$/i;

				if( rel.match( regex ) ) {
					$(this).fancybox({
						'width'      : Number( rel.replace( regex, '$1' ) ),
						'height'     : Number( rel.replace( regex, '$2' ) ),
						'type'       : 'iframe',
						'autoScale'  : false,
						'autoSize'   : false
					});
				}

			//  Just do an iframe.
			} else {
				$(this).fancybox({ 'type' : 'iframe' });
			}
		});
	}
}

//  DISABLE ALL FORM AUTOCOMPLETING
function disableAutoComplete() {

	if( typeof( disable_form_auto_complete ) != 'undefined' && !disable_form_auto_complete ) { return; }

	if( !( document.forms && document.forms.length > 0 ) ) {
		return;
	}

	var my_url = new String( document.location );

	//  Disable for admin login.
	if( my_url.match( /\/admin\/index\/login\/.*/ ) ) {
		return;
	}

	for( var i = 0; i < document.forms.length; i++ ) {
		if( !( document.forms[i] && document.forms[i].elements && document.forms[i].elements.length > 0 ) ) {
			continue;
		}
		for( var j = 0; j < document.forms[i].elements.length; j++ ) {
			if( !$(document.forms[i].elements[j]).hasClass( 'allow-autocomplete' ) ) {
				document.forms[i].elements[j].setAttribute( 'autocomplete', 'off' );
			}
		}
	}
}

function reloadIframeParent( hash ) {

	if( parent == null || parent == undefined ) {
		return;
	}

	//  remove the hash.
	var url = new String( parent.location );

	url = url.replace( /^(.*?)(#.*)?$/, '$1' );

	if( url.indexOf( "?" ) > 0 ) {
		url = url + "&u=" + Math.random();
	} else {
		url = url + "?u=" + Math.random();
	}

	url += "#" + hash;

	parent.$.fancybox.close();
	parent.location = url;
}

function loadCityStateFromZip( zip, destination_id, city, state, error_id, only_shippable_locations ) {

	//  Zip long enough?
	if( zip.length != 5 ) {
		$('#'+destination_id).html( '' );
		return;
	}

	var url = '/index/city-state-by-zip/?zip=' + encodeURIComponent( zip )
		+ "&city=" + encodeURIComponent( city )
		+ "&state=" + encodeURIComponent( state )
		+ "&only_shippable_locations=" + ( only_shippable_locations ? '1' : '0' )
		;

	$.get( url, function(data) {

		//  APO/FPO Box?
		if( data.mail_box == '1' ) {
			$('#box-zip-error').show();
			return;
		}

		//  Valid.
		if( data.success && data.html ) {
			$('#'+destination_id).html( data.html );

		//  Errors.
		} else if( data.success == '0' ) {
			$('#'+destination_id).html( '' );
			$('#'+error_id).css( 'display', 'block' );
		}

	}, 'json' );
}

function formatPhone( id ) {

	var p = new String( $('#'+id).val() );
	p = p.replace( /[^0-9]/g, '' );

	if( p.length == 10 ) {
		$('#'+id).val( p.replace( /^(\d\d\d)(\d\d\d)(\d\d\d\d)$/, '($1) $2-$3' ) );
	}
}

// placeholder polyfill
function addPlaceholder() {
	if($(this).val() == ''){
		$(this).val($(this).attr('placeholder')).addClass('placeholder');
	}
}

function removePlaceholder() {
	if($(this).val() == $(this).attr('placeholder')){
		$(this).val('').removeClass('placeholder');
	}
}

// Create a dummy element for feature detection
function fixPlaceholder() {
	if (!('placeholder' in $('<input>')[0])) {

		// Select the elements that have a placeholder attribute
		$('input[placeholder], textarea[placeholder]').blur(addPlaceholder).focus(removePlaceholder).each(addPlaceholder);

		// Remove the placeholder text before the form is submitted
		$('form').submit(function(){
			$(this).find('input[placeholder], textarea[placeholder]').each(removePlaceholder);
		});
	}
}

//  open popup window
var popupList = {};
function openWindow( winName, url, w, h, scroll ) {

	//  Scroll?
	if( scroll ) { scroll = '1'; } else { scroll = '0'; }

	var window_name = new String( winName );
	window_name = window_name.replace( /[^a-zA-Z0-9]/g, '' );

	//  Close other instances of this window.
	if( popupList[window_name] != null && typeof( popupList[window_name] ) == "object" && !popupList[window_name].closed ) {
		popupList[window_name].close();
		popupList[window_name] = null;
	}

	var specs = "height=" + h + ",width=" + w + ",channelmode=0,dependent=1,directories=1,fullscreen=0,location=1,menubar=1,resizable=0,scrollbars=" + scroll + ",status=1,toolbar=1";

	//  Setup the new window.
	popupList[window_name] = window.open( url, window_name, specs, false );

	//  Open the window.
	if( popupList[window_name].opener == null ) {
		popupList[window_name].opener = window;
	}

	if ( navigator.appName == 'Netscape' ) {
		popupList[window_name].focus;
	}
}

