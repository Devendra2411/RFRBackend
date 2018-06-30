package com.ge.rfr.workflow.model;

import com.ge.rfr.common.options.CommonOptionsEnum;

public enum TechnicalCode implements CommonOptionsEnum{

	
	AERO_GAS("Aero Gas"),
	COMBINEDCYCLE_STEAM("Combined-cycle Steam"),
	GENERATOR_GAS("Generator Gas"),
	GENERATOR_STEAM("Generator Steam"),
	HD_GAS("HD_Gas"),
	LARGE_STEAM("Large Steam"),
	MECH_DRIVE("Mech Drive"),
	MEDIUM_STEAM("Medium Steam"),
	NONGE_GENERATOR_GAS("Non-GE Generator Gas"),
	NONGE_GENERATOR_STEAM("Non-GE Generator Steam"),
	NONGE_HD_GAS("Non-GE HD Gas"),
	SMALL_STEAM("Small Steam");
	
	
	private final String text;
	
	TechnicalCode(String text){
		this.text=text;
	}
	
	@Override
    public String getText() {
        return text;
    }
	
}
