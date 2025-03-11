package com.tkpm.sms.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/metadata")
public class MetadataController {
    @GetMapping("/faculty")
    public List<String> getAllFaculty() {
        return List.of("Faculty of Japanese", "Faculty of French", "Faculty of Law", "Faculty of Business English");
    }
}
