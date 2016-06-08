
/**
 * cloud uc js framework
 * build by tc@hesong.net
 */

if(!ucf){
	var _resUrlPrefix = _resUrlPrefix;
	var _ctx;
	if(window.systemConfig){
		//静态资源URL前缀，需要在调用页面中定义该变量
		_resUrlPrefix = systemConfig.resUrlPrefix;
		_ctx = systemConfig.ctx;//request context path该变量
	}
	
	
	var ucf = {};
	ucf.urls = [];
	
	/**
	 * 数组添加indexOf增强函数
	 */
	if(!Array.indexOf){ 
	    Array.prototype.indexOf = function(obj){                
	        for(var i=0; i<this.length; i++){ 
	            if(this[i]==obj){ 
	                return i; 
	            } 
	        } 
	        return -1;
	    };
	}
	
	/**
	 * 为日期对象扩展格式化功能
	 * @param formatStr
	 * @returns
	 */
	Date.prototype.format = function(formatStr) {
		var date = this;
		/*
		 * 函数：填充0字符 参数：value-需要填充的字符串, length-总长度 返回：填充后的字符串
		 */
		var zeroize = function(value, length) {
			if (!length) {
				length = 2;
			}
			value = new String(value);
			for ( var i = 0, zeros = ''; i < (length - value.length); i++) {
				zeros += '0';
			}
			return zeros + value;
		};
		return formatStr
				.replace(
						/"[^"]*"|'[^']*'|\b(?:d{1,4}|M{1,4}|yy(?:yy)?|([hHmstT])\1?|[lLZ])\b/g,
						function($0) {
							switch ($0) {
							case 'd':
								return date.getDate();
							case 'dd':
								return zeroize(date.getDate());
							case 'ddd':
								return [ 'Sun', 'Mon', 'Tue', 'Wed', 'Thr', 'Fri',
										'Sat' ][date.getDay()];
							case 'dddd':
								return [ 'Sunday', 'Monday', 'Tuesday',
										'Wednesday', 'Thursday', 'Friday',
										'Saturday' ][date.getDay()];
							case 'M':
								return date.getMonth() + 1;
							case 'MM':
								return zeroize(date.getMonth() + 1);
							case 'MMM':
								return [ 'Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
										'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec' ][date
										.getMonth()];
							case 'MMMM':
								return [ 'January', 'February', 'March', 'April',
										'May', 'June', 'July', 'August',
										'September', 'October', 'November',
										'December' ][date.getMonth()];
							case 'yy':
								return new String(date.getFullYear()).substr(2);
							case 'yyyy':
								return date.getFullYear();
							case 'h':
								return date.getHours() % 12 || 12;
							case 'hh':
								return zeroize(date.getHours() % 12 || 12);
							case 'H':
								return date.getHours();
							case 'HH':
								return zeroize(date.getHours());
							case 'm':
								return date.getMinutes();
							case 'mm':
								return zeroize(date.getMinutes());
							case 's':
								return date.getSeconds();
							case 'ss':
								return zeroize(date.getSeconds());
							case 'l':
								return date.getMilliseconds();
							case 'll':
								return zeroize(date.getMilliseconds());
							case 'tt':
								return date.getHours() < 12 ? '上午' : '下午';
							case 'TT':
								return date.getHours() < 12 ? 'AM' : 'PM';
							}
						});
	};
	/**
	 * 判断指定的日期对象是否是今天
	 * @param dt
	 */
	Date.prototype.isToday = function(){
		var today = new Date();
		var d1 = today;
		var d2 = this;
		var result = d1.getFullYear() == d2.getFullYear()
        && d1.getMonth() == d2.getMonth()
        && d1.getDate() == d2.getDate();
		return result;
	};
	/**
	 * 判断指定的日期对象是否是本周
	 * @param dt
	 */
	Date.prototype.isThisWeek = function(){
		var today = new Date();
		var d1 = today;
		var d2 = this;
		var year = today.getYear();
		year += (year<2000)?1900:0;
		var date = today.getDate(); //当前日 
		var month = today.getMonth(); //当前月
		var dayOfWeek = today.getDay(); //今天本周的第几天
		var weekStart = new Date(year, month, date-dayOfWeek+1).getDate();
		var weekEnd = new Date(year, month, date +(7-dayOfWeek)).getDate();
		var result=false;
		if (weekStart > weekEnd) {
			if (d2.getDate() >= weekStart) {
				result = d1.getFullYear() == d2.getFullYear() && (d2.getMonth()+1==d1.getMonth() || d2.getMonth()==d1.getMonth());
			} else if(d2.getDate() <= weekEnd) {
				result = d1.getFullYear() == d2.getFullYear() && (d2.getMonth()-1==d1.getMonth() || d2.getMonth()==d1.getMonth());
			} else {
				result = false;
			}
		} else {
			result = d1.getFullYear() == d2.getFullYear() && d1.getMonth() == d2.getMonth() && (weekStart <= d2.getDate() && d2.getDate() <= weekEnd);
		}
		return result;
	};
	
	/**
	 * 判断指定的日期对象是否是本月
	 * @param dt
	 */
	Date.prototype.isThisMonth = function(){
		var today = new Date();
		var d1 = today;
		var d2 = this;
		var result = d1.getFullYear() == d2.getFullYear() && d1.getMonth() == d2.getMonth();
		return result;
	};
	
	ucf.$ = function (id) {
		var ret = id;
		if("string" == typeof id){
			ret = document.getElementById(id);
			if(!ret){
				ret = document.getElementsByTagName(id);
				if(ret.length>0){
					ret = ret[0];
				}
			}
		}
		return  ret;
	};
	
	/**
	 * 获取对象的属性值，支持深层次的属性获取
	 * 为了解决eval出现的代码混淆找不到变量时使用
	 * 局部变量eval时，由于代码混淆导致变量名发生变化，但是eval(内部出现的变量名不会一起混淆所以会出现找不到变量的问题
	 */
	ucf.$pv = function(obj,pname){
		var ps = pname.split(".");
		var tmpValue = obj;
		for(var i = 0;i<ps.length;i++){
			tmpValue = tmpValue[ps[i]];
		}
		return tmpValue;
	};
	
	/**
	 * 等待指定的变量生效以后，执行callback函数
	 * whenAvailable('dojo',function(){dojo.ready(function());});
	 */
	ucf.whenAvailable = function(name, callback) {
	    var interval = 100; // ms
	    window.setTimeout(function() {
	        if (window[name]) {
	            callback(window[name]);
	        } else {
	            window.setTimeout(arguments.callee, interval);
	        }
	    }, interval);
	};
	
	/**
	 * 加载脚本对象
	 * @identify 脚本标识，用于判断对应脚本是否已经加载过的依据,比如dojo框架的identify为dojo
	 * @url 脚本加载URL
	 * @param 参数，用户添加到<script>标签中的额外属性,比如dojo中的data-dojo-config
	 * @cb 脚本加载成功后调用
	 */
	ucf.loadScript = function(identify,url,param,cb){
		//如果标识的对象存在或者该脚本已经被加载过则忽略该加载过程
		var idObj = null;
		try{idObj = eval(identify);}catch(ex){}
		
		if(idObj!=null || this.urls.indexOf(url)>=0){
			return -1;
		}
		var head = document.getElementsByTagName("head").item(0);  
		var script = document.createElement ("script");
		script.src = url; 
		for ( var item in param) {
			var value = eval("param['"+item+"']");
			script.setAttribute(item, value);
		}
		if(cb){
			if(script.addEventListener){
				script.addEventListener("load", cb, false);
			}else if(script.attachEvent){
				//兼容IE,ie没有load事件
				script.attachEvent("onreadystatechange", function(){
					var target = window.event.srcElement;  
					if(target.readyState == "loaded"){
						cb.call(target);
					}
				});
			}
		};
		head.appendChild (script);
		ucf.urls.push(url);
		return script;
	};
	
	
	
	/**
	 * 加载样式表文件
	 * @url stylesheet文件url
	 */
	ucf.loadStyle = function(url){
		 var head = document.getElementsByTagName('head')[0];
         link = document.createElement('link');
         link.href = url;
         link.rel = 'stylesheet';
         head.appendChild( link );
         return link;
	};
	

	
	
	/**
	 * 调用指定的函数并制定动态参数数组
	 */
	ucf.callFn = function(fn,args){
		var executeStr="fn.call(this,@)";
		for(var i = 0;i<args.length;i++){
	        executeStr = executeStr.replace("@","args["+i+"],@");  
	    }  
		executeStr=executeStr.replace(",@","");
		eval(executeStr);  
	};
	
	

	/**
	 * 创建xmlhttprequest对象
	 */
	ucf.createRequest = function(){
	   if(typeof(XMLHttpRequest)!='undefined')
	        return new XMLHttpRequest();  
	    var axO=['Msxml2.XMLHTTP.6.0', 'Msxml2.XMLHTTP.4.0',  
	        'Msxml2.XMLHTTP.3.0', 'Msxml2.XMLHTTP', 'Microsoft.XMLHTTP'], i;  
	    for(i=0;i<axO.length;i++)  
	        try{  
	            return new ActiveXObject(axO[i]);  
	        }catch(e){}  
	    return null;  
	};

	/**
	 * 编码url路径中的非法字符
	 */
	ucf.eurl = function(urlpart){
		return encodeURIComponent(urlpart);
	};
	/**
	 * get请求
	 */
	ucf.doGet = function(url,param,callBack){
		var request = this.createRequest();
		var asyn = true;	//异步?
		request.open("get", url, asyn);
		request.onreadystatechange = function(){
			 if (this.readyState == 4) {//服务器完成
                 if (this.status == 200) {//如果装态码为200则是成功
                	 callBack(this.responseText);
                 }
             }
		};  
		request.send(param);
	};
	ucf.doPost = function(url,param,callBack){
		
	};
	
}



