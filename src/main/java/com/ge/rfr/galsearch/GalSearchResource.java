package com.ge.rfr.galsearch;

import com.ge.rfr.galsearch.dto.EmployeeDetailsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/galSearch")

public class GalSearchResource {

    private final GalSearchService galSearchService;

    @Autowired
    public GalSearchResource(GalSearchService galSearchService) {
        this.galSearchService = galSearchService;
    }

    @GetMapping
    @RequestMapping("/{searchText}")
    public List<EmployeeDetailsDto> getEmployeeDetails(@PathVariable("searchText") String searchText) throws IOException {
        return galSearchService.getEmployeeDetails(searchText);
    }
}
