package com.tkpm.sms.infrastructure.service;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.tkpm.sms.application.dto.request.phone.PhoneRequestDto;
import com.tkpm.sms.application.dto.response.PhoneDto;
import com.tkpm.sms.application.service.interfaces.PhoneParser;
import com.tkpm.sms.domain.exception.GenericDomainException;
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
            String internationalNumber = phoneNumberUtil.format(
                            phoneNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL)
                    .replaceAll(" ", "");

            return Phone.of(countryCode, internationalNumber);
        } catch (Exception e) {
            throw new InvalidPhoneNumberException(
                    String.format("Invalid phone number: %s", phoneString), e);
        }
    }

    public Phone parsePhoneNumberToPhone(String phoneString, String countryCode) {
        if (phoneString == null) {
            return null;
        }

        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber phoneNumber = phoneNumberUtil.parse(phoneString, countryCode);
            boolean isValidPhoneNumber = phoneNumberUtil.isValidNumberForRegion(phoneNumber, countryCode);

            if (!isValidPhoneNumber) {
                responseInvalidPhone(phoneString, countryCode);
            }

            String internationalNumber = phoneNumberUtil.format(
                            phoneNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL)
                    .replaceAll(" ", "");

            return Phone.of(countryCode, internationalNumber);
        } catch (Exception e) {
            responseInvalidPhone(phoneString, countryCode);
        }

        return null;
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
            throw new InvalidPhoneNumberException(
                    String.format("Invalid phone number: %s", phone), e);
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
            throw new InvalidPhoneNumberException(
                    String.format("Invalid phone number: %s", phone), e);
        }
    }

    private void responseInvalidPhone(String phone, String countryCode) {
        String errorMessage = String.format("Invalid phone number: %s", phone);
        if (Objects.isNull(countryCode) || countryCode.isEmpty()) {
            errorMessage = errorMessage + ", missing country code";
        } else {
            errorMessage = errorMessage + String.format(" with country code: %s", countryCode);
        }

        throw new GenericDomainException(errorMessage);
    }
}