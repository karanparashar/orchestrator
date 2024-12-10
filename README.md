
This file provides an overview, prerequisites, steps to run the services, and technical details. Let me know if you need changes or additional information!


# Orchestrator

The **Orchestrator Service** acts as the central coordinator, managing requests from clients and delegating tasks to these two services- **fetch-hello** and **json-processor**.

---

## **Features**
- Accepts JSON payloads from clients.
- Communicates with:
    - **fetch-hello** to fetch greeting strings.
    - **json-processor** to process JSON payloads.
- Aggregates responses from other services and returns them to the client.

---

## **Endpoints**

### `POST /process`
- **Description**: Orchestrates calls to fetch-hello and json-processor.
- **Request**: JSON payload.
- **Response**: Aggregated data from both services.

---

## **Swagger Documentation**
Access the Swagger UI: [Orchestrator Swagger UI](http://orchestrator.ap-south-1.elasticbeanstalk.com/swagger-ui/index.html#/)

---

## **How to Run**

### Prerequisites
- Java 17 or higher
- Maven 3.8.6 or higher

### Steps
1. Navigate to the `orchestrator` directory.
2. Build the service:
   ```bash
   mvn clean install


### Start the service:
   ```bash
   mvn spring-boot:run