Date.prototype.format = function(formatStr) {
	var date = this;
	/*
	 * 函数：填充0字符 参数：value-需要填充的字符串, length-总长度 返回：填充后的字符串
	 */
	var zeroize = function(value, length) {
		if (!length) {
			length = 2;
		}
		value = new String(value);
		for ( var i = 0, zeros = ''; i < (length - value.length); i++) {
			zeros += '0';
		}
		return zeros + value;
	};
	return formatStr
			.replace(
					/"[^"]*"|'[^']*'|\b(?:d{1,4}|M{1,4}|yy(?:yy)?|([hHmstT])\1?|[lLZ])\b/g,
					function($0) {
						switch ($0) {
						case 'd':
							return date.getDate();
						case 'dd':
							return zeroize(date.getDate());
						case 'ddd':
							return [ 'Sun', 'Mon', 'Tue', 'Wed', 'Thr', 'Fri',
									'Sat' ][date.getDay()];
						case 'dddd':
							return [ 'Sunday', 'Monday', 'Tuesday',
									'Wednesday', 'Thursday', 'Friday',
									'Saturday' ][date.getDay()];
						case 'M':
							return date.getMonth() + 1;
						case 'MM':
							return zeroize(date.getMonth() + 1);
						case 'MMM':
							return [ 'Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
									'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec' ][date
									.getMonth()];
						case 'MMMM':
							return [ 'January', 'February', 'March', 'April',
									'May', 'June', 'July', 'August',
									'September', 'October', 'November',
									'December' ][date.getMonth()];
						case 'yy':
							return new String(date.getFullYear()).substr(2);
						case 'yyyy':
							return date.getFullYear();
						case 'h':
							return date.getHours() % 12 || 12;
						case 'hh':
							return zeroize(date.getHours() % 12 || 12);
						case 'H':
							return date.getHours();
						case 'HH':
							return zeroize(date.getHours());
						case 'm':
							return date.getMinutes();
						case 'mm':
							return zeroize(date.getMinutes());
						case 's':
							return date.getSeconds();
						case 'ss':
							return zeroize(date.getSeconds());
						case 'l':
							return date.getMilliseconds();
						case 'll':
							return zeroize(date.getMilliseconds());
						case 'tt':
							return date.getHours() < 12 ? '上午' : '下午';
						case 'TT':
							return date.getHours() < 12 ? 'AM' : 'PM';
						}
					});
};


