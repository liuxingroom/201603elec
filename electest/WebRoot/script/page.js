function  gotopage(path,where){
		//设置当前页的值
       var page=document.Form2.pageNO;
       
       if(where=="next"){ 
    	   
          page.value=document.Form2.nextpageNO.value;
      
       }else if(where=="prev"){
     
          page.value=document.Form2.prevpageNO.value;
       }else if(where=="end"){
     
          page.value=document.Form2.sumPage.value;
       }else if(where=="start"){
          page.value=1;
       }else if(where=="go"){
         var theForm = document.Form2;   
         if(Trim(theForm.goPage.value)=="")
	     {
		     alert("请输入页数"); 
		     theForm.goPage.focus();   
		     return false;
	     }
	     if(!checkNumber(theForm.goPage.value))
	     {
		     alert("请输入正确页数(数字)"); 
		     theForm.goPage.focus();     
		     return false;
	     }
	     
	     var objgo=parseInt(theForm.goPage.value);
	     var objsum=parseInt(theForm.sumPage.value);
	     if(objgo<=0||objgo>objsum){
	         alert("不存在此页，请重新输入页数"); 
		     theForm.goPage.focus();     
		     return false; 
	     }
         
         page.value=theForm.goPage.value;                
      } 
     //将form2中的页数赋值到当前form1的当前页数（form1的隐藏域中）
      document.Form1.pageNO.value=document.Form2.pageNO.value;
      Pub.submitActionWithForm('Form2',path,'Form1');       
  }
  
  /**点击查询触发该事件*/
  function gotoquery(path){
	  /**设置当前页为1*/
      document.Form1.pageNO.value=1;
      Pub.submitActionWithForm('Form2',path,'Form1'); 
  }