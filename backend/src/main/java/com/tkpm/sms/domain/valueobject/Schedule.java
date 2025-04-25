package com.tkpm.sms.domain.valueobject;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Schedule {
    Integer startTime;
    Integer endTime;
    String day;

    @Override
    public String toString() {
        return String.format("%s(%d-%d)", day, startTime, endTime);
    }

    public static Schedule of(Integer startTime, Integer endTime, String day) {
        return new Schedule(startTime, endTime, day);
    }

    public static Schedule of(String schedule) {
        var dateOfWeek = schedule.substring(0, schedule.indexOf("("));
        int startTime = Integer
                .parseInt(schedule.substring(schedule.indexOf("(") + 1, schedule.indexOf("-")));
        int endTime = Integer
                .parseInt(schedule.substring(schedule.indexOf("-") + 1, schedule.indexOf(")")));

        log.info("Parsed schedule: dateOfWeek={}, startTime={}, endTime={}", dateOfWeek, startTime,
                endTime);

        return Schedule.of(startTime, endTime, dateOfWeek);
    }

    public boolean isOverlapping(Schedule other) {
        log.info("Checking if {} overlaps with {}", this, other);
        return Objects.equals(this.day, other.day)
                && (this.startTime <= other.endTime && this.endTime >= other.startTime);
    }
}
