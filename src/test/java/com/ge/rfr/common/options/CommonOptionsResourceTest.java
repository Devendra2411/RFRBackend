package com.ge.rfr.common.options;

import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ge.rfr.helper.AbstractIntegrationTest;
import com.ge.rfr.helper.TestUser;

public class CommonOptionsResourceTest extends AbstractIntegrationTest { 

    @Test
    public void testGetOptionsGridFrequency() throws Exception {/*
        ObjectNode commonOptions = getOptions();

        ArrayNode gridFrequencies = (ArrayNode) commonOptions.get("gridFrequencies");

        // Convert the list of options into an easily testable string list
        List<String> optionsJson = new ArrayList<>();
        gridFrequencies.forEach(node -> {
            optionsJson.add(node.path("id").textValue() + ":" + node.path("text").textValue());
        });

        List<String> optionsExpected = Arrays.stream(GridFrequency.values())
                .map(gf -> gf.name() + ":" + gf.getText())
                .collect(Collectors.toList());

        assertThat(optionsJson).isEqualTo(optionsExpected);
    */}

    private ObjectNode getOptions() throws Exception {
        return getClient(TestUser.RFR_TIM)
                .path("/common-options")
                .request()
                .get(ObjectNode.class);
    }

}