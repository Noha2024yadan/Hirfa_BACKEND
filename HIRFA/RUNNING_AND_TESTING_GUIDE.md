# HIRFA Backend - Running and Testing Guide

This guide provides comprehensive instructions for running and testing the HIRFA backend application with the new client request management functionality.

## Prerequisites

### 1. System Requirements
- **Java 17** or higher
- **Maven 3.6+**
- **MySQL 8.0+** or **MySQL 5.7+**
- **Git** (for cloning the repository)

### 2. Development Tools (Optional but Recommended)
- **IDE**: IntelliJ IDEA, Eclipse, or VS Code
- **API Testing**: Postman, Insomnia, or curl
- **Database Management**: MySQL Workbench, phpMyAdmin, or DBeaver

## Installation and Setup

### 1. Clone the Repository
```bash
git clone <repository-url>
cd Hirfa_BACKEND/HIRFA
```

### 2. Database Setup

#### Option A: Using MySQL (Recommended)
1. **Install MySQL** if not already installed
2. **Start MySQL service**:
   ```bash
   # Windows
   net start mysql
   
   # macOS (using Homebrew)
   brew services start mysql
   
   # Linux
   sudo systemctl start mysql
   ```

3. **Create Database** (optional - will be created automatically):
   ```sql
   CREATE DATABASE hirfa;
   ```

4. **Update Database Configuration** in `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/hirfa?createDatabaseIfNotExist=true
   spring.datasource.username=root
   spring.datasource.password=your_mysql_password
   ```

#### Option B: Using H2 Database (For Testing)
If you want to use H2 in-memory database for testing, add this dependency to `pom.xml`:
```xml
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
```

And update `application.properties`:
```properties
spring.datasource.url=jdbc:h2:mem:hirfa
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.h2.console.enabled=true
```

### 3. Email Configuration (Optional)
Update email settings in `application.properties`:
```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-specific-password
```

## Running the Application

### Method 1: Using Maven (Command Line)
```bash
# Navigate to the project directory
cd HIRFA

# Clean and compile
mvn clean compile

# Run the application
mvn spring-boot:run
```

### Method 2: Using IDE
1. **IntelliJ IDEA**:
   - Open the project
   - Right-click on `HirfaApplication.java`
   - Select "Run 'HirfaApplication'"

2. **Eclipse**:
   - Import as Maven project
   - Right-click on `HirfaApplication.java`
   - Run As → Java Application

3. **VS Code**:
   - Install Spring Boot Extension Pack
   - Open the project
   - Use Command Palette: "Spring Boot: Run"

### Method 3: Building JAR and Running
```bash
# Build the application
mvn clean package

# Run the JAR file
java -jar target/HIRFA-0.0.1-SNAPSHOT.jar
```

## Application Access

Once the application is running, you can access:

- **Application**: http://localhost:8081
- **H2 Console** (if using H2): http://localhost:8081/h2-console
- **API Base URL**: http://localhost:8081/api

## Testing the Application

### 1. Database Verification

#### Check Tables Created
Connect to your MySQL database and verify tables are created:
```sql
USE hirfa;
SHOW TABLES;
```

You should see tables like:
- `users`
- `clients`
- `cooperatives`
- `designers`
- `admins`
- `products`
- `designs`
- `orders`
- `order_items`
- `client_requests`
- `design_tags`
- And other related tables

### 2. API Testing

#### A. User Registration and Authentication

**1. Register a Cooperative:**
```bash
curl -X POST http://localhost:8081/api/cooperatives/register \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Test Cooperative",
    "prenom": "Manager",
    "email": "coop@test.com",
    "username": "testcoop",
    "telephone": "+1234567890",
    "motDePasse": "password123",
    "secteurActivite": "Technology",
    "nombreEmployes": 10,
    "adresse": "123 Tech Street",
    "siteWeb": "https://testcoop.com"
  }'
```

**2. Register a Client:**
```bash
curl -X POST http://localhost:8081/api/clients/register \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "John",
    "prenom": "Doe",
    "email": "client@test.com",
    "username": "testclient",
    "telephone": "+1234567891",
    "motDePasse": "password123",
    "adresse": "456 Client Street"
  }'
```

**3. Login as Cooperative:**
```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testcoop",
    "password": "password123"
  }'
```

Save the JWT token from the response for subsequent requests.

#### B. Testing Client Request Management

**1. Create a Test Client Request (via database or API):**
```sql
INSERT INTO client_requests (
    title, description, status, priority, category, 
    budget_range, estimated_duration, deadline, location,
    contact_preference, additional_requirements, is_urgent, is_public,
    created_at, updated_at, client_id, cooperative_id
) VALUES (
    'Website Design Request',
    'Need a modern website for my business',
    'PENDING',
    'HIGH',
    'DESIGN',
    '1000-5000',
    '2-4 weeks',
    '2024-12-31 23:59:59',
    'Remote',
    'Email',
    'Must be mobile responsive',
    false,
    true,
    NOW(),
    NOW(),
    (SELECT user_id FROM users WHERE username = 'testclient'),
    (SELECT user_id FROM users WHERE username = 'testcoop')
);
```

**2. Get All Client Requests:**
```bash
curl -X GET "http://localhost:8081/api/cooperatives/requests?page=0&size=10" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**3. Get Requests by Status:**
```bash
curl -X GET "http://localhost:8081/api/cooperatives/requests/status/PENDING?page=0&size=10" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**4. Search Requests:**
```bash
curl -X GET "http://localhost:8081/api/cooperatives/requests/search?q=website&page=0&size=10" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**5. Add Quote to Request:**
```bash
curl -X POST "http://localhost:8081/api/cooperatives/requests/1/quote" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d "quotedPrice=2500&quotedDuration=3 weeks&response=We can help with this project&notes=Includes 3 revisions"
```

**6. Accept Request:**
```bash
curl -X POST "http://localhost:8081/api/cooperatives/requests/1/accept" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**7. Complete Request:**
```bash
curl -X POST "http://localhost:8081/api/cooperatives/requests/1/complete" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**8. Get Request Statistics:**
```bash
curl -X GET "http://localhost:8081/api/cooperatives/requests/statistics" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 3. Using Postman for Testing

