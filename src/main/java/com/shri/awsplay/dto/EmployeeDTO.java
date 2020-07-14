package com.shri.awsplay.dto;

import lombok.Data;

@Data
public class EmployeeDTO {

    String id;
    String name;
    Gender gender;
    float salary;
    boolean permanent;

}
