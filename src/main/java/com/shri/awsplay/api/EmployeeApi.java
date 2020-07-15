package com.shri.awsplay.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shri.awsplay.dto.EmployeeDTO;
import com.shri.awsplay.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/employees")
public class EmployeeApi {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping()
    public ResponseEntity<List<EmployeeDTO>> getEmployees() {
        log.info("GET /employees");
        return ResponseEntity.ok(employeeService.getEmployees());
    }

    @PostMapping()
    public ResponseEntity<?> createEmployeeAsync(@RequestBody EmployeeDTO employee) {
        log.info("POST /employees [application/json]");
        employeeService.createEmployeeAsync(employee);
        return ResponseEntity.accepted().build();
    }

    @PostMapping(value = "/snsHandler", consumes = "text/plain;charset=UTF-8")
    public ResponseEntity<?> createEmployeeSnsHandler(@RequestBody String snsPayload) {
        log.info("POST /employees/snsHandler [application/json]");
        log.info("SNS Payload - {}", snsPayload);
        try {
            Map payloadMap = objectMapper.readValue(snsPayload, Map.class);
            if(null == payloadMap) {
                log.error("payloadMap is null");
            }
            EmployeeDTO employee = objectMapper.convertValue(payloadMap.get("body"), EmployeeDTO.class);
            if(null == employee) {
                log.error("employee is null");
            } else {
                employeeService.createEmployeeSnsHandler(employee);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        return ResponseEntity.ok().build();
    }

//    @PostMapping(value = "/snsHandler", consumes = MediaType.TEXT_PLAIN_VALUE)
//    public ResponseEntity<?> registerSNSHandler(@RequestBody String payload) {
//        log.info("POST /employees [text/plain]");
//        log.info("Payload - {}", payload);
//        return ResponseEntity.accepted().build();
//    }

}