/**
 * 格式化日期，如果是今天就格式化为  上午 10:20
 * 如果不是今天，格式化为全日期格式
 */
Date.prototype.formatMagic = function(){ 
	var today = todayDt;
	var result = "";
	var dt = this;
	if(dt.getFullYear() == today.getFullYear() && dt.getMonth() == today.getMonth() && dt.getDate() == today.getDate()){
		result = dt.format("tt hh:mm");
	}else{
		result = dt.format("yyyy/MM/dd HH:mm");
	}
	return result;
};





//===================================================================
//Author: Matt Kruse <matt@mattkruse.com>
//WWW: http://www.mattkruse.com/
//
//NOTICE: You may use this code for any purpose, commercial or
//private, without any further permission from the author. You may
//remove this notice from your final code if you wish, however it is
//appreciated by the author if at least my comm site address is kept.
//
//You may *NOT* re-distribute this code in any way except through its
//use. That means, you can include it in your product, or your comm
//site, or any other form where the code is actually being used. You
//may not put the plain javascript up on your site for download or
//include it in your javascript libraries for download. 
//If you wish to share this code with others, please just point them
//to the URL instead.
//Please DO NOT link directly to my .js files from your site. Copy
//the files to your server and use them there. Thank you.
//===================================================================

//HISTORY
//------------------------------------------------------------------
//May 17, 2003: Fixed bug in parseDate() for dates <1970
//March 11, 2003: Added parseDate() function
//March 11, 2003: Added "NNN" formatting option. Doesn't match up
//              perfectly with SimpleDateFormat formats, but 
//              backwards-compatability was required.

