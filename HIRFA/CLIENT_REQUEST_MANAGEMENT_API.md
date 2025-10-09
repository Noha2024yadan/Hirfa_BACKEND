# Client Request Management API for Cooperatives

This document describes the client request management functionality that has been added to the HIRFA backend for cooperatives.

## Overview

The client request management feature allows cooperatives to view, manage, and respond to client requests through various REST API endpoints. All endpoints are accessible under the `/api/cooperatives` base path.

## API Endpoints

### 1. Get All Client Requests
**GET** `/api/cooperatives/requests`

Retrieves all client requests for the current cooperative with pagination and sorting.

**Query Parameters:**
- `page` (optional, default: 0) - Page number (0-based)
- `size` (optional, default: 10) - Number of items per page
- `sortBy` (optional, default: "createdAt") - Field to sort by (createdAt, title, status, priority, etc.)
- `sortDir` (optional, default: "desc") - Sort direction (asc/desc)

**Example:**
```
GET /api/cooperatives/requests?page=0&size=5&sortBy=priority&sortDir=asc
```

### 2. Get Client Request by ID
**GET** `/api/cooperatives/requests/{id}`

Retrieves a specific client request by its ID.

**Path Parameters:**
- `id` - Request ID

**Example:**
```
GET /api/cooperatives/requests/1
```

### 3. Get Requests by Status
**GET** `/api/cooperatives/requests/status/{status}`

Retrieves requests filtered by status.

**Path Parameters:**
- `status` - Request status (PENDING, REVIEWED, QUOTED, ACCEPTED, IN_PROGRESS, COMPLETED, CANCELLED, REJECTED, EXPIRED)

**Query Parameters:**
- `page` (optional, default: 0)
- `size` (optional, default: 10)
- `sortBy` (optional, default: "createdAt")
- `sortDir` (optional, default: "desc")

**Example:**
```
GET /api/cooperatives/requests/status/PENDING?page=0&size=10&sortBy=createdAt&sortDir=desc
```

### 4. Get Requests by Priority
**GET** `/api/cooperatives/requests/priority/{priority}`

Retrieves requests filtered by priority.

**Path Parameters:**
- `priority` - Request priority (LOW, MEDIUM, HIGH, URGENT)

**Query Parameters:**
- `page` (optional, default: 0)
- `size` (optional, default: 10)
- `sortBy` (optional, default: "createdAt")
- `sortDir` (optional, default: "desc")

**Example:**
```
GET /api/cooperatives/requests/priority/URGENT?page=0&size=10
```

### 5. Get Requests by Category
**GET** `/api/cooperatives/requests/category/{category}`

Retrieves requests filtered by category.

**Path Parameters:**
- `category` - Request category (DESIGN, DEVELOPMENT, CONSULTATION, MAINTENANCE, TRAINING, SUPPORT, OTHER)

**Query Parameters:**
- `page` (optional, default: 0)
- `size` (optional, default: 10)
- `sortBy` (optional, default: "createdAt")
- `sortDir` (optional, default: "desc")

**Example:**
```
GET /api/cooperatives/requests/category/DESIGN?page=0&size=10
```

### 6. Get Urgent Requests
**GET** `/api/cooperatives/requests/urgent`

Retrieves all urgent requests for the cooperative.

**Query Parameters:**
- `page` (optional, default: 0)
- `size` (optional, default: 10)
- `sortBy` (optional, default: "createdAt")
- `sortDir` (optional, default: "desc")

**Example:**
```
GET /api/cooperatives/requests/urgent?page=0&size=10
```

### 7. Search Requests
**GET** `/api/cooperatives/requests/search`

Searches requests by title or description.

**Query Parameters:**
- `q` (required) - Search query
- `page` (optional, default: 0)
- `size` (optional, default: 10)
- `sortBy` (optional, default: "createdAt")
- `sortDir` (optional, default: "desc")

**Example:**
```
GET /api/cooperatives/requests/search?q=website&page=0&size=10
```

### 8. Get Recent Requests
**GET** `/api/cooperatives/requests/recent`

Retrieves requests created in the last 30 days.

