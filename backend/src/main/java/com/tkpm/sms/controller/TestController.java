package com.tkpm.sms.controller;

import com.tkpm.sms.logging.ConsoleLogger;
import com.tkpm.sms.logging.FileLogger;
import com.tkpm.sms.logging.JsonLogger;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class TestController {
    @GetMapping("/tests")
    public ResponseEntity<String> processText(String text) {
        var consoleLogger = new ConsoleLogger();
        var jsonLogger = new JsonLogger();
        var fileLogger = new FileLogger();

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