//------------------------------------------------------------------
//These functions use the same 'format' strings as the 
//java.text.SimpleDateFormat class, with minor exceptions.
//The format string consists of the following abbreviations:
//
//Field        | Full Form          | Short Form
//-------------+--------------------+-----------------------
//Year         | yyyy (4 digits)    | yy (2 digits), y (2 or 4 digits)
//Month        | MMM (name or abbr.)| MM (2 digits), M (1 or 2 digits)
//           | NNN (abbr.)        |
//Day of Month | dd (2 digits)      | d (1 or 2 digits)
//Day of Week  | EE (name)          | E (abbr)
//Hour (1-12)  | hh (2 digits)      | h (1 or 2 digits)
//Hour (0-23)  | HH (2 digits)      | H (1 or 2 digits)
//Hour (0-11)  | KK (2 digits)      | K (1 or 2 digits)
//Hour (1-24)  | kk (2 digits)      | k (1 or 2 digits)
//Minute       | mm (2 digits)      | m (1 or 2 digits)
//Second       | ss (2 digits)      | s (1 or 2 digits)
//AM/PM        | a                  |
//
//NOTE THE DIFFERENCE BETWEEN MM and mm! Month=MM, not mm!
//Examples:
//"MMM d, y" matches: January 01, 2000
//                   Dec 1, 1900
//                   Nov 20, 00
//"M/d/yy"   matches: 01/20/00
//                   9/2/00
//"MMM dd, yyyy hh:mm:ssa" matches: "January 01, 2000 12:30:45AM"
//------------------------------------------------------------------

var MONTH_NAMES=new Array('January','February','March','April','May','June','July','August','September','October','November','December','Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec');
var DAY_NAMES=new Array('Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday','Sun','Mon','Tue','Wed','Thu','Fri','Sat');
function LZ(x) {return(x<0||x>9?"":"0")+x;};
//------------------------------------------------------------------
//isDate ( date_string, format_string )
//Returns true if date string matches format of format string and
//is a valid date. Else returns false.
//It is recommended that you trim whitespace around the value before
//passing it to this function, as whitespace is NOT ignored!
//------------------------------------------------------------------
function isDate(val,format) {
	
	var date=getDateFromFormat(val,format);
	if (date==0) { return false; }
	return true;
	}

//-------------------------------------------------------------------
//compareDates(date1,date1format,date2,date2format)
//Compare two date strings to see which is greater.
//Returns:
//1 if date1 is greater than date2
//0 if date2 is greater than date1 of if they are the same
//-1 if either of the dates is in an invalid format
//-------------------------------------------------------------------
function compareDates(date1,dateformat1,date2,dateformat2) {
	var d1=getDateFromFormat(date1,dateformat1);
	var d2=getDateFromFormat(date2,dateformat2);
	if (d1==0 || d2==0) {
		return -1;
		}
	else if (d1 > d2) {
		return 1;
		}
	return 0;
	}

//------------------------------------------------------------------
//formatDate (date_object, format)
//Returns a date in the output format specified.
//The format string uses the same abbreviations as in getDateFromFormat()
//------------------------------------------------------------------
function formatDate(date,format) {
	format=format+"";
	var result="";
	var i_format=0;
	var c="";
	var token="";
	var y=date.getYear()+"";
	var M=date.getMonth()+1;
	var d=date.getDate();
	var E=date.getDay();
	var H=date.getHours();
	var m=date.getMinutes();
	var s=date.getSeconds();
	var yyyy,yy,MMM,MM,dd,hh,h,mm,ss,ampm,HH,H,KK,K,kk,k;
	// Convert real date parts into formatted versions
	var value=new Object();
	if (y.length < 4) {y=""+(y-0+1900);}
	value["y"]=""+y;
	value["yyyy"]=y;
	value["yy"]=y.substring(2,4);
	value["M"]=M;
	value["MM"]=LZ(M);
	value["MMM"]=MONTH_NAMES[M-1];
	value["NNN"]=MONTH_NAMES[M+11];
	value["d"]=d;
	value["dd"]=LZ(d);
	value["E"]=DAY_NAMES[E+7];
	value["EE"]=DAY_NAMES[E];
	value["H"]=H;
	value["HH"]=LZ(H);
	//if (H==0){value["h"]=12;}
	//else if (H>12){value["h"]=H-12;}
	//else {value["h"]=H;}
	value["h"]=H;
	value["hh"]=LZ(value["h"]);
	if (H>11){value["K"]=H-12;} else {value["K"]=H;}
	value["k"]=H+1;
	value["KK"]=LZ(value["K"]);
	value["kk"]=LZ(value["k"]);
	if (H > 11) { value["a"]="PM"; }
	else { value["a"]="AM"; }
	value["m"]=m;
	value["mm"]=LZ(m);
	value["s"]=s;
	value["ss"]=LZ(s);
	while (i_format < format.length) {
		c=format.charAt(i_format);
		token="";
		while ((format.charAt(i_format)==c) && (i_format < format.length)) {
			token += format.charAt(i_format++);
			}
		if (value[token] != null) { result=result + value[token]; }
		else { result=result + token; }
		}
	return result;
	}
	
//------------------------------------------------------------------
//Utility functions for parsing in getDateFromFormat()
//------------------------------------------------------------------
function _isInteger(val) {
	var digits="1234567890";
	for (var i=0; i < val.length; i++) {
		if (digits.indexOf(val.charAt(i))==-1) { return false; }
		}
	return true;
	}
