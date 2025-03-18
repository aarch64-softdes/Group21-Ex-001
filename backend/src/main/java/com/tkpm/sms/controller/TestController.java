package com.tkpm.sms.controller;

import com.tkpm.sms.enums.LoggerType;
import com.tkpm.sms.logging.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

        staticLogger.log("Processed text static: " + text, LogLevel.DEBUG);

        consoleLogger.log("Processing text: " + text);
        consoleLogger.log("Processed text debug: " + text, LogLevel.DEBUG);

        jsonLogger.log("Processed text json: " + text, LogLevel.DEBUG);
        jsonLogger.log("Processed text info: " + text, LogLevel.ERROR, Map.of("text", text));

        fileLogger.log("Processed text file: " + text, LogLevel.DEBUG);
        fileLogger.log("Processed text file: " + text, LogLevel.ERROR, Map.of("text", text));
        fileLogger.log("Processed text file: " + text, LogLevel.WARN);

        return ResponseEntity.ok("Processed: " + text);
    }
}
