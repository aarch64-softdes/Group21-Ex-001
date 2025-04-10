package com.tkpm.sms.application.dto.request.course;

import com.tkpm.sms.application.annotation.RequiredConstraint;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseScheduleDto {
    @RequiredConstraint(field = "Date of week")
    String dateOfWeek;
    @RequiredConstraint(field = "Start time")
    int startTime;
    @RequiredConstraint(field = "End time")
    int endTime;

    @Override
    public String toString() {
        return dateOfWeek + "(" + startTime + "-" + endTime + ")";
    }
}
