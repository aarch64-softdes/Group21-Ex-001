package com.tkpm.sms.controller;

import com.tkpm.sms.enums.EnumUtils;
import com.tkpm.sms.enums.Faculty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("/metadata")
public class MetadataController {
    @GetMapping("/faculty")
    public List<String> getAllFaculty() {
        return Arrays.stream(EnumUtils.getNames(Faculty.class)).toList();
    }
}
