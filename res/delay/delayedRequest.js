var DelayedRequest = (function() {
  var DEFAULT_DELAY = 0;

  function DelayedRequest(delayMillis) {
    if (isNaN(delayMillis)) {
      console.warn(delayMillis + " is not a valid number.");
      console.warn("default delay '" + DEFAULT_DELAY + "' is used instead.");
      delayMillis = DEFAULT_DELAY;
    }
    this.delayMillis = parseInt(delayMillis);
  }

  function copyProperty(fromObject, toObject, propertyName, defaultValue) {
    if (typeof fromObject[propertyName] === "undefined") {
      toObject[propertyName] = defaultValue;
    } else {
      // TODO: implement deep-copy
      toObject[propertyName] = fromObject[propertyName];
    }
  }

  function createCopyObject(target) {
    var copy = {};
    for (var key in target) {
      copyProperty(target, copy, key);
    }
    return copy;
  }

  function delayedCallbackFunc(func, delayInMillis) {
    return function() {
      setTimeout(func.bind(this, arguments), delayInMillis);
    }
  }

  function startsWith(str, prefix) {
    return str.indexOf(prefix) === 0;
  }

  // For prototype.js
  DelayedRequest.prototype.prototypeJs_Ajax_Request = function(url, options) {
      var copiedOptions = createCopyObject(options);
      for (var key in copiedOptions) {
        if (startsWith(key, "on") && (typeof copiedOptions[key]) === "function") {
          copiedOptions[key]
              = delayedCallbackFunc(copiedOptions[key], this.delayMillis);
        }
      }
      new Ajax.Request(url, copiedOptions);
  };

  // For any functions
  DelayedRequest.prototype.applyFunction = function(func) {
      // To deal with bug in Opera, we need to copy argument to array.
      var options = [];
      options.push.apply(options, arguments);
      options.shift();

      setTimeout(function() {func.apply(document, options)}, this.delayMillis);
  };


  return DelayedRequest;
})();