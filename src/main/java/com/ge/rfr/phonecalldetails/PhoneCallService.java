package com.ge.rfr.phonecalldetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ge.rfr.auth.SsoUser;
import com.ge.rfr.common.exception.InValidDataSubmittedException;
import com.ge.rfr.common.model.ChangeTracking;
import com.ge.rfr.common.model.User;
import com.ge.rfr.common.util.RfrRegionHelper;
import com.ge.rfr.common.util.SaveEntityConstraintHelper;
import com.ge.rfr.phonecalldetails.model.PhoneCallDetails;
import com.ge.rfr.phonecalldetails.model.dto.PhoneCallDetailsDto;
import com.ge.rfr.phonecalldetails.model.dto.PhoneCallDto;
import com.ge.rfr.workflow.WorkflowService;
import com.ge.rfr.workflow.model.RfrWorkflow;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;

/**
 * @author 503055888
 */
@Service
public class PhoneCallService {

    private static final Map<String, Supplier<? extends RuntimeException>> CONSTRAINT_MAPPING = ImmutableMap
            .of(PhoneCallDetails.UNQ_NAME_CONSTRAINT, PhoneCallLineAlreadyExistsException::new);

    private final PhoneCallDao phoneCallDao;

    private final WorkflowService workflowService;

    public PhoneCallService(PhoneCallDao phoneCallDao, WorkflowService workflowService) {
        this.phoneCallDao = phoneCallDao;
        this.workflowService = workflowService;

    }

    /**
     * Retrieves the phone call items for the given workflow id along with esn and outageid
     *
     * @param user       Captures the user details who logged in
     * @param workflowId
     * @return PhoneCallDto
     * @throws RfrWorkflowNotFoundException Thrown if the given workflow is not valid
     */
    public PhoneCallDto getPhoneCallItems(SsoUser user, int workflowId) {
        RfrWorkflow workflow = workflowService.getWorkflowDetails(user, workflowId);
        PhoneCallDto phoneCallDto = new PhoneCallDto();
        if (RfrRegionHelper.hasViewAccess(user, workflow)) {
            phoneCallDto.setEquipSerialNumber(workflow.getEquipSerialNumber());
            phoneCallDto.setSiteName(workflow.getSiteName());
            phoneCallDto.setPhoneCallDetailsDtoList(phoneCallDao.findByRfrWorkflowId(workflowId).stream()
                    .map(PhoneCallDetailsDto::valueOf).collect(Collectors.toList()));
        }
        return phoneCallDto;

    }

    /**
     * Retrieves the phone call details with phone call minutes for the given
     * workflow id along with esn and outageid
     *
     * @param user       Captures the user details who logged in
     * @param workflowId
     * @return PhoneCallDto
     * @throws RfrWorkflowNotFoundException Thrown if the given workflow is not valid
     */
    public PhoneCallDto getPhoneCallDetails(SsoUser user, int workflowId, int meetingId) {
        RfrWorkflow workflow = workflowService.getWorkflowDetails(user, workflowId);
        PhoneCallDto phoneCallDto = new PhoneCallDto();
        if (phoneCallDao.exists(meetingId)) {
            if (RfrRegionHelper.hasViewAccess(user, workflow)) {
                phoneCallDto.setEquipSerialNumber(workflow.getEquipSerialNumber());
                phoneCallDto.setSiteName(workflow.getSiteName());
                List<PhoneCallDetailsDto> list = new ArrayList<>();
                list.add(PhoneCallDetailsDto.valueOf(phoneCallDao.findOne(meetingId)));
                phoneCallDto.setPhoneCallDetailsDtoList(list);
            }
            return phoneCallDto;
        } else
            throw new PhoneCallMeetingNotFoundException(meetingId);
    }

    /**
     * Creates a new phone call for the given workflow id, initially the phone call
     * minutes will be empty
     *
     * @param user       Captures the user details who logged in
     * @param workflowId
     * @return PhoneCallDetails
     * @throws PhoneCallLineAlreadyExistsException Thrown when the meeting line used for a new phone call meeting
     *                                             is already in use by another meeting
     * @throws RfrWorkflowNotFoundException        Thrown if the given workflow is not valid
     */
    public PhoneCallDetails createPhoneCall(SsoUser user, int workflowId, PhoneCallDetailsDto createDto) {
        RfrWorkflow workflow = workflowService.getWorkflowDetails(user, workflowId);
        PhoneCallDetails phoneCallDetails = new PhoneCallDetails();

        if (RfrRegionHelper.hasCreateAccess(user, workflow.getOutageId(), workflow.getEquipSerialNumber())) {
            phoneCallDetails.setMeetingLine(createDto.getMeetingLine());
            phoneCallDetails.setMeetingDate(createDto.getMeetingDate());

            phoneCallDetails.setRfrWorkflow(workflow);
            phoneCallDetails.setChangeTracking(new ChangeTracking(User.fromSsoUser(user)));

            SaveEntityConstraintHelper.save(
                    phoneCallDao,
                    phoneCallDetails,
                    CONSTRAINT_MAPPING
            );
        }
        return phoneCallDetails;
    }

    /**
     * Updates a phone call meeting for a given meeting id
     *
     * @param user
     * @param workflowId
     * @param meetingId
     * @param phoneCallDetailsDto
     * @return PhoneCallDetails
     * @throws RfrWorkflowNotFoundException        Thrown if the given workflow is not valid
     * @throws PhoneCallLineAlreadyExistsException Thrown when the meeting line used to update a phone call meeting
     *                                             is already in use by another meeting
     * @throws InValidDataSubmittedException       Thrown if the given meeting content is empty
     */
    public PhoneCallDetails updatePhoneCall(SsoUser user, int workflowId, int meetingId,
                                            PhoneCallDetailsDto phoneCallDetailsDto) {
        RfrWorkflow workflow = workflowService.getWorkflowDetails(user, workflowId);
        PhoneCallDetails callDetails = phoneCallDao.findOne(meetingId);

        if (null != callDetails) {
            if (RfrRegionHelper.hasCreateAccess(user, workflow.getOutageId(), workflow.getEquipSerialNumber())) {
                updateFromDto(callDetails, phoneCallDetailsDto);

                callDetails.setId(meetingId);
                callDetails.setRfrWorkflow(workflow);
                callDetails.getChangeTracking().update(user);

                SaveEntityConstraintHelper.save(
                        phoneCallDao,
                        callDetails,
                        CONSTRAINT_MAPPING
                );
            }
            return callDetails;
        } else {
            throw new PhoneCallMeetingNotFoundException(meetingId);
        }
    }

    private void updateFromDto(PhoneCallDetails callDetails, PhoneCallDetailsDto dto) {
        callDetails.setMeetingLine(dto.getMeetingLine());
        callDetails.setMeetingDate(dto.getMeetingDate());
        callDetails.setPhoneCallMinutes(dto.getPhoneCallMinutes());
    }

}
