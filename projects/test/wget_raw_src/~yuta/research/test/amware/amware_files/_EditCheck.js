//*****************************************************************
//** functions:	date_check, time_check, num_check, 
//**			text_check, radio_check, email_check,
//**            phone_check
//**
//** parms: 	ObjName (Name or ID of the form control)
//**			MsgDes	(Description of the field for msg)
//**			Require	("R" for Required, "O" for Optional)
//**			Minf	(Minimum Value, if any)
//**			Maxf	(Maximum Value, if any)
//*****************************************************************
function phone_check(ObjName, MsgDes, Require, Minf, Maxf)   
{	
	check_Blanks(ObjName) //Function will clear txt box with all blanks
		
	if (Require == "o" || Require == "O" && document.forms[0].elements[ObjName].value.length == 0)
	{
		return  true;
	}//End IF
	
	if (Require == "r"|| Require == "R" && document.forms[0].elements[ObjName].value.length == 0)
	{
		alert(MsgDes + " (Phone) is a required field.");
		SelectFld(ObjName);
		return false;
	}//End If

	// Checks for the following valid phone formats:
	// 999-999-9999         (999) 999-9999         999.999.9999 	
	// 999-999-9999 x9999   (999) 999-9999 x9999   999.999.9999 x9999
	var phoneStr = document.forms[0].elements[ObjName].value.toLowerCase();
	var phonePat = /^(?:\(?)(\d{3})(?:\)?)(?:\-?\.?\ ?)(\d{3})(?:\-?\.?\ ?)(\d{4})(?:\ ?)(?:ext|ex|xt|x?)(?:\ ?)(\d{0,4})$/; 
	var matchArray = phoneStr.match(phonePat); // is the format ok?

	if (matchArray == null) 
	{
		alert(MsgDes + "(Phone) is not in a valid format 999-999-9999 x9999 (extention is optional).");
		SelectFld(ObjName);
		return false;
	}//End If	
			
	phn = matchArray[1] + "-" + matchArray[2] + "-" + matchArray[3];
	if (matchArray[4] != "")	{	phn += " x" + matchArray[4];	}
	document.forms[0].elements[ObjName].value = phn;
	
	return  true;
}//End Function		
//*****************************************************************


function date_check(ObjName, MsgDes, Require, Minf, Maxf)   
{	
	check_Blanks(ObjName) //Function will clear txt box with all blanks
		
	if (Require == "o" || Require == "O" && document.forms[0].elements[ObjName].value.length == 0)
	{
		return  true;
	}//End IF
	
	if (Require == "r"|| Require == "R" && document.forms[0].elements[ObjName].value.length == 0)
	{
		alert(MsgDes + " (Date) is a required field.");
		SelectFld(ObjName);
		return false;
	}//End If

// *************************************************
	// Checks for the following valid date formats:
	// MM/DD/YY   MM/DD/YYYY   MM-DD-YY   MM-DD-YYYY   MM.DD.YY   MM.DD.YYYY
	var dateStr = document.forms[0].elements[ObjName].value;
	var datePat = /^(\d{1,2})(\/|-|.)(\d{1,2})(\/|-|.)(\d{2}|\d{4})$/; 
	var matchArray = dateStr.match(datePat); // is the format ok?

	if (matchArray == null) 
	{
		alert(MsgDes + "(Date) is not in a valid format (MM/DD/YYYY).");
		SelectFld(ObjName);
		return false;
	}//End If
	
	// parse date into variables
	month = matchArray[1]; 
	day = matchArray[3];
	year = matchArray[5];
	
	if (month.length == 1) 	{month = "0" + month;}
	if (day.length == 1) 	{day = "0" + day;}
	if (year.length == 2) 
		{
		if (year >= 10) 	{year = "19" + year;}
		else				{year = "20" + year;}
		}
		
	dateStr = month + "/" + day + "/" + year;
	document.forms[0].elements[ObjName].value =  dateStr;

	if (month < 1 || month > 12) 
	{ 	
		alert("Month must be between 1 and 12.");
		SelectFld(ObjName);
		return false;
	}//End If

	var monthMax = new Array(0,31,29,31,30,31,30,31,31,30,31,30,31)
	if (day < 1 || day > monthMax[parseInt(month, 10)]) 
	{
		alert("Day must be between 1 and " + monthMax[parseInt(month, 10)] + ".");
		SelectFld(ObjName);
		return false;
	}//End If
		
	// check for february 29th
	if (parseInt(month, 10) == 2) 
	{ 
		var yyyy = parseInt(year, 10)
		var isleap = (yyyy % 4 == 0 && (yyyy % 100 != 0 || yyyy % 400 == 0));
		if ((parseInt(day, 10) == 29) && !isleap) 
		{
			alert("February " + year + " doesn't have " + day + " days!");
			SelectFld(ObjName);
			return false;
	   	}
	}
	
// *************************************************

	if (Maxf != "")
	{
		var maxArray = Maxf.match(datePat);
		if ((year + month + day) >= (maxArray[5] + maxArray[1] + maxArray[3]))
		{
			alert("The " + MsgDes + " must be less than or equal to " + Maxf + ".");
			SelectFld(ObjName);
			return false;
		}//Enf if
	}//End IF
	
	if (Minf != "")
	{
		var minArray = Minf.match(datePat);
//		alert("(" + year + month + day + ") <= (" + minArray[5] + minArray[1] + minArray[3] + ")");

		if ((year + month + day) <= (minArray[5] + minArray[1] + minArray[3]))
		{
			alert("The " + MsgDes + " must be greater than or equal to " + Minf + ".");
			SelectFld(ObjName);
			return false;
		}//Enf if
	}//End IF	
	
	return  true;
}//End Function		
		
		


