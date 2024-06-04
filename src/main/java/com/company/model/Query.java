package com.company.model;

import lombok.*;


import java.util.List;

@Data
public class Query {
    private List<String> columns;
    private List<Source> fromSources;
    private List<Join> joins;
    private List<WhereClause> whereClauses;
    private List<String> groupByColumns;
    private List<HavingClause> havingClauses;
    private List<Sort> sortColumns;
    private Integer limit;
    private Integer offset;

}