**Query Parameters:**
- `page` (optional, default: 0)
- `size` (optional, default: 10)

**Example:**
```
GET /api/cooperatives/requests/recent?page=0&size=10
```

### 9. Get Requests Needing Attention
**GET** `/api/cooperatives/requests/needing-attention`

Retrieves requests that need attention (pending, reviewed, or overdue).

**Query Parameters:**
- `page` (optional, default: 0)
- `size` (optional, default: 10)

**Example:**
```
GET /api/cooperatives/requests/needing-attention?page=0&size=10
```

### 10. Get Overdue Requests
**GET** `/api/cooperatives/requests/overdue`

Retrieves requests that are past their deadline.

**Query Parameters:**
- `page` (optional, default: 0)
- `size` (optional, default: 10)

**Example:**
```
GET /api/cooperatives/requests/overdue?page=0&size=10
```

### 11. Update Request Status
**PUT** `/api/cooperatives/requests/{id}/status`

Updates the status of a request.

**Path Parameters:**
- `id` - Request ID

**Query Parameters:**
- `status` (required) - New status

**Example:**
```
PUT /api/cooperatives/requests/1/status?status=REVIEWED
```

### 12. Add Cooperative Response
**POST** `/api/cooperatives/requests/{id}/respond`

Adds a response to a request.

**Path Parameters:**
- `id` - Request ID

**Query Parameters:**
- `response` (required) - Response text
- `notes` (optional) - Additional notes

**Example:**
```
POST /api/cooperatives/requests/1/respond?response=We can help with this project&notes=Will need 2 weeks
```

### 13. Add Quote to Request
**POST** `/api/cooperatives/requests/{id}/quote`

Adds a quote to a request.

**Path Parameters:**
- `id` - Request ID

**Query Parameters:**
- `quotedPrice` (required) - Quoted price
- `quotedDuration` (required) - Quoted duration
- `response` (required) - Response text
- `notes` (optional) - Additional notes

**Example:**
```
POST /api/cooperatives/requests/1/quote?quotedPrice=1500&quotedDuration=2 weeks&response=We can complete this project&notes=Includes 3 revisions
```

### 14. Accept Request
**POST** `/api/cooperatives/requests/{id}/accept`

Accepts a quoted request.

**Path Parameters:**
- `id` - Request ID

**Example:**
```
POST /api/cooperatives/requests/1/accept
```

### 15. Complete Request
**POST** `/api/cooperatives/requests/{id}/complete`

Marks a request as completed.

**Path Parameters:**
- `id` - Request ID

**Example:**
```
POST /api/cooperatives/requests/1/complete
```

### 16. Cancel Request
**POST** `/api/cooperatives/requests/{id}/cancel`

Cancels a request.

**Path Parameters:**
- `id` - Request ID

**Example:**
```
POST /api/cooperatives/requests/1/cancel
```

### 17. Reject Request
**POST** `/api/cooperatives/requests/{id}/reject`

Rejects a request with a reason.

**Path Parameters:**
- `id` - Request ID

**Query Parameters:**
- `reason` (required) - Reason for rejection

**Example:**
```
POST /api/cooperatives/requests/1/reject?reason=Outside our expertise
```

### 18. Get Request Statistics
**GET** `/api/cooperatives/requests/statistics`

Retrieves request statistics for the cooperative.

**Example:**
```
GET /api/cooperatives/requests/statistics
```

## Response Format

All endpoints return responses in the following format:

```json
{
  "success": true,
  "message": "Success message",
  "data": <response_data>
}
```

### Client Request Response DTO

For request-related endpoints, the response data follows this structure:

