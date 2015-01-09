package org.woym.controller.manage;

public enum ColorEnum {

	YELLOW("#FFFF66"),
	
	SECOND("#eceef0");

	private String color;

	private ColorEnum(String color) {
		this.color = color;
	}

	public String getColor() {
		return color;
	}

}
