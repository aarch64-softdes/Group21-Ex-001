package com.tkpm.sms.infrastructure.persistence.repository;

import com.tkpm.sms.domain.model.Transcript;
import com.tkpm.sms.domain.repository.TranscriptRepository;
import com.tkpm.sms.infrastructure.persistence.jpa.TranscriptJpaRepository;
import com.tkpm.sms.infrastructure.persistence.mapper.TranscriptPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@RequiredArgsConstructor
public class TranscriptRepositoryImpl implements TranscriptRepository {
    private final TranscriptJpaRepository jpaRepository;
    private final TranscriptPersistenceMapper mapper;

    @Override
    public List<Transcript> findAllByStudentId(String studentId) {
        return jpaRepository.findAllByStudentId(studentId).stream().map(mapper::toDomain).toList();
    }

    @Override
    public Transcript save(Transcript transcript) {
        var transcriptEntity = mapper.toEntity(transcript);

        return mapper.toDomain(jpaRepository.save(transcriptEntity));
    }

    @Override
    public boolean existsByEnrollmentId(Integer enrollmentId) {
        return jpaRepository.existsByEnrollmentId(enrollmentId);
    }
}