#### Import Collection
Create a Postman collection with the following structure:

**Environment Variables:**
- `base_url`: `http://localhost:8081`
- `jwt_token`: (set after login)

**Sample Requests:**

1. **Register Cooperative**
   - Method: POST
   - URL: `{{base_url}}/api/cooperatives/register`
   - Body: JSON with cooperative data

2. **Login**
   - Method: POST
   - URL: `{{base_url}}/api/auth/login`
   - Body: JSON with username/password
   - Tests: Set `jwt_token` variable from response

3. **Get All Requests**
   - Method: GET
   - URL: `{{base_url}}/api/cooperatives/requests`
   - Headers: `Authorization: Bearer {{jwt_token}}`

4. **Add Quote**
   - Method: POST
   - URL: `{{base_url}}/api/cooperatives/requests/1/quote`
   - Headers: `Authorization: Bearer {{jwt_token}}`
   - Body: form-data with quote details

### 4. Unit Testing

Run the existing tests:
```bash
mvn test
```

### 5. Integration Testing

Create integration tests for the new endpoints:

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class ClientRequestIntegrationTest {
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    void testGetAllRequests() {
        // Test implementation
    }
    
    @Test
    void testAddQuote() {
        // Test implementation
    }
}
```

## Troubleshooting

### Common Issues and Solutions

#### 1. Database Connection Issues
**Error**: `Connection refused` or `Access denied`

**Solutions**:
- Verify MySQL is running
- Check username/password in `application.properties`
- Ensure database exists
- Check MySQL port (default: 3306)

#### 2. Port Already in Use
**Error**: `Port 8081 was already in use`

**Solutions**:
- Change port in `application.properties`: `server.port=8082`
- Kill process using port 8081:
  ```bash
  # Windows
  netstat -ano | findstr :8081
  taskkill /PID <PID> /F
  
  # macOS/Linux
  lsof -ti:8081 | xargs kill -9
  ```

#### 3. JWT Token Issues
**Error**: `401 Unauthorized`

**Solutions**:
- Ensure you're using the correct JWT token
- Check token expiration
- Verify Authorization header format: `Bearer <token>`

#### 4. Entity Not Found
**Error**: `Request not found with id: X`

**Solutions**:
- Verify the request ID exists in database
- Check if cooperative has access to the request
- Ensure proper authentication

### 5. Maven Build Issues
**Error**: `Failed to resolve dependencies`

**Solutions**:
```bash
# Clean and update dependencies
mvn clean install -U

# Skip tests if needed
mvn clean install -DskipTests
```

## Performance Testing

### 1. Load Testing with Apache Bench
```bash
# Test concurrent requests
ab -n 1000 -c 10 -H "Authorization: Bearer YOUR_JWT_TOKEN" \
   http://localhost:8081/api/cooperatives/requests
```

### 2. Database Performance
Monitor database performance:
```sql
-- Check slow queries
SHOW VARIABLES LIKE 'slow_query_log';
SHOW VARIABLES LIKE 'long_query_time';

-- Monitor connections
SHOW STATUS LIKE 'Threads_connected';
SHOW STATUS LIKE 'Max_used_connections';
```

## Monitoring and Logging

### 1. Application Logs
Check application logs for errors:
```bash
# If running with Maven
tail -f logs/spring.log

# If running as JAR
java -jar target/HIRFA-0.0.1-SNAPSHOT.jar --logging.level.com.HIRFA=DEBUG
```

### 2. Database Monitoring
```sql
-- Check table sizes
SELECT 
    table_name,
    ROUND(((data_length + index_length) / 1024 / 1024), 2) AS 'Size (MB)'
FROM information_schema.tables
WHERE table_schema = 'hirfa'
ORDER BY (data_length + index_length) DESC;
```

## Production Deployment

### 1. Environment Configuration
Create `application-prod.properties`:
```properties
spring.datasource.url=jdbc:mysql://prod-db:3306/hirfa
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
server.port=8080
```

### 2. Build for Production
```bash
mvn clean package -Pprod
```

### 3. Docker Deployment (Optional)
Create `Dockerfile`:
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/HIRFA-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## API Documentation

### Swagger/OpenAPI (Optional)
Add Swagger dependency to `pom.xml`:
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.0.2</version>
</dependency>
```

Access Swagger UI at: http://localhost:8081/swagger-ui.html

## Next Steps

1. **Frontend Integration**: Connect with React/Vue/Angular frontend
2. **Real-time Updates**: Implement WebSocket for real-time notifications
3. **File Upload**: Add file upload functionality for request attachments
4. **Email Notifications**: Implement email notifications for status changes
5. **Advanced Analytics**: Add more detailed reporting and analytics
6. **Mobile API**: Optimize API for mobile applications

## Support

For issues and questions:
1. Check the logs for error messages
2. Verify database connectivity
3. Test with simple curl commands first
4. Check the API documentation
5. Review the troubleshooting section above

## Quick Start Checklist

- [ ] Java 17+ installed
- [ ] Maven 3.6+ installed
- [ ] MySQL running and accessible
- [ ] Database credentials configured
- [ ] Application starts without errors
- [ ] Can register users
- [ ] Can login and get JWT token
- [ ] Can access protected endpoints
- [ ] Client request management endpoints work
- [ ] Database tables created correctly




