package com.shri.awsplay.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shri.awsplay.dto.EmployeeDTO;
import com.shri.awsplay.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employees")
public class EmployeeApi {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping()
    public ResponseEntity<List<EmployeeDTO>> getEmployees() {
        return ResponseEntity.ok(employeeService.getEmployees());
    }

    @PostMapping()
    public ResponseEntity<?> createEmployeeAsync(@RequestBody EmployeeDTO employee) {
        employeeService.createEmployeeAsync(employee);
        return ResponseEntity.accepted().build();
    }

    @PostMapping(consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> registerSNSHandler(@RequestBody String payload) {
        System.out.println(payload);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/snsHandler")
    public ResponseEntity<?> createEmployeeSnsHandler(@RequestBody Map<String, Object> snsPayload) {
        EmployeeDTO employee = objectMapper.convertValue(snsPayload.get("body"), EmployeeDTO.class);
        employeeService.createEmployeeSnsHandler(employee);
        return ResponseEntity.ok().build();
    }

}
