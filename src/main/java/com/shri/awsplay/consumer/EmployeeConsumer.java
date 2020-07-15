package com.shri.awsplay.consumer;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shri.awsplay.dto.EmployeeDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class EmployeeConsumer {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;

    @Value("${employee.dynamodb.tableName}")
    private String employeeTable;

    @JmsListener(destination = "${employee.sqs.queueName}")
    public void listen(String employeeMessage) {
        try {
            log.info("EmployeeMessage consumed from SQS - {}", employeeMessage);
            EmployeeDTO employee = objectMapper.readValue(employeeMessage, EmployeeDTO.class);
            log.info("Employee consumed from SQS - {}", employee);
            String id = String.join("_", employee.getName(), employee.getGender().name(), String.valueOf(employee.getSalary()), String.valueOf(employee.isPermanent()));
            Map<String, AttributeValue> item = new HashMap<>();
            item.put("id", new AttributeValue(id.toLowerCase()));
            item.put("name", new AttributeValue(employee.getName()));
            item.put("gender", new AttributeValue(employee.getGender().name()));
            item.put("salary", new AttributeValue(String.valueOf(employee.getSalary())));
            item.put("permanent", new AttributeValue(String.valueOf(employee.isPermanent())));
            log.info("Putting item into DynamoDB - {}", item);
            amazonDynamoDB.putItem(employeeTable, item);
            log.info("Item put to DynamoDB");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
