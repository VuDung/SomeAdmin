package com.some.admin.model;

public class Online {

	private String namePlayer;
	private String moneyPlayer;
	
	public String getNamePlayer() {
		return namePlayer;
	}
	public void setNamePlayer(String namePlayer) {
		this.namePlayer = namePlayer;
	}
	public String getMoneyPlayer() {
		return moneyPlayer;
	}
	public void setMoneyPlayer(String moneyPlayer) {
		this.moneyPlayer = moneyPlayer;
	}
	
	public boolean equals(Object obj){
		Online item = (Online)obj;
		if(this.namePlayer.equalsIgnoreCase(item.getNamePlayer())){
			return true;
		}
		return false;
	}
}
