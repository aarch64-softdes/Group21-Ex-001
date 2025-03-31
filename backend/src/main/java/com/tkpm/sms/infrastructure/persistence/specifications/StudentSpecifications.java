package com.tkpm.sms.infrastructure.persistence.specifications;

import com.tkpm.sms.infrastructure.persistence.entity.*;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

public class StudentSpecifications {
    public static Specification<StudentEntity> withFilters(String search, String faculty) {
        return Specification
                .where(hasStudentIdOrName(search))
                .and(belongToFaculty(faculty));
    }

    private static Specification<StudentEntity> hasStudentIdOrName(String search) {
        return (root, query, criteriaBuilder) -> {
            if (Objects.isNull(search) || search.trim().isEmpty()) {
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
            if (Objects.isNull(faculty) || faculty.trim().isEmpty()) {
                return null;
            }

            // Use fetch join to optimize the query and reduce N+1 problem
            root.fetch("faculty", JoinType.LEFT);

            Join<StudentEntity, FacultyEntity> studentFacultyJoin = root.join("faculty", JoinType.LEFT);

            return criteriaBuilder.like(
                    criteriaBuilder.lower(studentFacultyJoin.get("name")),
                    "%" + faculty.toLowerCase() + "%");
        };
    }

    public static Specification<StudentEntity> fetchRelatedEntities() {
        return (root, query, criteriaBuilder) -> {
            // Only apply fetch joins when not doing a count query
            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                root.fetch("program", JoinType.LEFT);
                root.fetch("status", JoinType.LEFT);
                root.fetch("faculty", JoinType.LEFT);
                root.fetch("permanentAddress", JoinType.LEFT);
                root.fetch("temporaryAddress", JoinType.LEFT);
                root.fetch("mailingAddress", JoinType.LEFT);
                root.fetch("identity", JoinType.LEFT);
            }

            return criteriaBuilder.conjunction(); // Always true predicate
        };
    }
}