```json
{
  "id": 1,
  "title": "Website Design Request",
  "description": "Need a modern website for my business",
  "status": "PENDING",
  "statusDisplayName": "Pending",
  "priority": "HIGH",
  "priorityDisplayName": "High",
  "category": "DESIGN",
  "categoryDisplayName": "Design",
  "budgetRange": "1000-5000",
  "estimatedDuration": "2-4 weeks",
  "deadline": "2024-02-15T10:00:00",
  "location": "Remote",
  "contactPreference": "Email",
  "additionalRequirements": "Must be mobile responsive",
  "attachments": "[\"file1.pdf\", \"file2.jpg\"]",
  "quotedPrice": 2500.00,
  "quotedDuration": "3 weeks",
  "cooperativeResponse": "We can help with this project",
  "cooperativeNotes": "Will need 3 revisions",
  "clientFeedback": "Great work!",
  "rating": 5,
  "isUrgent": false,
  "isPublic": true,
  "createdAt": "2024-01-01T10:00:00",
  "updatedAt": "2024-01-01T10:00:00",
  "respondedAt": "2024-01-02T10:00:00",
  "completedAt": null,
  "clientId": "123e4567-e89b-12d3-a456-426614174000",
  "clientName": "John Doe",
  "clientEmail": "john@example.com",
  "clientPhone": "+1234567890",
  "cooperativeId": "456e7890-e89b-12d3-a456-426614174000",
  "cooperativeName": "ABC Cooperative",
  "cooperativeEmail": "coop@example.com",
  "isCompleted": false,
  "isCancelled": false,
  "canBeAccepted": false,
  "canBeQuoted": true,
  "isOverdue": false,
  "needsAttention": true
}
```

### Paginated Response

For paginated endpoints, the response includes pagination metadata:

```json
{
  "success": true,
  "message": "Client requests retrieved successfully",
  "data": {
    "content": [<array_of_requests>],
    "pageable": {
      "pageNumber": 0,
      "pageSize": 10,
      "sort": {
        "sorted": true,
        "unsorted": false
      }
    },
    "totalElements": 50,
    "totalPages": 5,
    "first": true,
    "last": false,
    "numberOfElements": 10,
    "size": 10,
    "number": 0
  }
}
```

### Statistics Response

The statistics endpoint returns:

```json
{
  "success": true,
  "message": "Request statistics retrieved successfully",
  "data": {
    "totalRequests": 100,
    "pendingRequests": 15,
    "completedRequests": 60,
    "cancelledRequests": 10,
    "overdueRequests": 5,
    "urgentRequests": 8,
    "statusCounts": {
      "PENDING": 15,
      "REVIEWED": 10,
      "QUOTED": 20,
      "ACCEPTED": 5,
      "IN_PROGRESS": 8,
      "COMPLETED": 60,
      "CANCELLED": 5,
      "REJECTED": 5
    },
    "priorityCounts": {
      "LOW": 20,
      "MEDIUM": 50,
      "HIGH": 25,
      "URGENT": 5
    },
    "categoryCounts": {
      "DESIGN": 30,
      "DEVELOPMENT": 25,
      "CONSULTATION": 20,
      "MAINTENANCE": 15,
      "TRAINING": 5,
      "SUPPORT": 3,
      "OTHER": 2
    }
  }
}
```

## Error Handling

The API returns appropriate HTTP status codes and error messages:

- **200 OK** - Successful request
- **400 Bad Request** - Invalid parameters or request format
- **404 Not Found** - Request not found
- **500 Internal Server Error** - Server error

Error responses include:
- `success: false`
- `message` - Human-readable error message
- `errorCode` - Machine-readable error code

## Features

### 1. Pagination
All list endpoints support pagination with configurable page size and page number.

### 2. Sorting
Requests can be sorted by any field (createdAt, title, status, priority, etc.) in ascending or descending order.

### 3. Filtering
- By status
- By priority
- By category
- By urgency
- By date range

### 4. Search
Full-text search across request title and description.

### 5. Request Management
- View all requests
- Update request status
- Add responses and quotes
- Accept/reject requests
- Complete requests
- Cancel requests

### 6. Specialized Views
- Urgent requests
- Recent requests
- Requests needing attention
- Overdue requests

### 7. Statistics
Comprehensive request statistics and analytics.

## Database Schema

The client request management functionality relies on the `ClientRequest` entity with the following key fields:

