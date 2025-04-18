package com.tkpm.sms.application.mapper;

import com.tkpm.sms.application.dto.request.enrollment.TranscriptUpdateRequestDto;
import com.tkpm.sms.application.dto.response.enrollment.TranscriptDto;
import com.tkpm.sms.application.dto.response.enrollment.ScoreDto;
import com.tkpm.sms.domain.valueobject.Score;

public interface ScoreMapper {
    TranscriptDto toScoreDto(Score score);

    ScoreDto toScoreMinimalDto(Score score);

    Score toScore(TranscriptDto transcriptDto);

    Score toScore(TranscriptUpdateRequestDto transcript);
}
