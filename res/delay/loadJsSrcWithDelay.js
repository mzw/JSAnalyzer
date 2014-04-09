function loadSrcWithDelay(src) {
  var script = document.createElement('script');
  script.src = src;
  document.body.appendChild(script);
}