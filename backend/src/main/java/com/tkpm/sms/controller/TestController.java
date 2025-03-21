package com.tkpm.sms.controller;

import com.tkpm.sms.enums.LoggerType;
import com.tkpm.sms.logging.*;
import com.tkpm.sms.logging.logger.ElasticsearchLogger;
import com.tkpm.sms.logging.logger.FileLogger;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class TestController {
    private final LoggerManager loggerManager;
    private final BaseLogger staticLogger = LoggerManager.getLoggerStatic(LoggerType.JSON);

    @Autowired
    public TestController(LoggerManager loggerManager) {
        this.loggerManager = loggerManager;
    }

    @GetMapping("/tests")
    public ResponseEntity<String> processText(String text) {
        var consoleLogger = loggerManager.getDefaultLogger();
        var jsonLogger = loggerManager.getLogger(LoggerType.JSON);
        var fileLogger = loggerManager.getLogger(FileLogger.class);
        var elasticLogger = loggerManager.getLogger(ElasticsearchLogger.class);

        elasticLogger.log("Processed text elastic: " + text, LogLevel.INFO);

//        staticLogger.log("Processed text static: " + text, LogLevel.DEBUG);
//
//        consoleLogger.log("Processing text: " + text);
//        consoleLogger.log("Processed text debug: " + text, LogLevel.DEBUG);
//
//        jsonLogger.log("Processed text json: " + text, LogLevel.DEBUG);
//        jsonLogger.log("Processed text info: " + text, LogLevel.ERROR, Map.of("text", text));
//
//        fileLogger.log("Processed text file: " + text, LogLevel.DEBUG);
//        fileLogger.log("Processed text file: " + text, LogLevel.ERROR, Map.of("text", text));
//        fileLogger.log("Processed text file: " + text, LogLevel.WARN);

        return ResponseEntity.ok("Processed: " + text);
    }

    @GetMapping("/error")
    public String errorLogging(HttpServletRequest request) {
        throw new RuntimeException("Error occurred");
    }

    @GetMapping("/structured")
    public String structuredLogging(@RequestParam String abc) {
        return "Logged with structured approach";
    }
}
