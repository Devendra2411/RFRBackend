package com.ge.rfr.workflow;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.ge.rfr.actionitem.model.AttributeType;
import com.ge.rfr.actionitem.model.Category;
import com.ge.rfr.actionitem.model.Owner;
import com.ge.rfr.actionitem.model.RfrActionItem;
import com.ge.rfr.actionitem.model.Status;
import com.ge.rfr.actionitem.model.TaskType;
import com.ge.rfr.auth.SsoUser;
import com.ge.rfr.boxapi.BoxService;
import com.ge.rfr.common.exception.InvalidJsonException;
import com.ge.rfr.common.model.ChangeTracking;
import com.ge.rfr.common.model.RfrRegion;
import com.ge.rfr.common.model.User;
import com.ge.rfr.common.util.RfrRegionHelper;
import com.ge.rfr.common.util.SaveEntityConstraintHelper;
import com.ge.rfr.fspevent.PgsFspEventDao;
import com.ge.rfr.fspevent.PgsFspEventService;
import com.ge.rfr.fspevent.model.RfrPgsFspEvent;
import com.ge.rfr.fspevent.model.TrainDataInterface;
import com.ge.rfr.pgsactionitem.PgsActionItemsService;
import com.ge.rfr.pgsactionitem.model.PgsActionItem;
import com.ge.rfr.workflow.assignengineer.AssignedEngineerService;
import com.ge.rfr.workflow.model.RfrWorkflow;
import com.ge.rfr.workflow.model.dto.RfrWorkflowDetailsDto;
import com.ge.rfr.workflow.model.dto.RfrWorkflowDto;
import com.google.common.collect.ImmutableMap;

/**
 * @author 503055886
 */
@Service
public class WorkflowService {
    /**
     * Maps DB constraint names to the corresponding fields in the entity purely for
     * displaying user-friendly error messages.
     */
    private static final Map<String, Supplier<? extends RuntimeException>> CONSTRAINT_MAPPING = ImmutableMap
            .of(RfrWorkflow.UNQ_NAME_CONSTRAINT, WorkflowAlreadyExistsException::new);

    private static final String INITIAL_TASK_TITLE = "Initial task for this workflow";
    private static final int INITIAL_TASK_LEVEL = 1;
    private static final String NON_SCOPE = "Non Scope";

    private final WorkflowDao workflowDao;
    private final PgsFspEventService fspEventService;
    private final AssignedEngineerService assignedEngineerService;
    private final PgsActionItemsService pgsActionItemsService;
    private final PgsFspEventDao pgsFspEventDao;
    private final BoxService boxService;

    public WorkflowService(WorkflowDao workflowDao, PgsFspEventService fspEventService,
                           AssignedEngineerService assignedEngineerService, PgsActionItemsService pgsActionItemsService
            , PgsFspEventDao pgsFspEventDao, BoxService boxService) {
        this.workflowDao = workflowDao;
        this.fspEventService = fspEventService;
        this.assignedEngineerService = assignedEngineerService;
        this.pgsActionItemsService = pgsActionItemsService;
        this.pgsFspEventDao = pgsFspEventDao;
        this.boxService = boxService;
    }


    /**
     * Gets the workflow details for the given workflow id
     *
     * @return RfrWorkflow
     * @throws RfrWorkflowNotFoundException Throws if the workflow does not exists
     */
    public RfrWorkflow getWorkflowDetails(SsoUser user, int workflowId) {
        RfrWorkflow workflow = workflowDao.findOne(workflowId);
        if (workflow == null) {
            throw new RfrWorkflowNotFoundException(workflowId);
        }
        List<RfrRegion> regions = new ArrayList<>();
        if (RfrRegionHelper.isAdmin(user)) {
        } else if (RfrRegionHelper.getRegions(user, regions)) {
            RfrPgsFspEvent rfrPgsFspEvent = pgsFspEventDao.findByEquipSerialNumberAndOutageIdAndRegionContaining(
                    workflow.getEquipSerialNumber(),
                    workflow.getOutageId(),
                    regions);

            if (rfrPgsFspEvent == null) {
                throw new AccessDeniedException("You don't have Permission to view this workflow");
            }
        } else {
            RfrWorkflow userWorkflow = workflowDao.findByIdAndAssignedEngineersSso(
                    workflowId,
                    User.fromSsoUser(user).getSso());

            if (userWorkflow == null) {
                throw new AccessDeniedException("You are not assigned to this workflow.");
            }
        }
        return workflow;
    }

