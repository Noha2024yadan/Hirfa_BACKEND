# Design Viewing API for Cooperatives

This document describes the design viewing functionality that has been added to the HIRFA backend for cooperatives.

## Overview

The design viewing feature allows cooperatives to browse, search, and filter designs created by designers through various REST API endpoints. All endpoints are accessible under the `/api/cooperatives` base path.

## API Endpoints

### 1. Get All Designs
**GET** `/api/cooperatives/designs`

Retrieves all available designs with pagination and sorting.

**Query Parameters:**
- `page` (optional, default: 0) - Page number (0-based)
- `size` (optional, default: 10) - Number of items per page
- `sortBy` (optional, default: "title") - Field to sort by (title, price, rating, createdAt, etc.)
- `sortDir` (optional, default: "asc") - Sort direction (asc/desc)

**Example:**
```
GET /api/cooperatives/designs?page=0&size=5&sortBy=price&sortDir=desc
```

### 2. Get Design by ID
**GET** `/api/cooperatives/designs/{id}`

Retrieves a specific design by its ID.

**Path Parameters:**
- `id` - Design ID

**Example:**
```
GET /api/cooperatives/designs/1
```

### 3. Get Designs by Category
**GET** `/api/cooperatives/designs/category/{category}`

Retrieves designs filtered by category.

**Path Parameters:**
- `category` - Design category name

**Query Parameters:**
- `page` (optional, default: 0)
- `size` (optional, default: 10)
- `sortBy` (optional, default: "title")
- `sortDir` (optional, default: "asc")

**Example:**
```
GET /api/cooperatives/designs/category/Logo?page=0&size=10&sortBy=price&sortDir=asc
```

### 4. Search Designs
**GET** `/api/cooperatives/designs/search`

Searches designs by title, description, tags, or category.

**Query Parameters:**
- `q` (required) - Search query
- `page` (optional, default: 0)
- `size` (optional, default: 10)
- `sortBy` (optional, default: "title")
- `sortDir` (optional, default: "asc")

**Example:**
```
GET /api/cooperatives/designs/search?q=logo&page=0&size=10&sortBy=rating&sortDir=desc
```

### 5. Get Designs by Price Range
**GET** `/api/cooperatives/designs/price-range`

Retrieves designs within a specific price range.

**Query Parameters:**
- `minPrice` (required) - Minimum price
- `maxPrice` (required) - Maximum price
- `page` (optional, default: 0)
- `size` (optional, default: 10)
- `sortBy` (optional, default: "price")
- `sortDir` (optional, default: "asc")

**Example:**
```
GET /api/cooperatives/designs/price-range?minPrice=50&maxPrice=200&page=0&size=10
```

### 6. Get Designs by Category and Price Range
**GET** `/api/cooperatives/designs/category/{category}/price-range`

Retrieves designs filtered by both category and price range.

**Path Parameters:**
- `category` - Design category name

**Query Parameters:**
- `minPrice` (required) - Minimum price
- `maxPrice` (required) - Maximum price
- `page` (optional, default: 0)
- `size` (optional, default: 10)
- `sortBy` (optional, default: "price")
- `sortDir` (optional, default: "asc")

**Example:**
```
GET /api/cooperatives/designs/category/Logo/price-range?minPrice=100&maxPrice=500&page=0&size=10
```

### 7. Get Designs by File Format
**GET** `/api/cooperatives/designs/file-format/{fileFormat}`

Retrieves designs filtered by file format.

**Path Parameters:**
- `fileFormat` - File format (e.g., PSD, AI, PDF, PNG)

**Query Parameters:**
- `page` (optional, default: 0)
- `size` (optional, default: 10)
- `sortBy` (optional, default: "title")
- `sortDir` (optional, default: "asc")

**Example:**
```
GET /api/cooperatives/designs/file-format/PSD?page=0&size=10&sortBy=title&sortDir=asc
```

### 8. Get Designs by Rating Range
**GET** `/api/cooperatives/designs/rating-range`

Retrieves designs within a specific rating range.

**Query Parameters:**
- `minRating` (required) - Minimum rating (0.0-5.0)
- `maxRating` (required) - Maximum rating (0.0-5.0)
- `page` (optional, default: 0)
- `size` (optional, default: 10)
- `sortBy` (optional, default: "rating")
- `sortDir` (optional, default: "desc")

**Example:**
```
GET /api/cooperatives/designs/rating-range?minRating=4.0&maxRating=5.0&page=0&size=10
```

