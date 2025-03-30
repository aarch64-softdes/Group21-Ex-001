package com.tkpm.sms.domain.valueobject;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Objects;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Phone {
    String countryCode;
    String number;
    String formattedNumber;

    private Phone(String countryCode, String number, String formattedNumber) {
        this.countryCode = countryCode;
        this.number = number;
        this.formattedNumber = formattedNumber;
    }

    public static Phone of(String countryCode, String number, String formattedNumber) {
        return new Phone(countryCode, number, formattedNumber);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Phone phone = (Phone) o;
        return Objects.equals(formattedNumber, phone.formattedNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(formattedNumber);
    }

    @Override
    public String toString() {
        return formattedNumber;
    }
}