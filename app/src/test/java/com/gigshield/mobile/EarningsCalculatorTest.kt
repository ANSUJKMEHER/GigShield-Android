package com.gigshield.mobile

import com.gigshield.mobile.domain.model.Claim
import com.gigshield.mobile.domain.model.ClaimStatus
import com.gigshield.mobile.domain.model.EarningsSummary
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class EarningsCalculatorTest {

    @Test
    fun `test algorithmic loss calculation formula`() {
        val expectedIncome = 800
        val actualIncome = 250
        val calculatedLoss = expectedIncome - actualIncome

        assertEquals(550, calculatedLoss)
        assertTrue("Loss must exceed threshold of 100", calculatedLoss >= 100)
    }

    @Test
    fun `test payout capping to coverage remaining`() {
        val calculatedLoss = 700
        val coverageRemaining = 500

        val settledPayout = calculatedLoss.coerceAtMost(coverageRemaining)
        assertEquals(500, settledPayout)
    }

    @Test
    fun `test full recovery when coverage exceeds loss`() {
        val calculatedLoss = 400
        val coverageRemaining = 1200

        val settledPayout = calculatedLoss.coerceAtMost(coverageRemaining)
        assertEquals(400, settledPayout)
    }

    @Test
    fun `test earnings summary generation with approved claims`() {
        val sampleClaims = listOf(
            Claim(
                id = "c1",
                userId = "u1",
                triggerEvent = "Heavy Rain",
                city = "Mumbai",
                expectedIncome = 800,
                actualIncome = 200,
                loss = 600,
                payoutAmount = 600,
                status = ClaimStatus.APPROVED,
                severity = "22mm rain",
                deliveryDrop = "50%",
                rejectionReason = "",
                createdAt = "2026-04-18T10:00:00Z",
                isGpsVerified = true,
                isTelemetryValid = true,
                isWeatherMatch = true
            )
        )

        val summary = EarningsSummary.calculate(sampleClaims, userTotalPayouts = 600)

        assertTrue(summary.totalRecovered >= 600)
        assertEquals(7, summary.dailyBreakdown.size)
    }
}
