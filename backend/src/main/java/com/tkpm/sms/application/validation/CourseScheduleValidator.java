package com.tkpm.sms.application.validation;

import com.tkpm.sms.application.annotation.CourseScheduleConstraint;
import com.tkpm.sms.application.dto.request.course.CourseScheduleDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class CourseScheduleValidator
        implements
            ConstraintValidator<CourseScheduleConstraint, CourseScheduleDto> {
    private final String[] daysOfWeek = {"T2", "T3", "T4", "T5", "T6", "T7", "CN"};
    private final int MIN_START_TIME = 1;
    private final int MAX_START_TIME = 15;
    @Override
    public void initialize(CourseScheduleConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(CourseScheduleDto value, ConstraintValidatorContext context) {
        if (Objects.isNull(value)) {
            return false;
        }

        if (value.getStartTime() < MIN_START_TIME || value.getStartTime() > MAX_START_TIME) {
            buildTemplate("Start time must be between 1 and 10", "startTime", context);
            return false;
        }

        if (value.getEndTime() < MIN_START_TIME || value.getEndTime() > MAX_START_TIME) {
            buildTemplate("End time must be between 1 and 10", "endTime", context);
            return false;
        }

        if (value.getStartTime() >= value.getEndTime()) {
            buildTemplate("Start time must be less than end time", "startTime", context);
            return false;
        }

        for (String day : daysOfWeek) {
            if (!day.equals(value.getDateOfWeek())) {
                return true;
            }
        }

        buildTemplate("Invalid day of week", "dateOfWeek", context);

        return false;
    }

    private void buildTemplate(String message, String propertyNode,
            ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addPropertyNode(propertyNode)
                .addConstraintViolation();
    }
}
