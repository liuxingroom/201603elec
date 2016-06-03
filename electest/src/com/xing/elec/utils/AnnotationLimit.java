package com.xing.elec.utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *自定义一个注释 
 */
 @Retention(RetentionPolicy.RUNTIME)
public @interface AnnotationLimit {
	String mid();//权限的code
	String pid();//父级权限的code
}
