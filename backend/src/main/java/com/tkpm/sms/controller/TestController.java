package com.tkpm.sms.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @GetMapping("/tests")
    public ResponseEntity<String> processText(String text) {
        logger.info("Received request to process text: {}", text);
        logger.debug("Received request to process text: {}", text);

        logger.info("Returning response for text: {}", text);
        return ResponseEntity.ok("Processed: " + text);
    }
}
