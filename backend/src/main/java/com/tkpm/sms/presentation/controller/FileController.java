package com.tkpm.sms.presentation.controller;

import com.tkpm.sms.application.dto.response.common.ApplicationResponseDto;
import com.tkpm.sms.application.service.interfaces.FileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/files/")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileController {
    FileService fileService;
    
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

    @PostMapping("/import")
    public ResponseEntity<ApplicationResponseDto<Void>> importStudentFile(
            @RequestParam("format") String format,
            @RequestParam("file") MultipartFile multipartFile
    ) {
        fileService.importStudentFile(format, multipartFile);
        return ResponseEntity.ok().body(ApplicationResponseDto.success());
    }

    private String generateFilename(String extension) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HHmmss").format(new Date());
        return "students" + "_" + timestamp + "." + extension;
    }
}