package com.tkpm.sms.application.mapper;

import com.tkpm.sms.application.dto.request.enrollment.TranscriptUpdateRequestDto;
import com.tkpm.sms.application.dto.response.enrollment.TranscriptDto;
import com.tkpm.sms.domain.model.Transcript;

public interface TranscriptMapper {
    TranscriptDto toTranscriptDto(Transcript transcript);

    Transcript toTranscript(TranscriptDto transcriptDto);

    Transcript toTranscript(TranscriptUpdateRequestDto transcript);
}
