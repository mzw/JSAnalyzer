function loadJsLiteralWithDelay(escapedLiteral) {
  var script = document.createElement('script');
  script.textContent = escapedLiteral;
  document.body.appendChild(script);
}