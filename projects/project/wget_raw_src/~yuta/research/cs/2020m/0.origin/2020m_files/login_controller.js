//**********************************************************************************************************
// js handling the login procedures
// constants
var NORMAL_STATE = 4;
var LOGIN_PREFIX = 'login.php?';
// variables
var messageElement = false;
var hasSeed = false;
var loggedIn = false;
var seed_id = 0;
var seed = 0;
var hash = '';
var fullname = '';
var messages = '';

//**********************************************************************************************************
function initLogin(){
	$('#user').bind('focus', function(){ focusField(); });
	$('#pass').bind('focus', function(){ focusField(); });
	$('#user').focus();
	$('#loginfrm').submit(function(){
		var action = $(this).attr('action');
		$('#loginfrm').attr('disabled','disabled'); //$('#loginbtn').attr('disabled','disabled');
		$('#task').val($('#loginbtn').val());
		if ($('#task').val() == 'LogIn'){
			validateLogin();
		}
		var user = $('#user').val();
		$.post(action, { task: $('#task').val(), user: $('#user').val(), pass: $('#pass').val(), hash: hash, id: seed_id },
		function(data){
			var result = data.split(':');
			switch(result[0]){
				case 'LoggedIn':
					window.location.replace($('#srcDomain').val()+'/wr_playlist_editor.php');
					//window.location.reload(true);
				break;
				case 'LoggedOut':
					loggedIn = false;
					hasSeed = false;
					location.replace($('#srcDomain').val());
				break;
				default:
					$('#user').val('');
					$('#pass').val('');
					$('#task').val('');
					$('#loginfrm').attr('disabled',''); //$('#loginbtn').attr('disabled','');
					showResponse(data);
					return false;
				break;
			}
			return false;
		});
		return false;
	});
	return false;
}
//**********************************************************************************************************
function forgotPasswordClick(){
	$('#forgotPasswordFrm').slideDown(500);
}
//**********************************************************************************************************
function initForgotUserName(){
	$('#forgot_usernamebtn').attr('disabled', 'disabled');
	$.post('../include/login.php', { task: 'ForgotPassword', forgot_username: $('#forgot_username').val() },
	function(data){
		switch(data){
			case 'EmailedAccountLogin':
				showResponse('An Email containing your Login Credentials has been sent to you.');
			break;
			case 'UserNameNotFound':
				showResponse('Could not find Username in our system.');
			break;
			default:
				alert('An Error Occured:'+data);
			break;
		}
		$('#forgotPasswordFrm').slideUp(500);
		return false;
	});
	return false;
}
//**********************************************************************************************************
function getSeed(){
	if (!loggedIn && !hasSeed) {
		$.post("include/login.php", { task: 'getseed' },
		function(data){
			results = data.split('|');
			seed_id = results[0];
			seed = results[1];
			hasSeed = true;
		});
	}
	return false;
}
//**********************************************************************************************************
function validateLogin(){
	if (loggedIn){ return false; }
	var user = $('#user').val();
	var pass = $('#pass').val();
	if (user != '' && pass != '') {
		hash = hex_md5(pass + seed);
	}
	return false;
}
//**********************************************************************************************************
function focusField(){
	try {
		if(messageElement != false){ alert('found message element?');
			document.getElementById('msg').removeChild(messageElement);
			//$(messageElement).remove();
		}
		getSeed();
	}
	catch (e){ } // do nothing... hides an apparent firefox bug: https://bugzilla.mozilla.org/show_bug.cgi?id=236791
	return false;
}
//**********************************************************************************************************
