# Mail Archive System

[中文文档](README_zh_CN.md)

A robust email archiving system that automatically archives corporate emails, supporting various encoding formats and ensuring no emails are missed.

## Features

- **Multi-mailbox Support**: Configure and manage multiple email accounts
- **Real-time Archiving**: Automatically archive emails in real-time
- **Encoding Compatibility**: Support various email encodings (UTF-8, GBK, GB2312, etc.)
- **Attachment Management**: Automatically save and manage email attachments
- **Search Capabilities**: Search archived emails by sender, date range, etc.
- **Web Interface**: User-friendly web interface for system management
- **High Performance**: Async processing and batch operations for high throughput
- **Reliable**: Exception handling, retry mechanism, and transaction management

## Technology Stack

- Java 8
- Spring Boot 2.7.5
- MySQL 8.0
- Maven
- Docker
- Jenkins
- Bootstrap 5
- jQuery

## Quick Start

### Prerequisites

- JDK 8
- Maven 3.6+
- MySQL 8.0
- Docker (optional)

### Database Setup

```sql
CREATE DATABASE mail_archive CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### Configuration

Edit `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mail_archive?useUnicode=true&characterEncoding=utf8&useSSL=false
    username: your_username
    password: your_password
```

### Build and Run

#### Using Maven

```bash
mvn clean package
java -jar target/mail-archive-1.0-SNAPSHOT.jar
```

#### Using Docker

```bash
# Build image
docker build -t mail-archive .

# Run container
docker run -d --name mail-archive \
    -p 8080:8080 \
    -v /data/attachments:/data/attachments \
    mail-archive
```

### Access the Application

Open http://localhost:8080 in your browser.

## Development

### Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── org/apollo/mail/
│   │       ├── controller/    # REST controllers
│   │       ├── entity/        # JPA entities
│   │       ├── repository/    # Data access
│   │       ├── service/       # Business logic
│   │       ├── util/          # Utility classes
│   │       └── MailArchiveApplication.java
│   └── resources/
│       ├── static/           # Static resources
│       ├── templates/        # Thymeleaf templates
│       └── application.yml   # Configuration
```

### CI/CD

The project uses Jenkins for continuous integration and deployment. The pipeline includes:

1. Code checkout
2. Build
3. Test
4. SonarQube analysis
5. Docker image build
6. Docker image push
7. Deployment

## API Documentation

### Mailbox Configuration

```
GET    /api/mailbox          # List all mailboxes
POST   /api/mailbox          # Add new mailbox
PUT    /api/mailbox/{id}     # Update mailbox
DELETE /api/mailbox/{id}     # Delete mailbox
```

### Email Archive

```
GET    /api/archive/search   # Search archived emails
POST   /api/archive/mailbox/{id}  # Trigger archiving
```

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.