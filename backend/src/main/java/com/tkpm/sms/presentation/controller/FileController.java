package com.tkpm.sms.presentation.controller;

import com.tkpm.sms.application.dto.response.common.ApplicationResponseDto;
import com.tkpm.sms.application.service.interfaces.FileService;
import com.tkpm.sms.infrastructure.service.DocumentTemplateProcessingService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/files")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileController {
    FileService fileService;
    DocumentTemplateProcessingService documentService;

    /**
     * Export students to a file in the specified format (CSV or JSON).
     * @param format The format of the file to export (CSV or JSON).
     */
    /**
     * Export students to a file in the specified format (CSV or JSON).
     * 
     * @param format The format of the file to export (CSV or JSON).
     */
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportStudentsToFile(@RequestParam("format") String format) {
        byte[] data = fileService.exportStudentFile(format);
        String filename = generateFilename(format);

        String mediaType = format.equalsIgnoreCase("json")
                ? "application/json"
                : "text/csv";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType(mediaType))
                .contentLength(data.length)
                .body(data);
    }

    /**
     * Generate a PDF document from the HTML template with server data
     * 
     * @param studentId Optional student ID to generate document for a specific
     *                  student (if null, uses sample data)
     * @return PDF document as byte array
     */
    @GetMapping("/export-html-pdf")
    public ResponseEntity<byte[]> exportHtmlAsPdf(
            @RequestParam(value = "studentId", required = false) String studentId) {
        try {
            Map<String, Object> data;
            // if (studentId != null && !studentId.isEmpty()) {
            // data = getStudentData(studentId);
            // } else {
            // data = getSampleData();
            // }

            data = getSampleData();
            byte[] pdfBytes = documentService.processTemplateAsHtmlToPdf("templates/template.html", data);

            String timestamp = new SimpleDateFormat("yyyy-MM-dd_HHmmss").format(new Date());
            String filename = "academic_record_" + (studentId != null ? studentId + "_" : "") + timestamp + ".pdf";

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);
        } catch (Exception e) {
            throw new RuntimeException("Failed to export from HTML template", e);
        }
    }

    @PostMapping("/import")
    public ResponseEntity<ApplicationResponseDto<Void>> importStudentFile(
            @RequestParam("format") String format,
            @RequestParam("file") MultipartFile multipartFile) {
        fileService.importStudentFile(format, multipartFile);
        return ResponseEntity.ok().body(ApplicationResponseDto.success());
    }

    /**
     * Generate sample data for templates
     * 
     * @return Map of sample data
     */
    private Map<String, Object> getSampleData() {
        Map<String, Object> data = new HashMap<>();

        // Student information
        data.put("studentId", "SV001_ID");
        data.put("studentName", "John Doe");
        data.put("className", "Computer Science");
        data.put("birthDate", "2000-01-01");
        data.put("gpa", 3.85);

        // Academic information
        List<Map<String, Object>> subjects = List.of(
                Map.of(
                        "courseId", "MATH101",
                        "name", "Mathematics",
                        "credits", 4,
                        "finalScore", 85.5),
                Map.of(
                        "courseId", "COMP201",
                        "name", "Data Structures",
                        "credits", 3,
                        "finalScore", 92.0),
                Map.of(
                        "courseId", "PHYS101",
                        "name", "Physics I",
                        "credits", 4,
                        "finalScore", 88.0),
                Map.of(
                        "courseId", "ENGL101",
                        "name", "English Composition",
                        "credits", 3,
                        "finalScore", 90.0));

        data.put("subjects", subjects);
        data.put("totalCredits", 14);
        data.put("semester", "Spring 2025");

        return data;
    }

    private String generateFilename(String extension) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HHmmss").format(new Date());
        return "students" + "_" + timestamp + "." + extension;
    }

    @PostMapping("/import/transcript")
    public ResponseEntity<ApplicationResponseDto<Void>> importTranscriptFile(
            @RequestParam("format") String format,
            @RequestParam("file") MultipartFile multipartFile
    ) {
        fileService.importTranscriptFile(format, multipartFile);
        return ResponseEntity.ok().body(ApplicationResponseDto.success());
    }
}