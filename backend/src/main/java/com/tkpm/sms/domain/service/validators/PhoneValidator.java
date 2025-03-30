package com.tkpm.sms.domain.service.validators;

import com.tkpm.sms.domain.exception.InvalidPhoneNumberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class PhoneValidator {
    private static final Pattern INTERNATIONAL_PHONE_PATTERN = 
        Pattern.compile("^\\+\\d{1,4}\\d{6,14}$");
    
    public void validatePhoneFormat(String phoneNumber) {
        if (phoneNumber == null || !INTERNATIONAL_PHONE_PATTERN.matcher(phoneNumber).matches()) {
            throw new InvalidPhoneNumberException(
                "Phone number must be in international format (e.g., +84123456789)");
        }
    }
    
    public void validateCountryCode(String countryCode, java.util.List<String> supportedCountryCodes) {
        if (countryCode == null || !supportedCountryCodes.contains(countryCode)) {
            throw new InvalidPhoneNumberException(
                String.format("Country code %s is not supported", countryCode));
        }
    }
}