function _getInt(str,i,minlength,maxlength) {
	for (var x=maxlength; x>=minlength; x--) {
		var token=str.substring(i,i+x);
		if (token.length < minlength) { return null; }
		if (_isInteger(token)) { return token; }
		}
	return null;
	}
	
//------------------------------------------------------------------
//getDateFromFormat( date_string , format_string )
//
//This function takes a date string and a format string. It matches
//If the date string matches the format string, it returns the 
//getTime() of the date. If it does not match, it returns 0.
//------------------------------------------------------------------
function getDateFromFormat(val,format) {
	val=val+"";
	format=format+"";
	var i_val=0;
	var i_format=0;
	var c="";
	var token="";
	var token2="";
	var x,y;
	var now=new Date();
	var year=now.getYear();
	var month=now.getMonth()+1;
	var date=1;
	var hh=now.getHours();
	var mm=now.getMinutes();
	var ss=now.getSeconds();
	var ampm="";
	
	while (i_format < format.length) {
		// Get next token from format string
		c=format.charAt(i_format);
		token="";
		while ((format.charAt(i_format)==c) && (i_format < format.length)) {
			token += format.charAt(i_format++);
			}
		// Extract contents of value based on format token
		if (token=="yyyy" || token=="yy" || token=="y") {
			if (token=="yyyy") { x=4;y=4; }
			if (token=="yy")   { x=2;y=2; }
			if (token=="y")    { x=2;y=4; }
			year=_getInt(val,i_val,x,y);
			if (year==null) { return 0; }
			i_val += year.length;
			if (year.length==2) {
				if (year > 70) { year=1900+(year-0); }
				else { year=2000+(year-0); }
				}
			}
		else if (token=="MMM"||token=="NNN"){
			month=0;
			for (var i=0; i<MONTH_NAMES.length; i++) {
				var month_name=MONTH_NAMES[i];
				if (val.substring(i_val,i_val+month_name.length).toLowerCase()==month_name.toLowerCase()) {
					if (token=="MMM"||(token=="NNN"&&i>11)) {
						month=i+1;
						if (month>12) { month -= 12; }
						i_val += month_name.length;
						break;
						}
					}
				}
			if ((month < 1)||(month>12)){return 0;}
			}
		else if (token=="EE"||token=="E"){
			for (var i=0; i<DAY_NAMES.length; i++) {
				var day_name=DAY_NAMES[i];
				if (val.substring(i_val,i_val+day_name.length).toLowerCase()==day_name.toLowerCase()) {
					i_val += day_name.length;
					break;
					}
				}
			}
		else if (token=="MM"||token=="M") {
			month=_getInt(val,i_val,token.length,2);
			if(month==null||(month<1)||(month>12)){return 0;}
			i_val+=month.length;}
		else if (token=="dd"||token=="d") {
			date=_getInt(val,i_val,token.length,2);
			if(date==null||(date<1)||(date>31)){return 0;}
			i_val+=date.length;}
		else if (token=="hh"||token=="h") {
			hh=_getInt(val,i_val,token.length,2);
			if(hh==null||(hh<1)||(hh>12)){return 0;}
			i_val+=hh.length;}
		else if (token=="HH"||token=="H") {
			hh=_getInt(val,i_val,token.length,2);
			if(hh==null||(hh<0)||(hh>23)){return 0;}
			i_val+=hh.length;}
		else if (token=="KK"||token=="K") {
			hh=_getInt(val,i_val,token.length,2);
			if(hh==null||(hh<0)||(hh>11)){return 0;}
			i_val+=hh.length;}
		else if (token=="kk"||token=="k") {
			hh=_getInt(val,i_val,token.length,2);
			if(hh==null||(hh<1)||(hh>24)){return 0;}
			i_val+=hh.length;hh--;}
		else if (token=="mm"||token=="m") {
			mm=_getInt(val,i_val,token.length,2);
			if(mm==null||(mm<0)||(mm>59)){return 0;}
			i_val+=mm.length;}
		else if (token=="ss"||token=="s") {
			ss=_getInt(val,i_val,token.length,2);
			if(ss==null||(ss<0)||(ss>59)){return 0;}
			i_val+=ss.length;}
		else if (token=="a") {
			if (val.substring(i_val,i_val+2).toLowerCase()=="am") {ampm="AM";}
			else if (val.substring(i_val,i_val+2).toLowerCase()=="pm") {ampm="PM";}
			else {return 0;}
			i_val+=2;}
		else {
			if (val.substring(i_val,i_val+token.length)!=token) {return 0;}
			else {i_val+=token.length;}
			}
		}
	// If there are any trailing characters left in the value, it doesn't match
	if (i_val != val.length) { return 0; }
	// Is date valid for month?
	if (month==2) {
		// Check for leap year
		if ( ( (year%4==0)&&(year%100 != 0) ) || (year%400==0) ) { // leap year
			if (date > 29){ return 0; }
			}
		else { if (date > 28) { return 0; } }
		}
	if ((month==4)||(month==6)||(month==9)||(month==11)) {
		if (date > 30) { return 0; }
		}
	// Correct hours value
	if (hh<12 && ampm=="PM") { hh=hh-0+12; }
	else if (hh>11 && ampm=="AM") { hh-=12; }
	var newdate=new Date(year,month-1,date,hh,mm,ss);
	return newdate.getTime();
	}

