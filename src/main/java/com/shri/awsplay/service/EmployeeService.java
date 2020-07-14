package com.shri.awsplay.service;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sqs.AmazonSQS;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shri.awsplay.dto.EmployeeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

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
        System.out.println("Helo");
        return Collections.EMPTY_LIST;
    }

    public void createEmployeeAsync(EmployeeDTO employee) {
        try {
            amazonSNS.publish(employeeTopic, objectMapper.writeValueAsString(employee));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void createEmployeeSnsHandler(EmployeeDTO employee) {
        try {
            amazonSQS.sendMessage(employeeQueue, objectMapper.writeValueAsString(employee));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