//*****************************************************************
function time_check(ObjName, MsgDes, Require, Minf, Maxf)   
{
	check_Blanks(ObjName) //Function will clear txt box with all blanks
		
	if (Require == "o" || Require == "O" && document.forms[0].elements[ObjName].value.length == 0)
	{
		return  true;
	}//End IF
	
	if (Require == "r"|| Require == "R" && document.forms[0].elements[ObjName].value.length == 0)
	{
		alert(MsgDes + " (Time) is a required field.");
		SelectFld(ObjName);
		return false;
	}//End If

	
	// Checks if time is in HH:MM:SS AM/PM format.
	// The seconds and AM/PM are optional.
	var timeStr = document.forms[0].elements[ObjName].value;
	var timePat = /^(\d{1,2}):(\d{2})(:(\d{2}))?(\s?(AM|am|PM|pm))?$/;
	var timePat = /^(\d{1,2}):(\d{2})(\s?(AM|PM))?$/i;
	
	var matchArray = timeStr.match(timePat);
	if (matchArray == null) 
	{
		alert(MsgDes + "(Time) is not in a valid format (HH:MM am/pm).");
		SelectFld(ObjName);
		return false;
	}//End If
	
	var hour = matchArray[1];
	var minute = matchArray[2];
	var ampm = "" + matchArray[3];
	ampm = trim(ampm);
	
	if (hour.length == 1) 	{hour = "0" + hour;}
	
	if ( ampm == "PM" || ampm == "Pm" || ampm == "pm" || ampm == "pM") 	 
		{ampm = "pm";}
	if ( ampm == "AM" || ampm == "Am" || ampm == "am" || ampm == "aM") 	 
		{ampm = "am";}
	if ( ampm == "" )
		{
		if ( hour < 7 ) { ampm = "pm"; } 
		else { ampm = "am" }
		}
			
	timeStr = hour + ":" + minute + " " + ampm;
	document.forms[0].elements[ObjName].value =  timeStr;

	if (hour < 1  || hour > 12) 
	{
		alert("Hour must be between 1 and 12.");
		SelectFld(ObjName);
		return false;
	}//End If
			
	if (minute < 0 || minute > 59) 
	{
		alert ("Minutes must be between 0 and 59.");
		SelectFld(ObjName);
		return false;
	}//End If
	
	// *************************************************

	if (Maxf != "")
	{
		var maxArray = Maxf.match(timePat);
		var time1 = ampm + hour + minute;
		var time2 = maxArray[3] + maxArray[1] + maxArray[2];
		if (trim(time1) >= trim(time2))
		{
			alert("The " + MsgDes + " must be less than or equal to " + Maxf + ".");
			SelectFld(ObjName);
			return false;
		}//Enf if
	}//End IF
	
	if (Minf != "")
	{
		var minArray = Minf.match(timePat);
		var time1 = ampm + hour + minute;
		var time2 = minArray[3] + minArray[1] + minArray[2];
		if (trim(time1) <= trim(time2))
		{
			alert("The " + MsgDes + " must be greater than or equal to " + Minf + ".");
			SelectFld(ObjName);
			return false;
		}//Enf if
	}//End IF	
	
	return true;
}//End Function 