- `id` - Primary key
- `title` - Request title
- `description` - Request description
- `status` - Request status
- `priority` - Request priority
- `category` - Request category
- `budgetRange` - Budget range
- `estimatedDuration` - Estimated duration
- `deadline` - Request deadline
- `location` - Project location
- `contactPreference` - Preferred contact method
- `additionalRequirements` - Additional requirements
- `attachments` - File attachments
- `quotedPrice` - Quoted price
- `quotedDuration` - Quoted duration
- `cooperativeResponse` - Cooperative response
- `cooperativeNotes` - Cooperative notes
- `clientFeedback` - Client feedback
- `rating` - Client rating
- `isUrgent` - Urgency flag
- `isPublic` - Public visibility flag
- `createdAt` - Creation timestamp
- `updatedAt` - Last update timestamp
- `respondedAt` - Response timestamp
- `completedAt` - Completion timestamp
- `client` - Reference to Client entity
- `cooperative` - Reference to Cooperative entity

## Security

All client request management endpoints are accessible to authenticated cooperatives. The endpoints are designed to only return requests associated with the authenticated cooperative.

## Usage Examples

### Frontend Integration

```javascript
// Get all requests with pagination
const response = await fetch('/api/cooperatives/requests?page=0&size=10&sortBy=priority&sortDir=desc');
const data = await response.json();

// Search requests
const searchResponse = await fetch('/api/cooperatives/requests/search?q=website&page=0&size=10');
const searchData = await searchResponse.json();

// Get urgent requests
const urgentResponse = await fetch('/api/cooperatives/requests/urgent?page=0&size=10');
const urgentData = await urgentResponse.json();

// Add quote to request
const quoteResponse = await fetch('/api/cooperatives/requests/1/quote', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/x-www-form-urlencoded',
  },
  body: 'quotedPrice=1500&quotedDuration=2 weeks&response=We can help&notes=Includes revisions'
});
const quoteData = await quoteResponse.json();
```

### cURL Examples

```bash
# Get all requests
curl -X GET "http://localhost:8080/api/cooperatives/requests?page=0&size=10&sortBy=createdAt&sortDir=desc"

# Search requests
curl -X GET "http://localhost:8080/api/cooperatives/requests/search?q=website&page=0&size=10"

# Get urgent requests
curl -X GET "http://localhost:8080/api/cooperatives/requests/urgent?page=0&size=10"

# Get requests by status
curl -X GET "http://localhost:8080/api/cooperatives/requests/status/PENDING?page=0&size=10"

# Add quote to request
curl -X POST "http://localhost:8080/api/cooperatives/requests/1/quote" \
  -d "quotedPrice=1500&quotedDuration=2 weeks&response=We can help&notes=Includes revisions"

# Accept request
curl -X POST "http://localhost:8080/api/cooperatives/requests/1/accept"

# Complete request
curl -X POST "http://localhost:8080/api/cooperatives/requests/1/complete"

# Get request statistics
curl -X GET "http://localhost:8080/api/cooperatives/requests/statistics"
```

## Request Status Flow

1. **PENDING** - Initial status when request is created
2. **REVIEWED** - Cooperative has reviewed the request
3. **QUOTED** - Cooperative has provided a quote
4. **ACCEPTED** - Client has accepted the quote
5. **IN_PROGRESS** - Work has started
6. **COMPLETED** - Work is finished
7. **CANCELLED** - Request was cancelled
8. **REJECTED** - Request was rejected
9. **EXPIRED** - Request has expired

## Request Priorities

- **LOW** - Low priority requests
- **MEDIUM** - Medium priority requests
- **HIGH** - High priority requests
- **URGENT** - Urgent requests requiring immediate attention

## Request Categories

- **DESIGN** - Design-related requests
- **DEVELOPMENT** - Development-related requests
- **CONSULTATION** - Consultation requests
- **MAINTENANCE** - Maintenance requests
- **TRAINING** - Training requests
- **SUPPORT** - Support requests
- **OTHER** - Other types of requests

## Notes

- All request management endpoints only return requests associated with the authenticated cooperative
- The search functionality is case-insensitive
- Pagination is 0-based (first page is page 0)
- All endpoints support CORS for cross-origin requests
- The API follows RESTful conventions and returns consistent response formats
- Request status changes are automatically tracked with timestamps
- Overdue requests are automatically identified based on deadline comparison




