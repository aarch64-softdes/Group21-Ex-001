package com.tkpm.sms.utils;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.tkpm.sms.dto.request.phone.PhoneRequestDto;
import com.tkpm.sms.dto.response.PhoneDto;
import com.tkpm.sms.exceptions.ApplicationException;
import com.tkpm.sms.exceptions.ErrorCode;

import java.util.Objects;

public class PhoneUtils {
    public static String ParsePhoneNumber(String phone, String countryCode) {
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        try{
            Phonenumber.PhoneNumber phoneNumber = phoneNumberUtil.parse(
                    phone, countryCode);
            boolean isValidPhoneNumber = phoneNumberUtil.isValidNumber(phoneNumber);
            if(isValidPhoneNumber){
                return phoneNumberUtil.format(
                                phoneNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL).
                        replaceAll(" ", "");
            }
            else{
                ResponseInvalidPhone(phone, countryCode);
            }
        }catch (Exception e){
            ResponseInvalidPhone(phone, countryCode);
        }

        return null;
    }

    public static PhoneDto parsePhoneToPhoneDto(String phone) {
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        try{
            Phonenumber.PhoneNumber phoneNumber = phoneNumberUtil.parse(
                    phone, null);
            return new PhoneDto(
                    phone, phoneNumberUtil.getRegionCodeForNumber(phoneNumber)
            );

        }catch (Exception e){
            throw new ApplicationException(ErrorCode.INVALID_PHONE.withMessage(
                    String.format("Invalid phone number: %s", phone)));
        }
    }

    public static PhoneRequestDto parsePhoneToPhoneRequestDto(String phone){
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        try{
            Phonenumber.PhoneNumber phoneNumber = phoneNumberUtil.parse(
                    phone, null);
            return new PhoneRequestDto(
                    phone, phoneNumberUtil.getRegionCodeForNumber(phoneNumber)
            );

        }catch (Exception e){
            throw new ApplicationException(ErrorCode.INVALID_PHONE.withMessage(
                    String.format("Invalid phone number: %s", phone)));
        }
    }

    private static void ResponseInvalidPhone(String phone, String countryCode) {
        String errorMessage = String.format("Invalid phone number: %s", phone);
        if(Objects.isNull(countryCode) || countryCode.isEmpty()){
            errorMessage = errorMessage + ", missing country code";
        }else{
            errorMessage = errorMessage + String.format(" with country code: %s", countryCode);
        }

        throw new ApplicationException(ErrorCode.INVALID_PHONE.withMessage(errorMessage));
    }
}
