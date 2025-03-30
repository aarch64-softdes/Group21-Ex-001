package com.tkpm.sms.infrastructure.persistence.specifications;

import com.tkpm.sms.infrastructure.persistence.entity.FacultyEntity;
import com.tkpm.sms.infrastructure.persistence.entity.StudentEntity;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

public class StudentSpecifications {
    public static Specification<StudentEntity> withFilters(String search, String faculty) {
        return Specification.where((hasStudentIdOrName(search))
                .and(belongToFaculty(faculty)));
    }

    private static Specification<StudentEntity> hasStudentIdOrName(String search) {
        return (root, query, criteriaBuilder) -> {
            if (Objects.isNull(search)) {
                return null;
            }

            String lowercaseSearch = "%" + search.toLowerCase() + "%";

            return criteriaBuilder.or(
                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("studentId")),
                            lowercaseSearch),
                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("name")),
                            lowercaseSearch)
            );
        };
    }

    private static Specification<StudentEntity> belongToFaculty(String faculty) {
        return (root, query, criteriaBuilder) -> {
            if (Objects.isNull(faculty)) {
                return null;
            }

            Join<StudentEntity, FacultyEntity> studentFacultyJoin = root.join("faculty");

            return criteriaBuilder.like(
                    criteriaBuilder
                            .lower(studentFacultyJoin.get("name")),
                    "%" + faculty.toLowerCase() + "%");
        };
    }
}