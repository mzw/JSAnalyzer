function getRequest(){
	if(location.search.length > 1) {
		var get = new Object();
		var ret = location.search.substr(1).split("&");
		for(var i = 0; i < ret.length; i++) {
			var r = ret[i].split("=");
			get[r[0]] = r[1];
		}
		return get;
	} else {
		return false;
	};
};

function calculate_and_display_total_price() {
	var price = 0;
	var slct_type = document.getElementById("reg_type");
	var price_type = parseInt(slct_type.value);
	var slct_attendee = document.getElementById("reg_attendee");
	var price_attendee = parseInt(slct_attendee.value);
	var slct_payment = document.getElementById("reg_payment");
	var price_payment = parseInt(slct_payment.value);
	price += price_type + price_attendee + price_payment;
	displayPrice(price);
};

function displayPrice(price) {
	var price_field = document.getElementById("price");
	price_field.innerHTML = price;
};

function isValidInput() {
	var t_valid = validSelectById("reg_type");
	var a_valid = validSelectById("reg_attendee");
	var p_valid = validSelectById("reg_payment");
	
	var q_valid = validSelectById("quantity");
	
	if(t_valid && a_valid && p_valid && q_valid) {
		return true;
	} else {
		return false;
	};
};

function validSelectById(id) {
	var slct = document.getElementById(id);
	var price = parseInt(slct.value);

	if(0 <= price) {
		return true;
	} else {
		return false;
	};
};

function validQuantityById(id) {
	var inpt_quantity = document.getElementById(id);
	var quantity = parseInt(obj_quantity.value);
	if(!isNaN(quantity)) {
		return true;
	} else {
		return false;
	};
};

function getParams() {
	var slct_type = document.getElementById("reg_type");
	var t = slct_type.value;
	var slct_attendee = document.getElementById("reg_attendee");
	var a = slct_attendee.value;
	var slct_payment = document.getElementById("reg_payment");
	var p = slct_payment.value;
	
	var inpt_quantity = document.getElementById("quantity");
	var q = inpt_quantity.value;
	
	return "t=" + t + "&a=" + a + "&p=" + p + "&q=" + q;
};

function disableAll() {
	var slct_type = document.getElementById("reg_type");
	var slct_attendee = document.getElementById("reg_attendee");
	var slct_payment = document.getElementById("reg_payment");
	var inpt_quantity = document.getElementById("quantity");
	
	slct_type.disabled = true;
	slct_attendee.disabled = true;
	slct_payment.disabled = true;
	inpt_quantity.disabled = true;
	
	var inpt_addcart = document.getElementById("addcart");
	inpt_addcart.disabled = true;
};

function jumpToConfirm() {
	document.location.href = "confirm.php?" + getParams();
};

