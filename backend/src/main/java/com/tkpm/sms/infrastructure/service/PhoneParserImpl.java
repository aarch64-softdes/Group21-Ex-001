package com.tkpm.sms.infrastructure.service;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.tkpm.sms.application.dto.request.phone.PhoneRequestDto;
import com.tkpm.sms.application.dto.response.PhoneDto;
import com.tkpm.sms.application.exception.ApplicationException;
import com.tkpm.sms.domain.exception.ErrorCode;
import com.tkpm.sms.application.service.interfaces.PhoneParser;
import com.tkpm.sms.domain.exception.InvalidPhoneNumberException;
import com.tkpm.sms.domain.repository.SettingRepository;
import com.tkpm.sms.domain.valueobject.Phone;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class PhoneParserImpl implements PhoneParser {
    private final SettingRepository settingRepository;

    public PhoneParserImpl(SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }

    public boolean isValidCountryCode(String countryCode) {
        return settingRepository.getPhoneSetting().contains(countryCode);
    }

    public String parsePhoneNumber(String phone, String countryCode) {
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber phoneNumber = phoneNumberUtil.parse(
                    phone, countryCode);

            boolean isValidPhoneNumber = phoneNumberUtil.isValidNumberForRegion(phoneNumber, countryCode);
            if (isValidPhoneNumber) {
                return phoneNumberUtil.format(
                                phoneNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL)
                        .replaceAll(" ", "");
            } else {
                responseInvalidPhone(phone, countryCode);
            }
        } catch (Exception e) {
            responseInvalidPhone(phone, countryCode);
        }

        return null;
    }

    // Convert String to Domain Phone Value Object
    public Phone parsePhoneToPhone(String phoneString) {
        if (phoneString == null) {
            return null;
        }

        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber phoneNumber = phoneNumberUtil.parse(phoneString, null);
            String countryCode = phoneNumberUtil.getRegionCodeForNumber(phoneNumber);
            String nationalNumber = String.valueOf(phoneNumber.getNationalNumber());

            return Phone.of(countryCode, nationalNumber, phoneString);
        } catch (Exception e) {
            throw new InvalidPhoneNumberException(
                    String.format("Invalid phone number: %s", phoneString));
        }
    }

    public PhoneDto parsePhoneToPhoneDto(String phone) {
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber phoneNumber = phoneNumberUtil.parse(
                    phone, null);
            return new PhoneDto(
                    phone, phoneNumberUtil.getRegionCodeForNumber(phoneNumber)
            );

        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.INVALID_PHONE.withMessage(
                    String.format("Invalid phone number: %s", phone)));
        }
    }

    public PhoneRequestDto parsePhoneToPhoneRequestDto(String phone) {
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber phoneNumber = phoneNumberUtil.parse(
                    phone, null);
            return new PhoneRequestDto(
                    phoneNumberUtil.getRegionCodeForNumber(phoneNumber),
                    phone
            );

        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.INVALID_PHONE.withMessage(
                    String.format("Invalid phone number: %s", phone)));
        }
    }

    private void responseInvalidPhone(String phone, String countryCode) {
        String errorMessage = String.format("Invalid phone number: %s", phone);
        if (Objects.isNull(countryCode) || countryCode.isEmpty()) {
            errorMessage = errorMessage + ", missing country code";
        } else {
            errorMessage = errorMessage + String.format(" with country code: %s", countryCode);
        }

        throw new InvalidPhoneNumberException(errorMessage);
    }
}