//------------------------------------------------------------------
//parseDate( date_string [, prefer_euro_format] )
//
//This function takes a date string and tries to match it to a
//number of possible date formats to get the value. It will try to
//match against the following international formats, in this order:
//y-M-d   MMM d, y   MMM d,y   y-MMM-d   d-MMM-y  MMM d
//M/d/y   M-d-y      M.d.y     MMM-d     M/d      M-d
//d/M/y   d-M-y      d.M.y     d-MMM     d/M      d-M
//A second argument may be passed to instruct the method to search
//for formats like d/M/y (european format) before M/d/y (American).
//Returns a Date object or null if no patterns match.
//------------------------------------------------------------------
function parseDate(val) {
	var preferEuro=(arguments.length==2)?arguments[1]:false;
	generalFormats=new Array('y-M-d','MMM d, y','MMM d,y','y-MMM-d','d-MMM-y','MMM d');
	monthFirst=new Array('M/d/y','M-d-y','M.d.y','MMM-d','M/d','M-d');
	dateFirst =new Array('d/M/y','d-M-y','d.M.y','d-MMM','d/M','d-M');
	var checkList=new Array('generalFormats',preferEuro?'dateFirst':'monthFirst',preferEuro?'monthFirst':'dateFirst');
	var d=null;
	for (var i=0; i<checkList.length; i++) {
		var l=window[checkList[i]];
		for (var j=0; j<l.length; j++) {
			d=getDateFromFormat(val,l[j]);
			if (d!=0) { return new Date(d); }
			}
		}
	return null;
	}

/**
* 解析日期
*/
parseDateCHS = function(dateString) {
	// /<summary>
	// /解析常用的中文日期并返回日期对象。
	// /</summary>
	// /<param name="dateString" type="string">
	// /日期字符串。包含的格式有："xxxx(xx)-xx-xx xx:xx:xx","xxxx(xx).xx.xx xx:xx:xx",
	// /"xxxx(xx)年xx月xx日 xx时xx分xx秒"
	// /</param>
	var regExp1 = /^\d{4}-\d{1,2}-\d{1,2}( \d{1,2}:\d{1,2}:\d{1,2})?$/;
	var regExp2 = /^\d{4}\.\d{1,2}\.\d{1,2}( \d{1,2}:\d{1,2}:\d{1,2})?$/;
	var regExp3 = /^\d{4}年\d{1,2}月\d{1,2}日( \d{1,2}时\d{1,2}分\d{1,2}秒)?$/;
	if (regExp1.test(dateString)) {
	} else if (regExp2.test(dateString)) {
		dateString = dateString.replace(/\./g, "-");
	} else if (regExp3.test(dateString)) {
		dateString = dateString.replace("年", "-").replace("月", "-").replace(
				"日", "").replace("时", ":").replace("分", ":").replace("秒", "");
	} else {
		throw "传给Date.parseCHS的参数值的格式不正确。请传递一个有效的日期格式字符串作为参数。";
	}
	var date_time = dateString.split(" ");
	var date_part = date_time[0].split("-");
	var time_part = (date_time.length > 1 ? date_time[1].split(":") : "");
	if (time_part == "") {
		return new Date(date_part[0], date_part[1] - 1, date_part[2]);
	} else {
		return new Date(date_part[0], date_part[1] - 1, date_part[2],
				time_part[0], time_part[1], time_part[2]);
	}
};


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

/**
 * 获得meta中的token值
 * @returns
 */
function getToken() {
	var o=document.getElementsByTagName("meta");   
	for(var i=0;i<o.length;i++) {
		if (o[i].name == 'token') {
			return o[i].content; 
		}
	}   
}

/**
 * 基于bootstrap3的wait组件
 */

var wait;
wait = wait || (function ($) {
    var pleaseWaitDiv = $('<div class="modal fade" id="pleaseWaitDialog"><div class="modal-dialog"><div class="modal-content"><div class="modal-header"><h4 class="modal-title msg-content">亲!!请稍等,马上就好了!</h4></div><div class="modal-body"><div class="progress"><div class="progress-bar progress-bar-striped active"  role="progressbar" aria-valuenow="100" aria-valuemin="0" aria-valuemax="100" style="width: 100%"></div></div></div></div></div></div>');
    return {
        show: function(msg) {
        	msg = msg?msg:"亲!!请稍等,马上就好了!";
        	pleaseWaitDiv.find(".msg-content").text(msg)
            pleaseWaitDiv.modal('show');
        },
        hide: function () {
            pleaseWaitDiv.modal('hide');
        },
    };
})(window.jQuery);

