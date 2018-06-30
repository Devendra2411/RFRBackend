package com.ge.rfr.workflow.assignengineer;

import com.ge.rfr.common.mail.MailService;
import com.ge.rfr.common.model.User;
import com.ge.rfr.workflow.model.RfrWorkflow;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class AssignedEngineerService {

    private static final Logger logger = LoggerFactory.getLogger(AssignedEngineerService.class);

    private final MailService mailService;

    public AssignedEngineerService(MailService mailService) {
        this.mailService = mailService;
    }

    public void sendProjectAssignmentMails(User user,
                                           List<User> previouslyAssigned,
                                           RfrWorkflow workflow) {

        Map<String, Object> model = ImmutableMap.of(
                "workflow", workflow
        );

        if(workflow.getAssignedEngineers().size()!=0)
        	sendAssignmentMails(user, previouslyAssigned, workflow.getAssignedEngineers(), "workflow",
                "workflowAssignment.ftl",
                model);

    }

    public void sendAssignmentMails(User user,
                                    List<User> previouslyAssigned,
                                    List<User> assigned,
                                    String context,
                                    String templateName,
                                    Map<String, Object> templateModel) {

        // Compute new assignees
        Set<User> newAssignees = new LinkedHashSet<>(assigned);
        newAssignees.removeAll(previouslyAssigned);

        // Compute removed assignees
        Set<User> removedAssignees = new LinkedHashSet<>(previouslyAssigned);
        removedAssignees.removeAll(assigned);

        // Just log removed assignees, don't send mails
        if (!removedAssignees.isEmpty()) {
            logger.info("Removed assigned engineers {} from {}", removedAssignees, context);
        }

        // Send mails to new assignees
        for (User newAssignee : newAssignees) {
            logger.info("Assigned {} to {}", newAssignee, context);

            try {
                mailService.sendMail(user, newAssignee, templateName, templateModel);
            } catch (Exception e) {
                logger.error("Failed to notify user {} about their new assignment", newAssignee, e);
            }
        }

    }
}
