package com.orchestrator.controller;

import com.orchestrator.dto.RequestPayload;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/orchestrator")
@Slf4j
@RequiredArgsConstructor
public class OrcController {

    private final RestTemplate restTemplate;

    @Value("${fetchHello.url}")
    private String service2Url;

    @Value("${jsonProcessor.url}")
    private String service3Url;

    private static final String FETCH_STRING_ENDPOINT = "/fetchString";
    private static final String PROCESS_ENDPOINT = "/process";

    @PostMapping("/concatenate")
    public ResponseEntity<String> concatenate(@Valid @RequestBody RequestPayload payload) {
        log.info("TraceId: {}", MDC.get("traceId"));
        log.info("Received Payload: {}", payload);

        try {
            // Call Service2 GET
            log.info("Calling Service2 at URL: {}", service2Url + FETCH_STRING_ENDPOINT);
            String service2Response = restTemplate.getForObject(service2Url + FETCH_STRING_ENDPOINT, String.class);
            log.info("Service2 Response: {}", service2Response);

            // Call Service3 POST
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<RequestPayload> requestEntity = new HttpEntity<>(payload, headers);

            log.info("Calling Service3 at URL: {}", service3Url + PROCESS_ENDPOINT);
            String service3Response = restTemplate.postForObject(service3Url + PROCESS_ENDPOINT, requestEntity, String.class);
            log.info("Service3 Response: {}", service3Response);

            // Combine responses
            String result = String.format("%s%s", service2Response, service3Response);
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            log.error("Error occurred while processing request: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("An error occurred while processing the request.");
        }
    }

    @GetMapping("/status")
    public ResponseEntity<String> status() {
        log.info("TraceId: {}", MDC.get("traceId"));
        return ResponseEntity.ok("Up");
    }

    // Handle MethodArgumentNotValidException if validation fails
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
        // Collect validation errors from the BindingResult
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation failed: " + errorMessage);
    }
}
