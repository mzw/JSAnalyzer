Event.observe(window, 'load', function() { 
    var CheckOutLink = $$('a.details');
    if (CheckOutLink[0] && CheckOutLink[0].href.indexOf("/coreg/index.jsp?step=logout") > -1 ) {
	    CheckOutLink[0].replace("<a href='/coreg/index.jsp?step=logout&np=logoutThanks' class='details'>click here</a>");
	}
});
