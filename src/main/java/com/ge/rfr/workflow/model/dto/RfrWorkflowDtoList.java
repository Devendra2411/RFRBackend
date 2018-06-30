package com.ge.rfr.workflow.model.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class RfrWorkflowDtoList{

	 	@Valid
	 	@NotNull
	    private List<RfrWorkflowDto> rfrWorkflowDtoList;

		public List<RfrWorkflowDto> getRfrWorkflowDtoList() {
			return rfrWorkflowDtoList;
		}

		public void setRfrWorkflowDtoList(List<RfrWorkflowDto> rfrWorkflowDtoList) {
			this.rfrWorkflowDtoList = rfrWorkflowDtoList;
		}

}
