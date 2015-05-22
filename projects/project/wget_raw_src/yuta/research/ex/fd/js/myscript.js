function updateProgress(str) {
	var progressField = document.getElementById("progress");
	var updateText = document.createTextNode(str);
	var curText = progressField.firstChild;
	if(curText == null) {
		progressField.appendChild(updateText);
	} else {
		progressField.replaceChild(updateText, curText);
	};
};
