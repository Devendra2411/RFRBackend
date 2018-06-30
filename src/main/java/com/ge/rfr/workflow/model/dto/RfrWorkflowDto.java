package com.ge.rfr.workflow.model.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.ge.rfr.auth.SsoUser;
import com.ge.rfr.common.model.ChangeTracking;
import com.ge.rfr.common.model.User;
import com.ge.rfr.workflow.model.RfrWorkflow;

import io.swagger.annotations.ApiModelProperty;

public class RfrWorkflowDto {

    @ApiModelProperty(hidden = true)
    private int workflowId;
    
    @NotNull
    @ApiModelProperty(notes = "Train ID")
    private String trainId;

    @NotNull
    @ApiModelProperty(notes = "Equipment Serial Number")
    private String equipSerialNumber;

    @NotNull
    @ApiModelProperty(notes = "Name of the WorkFlow ")
    private String workflowName;
    
    @NotNull
    @ApiModelProperty(notes = "Name of the Project ")
    private String siteName;

    @NotNull
    @ApiModelProperty(notes = "OutageId ")
    private int outageId;

    @ApiModelProperty(notes = "Change Tracking",hidden=true)
    private ChangeTracking changeTracking;

    @ApiModelProperty(notes = "List of Assigned Engineers")
    private List<User> assignedEngineers;
    

    public static RfrWorkflowDto valueOf(RfrWorkflow workflow) {
        RfrWorkflowDto result = new RfrWorkflowDto();
        result.setEquipSerialNumber(workflow.getEquipSerialNumber());
        result.setOutageId(workflow.getOutageId());
        result.setAssignedEngineers(workflow.getAssignedEngineers());
        result.setWorkflowId(workflow.getId());
        result.setTrainId(workflow.getTrainId());
        result.setWorkflowName(workflow.getWorkflowName());
        result.setSiteName(workflow.getSiteName());
        result.setChangeTracking(workflow.getChangeTracking());
        return result;
    }
    
    public static RfrWorkflow getEntityObj(RfrWorkflowDto workflow,SsoUser user) {
    	RfrWorkflow result = new RfrWorkflow();
        result.setEquipSerialNumber(workflow.getEquipSerialNumber());
        result.setOutageId(workflow.getOutageId());
        result.setTrainId(workflow.getTrainId());
        if(workflow.getAssignedEngineers()==null)
        	result.setAssignedEngineers(new ArrayList<>());
        else
        	result.setAssignedEngineers(workflow.getAssignedEngineers());
        result.setWorkflowName(workflow.getWorkflowName());
        result.setSiteName(workflow.getSiteName());
        result.setChangeTracking(new ChangeTracking(User.fromSsoUser(user)));
        return result;
    }

	public int getWorkflowId() {
		return workflowId;
	}

	public String getTrainId() {
		return trainId;
	}

	public String getEquipSerialNumber() {
		return equipSerialNumber;
	}

	public String getWorkflowName() {
		return workflowName;
	}

	public String getSiteName() {
		return siteName;
	}

	public int getOutageId() {
		return outageId;
	}

	public ChangeTracking getChangeTracking() {
		return changeTracking;
	}

	public List<User> getAssignedEngineers() {
		return assignedEngineers;
	}

	public void setWorkflowId(int workflowId) {
		this.workflowId = workflowId;
	}

	public void setTrainId(String trainId) {
		this.trainId = trainId;
	}

	public void setEquipSerialNumber(String equipSerialNumber) {
		this.equipSerialNumber = equipSerialNumber;
	}

	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public void setOutageId(int outageId) {
		this.outageId = outageId;
	}

	public void setChangeTracking(ChangeTracking changeTracking) {
		this.changeTracking = changeTracking;
	}

	public void setAssignedEngineers(List<User> assignedEngineers) {
		this.assignedEngineers = assignedEngineers;
	}

	@Override
	public String toString() {
		return "RfrWorkflowDto [workflowId=" + workflowId + ", trainId=" + trainId + ", equipSerialNumber="
				+ equipSerialNumber + ", workflowName=" + workflowName + ", siteName=" + siteName + ", outageId="
				+ outageId + ", changeTracking=" + changeTracking + ", assignedEngineers=" + assignedEngineers + "]";
	}

    
	
}
