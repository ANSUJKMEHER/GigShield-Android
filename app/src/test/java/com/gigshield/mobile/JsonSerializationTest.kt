package com.gigshield.mobile

import com.gigshield.mobile.data.model.ClaimDto
import com.gigshield.mobile.data.model.UserDto
import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class JsonSerializationTest {

    private val gson = Gson()

    @Test
    fun `test user dto deserialization parity with express mongoose schema`() {
        val json = """
            {
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
            }
        """.trimIndent()

        val userDto = gson.fromJson(json, UserDto::class.java)

        assertNotNull(userDto)
        assertEquals("660c1e89f81a7b001a1c9012", userDto.id)
        assertEquals("Ravi Kumar", userDto.name)
        assertEquals("Pro", userDto.activePlan)
        assertEquals(800, userDto.coverage)
        assertEquals(550, userDto.coverageRemaining)
    }

    @Test
    fun `test claim dto deserialization with 7-vector fraud payload`() {
        val json = """
            {
                "_id": "claim_9921",
                "userId": "660c1e89f81a7b001a1c9012",
                "triggerEvent": "High AQI",
                "city": "Delhi",
                "expectedIncome": 900,
                "actualIncome": 300,
                "loss": 600,
                "payoutAmount": 600,
                "status": "Approved",
                "fraudChecks": {
                    "gpsVerified": true,
                    "radiusValid": true,
                    "workerActive": true,
                    "telemetryValid": true,
                    "duplicateFree": true
                },
                "eventDetails": {
                    "severity": "AQI 420 Hazardous",
                    "deliveryDrop": "60%"
                }
            }
        """.trimIndent()

        val claimDto = gson.fromJson(json, ClaimDto::class.java)

        assertNotNull(claimDto)
        assertEquals("High AQI", claimDto.triggerEvent)
        assertEquals("Approved", claimDto.status)
        assertEquals(true, claimDto.fraudChecks?.gpsVerified)
        assertEquals("AQI 420 Hazardous", claimDto.eventDetails?.severity)
    }
}
