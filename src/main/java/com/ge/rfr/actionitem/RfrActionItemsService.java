package com.ge.rfr.actionitem;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ge.rfr.actionitem.model.RfrActionItem;
import com.ge.rfr.actionitem.model.dto.RfrActionItemDetailsDto;
import com.ge.rfr.actionitem.model.dto.RfrActionItemDto;
import com.ge.rfr.auth.SsoUser;
import com.ge.rfr.common.model.ChangeTracking;
import com.ge.rfr.common.model.User;
import com.ge.rfr.common.util.RfrRegionHelper;
import com.ge.rfr.workflow.WorkflowService;
import com.ge.rfr.workflow.model.RfrWorkflow;

/**
 * @author 503055886
 */
@Service
public class RfrActionItemsService {

    private final RfrActionItemsDao actionItemsDao;
    private final WorkflowService workflowService;

    RfrActionItemsService(RfrActionItemsDao actionItemsDao, WorkflowService workflowService) {
        this.actionItemsDao = actionItemsDao;
        this.workflowService = workflowService;
    }

    /**
     * Gets the list of Action items for the given workflow along with esn and outage id
     *
     * @param user       Captures the user details who logged in
     * @param workflowId
     * @return RfrActionItemDetailsDto
     */
    public RfrActionItemDetailsDto getRfrActionItemsList(SsoUser user, int workflowId) {
        RfrWorkflow workflow = workflowService.getWorkflowDetails(user, workflowId);
        RfrActionItemDetailsDto detailsDto = new RfrActionItemDetailsDto();
        if (RfrRegionHelper.hasViewAccess(user, workflow)) {
            detailsDto.setEquipSerialNumber(workflow.getEquipSerialNumber());
            detailsDto.setSiteName(workflow.getSiteName());
            detailsDto.setActionItemsList(actionItemsDao.findByWorkflowIdOrderByStatus(workflowId).stream()
                    .map(RfrActionItemDto::valueOf).collect(Collectors.toList()));
        }
        return detailsDto;

    }

    /**
     * Creates a new action item for the given workflow
     *
     * @param user       Captures the user details who logged in
     * @param workflowId
     * @param createDto
     * @return RfrActionItem
     */
    public RfrActionItem createRfrActionItem(SsoUser user, int workflowId, RfrActionItemDto createDto) {
        RfrWorkflow workflow = workflowService.getWorkflowDetails(user, workflowId);
        RfrActionItem rfrActionItem = new RfrActionItem();
        // Check if the Workflow for outage Id and ESN combination is already
        // created
        if (RfrRegionHelper.hasCreateAccess(user, workflow.getOutageId(), workflow.getEquipSerialNumber())) {
            rfrActionItem.setCategory(createDto.getCategory());
            rfrActionItem.setDueDate(createDto.getDueDate());
            rfrActionItem.setItemTitle(createDto.getItemTitle());
            rfrActionItem.setLevelValue(createDto.getLevelValue());
            rfrActionItem.setOwner(createDto.getOwner());
            rfrActionItem.setStatus(createDto.getStatus());
            rfrActionItem.setTaskType(createDto.getTaskType());
            rfrActionItem.setTypeOfItem("Non Scope");
            rfrActionItem.setWorkflow(workflow);
            rfrActionItem.setChangeTracking(new ChangeTracking(User.fromSsoUser(user)));
            actionItemsDao.save(rfrActionItem);
        }
        return rfrActionItem;
    }

    /**
     * Updates the action items that are created
     *
     * @param user         Captures the user details who logged in
     * @param actionItemId
     * @param updateDto
     * @return
     * @throws RfrActionItemNotFoundException Thrown if the given action item id is not valid
     */
    public RfrActionItem updateRfrActionItem(SsoUser user, int actionItemId, RfrActionItemDto updateDto) {
        RfrActionItem rfrActionItem = getRfrActionItemDetails(actionItemId);
        if (rfrActionItem == null)
            throw new RfrActionItemNotFoundException(actionItemId);

            // Check if the Workflow for outage Id and ESN combination is already
            // created
        else if (RfrRegionHelper.hasCreateAccess(user,
                rfrActionItem.getWorkflow().getOutageId(),
                rfrActionItem.getWorkflow().getEquipSerialNumber())) {
            rfrActionItem.setCategory(updateDto.getCategory());
            rfrActionItem.setDueDate(updateDto.getDueDate());
            rfrActionItem.setItemTitle(updateDto.getItemTitle());
            rfrActionItem.setLevelValue(updateDto.getLevelValue());
            rfrActionItem.setOwner(updateDto.getOwner());
            rfrActionItem.setStatus(updateDto.getStatus());
            rfrActionItem.setTaskType(updateDto.getTaskType());
            rfrActionItem.getChangeTracking().update(user);
            actionItemsDao.save(rfrActionItem);
        }
        return rfrActionItem;
    }

    /**
     * Gets the action item details for the given action item id
     *
     * @param actionItemId
     * @return RfrActionItem
     */
    public RfrActionItem getRfrActionItemDetails(int actionItemId) {
        return actionItemsDao.findOne(actionItemId);
    }

}
