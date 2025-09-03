# Spring Boot Webhook Solver

A Spring Boot application that automatically generates webhooks and submits SQL solutions on startup.

## 🚀 Features

- **Automatic Execution**: Runs webhook flow on application startup
- **Smart Question Detection**: Determines SQL question based on registration number (odd/even)
- **Retry Logic**: Handles API downtime with automatic retries
- **Production Ready**: Complete error handling and logging

## 📋 How It Works

1. **Startup**: Application sends POST request to generate webhook
2. **Detection**: REG12347 (ends in 47 - odd) → Solves SQL Question 1  
3. **Solution**: Finds highest salary not paid on 1st day of month
4. **Submission**: Posts solution to webhook with JWT authentication

## 🔧 Quick Start

### Prerequisites
- Java 17+
- Maven 3.6+

### Run Application
```bash
# Clone repository
git clone https://github.com/YOUR_USERNAME/spring-boot-webhook-solver.git
cd spring-boot-webhook-solver

# Build and run
mvn spring-boot:run

# Or run JAR directly
java -jar webhook-solver-0.0.1-SNAPSHOT.jar
```

## 📦 Download JAR

Get the latest release: [webhook-solver-0.0.1-SNAPSHOT.jar](https://github.com/YOUR_USERNAME/spring-boot-webhook-solver/releases/latest)

## 📊 SQL Solution

**Registration Number**: REG12347 (Odd → Question 1)

**Query**: Find highest salary not credited on 1st day of month
```sql
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
```

## 🏗️ Project Structure

```
src/main/java/com/example/webhooksolver/
├── WebhookSolverApplication.java       # Main application
├── config/RestTemplateConfig.java      # HTTP client config
├── dto/                                # Data transfer objects
├── service/                            # Business logic
└── listener/                           # Startup event handler
```

## ⚙️ Configuration

- **Endpoint**: `https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA`
- **Retry Attempts**: 3 times with 5-second delays
- **Registration**: REG12347 (John Doe, john@example.com)

## 📝 Tech Stack

- **Spring Boot 3.2.0**
- **Java 17**
- **Maven**
- **RestTemplate** for HTTP requests

---

*Built with ❤️ using Spring Boot*