package com.tkpm.sms.infrastructure.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tkpm.sms.application.service.interfaces.DocumentTemplateProcessingService;
import com.tkpm.sms.domain.exception.ErrorCode;
import com.tkpm.sms.domain.exception.FileProcessingException;
import com.tkpm.sms.domain.service.FileStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component("pdfStrategy")
@RequiredArgsConstructor
public class PdfStrategy implements FileStrategy {
    private final DocumentTemplateProcessingService templateService;

    @Value("${app.academic_transcript_template.path:templates/template.xlsx}")
    private String templatePath;

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
                log.error("You are here!");
                throw new IllegalArgumentException("Data must be a List containing exactly 2 elements");
            }

            Object first = dataList.iterator().next();
            Object second = ((List<?>) data).get(1);

            if (!(first instanceof String) || !(second instanceof Map)) {
                log.error("{}, {}", first instanceof String, second instanceof Map);
                throw new IllegalArgumentException("First element must be String, second must be Map");
            }

            String templatePath = (String) first;
            @SuppressWarnings("unchecked")
            Map<String, Object> templateData = (Map<String, Object>) second;

            return templateService.processTemplateAsPdf(templatePath, templateData);
        } catch (Exception e) {
            log.error("Failed to export with PDF format", e);
            throw new FileProcessingException("Failed to export with PDF format", ErrorCode.FAIL_TO_EXPORT_FILE);
        }
    }

    /**
     * Creates the template data model from the input data collection
     */
    private Map<String, Object> createTemplateDataModel(Iterable<?> data) {
        Map<String, Object> templateData = new HashMap<>();

        // Add core data
        templateData.put("items", data);
        templateData.put("timestamp", java.time.LocalDateTime.now().toString());
        templateData.put("reportTitle", "Academic Transcript");

        // Add sample student data (in a real app, this would come from the controller)
        templateData.put("studentName", "Nguyen Van A");
        templateData.put("studentId", "2012345");
        templateData.put("className", "12A1");
        templateData.put("semester", "HK1 2023-2024");
        templateData.put("gpa", 8.64);
        templateData.put("rank", "Excellent");

        return templateData;
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