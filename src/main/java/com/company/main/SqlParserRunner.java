package com.company.main;

import com.company.service.ParserService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SqlParserRunner {
    public static void main(String[] args) {

        String sql1 = "SELECT employee.name AS employee_name, department.name AS department_name, SUM(salary.amount) AS total_salary " +
                "FROM employee e " +
                "INNER JOIN department d ON e.department_id = d.id " +
                "LEFT JOIN salary s ON e.id = s.employee_id " +
                "WHERE e.age > 25 AND s.date > '2022-01-01' " +
                "GROUP BY e.name, d.name " +
                "HAVING SUM(salary.amount) > 50000 " +
                "ORDER BY total_salary DESC, employee_name ASC " +
                "LIMIT 20 OFFSET 10;";

        String sql2 = "SELECT employee.name AS employee_name, department.name AS department_name, SUM(salary.amount) AS total_salary " +
                "FROM employee e " +
                "INNER JOIN department d ON e.department_id = d.id " +
                "LEFT JOIN salary s ON e.id = s.employee_id " +
                "WHERE e.age > 25 AND s.date > '2022-01-01' " +
                "GROUP BY e.name, d.name " +
                "HAVING SUM(salary.amount) > 50000 " +
                "ORDER BY total_salary DESC, employee_name ASC ";

        ParserService sqlParserService = new ParserService();
        log.info(sqlParserService.parse(sql1) + ".");
        log.info(sqlParserService.parse(sql2) + ".");

    }


}
