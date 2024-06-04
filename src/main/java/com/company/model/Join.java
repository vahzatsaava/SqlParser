package com.company.model;

import lombok.*;

@Data
public class Join {
    private String type;
    private String tableName;
    private String alias;
    private String condition;
}
