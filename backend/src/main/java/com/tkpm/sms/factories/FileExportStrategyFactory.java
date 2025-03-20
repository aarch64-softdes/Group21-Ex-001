package com.tkpm.sms.factories;

import com.tkpm.sms.exceptions.ApplicationException;
import com.tkpm.sms.exceptions.ErrorCode;
import com.tkpm.sms.strategies.FileExportStrategy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class FileExportStrategyFactory {
    private final Map<String, FileExportStrategy> strategies;

    public FileExportStrategyFactory(List<FileExportStrategy> strategyList) {
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(
                        FileExportStrategy::getFormat,
                        Function.identity()
                ));
    }

    public FileExportStrategy getStrategy(String format) {
        FileExportStrategy strategy = strategies.get(format.toLowerCase());

        if (strategy == null) {
            throw new ApplicationException(ErrorCode.INVALID_FILE_FORMAT.withMessage("Unsupported file format " + format));
        }

        return strategies.get(format);
    }
}