    /**
     * Gets a list of workflow details for the logged in user.
     * If the user is admin or installation director fetch all the workflow's
     * If the user is coordinator fetch the workflow's of the user region.
     * If user is TIM or team member retrieve the assigned workflow's.
     *
     * @return List<RfrWorkflowDetailsDto>
     */
    public List<RfrWorkflowDetailsDto> getWorkflowList(SsoUser user) {

        List<RfrRegion> regions = new ArrayList<>();
        boolean isRegionalCoordinator = false;
        boolean isAdmin = false;

        List<RfrWorkflow> userWorkflowList;

        // Check if the user is Admin or Installation director
        if (RfrRegionHelper.isAdmin(user)) {
            isAdmin = true;
        } else {
            // else if user is RFR Coordinator then get the list of regions user belongs to
            isRegionalCoordinator = RfrRegionHelper.getRegions(user, regions);
        }

        if (isAdmin || isRegionalCoordinator)
            userWorkflowList = workflowDao.findAll();

            // Check if user is not Admin/Installation Director and not RFR Coordinator
            // then user must be TIM or Team member
            // Fetch the list of workflows assigned to user
        else
            userWorkflowList = getWorkflowListAssignedToUser(user);

        List<RfrWorkflowDetailsDto> eventList = new ArrayList<>();

        // If user is co-ordinator, then need to filter the list based on region user belongs to
        if (isRegionalCoordinator) {
            for (RfrWorkflow workflow : userWorkflowList) {
                RfrPgsFspEvent event = fspEventService.findByEquipSerialNumberAndOutageIdAndRegionContaining(
                        workflow.getEquipSerialNumber(),
                        workflow.getOutageId(),
                        regions);
                if (event != null) {
                    RfrWorkflowDetailsDto dto = updateWorkflowDetails(event, workflow.getId());
                    eventList.add(dto);
                }
            }

        }

        // If user role is other than RFR Co-ordinator then fetch the workflow details from fsppgsevent table
        else {
            userWorkflowList.forEach(workflow -> {
                RfrPgsFspEvent event = fspEventService
                        .findByEquipSerialNumberAndOutageId(
                                workflow.getEquipSerialNumber(),
                                workflow.getOutageId());
                if (event != null) {
                    RfrWorkflowDetailsDto dto = updateWorkflowDetails(event, workflow.getId());
                    eventList.add(dto);
                }
            });
        }
        return eventList;
    }

    /**
     * Creates a new workflow for the user and sends mail to the assigned engineer
     *
     * @param user          Captures the user details who logged in
     * @param createDtoList
     * @return List<RfrWorkflow>
     * @throws IOException 
     * @throws InvalidJsonException
     */
    @Transactional
    public List<RfrWorkflow> createRfrWorkflow(SsoUser user, List<RfrWorkflowDto> createDtoList) throws IOException {
        List<RfrWorkflow> workflowList = new ArrayList<>();
        List<RfrRegion> regions = new ArrayList<>();
        if (createDtoList.isEmpty())
            throw new InvalidJsonException("List Cannot be empty");

        if (RfrRegionHelper.isAdmin(user)) {
            validateCreateWorkflowList(createDtoList, "ADMIN", null);
            workflowList = saveAll(user, createDtoList);
            return workflowList;
        } else if (RfrRegionHelper.getRegions(user, regions)) {
            validateCreateWorkflowList(createDtoList, "COORDINATOR", regions);
            workflowList = saveAll(user, createDtoList);
            return workflowList;
        }
        return workflowList;
    }

    /**
     * Updates the assigned engineers for a workflow and sends mail to the assigned engineers
     *
     * @param user      Captures the user details who logged in
     * @param updateDto
     * @return RfrWorkflow
     * @throws RfrWorkflowNotFoundException   Thrown if the given workflow does not exists
     * @throws WorkflowAlreadyExistsException Thrown if the workflow name already exists
     */
    public RfrWorkflow updateRfrWorkflow(SsoUser user, int workflowId, RfrWorkflowDto updateDto) {
        RfrWorkflow workflow = getWorkflowDetails(user, workflowId);
        if (RfrRegionHelper.hasCreateAccess(user, updateDto.getOutageId(), updateDto.getEquipSerialNumber())) {
            // Save the old list of assignees
            List<User> previouslyAssigned = new ArrayList<>(workflow.getAssignedEngineers());

            updateFromDto(workflow, updateDto);
            workflow.getChangeTracking().update(user);

            SaveEntityConstraintHelper.save(
                    workflowDao,
                    workflow,
                    CONSTRAINT_MAPPING);

            // Now that the change has been flushed, go ahead and send mails to newly assigned users
            assignedEngineerService.sendProjectAssignmentMails(User.fromSsoUser(user), previouslyAssigned, workflow);
        }
        return workflow;

    }

