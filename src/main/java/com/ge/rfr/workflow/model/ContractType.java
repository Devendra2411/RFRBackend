package com.ge.rfr.workflow.model;

import com.ge.rfr.common.options.CommonOptionsEnum;

public enum ContractType implements CommonOptionsEnum {

	CSA("CSA"),
	TX("TX"),
	MMP("MMP"),
	OM_CSA("O&M/CSA"),
	OM_MMP("O&M/MMP");

	private final String text;

	ContractType(String text) {
		this.text = text;
	}

	@Override
	public String getText() {
		return text;
	}
}
