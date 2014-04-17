function showNormal(input){
	$('#'+input+"~span.error_tip").remove();
	$('#'+input).removeClass('error_input');
}

function hideMessage(input){
	$('#'+input+"~span.pub_msg").remove();
}
function showMessage(input,tip){
	var html = '<span class="pub_msg">'+tip+'</span>';
	$(html).appendTo($('#'+input)[0].parentNode);
}

function showError(input,error){
	showNormal(input);
	var html = ' <span class="error_tip">'+error+'</span>';
	$('#'+input).addClass('error_input');
	$(html).appendTo($('#'+input)[0].parentNode);
}
function checkInput(input,focus){
	var reg = new RegExp($('#'+input).attr('exp'));
	if (reg.test($('#'+input).val())){
		showNormal(input);
		return true;
	} else {
		showError(input,$('#'+input).attr('error'));
		if (focus) $('#'+input).focus();
		return false;
	}
}

