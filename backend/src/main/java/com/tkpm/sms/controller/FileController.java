package com.tkpm.sms.controller;

import com.tkpm.sms.service.FileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/files/")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileController {
    FileService fileService;

    @NonFinal
    @Value("${filetype.json}")
    String FILE_TYPE_JSON;

    @NonFinal
    @Value("${mediatype.json}")
    String MEDIA_TYPE_JSON;

    @NonFinal
    @Value("${mediatype.csv}")
    String MEDIA_TYPE_CSV;

    @GetMapping("/export/{fileType}")
    public ResponseEntity<byte[]> exportStudentsToFile(@PathVariable String fileType) {
        byte[] data = fileService.export(fileType);
        String filename = generateFilename(fileType);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType(
                        fileType.equals(FILE_TYPE_JSON) ? MEDIA_TYPE_JSON : MEDIA_TYPE_CSV
                ))
                .contentLength(data.length)
                .body(data);
    }

    private String generateFilename(String extension) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HHmmss").format(new Date());
        return "students" + "_" + timestamp + "." + extension;
    }
}
