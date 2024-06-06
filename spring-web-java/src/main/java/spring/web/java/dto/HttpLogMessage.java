package spring.web.java.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.SneakyThrows;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.function.Function;

public record HttpLogMessage(
        String transactionId,
        HttpMethod httpMethod,
        String requestUri,
        HttpStatus httpStatus,
        String remoteAddr,
        Double elapsedTime,
        String requestHeader,
        String requestParam,
        String requestBody,
        String responseBody
) {
    public HttpLogMessage(
            String transactionId,
            ContentCachingRequestWrapper requestWrapper,
            ContentCachingResponseWrapper responseWrapper,
            Double elapsedTime

    ) {
        this(
                transactionId,
                HttpMethod.valueOf(requestWrapper.getMethod()),
                requestWrapper.getRequestURI(),
                HttpStatus.valueOf(responseWrapper.getStatus()),
                requestWrapper.getRemoteAddr(),
                elapsedTime,
                generateHeaderOrParameter(requestWrapper.getHeaderNames(), requestWrapper::getHeader),
                generateHeaderOrParameter(requestWrapper.getParameterNames(), requestWrapper::getParameter),
                generateBody(requestWrapper.getContentAsByteArray()),
                generateBody(responseWrapper.getContentAsByteArray())
        );
    }

    private static String generateHeaderOrParameter(Enumeration<String> names, Function<String, String> valueFunction) {
        StringBuilder builder = new StringBuilder();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            String value = valueFunction.apply(name);
            builder.append("\n").append("  ").append(name).append(": ").append(value);
        }
        return builder.toString();
    }

    @SneakyThrows
    private static String generateBody(byte[] content) {
        String body = "";
        if (content.length > 0) {
            body = new String(content, StandardCharsets.UTF_8);
        }
        return setPretty(body);
    }

    @SneakyThrows
    private static String setPretty(String body) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        Object json = objectMapper.readValue(body, Object.class);
        return objectWriter.writeValueAsString(json);
    }

    public String getMessage() {
        return """
                [REQUEST] %s %s %s (%s)
                |>> TRANSACTION_ID: %s
                |>> CLIENT_IP: %s
                |>> REQUEST_HEADER: %s
                |>> REQUEST_PARAM: %s
                |>> REQUEST_BODY: %s
                |>> RESPONSE_BODY: %s
                """
                .formatted(httpMethod, requestUri, httpStatus, elapsedTime, transactionId, remoteAddr,
                        requestHeader, requestParam, requestBody, responseBody);
    }
}