var msgbox;
msgbox = msgbox || (function () {
    var msgbox = $('<div class="modal fade" id="msgBoxDiv"><div class="modal-dialog modal-sm"><div class="modal-content"><div class="modal-header"><button type="button" class="close"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button><h4 class="modal-title ">提示！</h4></div><div class="modal-body msg-content"></div><div class="modal-footer"><button type="button" class="btn btn-default">关闭</button></div></div></div></div>');
    return {
        show: function(msg) {
        	msg = msg?msg:"操作成功!";
        	msgbox.find(".msg-content").text(msg)
        	msgbox.modal({});
        	msgbox.find("button").click(function(){
        		msgbox.modal('hide');
        	})
        },
        hide: function () {
        	msgbox.modal('hide');
        },
    };
})();

//消息提示
var msgtip = function(tipType,title,content,callback){
	$.messager.model = { 
	        ok:{ text: "确认", classed: 'btn-success' },
	        cancel: { text: "取消", classed: 'btn-danger'}
	};
	if(tipType == 0){ //0-代表alert消息    1-代表confirm消息
		$.messager.alert(title, content);	
	}else{
		$.messager.confirm(title, content, function(){ 
			callback();
	    });	
	}
};



/*
 * 添加一个随机生成uuid的函数
 * */
(function() {   
	  // Private array of chars to use   
	  var CHARS = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('');   
	    
	  Math.uuid = function (len, radix) {   
	    var chars = CHARS, uuid = [], i;   
	    radix = radix || chars.length;   
	    
	    if (len) {   
	      // Compact form   
	      for (i = 0; i < len; i++) uuid[i] = chars[0 | Math.random()*radix];   
	    } else {   
	      // rfc4122, version 4 form   
	      var r;   
	    
	      // rfc4122 requires these characters   
	      uuid[8] = uuid[13] = uuid[18] = uuid[23] = '-';   
	      uuid[14] = '4';   
	    
	      // Fill in random data.  At i==19 set the high bits of clock sequence as   
	      // per rfc4122, sec. 4.1.5   
	      for (i = 0; i < 36; i++) {   
	        if (!uuid[i]) {   
	          r = 0 | Math.random()*16;   
	          uuid[i] = chars[(i == 19) ? (r & 0x3) | 0x8 : r];   
	        }   
	      }   
	    }   
	    
	    return uuid.join('');   
	  };   
	    
	  // A more performant, but slightly bulkier, RFC4122v4 solution.  We boost performance   
	  // by minimizing calls to random()   
	  Math.uuidFast = function(splitchar) {   
		var splitchar = ((splitchar || splitchar=='') ?splitchar:'-');
	    var chars = CHARS, uuid = new Array(36), rnd=0, r;   
	    for (var i = 0; i < 36; i++) {   
	      if (i==8 || i==13 ||  i==18 || i==23) {   
	        uuid[i] = splitchar;   
	      } else if (i==14) {   
	        uuid[i] = '4';   
	      } else {   
	        if (rnd <= 0x02) rnd = 0x2000000 + (Math.random()*0x1000000)|0;   
	        r = rnd & 0xf;   
	        rnd = rnd >> 4;   
	        uuid[i] = chars[(i == 19) ? (r & 0x3) | 0x8 : r];   
	      }   
	    }   
	    return uuid.join('');   
	  };   
	    
	  // A more compact, but less performant, RFC4122v4 solution:   
	  Math.uuidCompact = function() {   
	    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {   
	      var r = Math.random()*16|0, v = c == 'x' ? r : (r&0x3|0x8);   
	      return v.toString(16);   
	    });   
	  };   
	})();   




/**
 * 基于bootstrap3的wait组件
 */

//
//解决IE中console.info报错的问题
if (!window.console) console = {log: function() {}};

var wait;
wait = wait || (function ($) {
    var pleaseWaitDiv = $('<div class="modal fade" id="pleaseWaitDialog"><div class="modal-dialog"><div class="modal-content"><div class="modal-header"><h4 class="modal-title msg-content">亲!!请稍等,马上就好了!</h4></div><div class="modal-body"><div class="progress"><div class="progress-bar progress-bar-striped active"  role="progressbar" aria-valuenow="100" aria-valuemin="0" aria-valuemax="100" style="width: 100%"></div></div></div></div></div></div>');
    return {
        show: function(msg) {
        	msg = msg?msg:"亲!!请稍等,马上就好了!";
        	pleaseWaitDiv.find(".msg-content").text(msg);
            pleaseWaitDiv.modal({backdrop:'static'});
        },
        hide: function () {
            pleaseWaitDiv.modal('hide');
        }
    };
})(window.jQuery);

