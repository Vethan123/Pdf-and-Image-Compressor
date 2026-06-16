# Distributed Asynchronous PDF & Image Compression Service

A production-grade, low-latency microservice built with Spring Boot and MongoDB designed to handle heavy, CPU-bound file compression workloads asynchronously without blocking web server threads.

## Tech Stack & Architecture
- **Backend Framework:** Spring Boot 3.x
- **Database:** MongoDB (Metadata & Task State Tracking)
- **Processing Engine:** Apache PDFBox (PDF structural optimization) & Thumbnailator (Image scaling/sampling)
- **Concurrency Model:** Custom Spring `ThreadPoolTaskExecutor` (Dedicated worker threads)
- **Deployment:** Docker & Docker Compose (Multi-stage containerization)

## Key Engineering Features
- **Non-Blocking Execution:** HTTP threads immediately return a `202 Accepted` status token, offloading heavy CPU computation to a controlled background thread pool.
- **Fail-Safe Processing State Machine:** Tracks task lifetimes (`PENDING` -> `PROCESSING` -> `SUCCESS`/`FAILED`) inside MongoDB documents for clean tracking.
- **Data Isolation & Volume Mapping:** Uses Docker bind-mounts to safely stream outputs back out of container storage directly into the host machine's directory.

## 🏃 How to Run Locally (Free Stack)
1. Clone the repository.
2. Ensure Docker is running on your machine.
3. Run the complete ecosystem with one command:
   ```bash
   docker compose up --build -d

#  Step-by-Step API Testing (Postman)

## 1. Trigger File Compression (POST)
### Endpoint
Access the API endpoints at your
```bash
POST - http://localhost:8080/api/compression/upload
```
### Request Configuration

- **Method:** **POST**
- **Body Type:** form-data

### Form-Data Parameters

| Key  | Type | Value                                                    |
| ---- | ---- | -------------------------------------------------------- |
| file | File | Select a large PDF or Image file from your local machine |

### Expected Response

**Status:** **202** Accepted

```json
{
    message: "File compression started successfully in MongoDB backend*,
    taskId: "666f72645f6173796e633132",
    status: "PENDING"
}
```

---

## 2. Poll Task Progress (GET)

### Endpoint

http **GET** 
``` bash 
[http://localhost:8080/api/v1/compression/status/{taskId}]
```

Replace `{taskId}` with the value returned from the upload **API**.

### Example Successful Response

```json
{
    id: 666f72645f6173796e633132,
    fileName: sample_portfolio.pdf,
    fileType: PDF,
    status: SUCCESS,
    originalSize: 2048500,
    compressedSize: 819200,
    compressedFilePath*: "/app/compressed_files/compressed_sample_portfolio.pdf*,
}
```

---

## 3. Retrieve the Compressed File

Once the compression status becomes **SUCCESS**, open your local file explorer and navigate to the project root directory.

You will find a folder named:

```text 
shared_compressed_files
 ```

Inside this directory, the optimized compressed file will be available for download and use.

### Example Structure

```
text project-root/ │ 
    ├── shared_compressed_files/ 
    │   └── compressed_sample_portfolio.pdf 
    │ ├── src/ 
    ├── pom.xml 
    └── README.md 
```

---

## Workflow Summary

```text
### Upload File
    │
    ▼
Receive Task ID (PENDING)
    │
    ▼
Poll Status API
    │
    ├── PENDING
    ├── PROCESSING
    └── SUCCESS
    │
    ▼
Retrieve Compressed File
```