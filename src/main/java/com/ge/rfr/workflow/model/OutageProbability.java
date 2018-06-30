package com.ge.rfr.workflow.model;

import com.ge.rfr.common.options.CommonOptionsEnum;

public enum OutageProbability implements CommonOptionsEnum{

	PLANNED("PLANNED"),
	UN_PLANNED("UN PLANNED");

	private final String text;
	
	OutageProbability(String text){
		this.text=text;
	}
	@Override
	public String getText() {
		return text;
	}
}
