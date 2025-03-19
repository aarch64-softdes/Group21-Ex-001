package com.tkpm.sms.service;

import com.tkpm.sms.entity.Citizenship;
import com.tkpm.sms.mapper.CitizenshipMapper;
import com.tkpm.sms.repository.CitizenshipRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CitizenshipService {
    CitizenshipMapper citizenshipMapper;
    CitizenshipRepository citizenshipRepository;

    public Citizenship createOrGetCitizenship(String name) {
        if (name == null || name.isBlank()) {
            return null;
        }

        // Normalize country name to prevent duplicates with different casing
        String normalizedName = normalizeCountryName(name);

        // Try to find existing citizenship first
        return citizenshipRepository.findCitizenshipByCountryNameEqualsIgnoreCase(normalizedName)
                .orElseGet(() -> {
                    // Create new if not found
                    Citizenship citizenship = citizenshipMapper.createCitizenship(normalizedName);
                    return citizenshipRepository.save(citizenship);
                });
    }

    private String normalizeCountryName(String name) {
        if (name == null || name.isBlank()) {
            return "";
        }

        String[] words = name.trim().toLowerCase().split("\\s+");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                if (result.length() > 0) {
                    result.append(" ");
                }
                result.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1));
            }
        }

        return result.toString();
    }
}
