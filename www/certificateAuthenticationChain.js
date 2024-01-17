var exec = require('cordova/exec');

exports.clean = function (spKeyName, successCallback, errorCallback) {
  exec(successCallback, errorCallback, 'ClientCertificateAuthenticationChain', 'clean', [spKeyName]);
};

exports.getPreferences = function (successCallback, errorCallback) {
  exec(successCallback, errorCallback, 'ClientCertificateAuthenticationChain', 'getPreferences', []);
};
