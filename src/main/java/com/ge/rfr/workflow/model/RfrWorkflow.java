package com.ge.rfr.workflow.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.ge.rfr.actionitem.model.RfrActionItem;
import com.ge.rfr.common.model.ChangeTracking;
import com.ge.rfr.common.model.User;
import com.ge.rfr.phonecalldetails.model.PhoneCallDetails;
import com.ge.rfr.workflow.model.dto.ValidatorGroups.CalencoDocIdValidator;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "rfr_workflow")
@ApiModel(description="Entity for Rfr Workflow")
public class RfrWorkflow {

    // Name of the db constraint for uniqueness of the name
    public static final String UNQ_NAME_CONSTRAINT = "rfr_workflow_unq_name";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "Id")
    private int id;

    @NotBlank
    @ApiModelProperty(dataType = "String", notes = "Equipment Serial Number")
    private String equipSerialNumber;

    @NotNull
    @ApiModelProperty(dataType = "int", notes = "OutageId")
    private int outageId;
    
    @NotNull
    @ApiModelProperty(dataType = "int", notes = "Train Id")
    private String trainId;

    @NotBlank
    @ApiModelProperty(notes = "Name of the WorkFlow")
    private String workflowName;
    
    @NotBlank
    @ApiModelProperty(notes = "Name of the Project")
    private String siteName;

    @ApiModelProperty(notes="Id of the Calenco Document")
    @NotBlank(groups=CalencoDocIdValidator.class)
	private String calencoDocId;

    @ElementCollection
    @CollectionTable(name = "rfr_workflow_assigned_engineers", joinColumns = @JoinColumn(name = "workflow_id"))
    @ApiModelProperty(notes = "List of the Assigned Engineers")
    private List<User> assignedEngineers = new ArrayList<>();
    
    @OneToMany(mappedBy = "rfrWorkflow", cascade = CascadeType.ALL)
    @ApiModelProperty(notes = "List of Phone Call Details")
    private List<PhoneCallDetails> phoneCallDetails = new ArrayList<>();

	
	@OneToMany(mappedBy = "workflow", cascade = CascadeType.ALL)
	@ApiModelProperty(notes="List of Action Items")
	private List<RfrActionItem> actionItems = new ArrayList<>();

    @Embedded
    @Valid
    @NotNull
    @ApiModelProperty(notes = "User Details")
    private ChangeTracking changeTracking;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEquipSerialNumber() {
        return equipSerialNumber;
    }

    public void setEquipSerialNumber(String equipSerialNumber) {
        this.equipSerialNumber = equipSerialNumber;
    }

    public int getOutageId() {
        return outageId;
    }

    public void setOutageId(int outageId) {
        this.outageId = outageId;
    }

   

    public List<User> getAssignedEngineers() {
        return assignedEngineers;
    }

    public void setAssignedEngineers(List<User> assignedEngineers) {
        this.assignedEngineers = assignedEngineers;
    }
    
	public List<PhoneCallDetails> getPhoneCallDetails() {
		return phoneCallDetails;
	}

	public void setPhoneCallDetails(List<PhoneCallDetails> phoneCallDetails) {
		this.phoneCallDetails = phoneCallDetails;
	}

	public List<RfrActionItem> getActionItems() {
		return actionItems;
	}

	public void setActionItems(List<RfrActionItem> actionItems) {
		this.actionItems = actionItems;
	}
    public ChangeTracking getChangeTracking() {
        return changeTracking;
    }

    public void setChangeTracking(ChangeTracking changeTracking) {
        this.changeTracking = changeTracking;
    }

	public String getWorkflowName() {
		return workflowName;
	}

	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

    public String getTrainId() {
        return trainId;
    }

    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }
    public String getCalencoDocId() {
        return calencoDocId;
    }

    public void setCalencoDocId(String calencoDocId) {
        this.calencoDocId = calencoDocId;
    }
}
