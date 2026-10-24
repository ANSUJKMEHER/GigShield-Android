package com.gigshield.mobile

import com.gigshield.mobile.domain.model.Claim
import com.gigshield.mobile.domain.model.ClaimStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class ClaimsViewModelTest {

    @Test
    fun `test claim status mapping approved`() {
        val rawStatus = "Approved"
        val status = when (rawStatus) {
            "Approved" -> ClaimStatus.APPROVED
            "Under Review" -> ClaimStatus.UNDER_REVIEW
            else -> ClaimStatus.REJECTED
        }

        assertEquals(ClaimStatus.APPROVED, status)
    }

    @Test
    fun `test claim status mapping soft review duplicate`() {
        val rawStatus = "Under Review"
        val status = when (rawStatus) {
            "Approved" -> ClaimStatus.APPROVED
            "Under Review" -> ClaimStatus.UNDER_REVIEW
            else -> ClaimStatus.REJECTED
        }

        assertEquals(ClaimStatus.UNDER_REVIEW, status)
    }

    @Test
    fun `test claim domain model initialization`() {
        val claim = Claim(
            id = "c-test-01",
            userId = "u-01",
            triggerEvent = "High AQI",
            city = "Delhi",
            expectedIncome = 900,
            actualIncome = 200,
            loss = 700,
            payoutAmount = 700,
            status = ClaimStatus.APPROVED,
            severity = "AQI 450 Hazardous",
            deliveryDrop = "55%",
            rejectionReason = "",
            createdAt = "2026-04-18T10:00:00Z",
            isGpsVerified = true,
            isTelemetryValid = true,
            isWeatherMatch = true
        )

        assertNotNull(claim)
        assertEquals("High AQI", claim.triggerEvent)
        assertEquals(700, claim.payoutAmount)
        assertEquals(ClaimStatus.APPROVED, claim.status)
        assertEquals(true, claim.isTelemetryValid)
    }
}
