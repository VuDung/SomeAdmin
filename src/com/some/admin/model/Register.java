package com.some.admin.model;

public class Register {

	private String userRegister;
	private String ctime;
	public String getUserRegister() {
		return userRegister;
	}
	public void setUserRegister(String userRegister) {
		this.userRegister = userRegister;
	}
	public String getCtime() {
		return ctime;
	}
	public void setCtime(String ctime) {
		this.ctime = ctime;
	}
	
	public boolean equals(Object obj){
		Register item = (Register)obj;
		if(this.userRegister.equalsIgnoreCase(item.getUserRegister())){
			return true;
		}
		return false;
	}
}
