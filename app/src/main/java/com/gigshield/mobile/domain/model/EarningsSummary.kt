package com.gigshield.mobile.domain.model

data class DailyEarnings(
    val day: String,
    val expected: Int,
    val actual: Int,
    val lossRecovered: Int
)

data class EarningsSummary(
    val weeklyExpected: Int,
    val weeklyActual: Int,
    val totalLoss: Int,
    val totalRecovered: Int,
    val netIncomeProtected: Int,
    val dailyBreakdown: List<DailyEarnings>
) {
    companion object {
        fun calculate(claims: List<Claim>, userTotalPayouts: Int): EarningsSummary {
            val baseDailyExpected = 750
            val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

            val daily = days.mapIndexed { index, day ->
                val matchingClaim = claims.getOrNull(index)
                if (matchingClaim != null && matchingClaim.status == ClaimStatus.APPROVED) {
                    DailyEarnings(
                        day = day,
                        expected = matchingClaim.expectedIncome,
                        actual = matchingClaim.actualIncome,
                        lossRecovered = matchingClaim.payoutAmount
                    )
                } else {
                    DailyEarnings(
                        day = day,
                        expected = baseDailyExpected,
                        actual = baseDailyExpected,
                        lossRecovered = 0
                    )
                }
            }

            val expectedSum = daily.sumOf { it.expected }
            val actualSum = daily.sumOf { it.actual }
            val lossSum = (expectedSum - actualSum).coerceAtLeast(0)
            val recoveredSum = userTotalPayouts.coerceAtLeast(daily.sumOf { it.lossRecovered })

            return EarningsSummary(
                weeklyExpected = expectedSum,
                weeklyActual = actualSum,
                totalLoss = lossSum,
                totalRecovered = recoveredSum,
                netIncomeProtected = actualSum + recoveredSum,
                dailyBreakdown = daily
            )
        }
    }
}