    /**
     * Gets the list of workflow's assigned to the user
     *
     * @param user
     * @return RfrWorkflow
     */
    private List<RfrWorkflow> getWorkflowListAssignedToUser(SsoUser user) {
        // Fetch the list of workflow's assigned to logged in user
        return workflowDao.findByAssignedEngineersSso(User.fromSsoUser(user).getSso());
    }

    public List<RfrWorkflow> saveAll(SsoUser user, List<RfrWorkflowDto> createDtoList) throws IOException {
        List<RfrWorkflow> workflowList = createDtoList.stream()
                .map(obj -> RfrWorkflowDto.getEntityObj(obj, user))
                .collect(Collectors.toList());
        
		for(RfrWorkflow workflowObj:workflowList) {
		String	projectFolderId = boxService.createBoxFolder(workflowObj.getSiteName(), workflowObj.getEquipSerialNumber(), 
										workflowObj.getOutageId());
			String calencoFileId =	boxService.getCalencoDocId(workflowObj.getTrainId() + "-" + workflowObj.getSiteName());
		 	String copiedFileId = boxService.copyCalencoDoc(calencoFileId, projectFolderId);
			workflowObj.setCalencoDocId(copiedFileId);                           
		}
        SaveEntityConstraintHelper.saveAll(
                workflowDao,
                workflowList,
                CONSTRAINT_MAPPING);

        // fetch the list of default action-items and save in workflow
        workflowList = workflowList.stream()
                .map(workflowObj -> updateActionItems(workflowObj, new ArrayList<>(), user))
                .collect(Collectors.toList());

        SaveEntityConstraintHelper.saveAll(
                workflowDao,
                workflowList,
                CONSTRAINT_MAPPING);

        // Now that the change has been flushed, go ahead and send mails to newly assigned users
        workflowList.stream().forEach(workflowObj -> assignedEngineerService.sendProjectAssignmentMails(
                User.fromSsoUser(user),
                new ArrayList<>(),
                workflowObj));

        return workflowList;
    }

    public void validateCreateWorkflowList(List<RfrWorkflowDto> createDtoList, String role, List<RfrRegion> regions) {
        List<TrainDataInterface> trainDataDtoList = null;
        List<TrainDataInterface> filteredTrainDataDtoList = null;
        if (role.equals("ADMIN")) {
            trainDataDtoList = pgsFspEventDao.findBySiteName(createDtoList.get(0).getSiteName());
        } else {
            trainDataDtoList = pgsFspEventDao.findBySiteNameAndRegionIn(createDtoList.get(0).getSiteName(), regions);
        }

        for (RfrWorkflowDto createDto : createDtoList) {
            filteredTrainDataDtoList = trainDataDtoList.stream()
                    .filter(obj -> (obj.getTrainId().equals(createDto.getTrainId())
                            && obj.getEquipSerialNumber().equals(createDto.getEquipSerialNumber())
                            && obj.getOutageId() == createDto.getOutageId())).collect(Collectors.toList());
            if (filteredTrainDataDtoList.isEmpty())
                throw new InvalidJsonException("JSON is Not a Valid Combination Of Site Name,ESN,Outage and Train ID");
        }

    }

    private RfrWorkflow updateFromDto(RfrWorkflow workflow, RfrWorkflowDto rfrWorkflowDto) {
        workflow.setAssignedEngineers(rfrWorkflowDto.getAssignedEngineers());
        return workflow;
    }

    /**
     * saves the action items fetched from pgs_action_items into the RfrActionItem
     */
    private void copyActionItems(PgsActionItem pgsActionItem, RfrActionItem rfrItem, RfrPgsFspEvent fspEvent) {
        // calculate Due date
        Instant dueDate = Instant.now();
        if (null != pgsActionItem.getCalDate())
            dueDate = pgsActionItem.getCalDate();
        else {
            if (pgsActionItem.getRefDateId() == 6)
                dueDate = fspEvent.geteStartDate().minus(pgsActionItem.getSpanFromRef(), ChronoUnit.DAYS);
            else if (pgsActionItem.getRefDateId() == 7 || pgsActionItem.getRefDateId() == 10
                    || pgsActionItem.getRefDateId() == 12)
                dueDate = fspEvent.geteEndDate().minus(pgsActionItem.getSpanFromRef(), ChronoUnit.DAYS);
        }

        rfrItem.setDueDate(dueDate);
        rfrItem.setCategory(pgsActionItem.getCategory());
        rfrItem.setItemTitle(pgsActionItem.getItemTitle());
        rfrItem.setOwner(pgsActionItem.getOwner());
        rfrItem.setStatus(Status.COMPLETE);
        // All PGS Action Items should be set as First Fire
        rfrItem.setTaskType(TaskType.FIRST_FIRE);
        rfrItem.setTypeOfItem(NON_SCOPE);
    }

