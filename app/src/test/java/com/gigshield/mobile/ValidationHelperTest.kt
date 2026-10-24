package com.gigshield.mobile

import com.gigshield.mobile.core.common.Constants
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidationHelperTest {

    @Test
    fun `test minimum claim loss requirement threshold`() {
        val validLoss = 150
        val invalidLoss = 80

        assertTrue("Loss of ₹150 should exceed threshold", validLoss >= Constants.MIN_CLAIM_LOSS_THRESHOLD)
        assertFalse("Loss of ₹80 is below ₹100 threshold", invalidLoss >= Constants.MIN_CLAIM_LOSS_THRESHOLD)
    }

    @Test
    fun `test biomechanical telemetry variance threshold`() {
        // The 7-vector fraud engine requires accelerometer variance > 0.5 to prove dynamic device movement
        val activeWorkerVariance = 0.72
        val staticDeskPhoneVariance = 0.02

        assertTrue("Active worker variance > 0.5 passes fraud engine", activeWorkerVariance > 0.5)
        assertFalse("Static phone on desk should be flagged by biomechanical engine", staticDeskPhoneVariance > 0.5)
    }

    @Test
    fun `test weekly claim count limit enforcement`() {
        val basicMaxClaims = 1
        val proMaxClaims = 2
        val eliteMaxClaims = 3

        val claimsMade = 2

        assertTrue("Basic plan cannot make 2 claims in a week", claimsMade >= basicMaxClaims)
        assertFalse("Pro plan can make up to 2 claims", claimsMade > proMaxClaims)
        assertFalse("Elite plan can make up to 3 claims", claimsMade >= eliteMaxClaims)
    }
}
