package com.company.service;

import com.company.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class SqlParserConstants {

    protected static final String SELECT_REGEX = "SELECT (.+?) FROM";
    protected static final String FROM_REGEX = "FROM (.+?)( WHERE | GROUP BY | HAVING | ORDER BY | LIMIT | OFFSET |$)";
    protected static final String JOIN_REGEX = "(INNER|LEFT|RIGHT|FULL) JOIN (.+?) ON (.+?)(?= WHERE | GROUP BY | HAVING | ORDER BY | LIMIT | OFFSET |$|INNER|LEFT|RIGHT|FULL)";
    protected static final String WHERE_REGEX = "WHERE (.+?)( GROUP BY | HAVING | ORDER BY | LIMIT | OFFSET |$)";
    protected static final String GROUP_BY_REGEX = "GROUP BY (.+?)( HAVING | ORDER BY | LIMIT | OFFSET |$)";
    protected static final String HAVING_REGEX = "HAVING (.+?)( ORDER BY | LIMIT | OFFSET |$)";
    protected static final String ORDER_BY_REGEX = "ORDER BY (.+?)( LIMIT | OFFSET |$)";
    protected static final String LIMIT_REGEX = "LIMIT (\\d+)";
    protected static final String OFFSET_REGEX = "OFFSET (\\d+)";



    protected abstract List<String> parseColumns(String sql);
    protected abstract List<Source> parseFromSources(String sql);
    protected abstract List<Join> parseJoins(String sql);
    protected abstract List<WhereClause> parseWhereClauses(String sql);
    protected abstract List<String> parseGroupByColumns(String sql);
    protected abstract List<HavingClause> parseHavingClauses(String sql);
    protected abstract List<Sort> parseSortColumns(String sql);
    protected abstract Integer parseLimit(String sql);
    protected abstract Integer parseOffset(String sql);


    protected List<String> extractListWithPattern(String sql, String regex) {
        String result = extractSingleWithPattern(sql, regex);
        return result != null ? Arrays.asList(result.split("\\s*" + "," + "\\s*")) : new ArrayList<>();
    }

    protected String extractSingleWithPattern(String sql, String regex) {
        Matcher matcher = Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(sql);
        return matcher.find() ? matcher.group(1) : null;
    }

}