    /**
     * Creates a default action item for each workflow created and
     * fetch the list of action items for equipment serial number
     * and outage id from pgs_action_items
     *
     * @param workflow
     * @param rfrItemsList
     * @param user
     */
    private RfrWorkflow updateActionItems(RfrWorkflow workflow, List<RfrActionItem> rfrItemsList, SsoUser user) {
        // Fetch the FSP data for workflow being created
        RfrPgsFspEvent fspEvent = fspEventService.findByEquipSerialNumberAndOutageId(workflow.getEquipSerialNumber(),
                workflow.getOutageId());

        // Create the list of Attributes on basis of which the actions items needs to be fetched for workflow
        List<AttributeType> attributesList = new ArrayList<>();
        attributesList.add(AttributeType.OUTAGE_TYPE);
        attributesList.add(AttributeType.TECHNOLOGY);
        attributesList.add(AttributeType.EQUIP_SERIAL_NUMBER);
        attributesList.add(AttributeType.EQUIPMENT_TYPE);
        attributesList.add(AttributeType.OUTAGE_ID);
        attributesList.add(AttributeType.OUTAGE_STATUS);

        List<String> attributeValuesList = new ArrayList<>();
        attributeValuesList.add(fspEvent.getOutageType().toString());
        attributeValuesList.add(fspEvent.getTechnicalCode().toString());
        attributeValuesList.add(fspEvent.getEquipSerialNumber());
        attributeValuesList.add(fspEvent.getEquipmentType());
        attributeValuesList.add(String.valueOf(fspEvent.getOutageId()));
        attributeValuesList.add(fspEvent.getProjectStatus().toString());

        List<PgsActionItem> pgsItemsList = pgsActionItemsService.findWorkflowActionActionItems(attributesList,
                attributeValuesList);

        // Create the first default action item
        RfrActionItem rfrActionItem = new RfrActionItem();
        rfrActionItem.setCategory(Category.START_UP_CHECKLIST);
        rfrActionItem.setDueDate(Instant.now());
        rfrActionItem.setItemTitle(INITIAL_TASK_TITLE);
        rfrActionItem.setLevelValue(INITIAL_TASK_LEVEL);
        rfrActionItem.setStatus(Status.COMPLETE);
        rfrActionItem.setOwner(Owner.FE);
        rfrActionItem.setWorkflow(workflow);
        rfrActionItem.setTaskType(TaskType.FIRST_FIRE);
        rfrActionItem.setTypeOfItem(NON_SCOPE);
        rfrActionItem.setChangeTracking(new ChangeTracking(User.fromSsoUser(user)));
        rfrItemsList.add(rfrActionItem);

        pgsItemsList.forEach(pgsAction -> {
            RfrActionItem copiedActionItem = new RfrActionItem();
            copiedActionItem.setLevelValue(workflow.getOutageId());
            copyActionItems(pgsAction, copiedActionItem, fspEvent);
            copiedActionItem.setWorkflow(workflow);
            copiedActionItem.setChangeTracking(new ChangeTracking(User.fromSsoUser(user)));
            rfrItemsList.add(copiedActionItem);
        });

        workflow.setActionItems(rfrItemsList);

        return workflow;
    }

    private RfrWorkflowDetailsDto updateWorkflowDetails(RfrPgsFspEvent event, int workflowId) {
        RfrWorkflowDetailsDto result = new RfrWorkflowDetailsDto();
        result.setWorkflowId(workflowId);
        result.setEquipSerialNumber(event.getEquipSerialNumber());
        result.setOutageId(event.getOutageId());
        result.setSiteName(event.getSiteName());
        result.setOutageProbability(event.getOutageProbability());
        result.setTechnicalCode(event.getTechnicalCode());
        result.setContractType(event.getContractType());
        result.setOutageType(event.getOutageType());
        result.setProjectStatus(event.getProjectStatus());
        result.setEventStatus(event.getEventStatus());
        result.seteStartDate(event.geteStartDate());
        result.seteEndDate(event.geteEndDate());
        result.seteUnitStartupDate(event.geteUnitStartupDate());
        result.setRegion(event.getRegion());
        result.setChangeTracking(event.getChangeTracking());
        return result;
    }
}