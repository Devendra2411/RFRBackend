package com.ge.rfr.workflow.model;

import com.ge.rfr.common.options.CommonOptionsEnum;

public enum Status implements CommonOptionsEnum{

	CANCELLED("CANCELLED"),
	CLOSED("CLOSED"),
	COMPLETED("COMPLETED"),
	EXECUTION("EXECUTION"),
	FORECASTED("FORECASTED"),
	PLANNING("PLANNING"),
	PROPOSAL("PROPOSAL");
	

	private final String text;
	
	Status(String text){
		this.text=text;
	}
	@Override
	public String getText() {
		return text;
	}

}
