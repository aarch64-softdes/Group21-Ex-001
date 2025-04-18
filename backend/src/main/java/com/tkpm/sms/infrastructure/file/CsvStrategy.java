package com.tkpm.sms.infrastructure.file;

import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.tkpm.sms.application.dto.response.student.StudentFileDto;
import com.tkpm.sms.application.service.interfaces.StudentService;
import com.tkpm.sms.domain.exception.ErrorCode;
import com.tkpm.sms.domain.exception.FileProcessingException;
import com.tkpm.sms.domain.service.FileStrategy;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component("csvStrategy")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CsvStrategy implements FileStrategy {
    @Qualifier("csvMapper")
    CsvMapper csvMapper;
    CsvSchema csvSchema;

    @Override
    public byte[] toBytes(Iterable<?> data) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            csvMapper.writer(csvSchema).writeValue(outputStream, data);
        } catch (IOException e) {
            log.error("Failed to export with csv format", e);

            throw new FileProcessingException("Failed to export with csv format", ErrorCode.FAIL_TO_EXPORT_FILE);
        }

        return outputStream.toByteArray();
    }

    @Override
    public <T> List<T> convert(Object file, Class<T> clazz) {
        if (!(file instanceof MultipartFile multipartFile)) {
            throw new FileProcessingException("Invalid file type", ErrorCode.INVALID_FILE_FORMAT);
        }
        CsvSchema schema = csvMapper.schemaFor(clazz).withHeader();

        try {
            // Read values using the schema
            var iterator = csvMapper.readerFor(clazz).with(schema)
                    .<T>readValues(multipartFile.getInputStream());
            return iterator.readAll();
        } catch (IOException e) {
            log.info("Error reading CSV file", e);
            throw new FileProcessingException("Error reading CSV file", ErrorCode.FAIL_TO_IMPORT_FILE);
        }
    }
    @Override
    public String getFormat() {
        return "csv";
    }
}