package com.example.webhooksolver.service;

import org.springframework.stereotype.Service;

@Service
public class SqlSolutionService {
    
    // SQL Solution for Question 1 (Odd regNo)
    private static final String SQL_QUESTION_1 = """
        SELECT 
            p.AMOUNT as SALARY,
            CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) as NAME,
            TIMESTAMPDIFF(YEAR, e.DOB, CURDATE()) as AGE,
            d.DEPARTMENT_NAME
        FROM PAYMENTS p
        INNER JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID
        INNER JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID
        WHERE DAY(DATE(p.PAYMENT_TIME)) != 1
        ORDER BY p.AMOUNT DESC
        LIMIT 1;
        """;
    
    // SQL Solution for Question 2 (Even regNo)
    private static final String SQL_QUESTION_2 = """
        SELECT 
            e1.EMP_ID,
            e1.FIRST_NAME,
            e1.LAST_NAME,
            d.DEPARTMENT_NAME,
            COUNT(e2.EMP_ID) as YOUNGER_EMPLOYEES_COUNT
        FROM EMPLOYEE e1
        INNER JOIN DEPARTMENT d ON e1.DEPARTMENT = d.DEPARTMENT_ID
        LEFT JOIN EMPLOYEE e2 ON e1.DEPARTMENT = e2.DEPARTMENT 
                              AND e2.DOB > e1.DOB
        GROUP BY e1.EMP_ID, e1.FIRST_NAME, e1.LAST_NAME, d.DEPARTMENT_NAME
        ORDER BY e1.EMP_ID DESC;
        """;
    
    public String getSqlSolution(String regNo) {
        // Extract last two digits
        String lastTwoDigits = regNo.substring(regNo.length() - 2);
        int lastTwoDigitsInt = Integer.parseInt(lastTwoDigits);
        
        // If odd -> Question 1, If even -> Question 2
        if (lastTwoDigitsInt % 2 == 1) {
            return SQL_QUESTION_1.trim();
        } else {
            return SQL_QUESTION_2.trim();
        }
    }
    
    public int getQuestionNumber(String regNo) {
        String lastTwoDigits = regNo.substring(regNo.length() - 2);
        int lastTwoDigitsInt = Integer.parseInt(lastTwoDigits);
        return (lastTwoDigitsInt % 2 == 1) ? 1 : 2;
    }
}