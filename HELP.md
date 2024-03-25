# Online Directory API

This is API part of Online Directory Application. This Spring Boot application provides a secure API with JWT authentication, including user registration and login functionalities, role-based access control, and permissions management.

## Features

- User registration and login
- JWT authentication
- Role and permission based access control

## Prerequisites

Before you begin, ensure you have met the following requirements:
- JDK 11 or newer
- Gradle 6 (if building outside of IntelliJ IDEA)
- IntelliJ IDEA or your preferred IDE with Kotlin support

## Installation

To install the project, follow these steps:

### Repository Setup
1. Clone the repository:
   ```bash
   git clone https://yourproject/repository.git
   ```
2. Navigate to the project directory:
   ```bash
   cd repository
   ```

### Generating a Secure JWT Key
Before running the application, generate a secure JWT key

1. Run the custom Gradle task
   ```bash
   ./gradlew generateJwtKey
   ```
2. Copy the generated key and set it as an environment variable JWT_SECRET_KEY or update `application.properties`
   ```bash
   security.jwt.token.secret-key=<YourSecureKeyHere>
   ```

## Running the Application
To run the application, use:
   ```bash
   ./gradlew bootRun
   ```
Or, run the application directly from IntelliJ IDEA by executing the main class com.h5templates.directory.DirectoryApplication.

## Usage

### Register a User
Registers a new user with the provided details.

- Endpoint: `/api/auth/signup`
- Method: `POST`
- Payload:
   ```
   {
     "name": "John Doe",
     "email": "john.doe@example.com",
     "phone": "1234567890",
     "password": "yourPassword",
     "password_confirm": "yourPassword"
   }
   ```

### Login
Authenticates the user and returns a JWT token for accessing protected endpoints.

- Endpoint: `/api/auth/login`
- Method: `POST`
- Payload:
   ```
   {
     "email": "john.doe@example.com",
     "password": "yourPassword"
   }
   ```

## Contributing
Contributions to this project are welcome. Please follow the standard pull request process for your contributions.

## License

MIT