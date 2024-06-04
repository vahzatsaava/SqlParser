package com.company.service;

import com.company.model.*;
import com.company.utils.SQLValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@Slf4j
public class ParserService extends SqlParserConstants {

    public String parse(String sql) {

        if (!SQLValidator.isValidSQL(sql)) {
            return "Invalid sql query";
        }

        Query query = new Query();
        query.setColumns(parseColumns(sql));
        query.setFromSources(parseFromSources(sql));
        query.setJoins(parseJoins(sql));
        query.setWhereClauses(parseWhereClauses(sql));
        query.setGroupByColumns(parseGroupByColumns(sql));
        query.setHavingClauses(parseHavingClauses(sql));
        query.setSortColumns(parseSortColumns(sql));
        query.setLimit(parseLimit(sql));
        query.setOffset(parseOffset(sql));

        return generateQueryToJson(query);
    }


    public List<String> parseColumns(String sql) {
        return extractListWithPattern(sql, SELECT_REGEX);
    }

    public List<Source> parseFromSources(String sql) {
        String fromSourcesString = extractSingleWithPattern(sql, FROM_REGEX);
        if (fromSourcesString == null) {
            return new ArrayList<>();
        }
        String[] tables = fromSourcesString.split("\\s*,\\s*");
        List<Source> sources = new ArrayList<>();
        for (String table : tables) {
            Source source = new Source();
            String[] parts = table.trim().split("\\s+");
            source.setTableName(parts[0]);
            if (parts.length > 1) {
                source.setAlias(parts[1]);
            }
            sources.add(source);
        }
        return sources;
    }

    public List<Join> parseJoins(String sql) {
        List<Join> joins = new ArrayList<>();
        Matcher matcher = Pattern.compile(JOIN_REGEX, Pattern.CASE_INSENSITIVE).matcher(sql);
        while (matcher.find()) {
            Join join = new Join();
            join.setType(matcher.group(1).toUpperCase());
            String[] tableParts = matcher.group(2).split("\\s+");
            join.setTableName(tableParts[0]);
            if (tableParts.length > 1) {
                join.setAlias(tableParts[1]);
            }
            join.setCondition(matcher.group(3).trim());
            joins.add(join);
        }
        return joins;
    }

    public List<WhereClause> parseWhereClauses(String sql) {
        String whereClauseString = extractSingleWithPattern(sql, WHERE_REGEX);
        if (whereClauseString == null) {
            return new ArrayList<>();
        }
        WhereClause whereClause = new WhereClause();
        whereClause.setCondition(whereClauseString.trim());
        return List.of(whereClause);
    }

    public List<String> parseGroupByColumns(String sql) {
        return extractListWithPattern(sql, GROUP_BY_REGEX);
    }

    public List<HavingClause> parseHavingClauses(String sql) {
        String havingClauseString = extractSingleWithPattern(sql, HAVING_REGEX);
        if (havingClauseString == null) {
            return new ArrayList<>();
        }
        String[] conditions = havingClauseString.split("\\s*AND\\s*");
        List<HavingClause> havingClauses = new ArrayList<>();
        for (String condition : conditions) {
            HavingClause havingClause = new HavingClause();
            havingClause.setCondition(condition.trim());
            havingClauses.add(havingClause);
        }
        return havingClauses;
    }

    public List<Sort> parseSortColumns(String sql) {
        String sortClauseString = extractSingleWithPattern(sql, ORDER_BY_REGEX);
        if (sortClauseString == null) {
            return new ArrayList<>();
        }
        String[] sortClauses = sortClauseString.split("\\s*,\\s*");
        List<Sort> sortColumns = new ArrayList<>();
        for (String clause : sortClauses) {
            Sort sort = new Sort();
            String[] parts = clause.trim().split("\\s+");
            sort.setColumn(parts[0]);
            if (parts.length > 1) {
                sort.setDirection(parts[1].toUpperCase());
            } else {
                sort.setDirection("ASC");
            }
            sortColumns.add(sort);
        }
        return sortColumns;
    }

    public Integer parseLimit(String sql) {
        String limitString = extractSingleWithPattern(sql, LIMIT_REGEX);
        return limitString != null ? Integer.parseInt(limitString) : null;
    }

    public Integer parseOffset(String sql) {
        String offsetString = extractSingleWithPattern(sql, OFFSET_REGEX);
        return offsetString != null ? Integer.parseInt(offsetString) : null;
    }

    private String generateQueryToJson(Query query) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            return mapper.writeValueAsString(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