//*****************************************************************
function num_check(ObjName, MsgDes, Require, Minf, Maxf)   
{
	check_Blanks(ObjName) //Function will clear txt box with all blanks
		
	if (Require == "o" || Require == "O" && document.forms[0].elements[ObjName].value.length == 0)
	{
		return  true;
	}//End IF
	
	if (Require == "r"|| Require == "R" && document.forms[0].elements[ObjName].value.length == 0)
	{
		alert(MsgDes + " (Number) is a required field.");
		SelectFld(ObjName);
		return false;
	}//End If
	
	var OneChar
	var Dex = 0
	var FldChk = document.forms[0].elements[ObjName].value

//	if (isNaN(FldChk))
	if (isNaN(parseFloat(FldChk)))
	{ 
		alert("The " + MsgDes + " is not a numeric value.");
		SelectFld(ObjName);
		return false
	} //End If
	
	for (Dex = 0; Dex < FldChk.length; Dex++) 
	{
		OneChar = FldChk.substring(Dex,Dex+1)
		if (OneChar < "0" || OneChar > "9")
		{
			if (OneChar != "-")
			{
			if (OneChar != ".")
				{
					alert("The " + MsgDes + " is not a numeric value.");
					SelectFld(ObjName);
					return false;
				}//End If
			}//End If
		}//end if
	}//end for
	
	if (Maxf != "")
	{
		if (Number(FldChk) > Number(Maxf) )
		{
			alert("The " + MsgDes + " must be less than or equal to " + Maxf + ".");
			SelectFld(ObjName);
			return false;
		}//Enf if
	}//End IF
	
	if (Minf != "")
	{
		if (Number(FldChk) < Number(Minf) )
		{
			alert("The " + MsgDes + " must be greater than or equal to " + Minf + ".");
			SelectFld(ObjName);
			return false;
		}//End if
	}//End IF

	return true;
}//end function
	
//*****************************************************************
function text_check(ObjName, MsgDes, Require, Minf, Maxf)   
{
	check_Blanks(ObjName) //Function will clear txt box with all blanks
		
	if (Require == "o" || Require == "O" && document.forms[0].elements[ObjName].value.length == 0)
	{
		return  true;
	}//End IF
	
	if (Require == "r"|| Require == "R" && document.forms[0].elements[ObjName].value.length == 0)
	{
		alert(MsgDes + " (Text) is a required field.");
		SelectFld(ObjName);
		return false;
	}//End If

	if (document.forms[0].elements[ObjName].value.length < Minf && Minf > 0 )
		{
			alert("The " + MsgDes + " is to short, it should be at least " + Minf + " characters.");
			SelectFld(ObjName);
			return false;
		}//Enf if

	if (document.forms[0].elements[ObjName].value.length > Maxf && Maxf > 0 )
		{
			alert("The " + MsgDes + " is to long, it should be at most " + Maxf + " characters.");
			SelectFld(ObjName);
			return false;
		}//Enf if

	return true;			
}//End Function 

//*****************************************************************
function radio_check(ObjName, MsgDes, Require, Minf, Maxf)   
{
	var Sel=-1;
	var Coll = document.forms[0].elements[ObjName];
	
	for (var i = 0; i < Coll.length; i++) {
		if (Coll[i].checked) 
		{
			return true;
		}//End IF
	}//End For
	
	alert ("Please select a value for: " + MsgDes + ".");
	return false;
}//End Function 

