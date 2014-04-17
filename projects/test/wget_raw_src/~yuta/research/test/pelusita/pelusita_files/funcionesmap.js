//*****************************************************
var map;
var markersArray = [];
var geocoder;
var image = 'aqui.png';

function initialize(lat, longe, nuzoom) {
  geocoder = new google.maps.Geocoder();
  var myLatlng = new google.maps.LatLng(lat, longe);
  var myOptions = {
	zoom: nuzoom,
	center: myLatlng,
	mapTypeControl: true,
	mapTypeControlOptions: {style: google.maps.MapTypeControlStyle.DROPDOWN_MENU},
	navigationControl: true,
	navigationControlOptions: {style: google.maps.NavigationControlStyle.SMALL},
	mapTypeId: google.maps.MapTypeId.ROADMAP
  }

  map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);
  var marker = new google.maps.Marker({
	map: map,
    position: myLatlng,
	icon: image
  });

  google.maps.event.addListener(map, 'click', function(event) {
    placeMarker(event.latLng);
	deleteOverLays();
	marker.setMap(null);//eliminamos el marcador principal
	addMarker(event.latLng);
	map.setZoom(15);
	//document.forms['alta_cliente'].submit();
	//alert('el nuevo puntero ubicara el lugar exacto \n de tu servicio');
  });
}//fin initialize
//*****************************************************
function codeAddress(name_form){
	var form = document.forms[name_form];
  	var dir = form.dir.value;
	var cp = ', ' + form.cp.value;
	var city = ', ' + form.ciudad.value;
	var address = dir  + cp  + city;
	var image = 'aqui.png';
  	if (geocoder) {
  		geocoder.geocode({
  			'address': address
  		}, function(results, status){
  			if (status == google.maps.GeocoderStatus.OK) {
  				map.setCenter(results[0].geometry.location);
				map.setZoom(15);
				document.forms[name_form].elements['lat'].value = results[0].geometry.location;
				if(markersArray) markersArray.length = 0;
				var marker = new google.maps.Marker({
					map: map, 
					position: results[0].geometry.location,
					icon: image
				});
				//alert('si tu servicio no esta correctamente ubicado, \n pulsa en el mapa en el lugar exacto');
  			}
  			else {
  				initialize('28.4696738', '-16.263339599999995', 8);
  				alert("Geocode was not successful for the following reason: " + status);
  			}
  		});
  	}
}//fin codeAddress
//*****************************************************

function mostrar_datos(name_form){
	var form = document.forms[name_form];
  	var dir = form.dir.value;
	var cp = form.cp.value;
	var city = form.ciudad.value;
	var address = dir + ', ' + cp + ', ' + city;
	alert(address);
}

function placeMarker(location) {
  var clickedLocation = new google.maps.LatLng(location);
  document.forms['alta_cliente'].elements['lat'].value = location;
  map.setCenter(location);
}//fin placeMarker(location)
//*****************************************************
function addMarker(location){
	marker = new google.maps.Marker({
		position: location,
  		map: map,
		icon: image
	});
	markersArray.push(marker);
}//fin addMaker
//*****************************************************
function deleteOverLays(){
	if(markersArray){
		for(i in markersArray){
			markersArray[i].setMap(null);
		}
		markersArray.length = 0;
	}
}
// Sets the map on all markers in the array.
function setAllMap(map){
  for (var i = 0; i < markers.length; i++) {
    markers[i].setMap(map);
  }
}


//*****************************************************
var img = 'aqui.png';
var map;
function inicializar(lat, longe , div){
	div = (typeof div == 'undefined') ? "map_canvas": div ;
	var myLatlng = new google.maps.LatLng(lat, longe);
	var myOptions = {
	zoom: 15,
	center: myLatlng,
	mapTypeControl: true,
	mapTypeControlOptions: {style: google.maps.MapTypeControlStyle.DROPDOWN_MENU},
	navigationControl: true,
	navigationControlOptions: {style: google.maps.NavigationControlStyle.SMALL},
	mapTypeId: google.maps.MapTypeId.ROADMAP
	}
	map = new google.maps.Map(document.getElementById(div), myOptions);
	var marker = new google.maps.Marker({ 
		map: map,
		position: myLatlng,
		icon: img
       });
}
//*****************************************************
//funcion para ver mapa en facturas
var img_fac = 'aqui.png';
var map;
function inicializarFac(lat, longe , div){
	div = (typeof div == 'undefined') ? "map_canvas": div ;
	var myLatlng = new google.maps.LatLng(lat, longe);
	var myOptions = {
	zoom: 15,
	center: myLatlng,
	mapTypeControl: true,
	mapTypeControlOptions: {style: google.maps.MapTypeControlStyle.DROPDOWN_MENU},
	navigationControl: true,
	navigationControlOptions: {style: google.maps.NavigationControlStyle.SMALL},
	mapTypeId: google.maps.MapTypeId.ROADMAP
	}
	map = new google.maps.Map(document.getElementById(div), myOptions);
	var marker = new google.maps.Marker({ 
		map: map,
		position: myLatlng,
		icon: img_fac
       });
}
//*****************************************************
function mostrarMAP(div, lat, longe){
	posDIV('ventana', 600, 300);
	mostrarDIV('ventana');
	var cargando = '<p style="text-align:center;"><img src="images/0.gif" alt="cargando..." /></p>';
	$('#ventana').html(cargando);
	$('#ventana').load('mapa.php',{'lat':lat,'longe':longe});
	//inicializar(lat,longe,div);
}
//*****************************************************
function cambia_ciudad(id_mun){
	$('#cambia_ciudad').load('list_datos/cambia_ciudad.php',{'id_mun':id_mun});
}
//*****************************************************
