package com.tkpm.sms.controller;

import com.tkpm.sms.enums.*;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/metadata")
public class MetadataController {
    @GetMapping("/faculty")
    public List<Map<String, String>> getAllFaculty() {
        return EnumUtils.getNamesAndValues(Faculty.class);
    }

    @GetMapping("/identity-type")
    public List<String> getAllIdentityType() {
        return Arrays.stream(IdentityType.availableValues).toList();
    }

    @GetMapping("/student-status")
    public List<String> getAllStudentStatus() {
        return Arrays.stream(EnumUtils.getNames(Status.class)).toList();
    }

    @GetMapping("/gender")
    public List<String> getAllGender() {
        return Arrays.stream(EnumUtils.getNames(Gender.class)).toList();
    }
}