//*****************************************************************
function email_check(ObjName, MsgDes, Require, Minf, Maxf)   
{
	// Min & Max are not used by this check
	check_Blanks(ObjName) //Function will trim blanks from the object
		
	if (Require == "o" || Require == "O" && document.forms[0].elements[ObjName].value.length == 0)
	{
		return  true;
	}
	
	if (Require == "r"|| Require == "R" && document.forms[0].elements[ObjName].value.length == 0)
	{
		alert(MsgDes + " (Email) is a required field.");
		SelectFld(ObjName);
		return false;
	}

	var email = document.forms[0].elements[ObjName].value;
	var at = "@";
	var dot = ".";
	var error = false;
	var len = email.length;
	
	var atPos = email.indexOf(at);
	
	//  AT not found or first or last character
	if (atPos == -1 || atPos == 0 || atPos == len) 
	{
		alert(MsgDes + " (Email) seems to be invalid, please correct it.");
		SelectFld(ObjName);
		return false;
	}	
	//  more than one AT
	if (email.indexOf(at, (atPos+1)) != -1) 	
	{
		alert(MsgDes + " (Email) seems to be invalid, please correct it.");
		SelectFld(ObjName);
		return false;
	}	
	
	//  spaces in email address
	if (email.indexOf(" ") != -1)
	{
		alert(MsgDes + " (Email) seems to be invalid, please correct it.");
		SelectFld(ObjName);
		return false;
	}	

	//  DOT not found after AT
	if (email.indexOf(dot, atPos) == -1) 
	{
		alert(MsgDes + " (Email) seems to be invalid, please correct it.");
		SelectFld(ObjName);
		return false;
	}	
	//  DOT first or last character
	if (email.substring(0, 1) == dot || email.substring(len-1, len) == dot) 
	{
		alert(MsgDes + " (Email) seems to be invalid, please correct it.");
		SelectFld(ObjName);
		return false;
	}	
	//  DOT preceeds or follows AT
	if (email.substring(atPos-1, atPos) == dot || email.substring(atPos+1, atPos+2) == dot) 
	{
		alert(MsgDes + " (Email) seems to be invalid, please correct it.");
		SelectFld(ObjName);
		return false;
	}	
	
	return true;
}// End Function 



function identical_check(Object1, Desc1, Object2, Desc2)   
{
	check_Blanks(Object1); //Function will clear txt box with all blanks
	check_Blanks(Object2); //Function will clear txt box with all blanks

	if (document.forms[0].elements[Object1].value.toLowerCase() == 	
		document.forms[0].elements[Object2].value.toLowerCase())
		{return true;}
	
	alert (Desc1 + " must match " + Desc2 + "."); 
	SelectFld(Object1);
	return false;
}

//*****************************************************************
//** match_check compares two objects 
//** They pass if both are blank or have data.
//** 
//*****************************************************************
function match_check(Object1, Desc1, Object2, Desc2)   
{
	check_Blanks(Object1); //Function will clear txt box with all blanks
	check_Blanks(Object2); //Function will clear txt box with all blanks

	if ( document.forms[0].elements[Object1].type == "checkbox") 
		{ var Len1 = document.forms[0].elements[Object1].checked; }
	else
		{ var Len1 = document.forms[0].elements[Object1].value.length; }
	
	if ( document.forms[0].elements[Object2].type == "checkbox") 
		{ var Len2 = document.forms[0].elements[Object2].checked; }
	else
		{ var Len2 = document.forms[0].elements[Object2].value.length; }
		
	if ( Len1 == 0 && Len2 == 0 )
	   { return true; }
		 
	if ( Len1 > 0 && Len2 > 0 )
	   { return true; }
	
	if ( Len1 == 0 )
	   { alert ("Since " + Desc2 + " is filled in, " + Desc1 + " must also be filled in."); 
	   	 SelectFld(Object1); }

	if ( Len2 == 0 )
	   { alert ("Since " + Desc1 + " is filled in, " + Desc2 + " must also be filled in."); 
	     SelectFld(Object2); }

	return false;
}//End Function 

//*******************************Common functions Follow *******************************
function check_Blanks(ChkObj)
{
	document.forms[0].elements[ChkObj].value = trim(document.forms[0].elements[ChkObj].value);
}//End Function

//*****************************************************************
function SelectFld(Obj)	
{
if (document.forms[0].elements[Obj].type != "select-one")
	{
	document.forms[0].elements[Obj].select();
    document.forms[0].elements[Obj].focus();
	}
}//End Function

//*****************************************************************
function trim(txt)  
{
while('' + txt.charAt(0) == ' ')
	txt = txt.substring(1, txt.length);
	
while('' + txt.charAt(txt.length - 1) == ' ')
	txt = txt.substring(0, txt.length - 1);
	
return txt;
}
//*****************************************************************
function radio_yes(Object1, Desc1, Object2, Desc2)   
{
	check_Blanks(Object2) //Function will clear txt box with all blanks
	
	var Sel=-1
	var Coll = document.forms[0].elements[Object1]
	
		if (Coll[0].checked && document.forms[0].elements[Object2].value.length>0) 
		{
			return true;
		}//End IF
		if (Coll[1].checked) 
		{
			return true;
		}//End IF
	
	alert ("Since " + Desc1 + " is yes, " + Desc2 + " must also be filled in.");
	return false;
}//End Function 

//*****************************************************************
