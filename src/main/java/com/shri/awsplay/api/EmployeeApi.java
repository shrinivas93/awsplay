package com.shri.awsplay.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shri.awsplay.dto.EmployeeDTO;
import com.shri.awsplay.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        EmployeeDTO employee = objectMapper.convertValue(snsPayload, EmployeeDTO.class);
        if (null == employee) {
            log.error("employee is null");
        } else {
            employeeService.createEmployeeSnsHandler(employee);
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
