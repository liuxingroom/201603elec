package com.xing.elec.web.form;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MenuForm implements Serializable{
	private String name;
	private String password;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