var msgbox;
msgbox = msgbox || (function ($) {
    var msgbox = $('<div class="modal fade" id="msgBoxDiv"><div class="modal-dialog modal-sm"><div class="modal-content"><div class="modal-header"><button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button><h4 class="modal-title ">提示！</h4></div><div class="modal-body msg-content"></div><div class="modal-footer"><button type="button" class="btn btn-default">关闭</button></div></div></div></div>');
    msgbox.find("button").click(function(){
    	msgbox.modal('hide');
    })
    return {
        show: function(msg) {
        	msg = msg?msg:"操作成功!";
        	msgbox.find(".msg-content").text(msg);
        	msgbox.modal({});
        	
        }
    };
})(window.jQuery);




Date.prototype.format = function(formatStr) {
	var date = this;
	/*
	 * 函数：填充0字符 参数：value-需要填充的字符串, length-总长度 返回：填充后的字符串
	 */
	var zeroize = function(value, length) {
		if (!length) {
			length = 2;
		}
		value = new String(value);
		for ( var i = 0, zeros = ''; i < (length - value.length); i++) {
			zeros += '0';
		}
		return zeros + value;
	};
	return formatStr
			.replace(
					/"[^"]*"|'[^']*'|\b(?:d{1,4}|M{1,4}|yy(?:yy)?|([hHmstT])\1?|[lLZ])\b/g,
					function($0) {
						switch ($0) {
						case 'd':
							return date.getDate();
						case 'dd':
							return zeroize(date.getDate());
						case 'ddd':
							return [ 'Sun', 'Mon', 'Tue', 'Wed', 'Thr', 'Fri',
									'Sat' ][date.getDay()];
						case 'dddd':
							return [ 'Sunday', 'Monday', 'Tuesday',
									'Wednesday', 'Thursday', 'Friday',
									'Saturday' ][date.getDay()];
						case 'M':
							return date.getMonth() + 1;
						case 'MM':
							return zeroize(date.getMonth() + 1);
						case 'MMM':
							return [ 'Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
									'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec' ][date
									.getMonth()];
						case 'MMMM':
							return [ 'January', 'February', 'March', 'April',
									'May', 'June', 'July', 'August',
									'September', 'October', 'November',
									'December' ][date.getMonth()];
						case 'yy':
							return new String(date.getFullYear()).substr(2);
						case 'yyyy':
							return date.getFullYear();
						case 'h':
							return date.getHours() % 12 || 12;
						case 'hh':
							return zeroize(date.getHours() % 12 || 12);
						case 'H':
							return date.getHours();
						case 'HH':
							return zeroize(date.getHours());
						case 'm':
							return date.getMinutes();
						case 'mm':
							return zeroize(date.getMinutes());
						case 's':
							return date.getSeconds();
						case 'ss':
							return zeroize(date.getSeconds());
						case 'l':
							return date.getMilliseconds();
						case 'll':
							return zeroize(date.getMilliseconds());
						case 'tt':
							return date.getHours() < 12 ? '上午' : '下午';
						case 'TT':
							return date.getHours() < 12 ? 'AM' : 'PM';
						}
					});
};


/**
 * 格式化日期，如果是今天就格式化为  上午 10:20
 * 如果不是今天，格式化为全日期格式
 */
Date.prototype.formatMagic = function(){ 
	var today = todayDt;
	var result = "";
	var dt = this;
	if(dt.getFullYear() == today.getFullYear() && dt.getMonth() == today.getMonth() && dt.getDate() == today.getDate()){
		result = dt.format("tt hh:mm");
	}else{
		result = dt.format("yyyy/MM/dd HH:mm");
	}
	return result;
};

/**
 * 重载JQUERY的on方法，对于click事件进行判断。
 * 如果是PC端使用click
 * 如果是移动端使用touchstart
 */
;(function(){
    var isTouch = ('ontouchstart' in document.documentElement) ? 'touchstart' : 'click', _on = $.fn.on;
    $.fn.on = function(){
    arguments[0] = (arguments[0] === 'click') ? isTouch: arguments[0];
    return _on.apply(this, arguments);
    };
})();


/**
 * 删除cookie
 */
function delCookie(name)
{
    var exp = new Date();
    exp.setTime(exp.getTime() - 1);
    var cval=getCookie(name);
    if(cval!=null) document.cookie= name + "="+cval+";expires="+exp.toGMTString();
}

/**
 * 获取cookie
 */
function getCookie(name)
{
    var arr = document.cookie.match(new RegExp("(^| )"+name+"=([^;]*)(;|$)"));
     if(arr != null) return unescape(arr[2]); return null;

}

/**
 * 设置cookie
 */
function setCookie(name, value, days) {
	days = days || 0;
	var expires = "";
	if (days != 0 ) {
		var date = new Date();
		date.setTime(date.getTime()+(days*24*60*60*1000));
		expires = "; expires="+date.toGMTString();
	}
	document.cookie = name+"="+value+expires+"; path=/";
}