package com.tkpm.sms.strategies;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tkpm.sms.exceptions.ApplicationException;
import com.tkpm.sms.exceptions.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
@Component
public class JsonExportStrategy implements FileExportStrategy {

    @Override
    public byte[] export(Iterable<?> data) {
        var jsonMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            jsonMapper.writerWithDefaultPrettyPrinter().writeValue(outputStream, data);
        } catch (IOException e) {
            log.error("Failed to export file with Json format", e);
            throw new ApplicationException(ErrorCode.FAIL_TO_EXPORT_FILE.withMessage("Failed to export file with Json format"));
        }

        return outputStream.toByteArray();
    }

    @Override
    public String getFormat() {
        return "json";
    }
}
