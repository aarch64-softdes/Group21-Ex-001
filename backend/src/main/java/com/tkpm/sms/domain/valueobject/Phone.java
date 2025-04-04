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

    private Phone(String countryCode, String number) {
        this.countryCode = countryCode;
        this.number = number;
    }

    public static Phone of(String countryCode, String number) {
        return new Phone(countryCode, number);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Phone phone = (Phone) o;
        return Objects.equals(number, phone.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }

    @Override
    public String toString() {
        return number;
    }
}