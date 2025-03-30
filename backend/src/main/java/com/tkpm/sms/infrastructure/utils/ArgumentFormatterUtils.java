package com.tkpm.sms.infrastructure.utils;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Optional;

public class ArgumentFormatterUtils {

    private static final ExpressionParser PARSER = new SpelExpressionParser();

    private ArgumentFormatterUtils() {
    }

    public static Object[] getFormattedArgs(JoinPoint joinPoint, String[] argNames) {
        if (argNames.length == 0) return new Object[0];

        Object[] args = joinPoint.getArgs();
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        String[] parameterNames = Arrays.stream(method.getParameters())
                .map(Parameter::getName)
                .toArray(String[]::new);

        return Arrays.stream(argNames)
                .map(argName -> resolveArgument(argName, args, parameterNames))
                .toArray();
    }

    private static Object resolveArgument(String argName, Object[] args, String[] parameterNames) {
        if (argName.matches("\\d+")) { // Check if argName is an index
            int index = Integer.parseInt(argName);
            return (index >= 0 && index < args.length) ? args[index] : "[invalid index]";
        }

        return findByParameterName(argName, args, parameterNames)
                .orElseGet(() -> resolvePropertyPath(args, argName));
    }

    private static Optional<Object> findByParameterName(String argName, Object[] args, String[] parameterNames) {
        for (int i = 0; i < parameterNames.length; i++) {
            if (parameterNames[i].equals(argName)) return Optional.ofNullable(args[i]);
        }
        return Optional.empty();
    }

    private static Object resolvePropertyPath(Object[] args, String path) {
        String[] parts = path.split("\\.");
        if (parts.length == 1) { // Try to match a full object name
            return Arrays.stream(args)
                    .filter(arg -> arg != null && arg.getClass().getSimpleName().equalsIgnoreCase(parts[0]))
                    .findFirst()
                    .orElse("[unknown: " + path + "]");
        }

        // Find object matching the first part
        Object targetObj = Arrays.stream(args)
                .filter(arg -> arg != null && arg.getClass().getSimpleName().equalsIgnoreCase(parts[0]))
                .findFirst()
                .orElse(null);

        if (targetObj == null) return "[unknown: " + path + "]";

        // Use SpEL to evaluate property path
        try {
            return PARSER.parseExpression(String.join(".", Arrays.copyOfRange(parts, 1, parts.length)))
                    .getValue(new StandardEvaluationContext(targetObj));
        } catch (Exception e) {
            return "[error: " + e.getMessage() + "]";
        }
    }
}
