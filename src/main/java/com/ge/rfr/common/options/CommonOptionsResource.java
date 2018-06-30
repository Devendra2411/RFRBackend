package com.ge.rfr.common.options;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ge.rfr.actionitem.model.Category;
import com.ge.rfr.actionitem.model.Owner;
import com.ge.rfr.actionitem.model.Status;
import com.ge.rfr.actionitem.model.TaskType;
import com.ge.rfr.workflow.model.OutageType;

/**
 * This REST controller returns a list of all common enumerations throughout the
 * application, which would normally be replicated into the client source code.
 */
@RestController
@RequestMapping("/common-options")
public class CommonOptionsResource {

    // Pre-cached JSON document that contains all common enums
    private final String commonOptionsJson;

    @Autowired
    public CommonOptionsResource(ObjectMapper mapper) throws JsonProcessingException {
        this.commonOptionsJson = buildCommonOptions(mapper);
    }

    // Pre-build a string representation of the list of all common options
    private static String buildCommonOptions(ObjectMapper mapper) throws JsonProcessingException {

        ObjectNode result = mapper.createObjectNode();

        addOptions(OutageType.class, result.putArray("outageTypes"));
        addOptions(Category.class, result.putArray("categories"));
        addOptions(Status.class, result.putArray("status"));
        addOptions(Owner.class, result.putArray("owner"));
        addOptions(TaskType.class, result.putArray("taskTypes"));
        return mapper.writeValueAsString(result);

    }

    // Adds all enumeration literals from the given enum class to the output array
    private static <T extends Enum<T> & CommonOptionsEnum> void addOptions(Class<T> enumClass, ArrayNode arrayOut) {

        for (T enumConstant : enumClass.getEnumConstants()) {
            ObjectNode obj = arrayOut.addObject();
            obj.put("id", enumConstant.name());
            obj.put("text", enumConstant.getText());
            enumConstant.getExtraProperties().forEach(obj::putPOJO);
        }

    }

    @GetMapping(produces = "application/json; charset=UTF-8")
    public String getOptions() {
        return commonOptionsJson;
    }

}
