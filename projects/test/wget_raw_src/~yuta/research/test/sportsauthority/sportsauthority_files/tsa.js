(function () {
	"use strict";
	/**
	 * Wrapper function so we don't have to keep typing "use strict";
	 **/

	var hack_hover = function hack_hover() {
		/**
		 * Navigation hack for :hover support
		 */
		var nav_els = $$("#main-nav>li");
		nav_els.invoke("observe", "mouseover", function () {
			this.addClassName("over");
		});
		nav_els.invoke("observe", "mouseout", function () {
			this.removeClassName("over");
		});
	},
		hide_power_reviews = function hide_power_reviews() {
			/**
			 * Some Power Reviews edge case
			 */
			$$('.prStars').each(function (el, i) {
				var PRODUCTS_PER_PAGE = PRODUCTS_PER_PAGE || 9;
				if (i < PRODUCTS_PER_PAGE - 1) {
					if (el.style.backgroundPosition === '0px 0px') {
						el.parentNode.parentNode.style.display = 'none';
					}
				}
			});
		},
		handle_common_fields = function handle_common_fields() {
			var email_focus = function email_focus(e) {
				if(email_field.value=='Enter Email Address'){
				this.setAttribute("value", "");
				this.addClassName("active");
				}
			},
				email_blur = function email_blur(e) {
					if(email_field.value==''){
					this.setAttribute("value", "Enter Email Address");
					this.removeClassName("active");
					}
				},
				phone_focus = function phone_focus(e) {
					if(phone_field.value=='Enter Your Mobile #'){
					this.setAttribute("value", "");
					this.addClassName("active");
					}
				},
				phone_blur = function phone_blur(e) {
					if(phone_field.value==''){
					this.setAttribute("value", "Enter Your Mobile #");
					this.removeClassName("active");
					}
				},
				check_email = function check_email(e) {
					var emailRegEx = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
					if (emailRegEx.test(this.emailAddress.value)) {
						createEPixelParams();
					} else {
						alert('Please enter a valid e-mail address.');
						e.stop();
					}
				},
				check_phone = function check_phone(e) {
					var phoneRegEx = /^([\(]{1}[0-9]{3}[\)]{1}[\.| |\-]{0,1}|^[0-9]{3}[\.|\-| ]?)?[0-9]{3}(\.|\-| )?[0-9]{4}$/;
					if (phoneRegEx.test(this.phoneNumber.value)) {
						createEPixelParams();
					} else {
						alert('Please enter a valid mobile number.');
						e.stop();
					}
				},
				email_field = $("emailAddress"),
				phone_field = $("phoneNumber");

				if (email_field && "observe" in email_field) {
					email_field.observe("focus", email_focus);
					email_field.observe("blur", email_blur);
					if ("form" in email_field && "observe" in email_field.form) {
						$(email_field.form).observe("submit", check_email);
					}
				}
				if (phone_field && "observe" in phone_field) {
					phone_field.observe("focus", phone_focus);
					phone_field.observe("blur", phone_blur);
					if ("form" in phone_field && "observe" in phone_field.form) {
						$(phone_field.form).observe("submit", check_phone);
					}
				}
			};

	document.observe("dom:loaded", hack_hover);
	document.observe("dom:loaded", hide_power_reviews);
	document.observe("dom:loaded", handle_common_fields);
}());
if($("facebookLikeButton")!=null){
	$$("#main-nav > li").each(function(i){
		i.observe('mouseover',function(){
			$("facebookLikeButton").setStyle({ visibility: 'hidden' });
		});
		i.observe('mouseout',function(){
			$("facebookLikeButton").setStyle({ visibility: 'visible' });
		});
	});
} 	
if(document.getElementsByClassName("payPalExpressAuthFail").length > 0){
	document.getElementById("cartTopButtonContainer").style.height = '80px';
	document.getElementsByClassName("payPalExpressAuthFail")[0].onclick = function(event){
		event.preventDefault();
	};
}
document.observe('dom:loaded', function() {
	if($("paymentMethod")!=null){
		if($$("#paymentMethod img").length == 0){
			$$("#paymentMethod div")[0].setStyle({width:"auto"});
			$$("#paymentMethod .edit-link")[0].setStyle({width:"auto",
			marginLeft:"5px"});
		}
	}
});
