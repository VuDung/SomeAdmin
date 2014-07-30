package com.some.admin.model;

public class Bound {
	private String uname;
	private String money;
	private String ctime;
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getCtime() {
		return ctime;
	}
	public void setCtime(String ctime) {
		this.ctime = ctime;
	}
	
	public boolean equals(Object obj){
		Bound item = (Bound)obj;
		if(this.uname.equalsIgnoreCase(item.getUname())){
			return true;
		}
		return false;
	}
}
