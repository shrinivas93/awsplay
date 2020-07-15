package com.shri.awsplay.service;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sqs.AmazonSQS;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shri.awsplay.dto.EmployeeDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class EmployeeService {

    @Autowired
    private AmazonSNS amazonSNS;

    @Autowired
    private AmazonSQS amazonSQS;

    @Value("${employee.sns.topicARN}")
    private String employeeTopic;

    @Value("${employee.sqs.queueUrl}")
    private String employeeQueue;

    @Autowired
    private ObjectMapper objectMapper;

    public List<EmployeeDTO> getEmployees() {
        log.info("Fetching all employees");
        return Collections.EMPTY_LIST;
    }

    public void createEmployeeAsync(EmployeeDTO employee) {
        try {
            log.info("Publishing to SNS - {}", employee);
            amazonSNS.publish(employeeTopic, objectMapper.writeValueAsString(employee));
            log.info("Published to SNS");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void createEmployeeSnsHandler(EmployeeDTO employee) {
        try {
            log.info("Publishing to SQS - {}", employee);
            amazonSQS.sendMessage(employeeQueue, objectMapper.writeValueAsString(employee));
            log.info("Published to SQS");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
