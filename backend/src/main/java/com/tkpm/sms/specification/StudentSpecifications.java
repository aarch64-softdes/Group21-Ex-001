package com.tkpm.sms.specification;

import com.tkpm.sms.dto.request.student.StudentCollectionRequest;
import com.tkpm.sms.entity.Student;
import com.tkpm.sms.infrastructure.persistence.entity.FacultyEntity;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

public class StudentSpecifications {
    public static Specification<Student> withFilters(StudentCollectionRequest request) {
        return Specification.where((hasStudentId(request.getSearch()))
                        .or(hasName(request.getSearch())))
                .and(belongToFaculty(request.getFaculty()));
    }

    private static Specification<Student> hasName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (Objects.isNull(name)) {
                return null;
            }

            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("name")),
                    "%" + name.toLowerCase() + "%");
        };
    }

    private static Specification<Student> hasStudentId(String studentId) {
        return (root, query, criteriaBuilder) -> {
            if (Objects.isNull(studentId)) {
                return null;
            }

            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("studentId")),
                    "%" + studentId.toLowerCase() + "%");
        };
    }

    private static Specification<Student> belongToFaculty(String faculty) {
        return (root, query, criteriaBuilder) -> {
            if (Objects.isNull(faculty)) {
                return null;
            }

            Join<Student, FacultyEntity> studentFacultyJoin = root.join("faculty");

            return criteriaBuilder.like(
                    criteriaBuilder
                            .lower(studentFacultyJoin.get("name")),
                    "%" + faculty.toLowerCase() + "%");
        };
    }
}
