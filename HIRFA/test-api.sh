#!/bin/bash

# HIRFA Backend API Test Script
# This script helps test the client request management functionality

BASE_URL="http://localhost:8081"
JWT_TOKEN=""

echo "🚀 HIRFA Backend API Test Script"
echo "================================="

# Function to make API calls
api_call() {
    local method=$1
    local endpoint=$2
    local data=$3
    local headers=$4
    
    if [ -n "$data" ]; then
        curl -s -X $method "$BASE_URL$endpoint" \
            -H "Content-Type: application/json" \
            $headers \
            -d "$data"
    else
        curl -s -X $method "$BASE_URL$endpoint" \
            -H "Content-Type: application/json" \
            $headers
    fi
}

# Function to make authenticated API calls
auth_api_call() {
    local method=$1
    local endpoint=$2
    local data=$3
    
    if [ -z "$JWT_TOKEN" ]; then
        echo "❌ No JWT token available. Please login first."
        return 1
    fi
    
    local headers="-H \"Authorization: Bearer $JWT_TOKEN\""
    
    if [ -n "$data" ]; then
        curl -s -X $method "$BASE_URL$endpoint" \
            -H "Content-Type: application/json" \
            -H "Authorization: Bearer $JWT_TOKEN" \
            -d "$data"
    else
        curl -s -X $method "$BASE_URL$endpoint" \
            -H "Content-Type: application/json" \
            -H "Authorization: Bearer $JWT_TOKEN"
    fi
}

# Test 1: Check if application is running
echo "📡 Testing application connectivity..."
response=$(curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/api/auth/login")
if [ "$response" = "400" ] || [ "$response" = "405" ]; then
    echo "✅ Application is running on $BASE_URL"
else
    echo "❌ Application is not running on $BASE_URL"
    echo "Please start the application first: mvn spring-boot:run"
    exit 1
fi

echo ""

# Test 2: Register a test cooperative
echo "👥 Registering test cooperative..."
coop_data='{
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

coop_response=$(api_call "POST" "/api/cooperatives/register" "$coop_data")
echo "Cooperative registration response: $coop_response"
echo ""

# Test 3: Register a test client
echo "👤 Registering test client..."
client_data='{
    "nom": "John",
    "prenom": "Doe",
    "email": "client@test.com",
    "username": "testclient",
    "telephone": "+1234567891",
    "motDePasse": "password123",
    "adresse": "456 Client Street"
}'

client_response=$(api_call "POST" "/api/clients/register" "$client_data")
echo "Client registration response: $client_response"
echo ""

# Test 4: Login as cooperative
echo "🔐 Logging in as cooperative..."
login_data='{
    "username": "testcoop",
    "password": "password123"
}'

login_response=$(api_call "POST" "/api/auth/login" "$login_data")
echo "Login response: $login_response"

# Extract JWT token
JWT_TOKEN=$(echo $login_response | grep -o '"accessToken":"[^"]*' | cut -d'"' -f4)
if [ -n "$JWT_TOKEN" ]; then
    echo "✅ JWT token obtained: ${JWT_TOKEN:0:20}..."
else
    echo "❌ Failed to obtain JWT token"
    exit 1
fi
echo ""

# Test 5: Get all client requests
echo "📋 Getting all client requests..."
requests_response=$(auth_api_call "GET" "/api/cooperatives/requests?page=0&size=10")
echo "Requests response: $requests_response"
echo ""

# Test 6: Get requests by status
echo "📊 Getting requests by status (PENDING)..."
status_response=$(auth_api_call "GET" "/api/cooperatives/requests/status/PENDING?page=0&size=10")
echo "Status response: $status_response"
echo ""

# Test 7: Search requests
echo "🔍 Searching requests..."
search_response=$(auth_api_call "GET" "/api/cooperatives/requests/search?q=test&page=0&size=10")
echo "Search response: $search_response"
echo ""

# Test 8: Get urgent requests
echo "⚡ Getting urgent requests..."
urgent_response=$(auth_api_call "GET" "/api/cooperatives/requests/urgent?page=0&size=10")
echo "Urgent response: $urgent_response"
echo ""

# Test 9: Get recent requests
echo "🕒 Getting recent requests..."
recent_response=$(auth_api_call "GET" "/api/cooperatives/requests/recent?page=0&size=10")
echo "Recent response: $recent_response"
echo ""

# Test 10: Get requests needing attention
echo "⚠️ Getting requests needing attention..."
attention_response=$(auth_api_call "GET" "/api/cooperatives/requests/needing-attention?page=0&size=10")
echo "Attention response: $attention_response"
echo ""

# Test 11: Get overdue requests
echo "⏰ Getting overdue requests..."
overdue_response=$(auth_api_call "GET" "/api/cooperatives/requests/overdue?page=0&size=10")
echo "Overdue response: $overdue_response"
echo ""

# Test 12: Get request statistics
echo "📈 Getting request statistics..."
stats_response=$(auth_api_call "GET" "/api/cooperatives/requests/statistics")
echo "Statistics response: $stats_response"
echo ""

echo "🎉 API testing completed!"
echo ""
echo "📝 Next steps:"
echo "1. Check the database for created users and any sample data"
echo "2. Create some test client requests manually in the database"
echo "3. Test the request management endpoints (add quote, accept, complete, etc.)"
echo "4. Use Postman or similar tool for more detailed testing"
echo ""
echo "💡 To create a test client request, run this SQL in your database:"
echo "INSERT INTO client_requests (title, description, status, priority, category, budget_range, estimated_duration, deadline, location, contact_preference, additional_requirements, is_urgent, is_public, created_at, updated_at, client_id, cooperative_id) VALUES ('Website Design Request', 'Need a modern website for my business', 'PENDING', 'HIGH', 'DESIGN', '1000-5000', '2-4 weeks', '2024-12-31 23:59:59', 'Remote', 'Email', 'Must be mobile responsive', false, true, NOW(), NOW(), (SELECT user_id FROM users WHERE username = 'testclient'), (SELECT user_id FROM users WHERE username = 'testcoop'));"




