function pub(){}

pub.newXMLHttpRequest=function newXMLHttpRequest(){
	var xmlreq = false;
    if (window.XMLHttpRequest) {
        xmlreq = new XMLHttpRequest();
    }
    else 
        if (window.ActiveXObject) {
        
            try {
            
                xmlreq = new ActiveXObject("Msxml2.XMLHTTP");
            } 
            catch (e1) {
            
                try {
                
                    xmlreq = new ActiveXObject("Microsoft.XMLHTTP");
                } 
                catch (e2) {
                
                    alert(e2);
                }
            }
        }
    return xmlreq;
}

pub.getParameter=function(form1){
	var strDiv="";
	var objForm=document.forms[form1];
	if(!objForm){
		return strDiv;
	}
	
	var elt, sName, sValue;
	for(var fld=0;fld<objForm.length;fld++){
		//获取第一个元素
		elt=objForm.elements[fld];
		sName=elt.name+"";
		sValue=elt.value+"";
		//判断是否是最后一个元素
		if(fld==objForm.elements.length-1){
			strDiv=strDiv+sName+"="+sValue;
		}else{
			strDiv=strDiv+sName+"="+sValue+"&";
		}
	}
	return strDiv;
	
}

//pub.getReadyStateHandler=function(req,form1,handlerResponse){
//	 return function(){
//        /**
//         * req.readyState:用来监听客户端与服务器端的连接状态
//         * 0：表示此时客户端没有调用open()方法
//         * 1：表示客户端执行open方法，但是send方法没有执行
//         * 2：open方法执行，send方法也执行
//         * 3：服务器开始处理数据，并返回数据
//         * 4：返回数据成功，只有4这个状态，才能获取服务器端返回的结果
//         */
//        if (req.readyState == 4) {
//            /**
//             * req.status：表示java的状态码
//             * 404：路径错误
//             * 500：服务器异常
//             * 200：表示没有异常，正常访问数据，只有200这个状态的时候，才能获取服务器端返回的结果
//             */
//            if (req.status == 200) {
//                /**
//                 * req.responseText:获取服务器端返回的结果，返回的是文本的结果（字符串，json数据）
//                 * req.responseXML:获取服务器端返回的结果，返回的是XML数据的结果
//                 */
//                handlerResponse(req.responseText, form1);
//                
//            }
//            else {
//            
//                alert("HTTP error: " + req.status);
//                return false;
//            }
//        }
//    }
//}

pub.handlerResponse=function(data,form2){
	//获取表单form2的对象
	var ele=document.getElementById(form2);
	ele.innerHTML=data;
}



/**
 * form2   form2的表单名
 * action  请求连接
 * form1  form1表单名
 */
pub.submitActionWithForm=function(form2,action,form1){
	//获取XMLHttpRequest 对象
	var req=pub.newXMLHttpRequest();
	//req.onreadystatechange 表示事件处理函数  （相当于一个监听） ,用来监听客户端与服务端的连接状态
	//设置监听的第一种方式
	//req.onreadystatechange=pub.getReadyStateHandler(req,form2,pub.handlerResponse);
	
	//设置监听的第二种方式
	req.onreadystatechange=function(){
		 /**
         * req.readyState:用来监听客户端与服务器端的连接状态
      	 * 0：表示此时客户端没有调用open()方法
         * 1：表示客户端执行open方法，但是send方法没有执行         
         * 2：open方法执行，send方法也执行
         * 3：服务器开始处理数据，并返回数据
         * 4：返回数据成功，只有4这个状态，才能获取服务器端返回的结果
         */
        if (req.readyState == 4) {
            /**
             * req.status：表示java的状态码
             * 404：路径错误
             * 500：服务器异常
             * 200：表示没有异常，正常访问数据，只有200这个状态的时候，才能获取服务器端返回的结果
             */
            if (req.status == 200) {
                /**
                 * req.responseText:获取服务器端返回的结果，返回的是文本的结果（字符串，json数据）
                 * req.responseXML:获取服务器端返回的结果，返回的是XML数据的结果
                 */
                pub.handlerResponse(req.responseText, form2);
                
            }
           
        }
	}
	
	req.open("POST",action,true);
	req.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	//获取form1提交的参数
	var attr=pub.getParameter(form1);
	//向服务器发送请求
	req.send(attr);
	
}


