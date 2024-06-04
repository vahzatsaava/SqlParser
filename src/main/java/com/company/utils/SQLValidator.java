package com.company.utils;

import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;

@Slf4j
public class SQLValidator {
    private SQLValidator() {}

    public static boolean isValidSQL(String sql) {
        try {
            CCJSqlParserUtil.parse(sql);
            return true;
        } catch (JSQLParserException e) {
            log.error("SQL syntax error: " + e.getMessage());
            return false;
        }
    }
}
