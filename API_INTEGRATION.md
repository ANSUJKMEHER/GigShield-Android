# GigShield Mobile — API Integration Reference

This document details the REST API integration between the **GigShield Mobile** Android application and the existing Express 5 backend.

---

## 1. Environment & Base URL Configuration

The application allows dynamic toggling of the backend connection directly in the **Profile & Settings** screen:

| Mode | Base URL | Usage Context |
|---|---|---|
| **Render Cloud (Production)** | `https://gigshield-backend-c1z7.onrender.com/` | Default live environment |
| **Emulator Localhost** | `http://10.0.2.2:5000/` | Android Studio Emulator accessing host machine |
| **Physical Device LAN** | `http://<HOST_IP>:5000/` | Testing over local Wi-Fi with custom URL |
| **Offline Demo Mode** | Routed through `MockApiClient` | Testing without active backend or offline |

---

## 2. Authentication Headers

Requests include standard headers handled by `AuthInterceptor`:
```http
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json
Accept: application/json
```

---

## 3. Detailed Endpoint Contracts

### A. Worker Signup
* **Endpoint:** `POST /api/auth/signup`
* **Request:**
```json
{
  "name": "Ravi Kumar",
  "email": "ravi@gigshield.ai",
  "password": "password123",
  "city": "Mumbai",
  "platform": "Zomato",
  "zone": "Flood Prone (Dharavi)",
  "lat": 19.0760,
  "lon": 72.8777,
  "workingHoursStart": "09:00",
  "workingHoursEnd": "21:00"
}
```
* **Response (201 Created):**
```json
{
  "message": "User created successfully",
  "user": { ... },
  "token": "eyJhbGciOiJIUzI1NiIsIn...",
  "premiumSuggestion": "Medium (₹24/week)"
}
```

---

### B. Worker Login
* **Endpoint:** `POST /api/auth/login`
* **Request:**
```json
{
  "email": "ravi@gigshield.ai",
  "password": "password123"
}
```
* **Response (200 OK):**
```json
{
  "message": "Login successful",
  "user": { ... },
  "token": "eyJhbGciOiJIUzI1NiIsIn..."
}
```

---

### C. Fetch Profile & Claim History
* **Endpoint:** `GET /api/auth/user/:id`
* **Response (200 OK):**
```json
{
  "user": {
    "_id": "660c1e89f81a7b001a1c9012",
    "name": "Ravi Kumar",
    "email": "ravi@gigshield.ai",
    "city": "Mumbai",
    "zone": "Flood Prone (Dharavi)",
    "platform": "Zomato",
    "riskScore": 0.65,
    "activePlan": "Pro",
    "coverage": 800,
    "coverageRemaining": 550,
    "premium": 24,
    "maxClaimsPerWeek": 2,
    "claimsThisWeek": 1,
    "totalPayouts": 250
  },
  "claims": [ ... ]
}
```

---

### D. Dynamic AI Risk Assessment
* **Endpoint:** `GET /api/insurance/risk/:city?zone=:zone&lat=:lat&lon=:lon&pastClaimsCount=:count`
* **Response (200 OK):**
```json
{
  "riskScore": 0.65,
  "premiumSuggestion": "High (₹33/week)",
  "basePremium": 33
}
```

---

### E. Subscribe / Change Policy
* **Endpoint:** `POST /api/insurance/subscribe`
* **Request:**
```json
{
  "userId": "660c1e89f81a7b001a1c9012",
  "plan": "Pro"
}
```
* **Response (200 OK):**
```json
{
  "message": "Subscribed successfully",
  "user": { ... }
}
```

---

### F. Simulate Disruption & Claim Adjudication
* **Endpoint:** `POST /api/insurance/simulate-event`
* **Request:**
```json
{
  "userId": "660c1e89f81a7b001a1c9012",
  "triggerEvent": "Heavy Rain",
  "city": "Mumbai",
  "expectedIncome": 800,
  "actualIncome": 250,
  "liveGps": { "lat": 19.0760, "lon": 72.8777 },
  "liveWeather": { "rain": 24.0, "weather_code": 65 },
  "telemetryVariance": 0.74,
  "isWorkerActive": true
}
```
* **Approved Claim Response (200 OK):**
```json
{
  "message": "₹550 credited instantly!",
  "payoutAmount": 550,
  "coverageRemaining": 0,
  "claim": {
    "_id": "claim_81237",
    "status": "Approved",
    "payoutAmount": 550,
    "loss": 550,
    "triggerEvent": "Heavy Rain",
    "fraudChecks": {
      "gpsVerified": true,
      "radiusValid": true,
      "workerActive": true,
      "telemetryValid": true,
      "duplicateFree": true
    }
  }
}
```
* **Soft-Review Flagged Claim Response (200 OK):**
```json
{
  "message": "Claim soft-flagged for Admin Manual Review. Payout paused.",
  "claim": {
    "_id": "claim_81238",
    "status": "Under Review",
    "payoutAmount": 0,
    "rejectionReason": "24-hour Cooldown triggered. Claim flagged for manual duplication review."
  },
  "requiresReview": true
}
```
