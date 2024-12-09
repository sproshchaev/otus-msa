package com.prosoft.controller;

import com.prosoft.service.DataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataController {

    private static final Logger logger = LoggerFactory.getLogger(DataController.class);

    private final DataService dataService;

    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping("/getData/{id}")
    public String getDataFromDatabase(@PathVariable int id) {
        logger.info("Начало запроса данных для ID {}", id);
        String data = dataService.getDataFromDatabase(id);
        return data;
    }
}
