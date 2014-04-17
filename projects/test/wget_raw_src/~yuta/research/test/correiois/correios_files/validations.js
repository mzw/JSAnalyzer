/*
	Javascript de valida��o de campos e submi��o de formularios do portal 2010.
 function validate_required(field,alerttxt) 
 	Usada para obrigar a digita��o do campo, ou seja n�o aceita nem nulo nem branco.
 function validate_iguals(field1,field2,alerttxt)
 	Usdada validar campos iguais, como senha e redigita��o de senhas.
 function validate_form(thisform)
 	No formul�rio a ser validado colocam-se os campos que devem ser validados e qual a opera��o de valida��o.
	
  function validate_form(thisform)
{
with (thisform)
  {
  if (validate_required(email,"Email must be filled out!")==false)
  {email.focus();return false;}
  }
}

Resumo:
Todas as fun��es de valida��o dever�o estar neste documento.
E cada formul�rio devera conter a sua fun��o validate_form	


Para compactar este arquivo favor utilizar o site:

http://javascriptcompressor.com/

*/


//************************** Fun��es auxiliares de valida��o *********************************

// fun��o trim() retira espa�os em branco de strings
function trim (str) {
	str = str.replace(/^\s+/, '');
	for (var i = str.length - 1; i >= 0; i--) {
		if (/\S/.test(str.charAt(i))) {
			str = str.substring(0, i + 1);
			break;
		}
	}
	return str;
}

// fun��o ValidarCNPJ verifica a validade do CNPJ digitado

function ValidarCNPJ(ObjCnpj){
        var cnpj = ObjCnpj.value;
        var valida = new Array(6,5,4,3,2,9,8,7,6,5,4,3,2);
        var dig1= new Number;
        var dig2= new Number;
        
        exp = /\.|\-|\//g
        cnpj = cnpj.toString().replace( exp, "" ); 
        var digito = new Number(eval(cnpj.charAt(12)+cnpj.charAt(13)));
                
        for(i = 0; i<valida.length; i++){
                dig1 += (i>0? (cnpj.charAt(i-1)*valida[i]):0);  
                dig2 += cnpj.charAt(i)*valida[i];       
        };
        dig1 = (((dig1%11)<2)? 0:(11-(dig1%11)));
        dig2 = (((dig2%11)<2)? 0:(11-(dig2%11)));
        
        if(((dig1*10)+dig2) != digito){
			return false;
		}else
		{return true;};
}




// fun��o ValidarCPF verifica a validade do CPF digitado

function ValidarCPF(Objcpf){
        var cpf = Objcpf.value;
        exp = /\.|\-/g
        cpf = cpf.toString().replace( exp, "" ); 
        var digitoDigitado = eval(cpf.charAt(9)+cpf.charAt(10));
        var soma1=0, soma2=0;
        var vlr =11;
        
        for(i=0;i<9;i++){
                soma1+=eval(cpf.charAt(i)*(vlr-1));
                soma2+=eval(cpf.charAt(i)*vlr);
                vlr--;
        }       
        soma1 = (((soma1*10)%11)==10 ? 0:((soma1*10)%11));
        soma2=(((soma2+(2*soma1))*10)%11);
        
        var digitoGerado=(soma1*10)+soma2;
        if(digitoGerado!=digitoDigitado){
			return false;
		}
		else{
			return true;
		}
                
}


//************************  FUN��ES DE VALIDA��O *******************************
function validate_required(field,alerttxt){
	
	with (field){
		
		if (trim(value)==null||trim(value)==""){
			alert((alerttxt));return false;
    	}
		else{
			return true;
		}
	}
	
	
	
}
		
 function validate_required_default_value(field1,default_value,alerttxt){
	 if (field1.value == default_value){
		 alert(alerttxt);return false;
	}
  	else{
		return true;
    }
 }
	 
 function validate_iguals(field1,field2,alerttxt){
	 if (field1.value != field2.value){
		 alert(alerttxt);return false;
	}
  	else{
		return true;
    }
 }

function validate_lenght(field1,leng,alerttxt){
	if (trim(field1.value).length < leng){
		alert(alerttxt);return false;
    }
	else{
		return true;
    }
}
function validate_email(field,alerttxt){
	with (field){
		apos=value.indexOf("@");
		dotpos=value.lastIndexOf(".");
		if (apos<1||dotpos-apos<2){
			alert(alerttxt);return false;
		}
		else {return true;
		}
	}
}
function validate_cnpj(field,alerttxt){
	if(ValidarCNPJ(field)){
		return true;
	}
	
		alert(alerttxt);return false;
}
