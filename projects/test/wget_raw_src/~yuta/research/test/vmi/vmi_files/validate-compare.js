function validatemini()
{	var flag;
	flag=true;
	if(document.form1.Age.value==''||isNaN(document.form1.Age.value)||document.form1.Age.value<1||document.form1.Age.value>99)
		{alert('Visitor\'s Age is mandatory and it should be in numeric format in the range 1-99.');
		document.form1.Age.focus();
		flag=false;
		return ;
		}
	if(document.form1.spouseAge.value!=''&&(isNaN(document.form1.spouseAge.value)||document.form1.Age.value<1||document.form1.Age.value>99))
		{alert('Spouce Age is mandatory and it should be in numeric format in the range 1-99..');
		document.form1.spouseAge.focus();
		flag=false;
		return ;
		}
	if(document.form1.monthsOfCoverage.value==''||document.form1.daysOfCoverage.value==''||document.form1.monthsOfCoverage.value>12||document.form1.monthsOfCoverage.value==12&&document.form1.daysOfCoverage.value>0||document.form1.monthsOfCoverage.value==11&&document.form1.daysOfCoverage.value>30){alert('Coverage period can not be blank or more than one year(12 months).'); document.form1.monthsOfCoverage.focus(); return;}

		if(flag==true&&conditionalparameters()==true)
			{document.form1.quote.disabled=false;
			 document.form1.submit();
			}

}

function numbersonly(e){
			var unicode=document.all? e.keyCode : e.which;
			if(unicode!=8){if(unicode<48||unicode>57)	return false ;}
			}

function conditionalparameters()
{var a,s,x,d;
	a=parseInt(document.form1.Age.value,10);
	s=parseInt(document.form1.spouseAge.value,10)?s!='':0;
	x=parseInt(document.form1.MaxPolicyLimit.value,10);
	if(x==75000)x=100000; if(x==150000)x=250000;
	d=parseInt(document.form1.Deductible.value,10);
	ct=document.form1.CountryofCitizenship[0].checked==true?parseInt(document.form1.CountryofCitizenship[0].value,10):parseInt(document.form1.CountryofCitizenship[1].value);

	if ((a>80||s>80)&&((x >100000&&ct==1)||(x >50000&&ct==2)))
		{alert('For travelers who are 80 yrs and above, the maximum coverage amount has to be $25,000 or $50,000 or $100,000(US citizen and deductible $50).\n\n		Please change your selection and then click "Get Quote"');
		 document.form1.MaxPolicyLimit.focus(); return false;}
	if ((a>70||s>70)&&((x >250000&&ct==1)||(x >100000&&ct==2)) )
		{alert('For travelers who are 70 yrs and above, the maximum coverage amount has to be $25,000, $50,000 or $100,000(Non-US citizen) or $250,000(US citizen).\n\n		Please change your selection and then click "Get Quote"');
		 document.form1.MaxPolicyLimit.focus(); return false;}
	if ((a>70||s>70)&&x!=50000&&d==0)
		{alert('For travelers who are 70 yrs and above requiring a $0 deductible, the maximum coverage amount has to be $50,000.\n\n		Please change your selection and then click "Get Quote"');
		 document.form1.Deductible.focus(); return false;	}
	if ((a<80||s<80) && d==0 && x==25000)
		{alert('For all travelers younger than 80 yrs requiring a $0 deductible, maximum policy coverage has to be greater than $25,000.\n\n		Please change your selection and then click "Get Quote"');
		 document.form1.Deductible.focus(); return false;	}
	if ((a>=80||s>=80) && d==0 && x!=25000)
		{alert('For all travelers older than 80 yrs requiring a $0 deductible, maximum policy coverage has to be $25,000 or lower.\n\n		Please change your selection and then click "Get Quote"');
		 document.form1.Deductible.focus(); return false;	}
	return true;
}

function gomain()
		{
		document.form1.P.value=document.form1.Age.value;
		document.form1.S.value=document.form1.spouseAge.value;
		document.form1.mths.value=document.form1.monthsOfCoverage.value;
		document.form1.dys.value=document.form1.daysOfCoverage.value;
		document.form1.x.value=document.form1.MaxPolicyLimit.value;
		document.form1.us.value=document.form1.CountryofCitizenship[0].checked==true?document.form1.CountryofCitizenship[0].value:document.form1.CountryofCitizenship[1].value;
		document.form1.action='http://www.visitorshealthinsurance.com/compare-visitors-medical-insurance/';
		document.form1.submit();
		}
