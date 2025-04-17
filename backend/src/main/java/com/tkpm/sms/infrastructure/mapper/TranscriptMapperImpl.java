package com.tkpm.sms.infrastructure.mapper;

import com.tkpm.sms.application.dto.request.enrollment.TranscriptUpdateRequestDto;
import com.tkpm.sms.application.dto.response.enrollment.TranscriptDto;
import com.tkpm.sms.application.mapper.TranscriptMapper;
import com.tkpm.sms.domain.model.Transcript;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TranscriptMapperImpl extends TranscriptMapper {
    TranscriptDto toTranscriptDto(Transcript transcript);

    Transcript toTranscript(TranscriptDto transcriptDto);

    Transcript toTranscript(TranscriptUpdateRequestDto transcript);
}
