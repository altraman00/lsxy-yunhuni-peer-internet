/**
 * 判断浏览器版本是否正常 
 */
function checkBrowser(callback){
	var _browser = getBrowserInfo();
	var browserType = (_browser+"").replace(/[0-9.]/ig,""); 
	var browserVersion = (_browser+"").replace(/[^0-9.]/ig,"");

	if(browserType == "msie " && browserVersion < 9.0){//判断IE浏览器版本 低于IE9.0给予提示
		if(callback){
			callback();
		}
	}
}


/**
 * 获取浏览器信息
 * @returns
 */
function getBrowserInfo(){
	var browserInfo = null;
	var agent = navigator.userAgent.toLowerCase() ;
	var regStr_ie = /msie [\d.]+/gi ;
	var regStr_ff = /firefox\/[\d.]+/gi;
	var regStr_chrome = /chrome\/[\d.]+/gi ;
	var regStr_saf = /safari\/[\d.]+/gi ;
	//IE
	if(agent.indexOf("msie") > 0)
	{
		browserInfo=  agent.match(regStr_ie) ;
	}	
	//firefox
	if(agent.indexOf("firefox") > 0)
	{
		browserInfo =  agent.match(regStr_ff) ;
	}	
	//Chrome
	if(agent.indexOf("chrome") > 0)
	{
		browserInfo =  agent.match(regStr_chrome) ;
	}	
	//Safari
	if(agent.indexOf("safari") > 0 && agent.indexOf("chrome") < 0)
	{
		browserInfo =  agent.match(regStr_saf) ;
	}
	 
	return browserInfo;
}

/**
 * 检查是否移动端浏览器
 * @returns {Boolean}
 */
function checkMobile() {
    var pda_user_agent_list = new Array("2.0 MMP", "240320", "AvantGo", "BlackBerry", "Blazer",
        "Cellphone", "Danger", "DoCoMo", "Elaine/3.0", "EudoraWeb", "hiptop", "IEMobile", "KYOCERA/WX310K", "LG/U990",
        "MIDP-2.0", "MMEF20", "MOT-V", "NetFront", "Newt", "Nintendo Wii", "Nitro", "Nokia",
        "Opera Mini", "Opera Mobi",
        "Palm", "Playstation Portable", "portalmmm", "Proxinet", "ProxiNet",
        "SHARP-TQ-GX10", "Small", "SonyEricsson", "Symbian OS", "SymbianOS", "TS21i-10", "UP.Browser", "UP.Link",
        "Windows CE", "WinWAP", "Androi", "iPhone", "iPod", "iPad", "Windows Phone", "HTC");
    var pda_app_name_list = new Array("Microsoft Pocket Internet Explorer");

    var user_agent = navigator.userAgent.toString();
    for (var i = 0; i < pda_user_agent_list.length; i++) {
        if (user_agent.indexOf(pda_user_agent_list[i]) >= 0) {
            return true;
        }
    }
    var appName = navigator.appName.toString();
    for (var i = 0; i < pda_app_name_list.length; i++) {
        if (user_agent.indexOf(pda_app_name_list[i]) >= 0) {
            return true;
        }
    }

    return false;
}