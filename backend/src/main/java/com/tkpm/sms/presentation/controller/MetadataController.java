package com.tkpm.sms.presentation.controller;

import com.tkpm.sms.domain.enums.Gender;
import com.tkpm.sms.domain.enums.IdentityType;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/metadata")
public class MetadataController {
    @GetMapping("/identity-type")
    public List<String> getAllIdentityType() {
        return Arrays.stream(IdentityType.values())
            .map(IdentityType::getDisplayName)
            .collect(Collectors.toList());
    }

    @GetMapping("/gender")
    public List<String> getAllGender() {
        return Arrays.stream(Gender.values())
            .map(Gender::getDisplayName)
            .collect(Collectors.toList());
    }
}