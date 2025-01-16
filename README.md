# Task Manager

## Overview
Task Manager is a Spring Boot application for managing tasks. It uses Kotlin and Java, and is built with Gradle.

## Prerequisites
- JDK 11 or higher
- Gradle
- Docker and Docker Compose

## Getting Started

### Running the Application
1. Clone the repository:
    ```sh
    git clone <repository-url>
    cd <repository-directory>
    ```

2. Build the project:
    ```sh
    ./gradlew build
    ```

3. Start the services using Docker Compose:
    ```sh
    docker-compose up
    ```

4. Run the application:
    ```sh
    ./gradlew bootRun
    ```

## Configuration
- Database: PostgreSQL
- Object Storage: MinIO

## License
This project is licensed under the MIT License.