### 9. Get Designs by Multiple Criteria
**GET** `/api/cooperatives/designs/filter`

Retrieves designs filtered by multiple criteria.

**Query Parameters:**
- `category` (optional) - Design category
- `fileFormat` (optional) - File format
- `minPrice` (optional) - Minimum price
- `maxPrice` (optional) - Maximum price
- `minRating` (optional) - Minimum rating
- `maxRating` (optional) - Maximum rating
- `page` (optional, default: 0)
- `size` (optional, default: 10)
- `sortBy` (optional, default: "title")
- `sortDir` (optional, default: "asc")

**Example:**
```
GET /api/cooperatives/designs/filter?category=Logo&fileFormat=PSD&minPrice=100&maxPrice=500&minRating=4.0&page=0&size=10
```

### 10. Get Designs by Designer
**GET** `/api/cooperatives/designs/designer/{designerId}`

Retrieves designs created by a specific designer.

**Path Parameters:**
- `designerId` - Designer UUID

**Query Parameters:**
- `page` (optional, default: 0)
- `size` (optional, default: 10)
- `sortBy` (optional, default: "title")
- `sortDir` (optional, default: "asc")

**Example:**
```
GET /api/cooperatives/designs/designer/123e4567-e89b-12d3-a456-426614174000?page=0&size=10
```

### 11. Get All Categories
**GET** `/api/cooperatives/designs/categories`

Retrieves all available design categories.

**Example:**
```
GET /api/cooperatives/designs/categories
```

### 12. Get All File Formats
**GET** `/api/cooperatives/designs/file-formats`

Retrieves all available file formats.

**Example:**
```
GET /api/cooperatives/designs/file-formats
```

### 13. Get Featured Designs
**GET** `/api/cooperatives/designs/featured`

Retrieves featured designs.

**Query Parameters:**
- `limit` (optional, default: 5) - Number of featured designs to return

**Example:**
```
GET /api/cooperatives/designs/featured?limit=10
```

### 14. Get Top-Rated Designs
**GET** `/api/cooperatives/designs/top-rated`

Retrieves top-rated designs.

**Query Parameters:**
- `page` (optional, default: 0)
- `size` (optional, default: 10)

**Example:**
```
GET /api/cooperatives/designs/top-rated?page=0&size=10
```

### 15. Get Most Downloaded Designs
**GET** `/api/cooperatives/designs/most-downloaded`

Retrieves most downloaded designs.

**Query Parameters:**
- `page` (optional, default: 0)
- `size` (optional, default: 10)

**Example:**
```
GET /api/cooperatives/designs/most-downloaded?page=0&size=10
```

### 16. Get Recent Designs
**GET** `/api/cooperatives/designs/recent`

Retrieves recently created designs.

**Query Parameters:**
- `page` (optional, default: 0)
- `size` (optional, default: 10)

**Example:**
```
GET /api/cooperatives/designs/recent?page=0&size=10
```

### 17. Get Design Statistics
**GET** `/api/cooperatives/designs/statistics`

Retrieves design statistics and metadata.

