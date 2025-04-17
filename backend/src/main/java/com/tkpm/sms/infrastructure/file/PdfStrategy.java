package com.tkpm.sms.infrastructure.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tkpm.sms.domain.exception.ErrorCode;
import com.tkpm.sms.domain.exception.FileProcessingException;
import com.tkpm.sms.domain.service.FileStrategy;
import com.tkpm.sms.infrastructure.service.DocumentTemplateProcessingService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component("pdfStrategy")
@RequiredArgsConstructor
public class PdfStrategy implements FileStrategy {
    private final DocumentTemplateProcessingService templateService;

    /*
     * Convert the input data to a PDF file
     * 
     * @param data the input data. It should be a List containing two elements:
     * 1. The path to the template file (String)
     * 2. The data to be used in the template (Map<String, Object>)
     * 
     * @return the generated PDF file as a byte array
     */
    @Override
    public byte[] toBytes(Iterable<?> data) {
        try {
            if (!(data instanceof List<?> dataList) || dataList.size() != 2) {
                throw new IllegalArgumentException("Data must be a List containing exactly 2 elements");
            }

            Object first = dataList.iterator().next();
            Object second = ((List<?>) data).get(1);

            if (!(first instanceof String) || !(second instanceof Map)) {
                throw new IllegalArgumentException("First element must be String, second must be Map");
            }

            String templatePath = (String) first;
            @SuppressWarnings("unchecked")
            Map<String, Object> templateData = (Map<String, Object>) second;

            // TODO: Add sample subjects data if not already present
            // TODO: (This can be removed once actual data is provided externally)
            if (!templateData.containsKey("subjects")) {
                List<Map<String, Object>> subjects = List.of(
                        Map.of(
                                "courseId", "MATH101",
                                "name", "Mathematics",
                                "finalScore", 85.5),
                        Map.of(
                                "courseId", "LIT201",
                                "name", "Literature",
                                "finalScore", 92.0),
                        Map.of(
                                "courseId", "HIS301",
                                "name", "History",
                                "finalScore", 88.0),
                        Map.of(
                                "courseId", "PHY401",
                                "name", "Physics",
                                "finalScore", 90.0),
                        Map.of(
                                "courseId", "CHE501",
                                "name", "Chemistry",
                                "finalScore", 95.0),
                        Map.of(
                                "courseId", "BIO601",
                                "name", "Biology",
                                "finalScore", 89.0),
                        Map.of(
                                "courseId", "ENG701",
                                "name", "English",
                                "finalScore", 91.0));

                // Add the subjects to the template data
                templateData.put("subjects", subjects);
            }

            // Use the HTML processing approach
            return templateService.processTemplateAsHtmlToPdf(templatePath, templateData);
        } catch (Exception e) {
            log.error("Failed to export with PDF format", e);
            throw new FileProcessingException("Failed to export with PDF format", ErrorCode.FAIL_TO_EXPORT_FILE);
        }
    }

    @Override
    public <T> List<T> convert(Object file, Class<T> clazz) {
        throw new UnsupportedOperationException("Unsupported operation for PDF format");
    }

    @Override
    public String getFormat() {
        return "pdf";
    }
}