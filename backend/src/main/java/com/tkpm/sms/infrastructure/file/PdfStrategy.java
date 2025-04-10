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
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component("pdfStrategy")
@RequiredArgsConstructor
public class PdfStrategy implements FileStrategy {

    @Qualifier("jsonMapper")
    private final ObjectMapper jsonMapper;
    private final DocumentTemplateProcessingService templateService;

    @Value("${app.academic_transcript_template.student:templates/student_template.docx}")
    private String studentTemplatePath;

    @Override
    public byte[] export(Iterable<?> data) {
        try {
            Map<String, Object> templateData = new HashMap<>();
            templateData.put("items", data);
            templateData.put("timestamp", java.time.LocalDateTime.now().toString());
            templateData.put("reportTitle", "Student Data Export");

            return templateService.processTemplateAsPdf(studentTemplatePath, templateData);
        } catch (Exception e) {
            log.error("Failed to export with PDF format", e);
            throw new FileProcessingException("Failed to export with PDF format", ErrorCode.FAIL_TO_EXPORT_FILE);
        }
    }

    @Override
    public void importFile(Object file) {
        throw new FileProcessingException("PDF import is not supported", ErrorCode.INVALID_FILE_FORMAT);
    }

    @Override
    public String getFormat() {
        return "pdf";
    }
}