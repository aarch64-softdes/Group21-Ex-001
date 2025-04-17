package com.tkpm.sms.infrastructure.persistence.repository;

import com.tkpm.sms.domain.model.Score;
import com.tkpm.sms.domain.repository.TranscriptRepository;
import com.tkpm.sms.infrastructure.persistence.jpa.TranscriptJpaRepository;
import com.tkpm.sms.infrastructure.persistence.mapper.ScorePersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TranscriptRepositoryImpl implements TranscriptRepository {
    private final TranscriptJpaRepository jpaRepository;
    private final ScorePersistenceMapper mapper;

    @Override
    public List<Score> findAllByStudentId(String studentId) {
        return jpaRepository.findAllByStudentId(studentId).stream().map(mapper::toDomain).toList();
    }

    @Override
    public Score save(Score score) {
        var transcriptEntity = mapper.toEntity(score);

        return mapper.toDomain(jpaRepository.save(transcriptEntity));
    }

    @Override
    public boolean existsByEnrollmentId(Integer enrollmentId) {
        return jpaRepository.existsByEnrollmentId(enrollmentId);
    }
}
