//Mecanismo para filtrar os itens de listas textuais.
//O script recebe o nome do imput e da lista que deve manipular.

/*
$(document).ready(function(){
	$('#inputSearch').keyup(function(){
		var texto = $('#inputSearch').val();
		texto +=  '.*';
		var rTexto = new RegExp(texto,'i');

		if (texto.length > 2){
			$('#listSearch li').hide();
			$('#listSearch li').filter(function(i){	   
				return rTexto.test($(this).text());
			}).show();
		}else{
			$('#listSearch li').show();
			texto.length = "";
		}

	});
});

*/

/*
 * Mecanismo de controle da barra de acessibilidade
 */
$(document).ready(function(){	
	
	//Funcao para a criacao de cookies
	$.createCookie = function(name, value, params) {
		$.cookie(name, value, params);
	}
	
	
	//Funcao para exclusao de cookies
	$.deleteCookie = function(name,params) {
		$.cookie(name, null, params);
	}

/*
	$.hasCookie = function(){
		var has_correios_contrast = $.cookie('correios_contrast');
		var has_correios_font = $.cookie('correios_font');
		alert( "contrast= "+has_correios_contrast+" Font= "+has_correios_font);
	}
	
	$('#hasCookie').click(function(){
		$.hasCookie();
	});
*/	
	//Habilita e desabilita o contraste
	$('#contrast').click(function() {
		var valueCookie = $.cookie('correios_contrast');
		if(valueCookie == null){
			$.createCookie('correios_contrast', 1, {path : '/'} );
			$('body').addClass('contrast');
		}else{
			$.deleteCookie('correios_contrast',{path : '/'});
			$('body').removeClass('contrast');
		}
		
	});
	
	//Conjunto de funcoes para a edicao do tamanho do texto das paginas
	//Diminui a fonte
	$('#smallerFont').click(function() {
		var valueCookie = $.cookie('correios_font');

		if(valueCookie >= -1){
			valueCookie --;
			$.createCookie('correios_font', valueCookie, {path : '/'} );
			$.modeFont(valueCookie);
		}if(valueCookie == null){
			$.createCookie('correios_font', -1,{path : '/'} );
			$.modeFont(-1);
		}
	});
	
	//Aumenta a fonte
	$('#biggerFont').click(function() {
		var valueCookie = $.cookie('correios_font');

		if(valueCookie <= 1){
			valueCookie ++;
			$.createCookie('correios_font',valueCookie, {path : '/'});
			$.modeFont(valueCookie);
		}if(valueCookie == null){
			$.createCookie('correios_font', 1 , {path : '/'} );
			$.modeFont(1);
		}
	});
	
	//Define a fonte no tamanho padr�o
	$('#defaultFont').click(function() {
		$.deleteCookie('correios_font',{path : '/'});
		$.modeFont(0);
	});
	
	//verificar os cookies e executar os controles das fonts
	valueCookieFont = parseInt($.cookie('correios_font'));
	
	$.modeFont = function(n){

		switch (n)
			{
				case 2:
				//console.log(n);
				$('body').css('font-size','12px');
				break;
				
				case 1:
				//console.log(n);
				$('body').css('font-size','11px');
				break;
				
				case 0:
				//console.log(n);
				$('body').css('font-size','10px');
				break;
				
				case -1:
				//console.log(n);
				$('body').css('font-size','9px');
				break;
				
				case -2:
				//console.log(n);
				$('body').css('font-size','8px');
				break;
				
				default:
				//console.log('caiu fora');
			}	
	};
	
	
	$.modeFont(valueCookieFont);
	// Verifica os cookies e executa os controles do contraste
	valueCookieContrast = parseInt($.cookie('correios_contrast'));
	
	if(valueCookieContrast == 1){
		$('body').addClass('contrast');
	}else{
		$('body').removeClass('contrast');
	}
	
	
});