**Example:**
```
GET /api/cooperatives/designs/statistics
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

### Design Response DTO

For design-related endpoints, the response data follows this structure:

```json
{
  "id": 1,
  "title": "Modern Logo Design",
  "description": "A sleek and modern logo design perfect for tech companies",
  "price": 150.00,
  "category": "Logo",
  "imageUrl": "https://example.com/design-preview.jpg",
  "isAvailable": true,
  "isFeatured": false,
  "rating": 4.5,
  "reviewCount": 23,
  "downloadCount": 45,
  "tags": "modern,logo,tech,minimalist",
  "fileFormat": "PSD",
  "fileSize": 2048576,
  "dimensions": "1920x1080",
  "createdAt": "2024-01-01T10:00:00",
  "updatedAt": "2024-01-01T10:00:00",
  "designerId": "123e4567-e89b-12d3-a456-426614174000",
  "designerName": "John Doe",
  "designerUsername": "johndoe",
  "designerPortfolio": "https://johndoe.design"
}
```

### Paginated Response

For paginated endpoints, the response includes pagination metadata:

```json
{
  "success": true,
  "message": "Designs retrieved successfully",
  "data": {
    "content": [<array_of_designs>],
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
  }
}
```

### Statistics Response

The statistics endpoint returns:

```json
{
  "success": true,
  "message": "Design statistics retrieved successfully",
  "data": {
    "totalDesigns": 150,
    "featuredDesigns": 25,
    "categoryCount": 8,
    "fileFormatCount": 5,
    "categories": ["Logo", "Poster", "Business Card", "Flyer", "Banner", "Icon", "Illustration", "Web Design"],
    "fileFormats": ["PSD", "AI", "PDF", "PNG", "SVG"]
  }
}
```

## Error Handling

The API returns appropriate HTTP status codes and error messages:

- **200 OK** - Successful request
- **400 Bad Request** - Invalid parameters or request format
- **404 Not Found** - Design not found
- **500 Internal Server Error** - Server error

Error responses include:
- `success: false`
- `message` - Human-readable error message
- `errorCode` - Machine-readable error code

## Features

### 1. Pagination
All list endpoints support pagination with configurable page size and page number.

### 2. Sorting
Designs can be sorted by any field (title, price, rating, createdAt, etc.) in ascending or descending order.

### 3. Filtering
- By category
- By price range
- By file format
- By rating range
- By designer
- By availability (only available designs are shown)

### 4. Search
Full-text search across design title, description, tags, and category.

### 5. Advanced Filtering
Multiple criteria can be combined for precise filtering.

### 6. Specialized Views
- Featured designs
- Top-rated designs
- Most downloaded designs
- Recent designs

## Database Schema

The design viewing functionality relies on the `Design` entity with the following key fields:

- `id` - Primary key
- `title` - Design title
- `description` - Design description
- `price` - Design price
- `category` - Design category
- `imageUrl` - Design preview image URL
- `isAvailable` - Availability status
- `isFeatured` - Featured status
- `rating` - Average rating
- `reviewCount` - Number of reviews
- `downloadCount` - Number of downloads
- `tags` - Comma-separated tags
- `fileFormat` - File format (PSD, AI, PDF, etc.)
- `fileSize` - File size in bytes
- `dimensions` - Design dimensions
- `createdAt` - Creation timestamp
- `updatedAt` - Last update timestamp
- `designer` - Reference to Designer entity

## Security

All design viewing endpoints are accessible to authenticated cooperatives. The endpoints are designed to only return available designs and include designer information for transparency.

## Usage Examples

### Frontend Integration

```javascript
// Get all designs with pagination
const response = await fetch('/api/cooperatives/designs?page=0&size=10&sortBy=price&sortDir=asc');
const data = await response.json();

// Search designs
const searchResponse = await fetch('/api/cooperatives/designs/search?q=logo&page=0&size=10');
const searchData = await searchResponse.json();

// Get designs by category
const categoryResponse = await fetch('/api/cooperatives/designs/category/Logo?page=0&size=10');
const categoryData = await categoryResponse.json();

// Filter designs by multiple criteria
const filterResponse = await fetch('/api/cooperatives/designs/filter?category=Logo&fileFormat=PSD&minPrice=100&maxPrice=500&minRating=4.0');
const filterData = await filterResponse.json();
```

### cURL Examples

```bash
# Get all designs
curl -X GET "http://localhost:8080/api/cooperatives/designs?page=0&size=10&sortBy=title&sortDir=asc"

# Search designs
curl -X GET "http://localhost:8080/api/cooperatives/designs/search?q=logo&page=0&size=10"

# Get designs by category
curl -X GET "http://localhost:8080/api/cooperatives/designs/category/Logo?page=0&size=10"

# Get designs by price range
curl -X GET "http://localhost:8080/api/cooperatives/designs/price-range?minPrice=100&maxPrice=500&page=0&size=10"

# Get designs by file format
curl -X GET "http://localhost:8080/api/cooperatives/designs/file-format/PSD?page=0&size=10"

# Get top-rated designs
curl -X GET "http://localhost:8080/api/cooperatives/designs/top-rated?page=0&size=10"

# Get design statistics
curl -X GET "http://localhost:8080/api/cooperatives/designs/statistics"
```

## Design Categories

Common design categories include:
- Logo
- Poster
- Business Card
- Flyer
- Banner
- Icon
- Illustration
- Web Design
- Social Media
- Print Design

## File Formats

Supported file formats include:
- PSD (Photoshop)
- AI (Illustrator)
- PDF
- PNG
- SVG
- JPG
- EPS
- INDD (InDesign)

## Notes

- All design viewing endpoints only return available designs (`isAvailable = true`)
- The search functionality is case-insensitive
- Pagination is 0-based (first page is page 0)
- All endpoints support CORS for cross-origin requests
- The API follows RESTful conventions and returns consistent response formats
- Designer information is included in design responses for transparency
- File size is provided in bytes for download planning
- Rating system uses 0.0-5.0 scale


