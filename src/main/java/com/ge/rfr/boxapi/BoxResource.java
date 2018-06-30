package com.ge.rfr.boxapi;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/boxApi")
public class BoxResource {

    private BoxService boxService;

    @Autowired
    public BoxResource(BoxService boxService) {
        this.boxService = boxService;
    }

    @GetMapping
    public String getTokenForBox() throws IOException {
        return boxService.getTokenForBox();
    }

    @GetMapping
    @RequestMapping("/{siteName}/{esn}/{outageId}")
    public void createBoxFolder(@PathVariable("siteName") String siteName,
                                @PathVariable("esn") String esn,
                                @PathVariable("outageId") int outageId) throws IOException {
        boxService.createBoxFolder(siteName, esn, outageId);
    }


    @PostMapping
    @RequestMapping("/{fileName}/{destinationId}")
    public String copyCalencoDoc(@PathVariable("sourceId") String sourceId,
                                              @PathVariable("destinationId") String destinationId) throws IOException {
        return boxService.copyCalencoDoc(sourceId, destinationId);
    }
}
