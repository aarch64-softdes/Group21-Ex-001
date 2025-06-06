package com.tkpm.sms.infrastructure.factories;

import com.tkpm.sms.domain.exception.ErrorCode;
import com.tkpm.sms.domain.exception.FileProcessingException;
import com.tkpm.sms.domain.service.FileStrategy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class FileStrategyFactory {
    private final Map<String, FileStrategy> strategies;

    public FileStrategyFactory(List<FileStrategy> strategyList) {
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(FileStrategy::getFormat, Function.identity()));
    }

    public FileStrategy getStrategy(String format) {
        FileStrategy strategy = strategies.get(format.toLowerCase());

        if (strategy == null) {
            throw new FileProcessingException("Unsupported file format",
                    ErrorCode.INVALID_FILE_FORMAT);
        }

        return strategies.get(format.toLowerCase());
    }
}