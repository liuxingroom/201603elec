package com.xing.elec.utils;

import com.opensymphony.xwork2.ActionContext;

public class ValueUtils {
	/*将对象压入栈顶**/
	public static void putValueStack(Object object){
		ActionContext.getContext().getValueStack().push(object);
	}
}
