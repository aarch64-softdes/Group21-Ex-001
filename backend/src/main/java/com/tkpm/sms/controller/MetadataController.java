package com.tkpm.sms.controller;

import com.tkpm.sms.enums.EnumUtils;
import com.tkpm.sms.enums.Faculty;
import com.tkpm.sms.enums.Gender;
import com.tkpm.sms.enums.Status;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/metadata")
public class MetadataController {
    @GetMapping("/faculty")
    public List<String> getAllFaculty() {
        return Arrays.stream(EnumUtils.getNames(Faculty.class)).toList();
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
