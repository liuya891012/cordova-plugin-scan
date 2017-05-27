var exec = require('cordova/exec');


var ScanPlugin = function () {}
// arg1：成功回调; arg2：失败回调; arg3：将要调用类配置的标识; arg4：调用的原生方法名; arg5：参数，json格式
   ScanPlugin.prototype.getCode = function (success, error) {
    exec(success, error, "ScanPlugin", "getCode", []);
  };
  ScanPlugin.prototype.closeScan = function (success, error) {
    exec(success, error, "ScanPlugin", "closeScan", []);
  };
  ScanPlugin.prototype.closeKeyboard = function (success, error) {
    exec(success, error, "ScanPlugin", "closeKeyboard", []);
  };
  ScanPlugin.prototype.playOk = function (success, error) {
	exec(success, error, "ScanPlugin", "playOk", []);
  };
  ScanPlugin.prototype.playError = function (success, error) {
	exec(success, error, "ScanPlugin", "playError", []);
   };
   ScanPlugin.prototype.playSys = function (success, error) {
  exec(success, error, "ScanPlugin", "playSys", []);
   };
var scanPlugin=new ScanPlugin();
module.exports = scanPlugin;
