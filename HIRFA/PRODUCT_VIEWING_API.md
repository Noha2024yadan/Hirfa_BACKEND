# Product Viewing API for Clients

This document describes the product viewing functionality that has been added to the HIRFA backend for clients.

## Overview

The product viewing feature allows clients to browse, search, and filter products through various REST API endpoints. All endpoints are accessible under the `/api/clients` base path.

## API Endpoints

### 1. Get All Products
**GET** `/api/clients/products`

Retrieves all available products with pagination and sorting.

**Query Parameters:**
- `page` (optional, default: 0) - Page number (0-based)
- `size` (optional, default: 10) - Number of items per page
- `sortBy` (optional, default: "name") - Field to sort by (name, price, createdAt, etc.)
- `sortDir` (optional, default: "asc") - Sort direction (asc/desc)

**Example:**
```
GET /api/clients/products?page=0&size=5&sortBy=price&sortDir=desc
```

### 2. Get Product by ID
**GET** `/api/clients/products/{id}`

Retrieves a specific product by its ID.

**Path Parameters:**
- `id` - Product ID

**Example:**
```
GET /api/clients/products/1
```

### 3. Get Products by Category
**GET** `/api/clients/products/category/{category}`

Retrieves products filtered by category.

**Path Parameters:**
- `category` - Product category name

**Query Parameters:**
- `page` (optional, default: 0)
- `size` (optional, default: 10)
- `sortBy` (optional, default: "name")
- `sortDir` (optional, default: "asc")

**Example:**
```
GET /api/clients/products/category/Electronics?page=0&size=10&sortBy=price&sortDir=asc
```

### 4. Search Products
**GET** `/api/clients/products/search`

Searches products by name, description, or category.

**Query Parameters:**
- `q` (required) - Search query
- `page` (optional, default: 0)
- `size` (optional, default: 10)
- `sortBy` (optional, default: "name")
- `sortDir` (optional, default: "asc")

**Example:**
```
GET /api/clients/products/search?q=laptop&page=0&size=10&sortBy=price&sortDir=asc
```

### 5. Get Products by Price Range
**GET** `/api/clients/products/price-range`

Retrieves products within a specific price range.

**Query Parameters:**
- `minPrice` (required) - Minimum price
- `maxPrice` (required) - Maximum price
- `page` (optional, default: 0)
- `size` (optional, default: 10)
- `sortBy` (optional, default: "price")
- `sortDir` (optional, default: "asc")

**Example:**
```
GET /api/clients/products/price-range?minPrice=100&maxPrice=500&page=0&size=10
```

### 6. Get Products by Category and Price Range
**GET** `/api/clients/products/category/{category}/price-range`

Retrieves products filtered by both category and price range.

**Path Parameters:**
- `category` - Product category name

**Query Parameters:**
- `minPrice` (required) - Minimum price
- `maxPrice` (required) - Maximum price
- `page` (optional, default: 0)
- `size` (optional, default: 10)
- `sortBy` (optional, default: "price")
- `sortDir` (optional, default: "asc")

**Example:**
```
GET /api/clients/products/category/Electronics/price-range?minPrice=200&maxPrice=800&page=0&size=10
```

### 7. Get All Categories
**GET** `/api/clients/products/categories`

Retrieves all available product categories.

**Example:**
```
GET /api/clients/products/categories
```

### 8. Get Featured Products
**GET** `/api/clients/products/featured`

Retrieves featured products (recently added products).

**Query Parameters:**
- `limit` (optional, default: 5) - Number of featured products to return

**Example:**
```
GET /api/clients/products/featured?limit=10
```

## Response Format

All endpoints return responses in the following format:

```json
{
  "data": <response_data>,
  "message": "Success message or error message",
  "errorCode": "ERROR_CODE_IF_APPLICABLE"
}
```

### Product Response DTO

For product-related endpoints, the response data follows this structure:

```json
{
  "id": 1,
  "name": "Product Name",
  "description": "Product description",
  "price": 99.99,
  "category": "Electronics",
  "imageUrl": "https://example.com/image.jpg",
  "isAvailable": true,
  "stockQuantity": 50,
  "createdAt": "2024-01-01T10:00:00",
  "updatedAt": "2024-01-01T10:00:00"
}
```

### Paginated Response

For paginated endpoints, the response includes pagination metadata:

```json
{
  "data": {
    "content": [<array_of_products>],
    "pageable": {
      "pageNumber": 0,
      "pageSize": 10,
      "sort": {
        "sorted": true,
        "unsorted": false
      }
    },
    "totalElements": 100,
    "totalPages": 10,
    "first": true,
    "last": false,
    "numberOfElements": 10,
    "size": 10,
    "number": 0
  },
  "message": "Products retrieved successfully"
}
```

## Error Handling

The API returns appropriate HTTP status codes and error messages:

- **200 OK** - Successful request
- **400 Bad Request** - Invalid parameters or request format
- **404 Not Found** - Product not found
- **500 Internal Server Error** - Server error

Error responses include:
- `message` - Human-readable error message
- `errorCode` - Machine-readable error code

## Features

### 1. Pagination
All list endpoints support pagination with configurable page size and page number.

### 2. Sorting
Products can be sorted by any field (name, price, createdAt, etc.) in ascending or descending order.

### 3. Filtering
- By category
- By price range
- By availability (only available products are shown)

### 4. Search
Full-text search across product name, description, and category.

### 5. Featured Products
Quick access to recently added products for showcasing.

## Database Schema

The product viewing functionality relies on the existing `Product` entity with the following key fields:

- `id` - Primary key
- `name` - Product name
- `description` - Product description
- `price` - Product price
- `category` - Product category
- `imageUrl` - Product image URL
- `isAvailable` - Availability status
- `stockQuantity` - Stock quantity
- `createdAt` - Creation timestamp
- `updatedAt` - Last update timestamp

## Security

All product viewing endpoints are accessible to authenticated clients. The endpoints are designed to only return available products and do not expose sensitive information.

## Usage Examples

### Frontend Integration

```javascript
// Get all products with pagination
const response = await fetch('/api/clients/products?page=0&size=10&sortBy=price&sortDir=asc');
const data = await response.json();

// Search products
const searchResponse = await fetch('/api/clients/products/search?q=laptop&page=0&size=10');
const searchData = await searchResponse.json();

// Get products by category
const categoryResponse = await fetch('/api/clients/products/category/Electronics?page=0&size=10');
const categoryData = await categoryResponse.json();
```

### cURL Examples

```bash
# Get all products
curl -X GET "http://localhost:8080/api/clients/products?page=0&size=10&sortBy=name&sortDir=asc"

# Search products
curl -X GET "http://localhost:8080/api/clients/products/search?q=smartphone&page=0&size=10"

# Get products by category
curl -X GET "http://localhost:8080/api/clients/products/category/Electronics?page=0&size=10"

# Get products by price range
curl -X GET "http://localhost:8080/api/clients/products/price-range?minPrice=100&maxPrice=500&page=0&size=10"
```

## Notes

- All product viewing endpoints only return available products (`isAvailable = true`)
- The search functionality is case-insensitive
- Pagination is 0-based (first page is page 0)
- All endpoints support CORS for cross-origin requests
- The API follows RESTful conventions and returns consistent response formats


