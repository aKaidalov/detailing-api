## 1. Build Check

`./gradlew clean build`
This compiles the code and runs tests. If successful, your code compiles without errors.

## 2. Run Tests

`./gradlew test`
Executes all unit and integration tests to verify business logic.

## 3. Start the Application

### Start PostgreSQL
`docker-compose up -d`

### Run the API
`./gradlew bootRun`
The API should start on http://localhost:8080
