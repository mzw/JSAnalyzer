function loadpag(div, pag){
	var elem = '#' + div;
	$(elem).load(pag);
}//cargar una pagina en un div

function loadpagver(div, pag){
	var elem = '#' + div;
	
	//si ha puesto los parametros de alto y ancho
	var num_parametros = arguments.length;
	if(num_parametros > 2){
		var ancho = arguments[2];
		var alto = arguments[3];
		posDIV(div, ancho,alto);
	}else{posDIV(div, 400, 400);}
	
	var cargando = '<p style="text-align:center;"><img src="../images/0.gif" alt="cargando..." /></p>';
	$(elem).html(cargando);
	$(elem).load(pag);
	mostrarDIV(elem);
}//mostrar una pagina en un div y mostrarlo

function loadpagverDat(div, pag, dat,ancho,alto){
	var elem = '#' + div;
	posDIV(div, ancho, alto);
	var cargando = '<p style="text-align:center;"><img src="images/0.gif" alt="cargando..." /></p>';
	$(elem).html(cargando);
	$(elem).load(pag,{'dat':dat});
	mostrarDIV(elem);
}//mostrar una pagina en un div y mostrarlo

function loadpagDat(div, pag, dat){
	var elem = '#' + div;
	var cargando = '<p style="text-align:center;"><img src="images/0.gif" alt="cargando..." /></p>';
	$(elem).html(cargando);
	$(elem).load(pag,{'dat':dat});
	
}//cargar pagina con datos en un div

function loadpagDatVent(div, pag, dat){
	var elem = '#' + div;
	$(elem).load(pag,{'dat':dat});
	mostrarDIV(elem);
}//cargar pagina con datos y mostrar el div

function confirmar(todoCogido){
	var estado_conf = document.getElementById('conf_condicion').checked;
	if(todoCogido == 'si'){
		if(estado_conf){
			$('.confirmar').css('display','block');
			$('.confirm').css('display','none');
		}else{
			$('.confirmar').css('display','none');
			$('.confirm').css('display','block');
		}
	}else{
		posDIV('ventana', 400, 250);
		var contenido = '<a href="javascript:cerrarDIV(' + "#ventana" + ');">';
		contenido += '<img class="cerrar" src="../images/zona.png" alt="cerrar" />';
		contenido += '</a>';
		contenido += '<br><br>';
		contenido += '<h2>A&uacute;n te quedan servicios por ubicar</h2>';
		$('#ventana').html(contenido);
	}
}

function solicitar_hor(id_sec,id_estil,fecha){
	var form = document.forms['solicitar_serv'];
	var scrolltop = $('.content_tabla_horario').scrollTop();
	//ponemos el servicio elegido en el campo servicio
	form.id_sec.value = id_sec;
	form.id_estil.value = id_estil;
	form.fecha.value = fecha;
	form.scrolltop.value = scrolltop;
	form.submit();//enviamos formulario
}

function search_pel(nombre){
	$('#autocomplete-suggestions').css({'display':'block'});
	var msg = "<p align='center'><img src='images/0.gif' /></p>";
	$("#autocomplete-suggestions").html(msg);
	var text_esc = escape(nombre);
	var city = document.forms['buscador_belt'].elements['city'].value;
	$('#autocomplete-suggestions').load('buscadores/buscar_pel.php',{'texto':text_esc,'city':city});
}
function elegir_pel(id_pel,nombre){
	document.forms['buscador_belt'].elements['name'].value = nombre;
	$('#autocomplete-suggestions').css({'display':'none'});
}
//funcion de botones de siguiente y atras de meses
function enviar_mes_anno3(mes,anno,dia,mesesanticipados){
	$('#calendario').load(
		'../admin/calendario/ver_calendario3.php',
		{'mes':mes, 'anno':anno,'dia':dia,'mesesanticipados':mesesanticipados}
		);
}

//formulario de reserva de la peluqueria
function form_reserva_pelu(id_pelu,can_serv_elegir){
	
	//primero vaciamos y ocultamos todos los posibles formularios abiertos
	$('.form_pelu').text('');
	cerrarDIV('.form_pelu');
	
	//cargamos datos en el div
	var name_div = '#form_pelu' + id_pelu;
	
	$(name_div).load(
		'ajax_php/form_reserva_pelu.php',
		{'id_pelu':id_pelu,'can_serv_elegir':can_serv_elegir},
		function(){
			mostrarDIVefect(name_div);
			}
		);
	
}//fin formulario de reserva de la peluqueria

function add_select(id_ser,id_pel,selector){
	var nombre_form = 'form_serv' + id_pel;
	var form = document.forms[nombre_form];
	//ponemos el servicio elegido en el campo servicio
	form.servicio.value = id_ser;
	form.selector.value = selector;
	form.submit();//enviamos formulario
}

//cargar servicio en lista
function enviaServ(name_form,name_div){
	//recuperamos varibles del formulario
	var servicio = document.forms[name_form].elements['cat_serv'].value;
	var fecha = document.forms[name_form].elements['fecha'].value;
	var can_serv_elegir = document.forms[name_form].elements['can_serv_elegir'].value;
	var id_pelu = document.forms[name_form].elements['id_pelu'].value;
	$(name_div).load(
		'ajax_php/servicios_elegidos.php',
		{'servicio':servicio,'fecha':fecha,'can_serv_elegir':can_serv_elegir,'id_pelu':id_pelu},
		function(){mostrarDIV(name_div);}
		);
}

//envio servicio desde select categoria
function mostrarServ(name_form,name_div){
	
	//recuperamos valores del formulario
	var cat= document.forms[name_form].elements['cat'].value;
	//enviamos los datos y recibimos respuesta
	$(name_div).load(
		'ajax_php/servs_de_cat.php',
		{'cat':cat},
		function(){mostrarDIVefect(name_div);}
		);
	
}//fin envio servicio desde select categoria

//funciones de calendario*********************************
function ver_calendario(){
	$('#ventana').html('');
	posDIV('ventana', 400, 350);
	$('#ventana').load('admin/calendario/ver_calendario.php');
	mostrar('ventana');	
}//fin editar estilista

/*
 * condicion = condicion de la busqueda
 * pag = pagina php a cargar
 * idart = id usuario
 * div = id de capa donde se carga
 * pagina = numero de pagina
*/
function loadpagCondPg(div, pag, idart, pagina, condicion){
	var elem = '#' + div;
	$(elem).load(pag,{'idelemnt':idart, 'pag':pagina, 'condicion':condicion, 'dat':0});
}
//cargar condicion y pagina para paginado con dat
/*
 * condicion = condicion de la busqueda
 * pag = pagina php a cargar
 * idart = id usuario
 * div = id de capa donde se carga
 * pagina = numero de pagina
 * dat = seccion a la que pertenece los articulos
*/
function loadpagCondDat(div, pag, idart, pagina, condicion, dat){
	var elem = '#' + div;
	$(elem).load(pag,{'idelemnt':idart, 'pag':pagina, 'condicion':condicion, 'dat':dat});
}
