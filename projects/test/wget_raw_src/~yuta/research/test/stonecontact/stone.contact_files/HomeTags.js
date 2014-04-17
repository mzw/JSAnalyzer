// JavaScript Document
function selectTag(showContent,selfObj){
	// ������ǩ
	var tag = document.getElementById("HomeTags").getElementsByTagName("li");
	var taglength = tag.length;
	for(i=0; i<taglength; i++){
		tag[i].className = "";
	}
	selfObj.parentNode.className = "HomeSelectTag";
	// ��������
	for(i=0; j=document.getElementById("tagContent"+i); i++){
		j.style.display = "none";
	}
	document.getElementById(showContent).style.display = "block";
	
	
}
function selectTagSerCon(showContent,selfObj){
	// ������ǩ
	var tag = document.getElementById("HomeTagsSerCon").getElementsByTagName("li");
	var taglength = tag.length;
	for(i=0; i<taglength; i++){
		tag[i].className = "";
	}
	selfObj.parentNode.className = "HomeSelectTagSerCon";
	// ��������
	for(i=0; j=document.getElementById("tagContentSerCon"+i); i++){
		j.style.display = "none";
	}
	document.getElementById(showContent).style.display = "block";
	
	
}
function selectTagSerCon1(showContent,selfObj){
	// ������ǩ
	var tag = document.getElementById("HomeTagsSerCon1").getElementsByTagName("li");
	var taglength = tag.length;
	for(i=0; i<taglength; i++){
		tag[i].className = "";
	}
	selfObj.parentNode.className = "HomeSelectTagSerCon1";
	// ��������
	for(i=0; j=document.getElementById("StonetagContentSerCon"+i); i++){
		j.style.display = "none";
	}
	document.getElementById(showContent).style.display = "block";
	
	
}
