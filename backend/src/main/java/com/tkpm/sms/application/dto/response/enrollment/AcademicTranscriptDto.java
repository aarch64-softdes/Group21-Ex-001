package com.tkpm.sms.application.dto.response.enrollment;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AcademicTranscriptDto {
    String studentId;
    String studentName;
    String courseName;
    LocalDate studentDob;
    Double gpa;

    List<TranscriptDto> transcriptList;
}
