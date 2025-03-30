package com.tkpm.sms.infrastructure.config;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tkpm.sms.dto.response.student.StudentFileDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class FileMapperConfig {
    @Bean(name = "jsonMapper")
    @Primary
    public ObjectMapper jsonMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    }

    @Bean(name = "csvMapper")
    public CsvMapper csvMapper() {
        var csvMapper = new CsvMapper();
        csvMapper.findAndRegisterModules();
        csvMapper = new CsvMapper();
        csvMapper.registerModule(new JavaTimeModule());
        csvMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        csvMapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, false);

        return csvMapper;
    }

    @Bean
    public CsvSchema csvSchema(CsvMapper csvMapper) {
        return csvMapper.schemaFor(StudentFileDto.class).withHeader();
    }
}
