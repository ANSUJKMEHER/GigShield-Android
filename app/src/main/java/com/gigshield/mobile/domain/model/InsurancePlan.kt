package com.gigshield.mobile.domain.model

data class InsurancePlan(
    val tier: String, // "Basic", "Pro", "Elite"
    val basePremium: Int,
    val maxCoverage: Int,
    val maxClaimsPerWeek: Int,
    val description: String,
    val targetWorker: String,
    val features: List<String>,
    val badge: String? = null
) {
    companion object {
        fun getAvailablePlans(dynamicBaseRate: Int = 10): List<InsurancePlan> {
            val proMultiplier = 2
            val eliteMultiplier = 3

            val basicRate = dynamicBaseRate
            val proRate = dynamicBaseRate * proMultiplier
            val eliteRate = dynamicBaseRate * eliteMultiplier

            return listOf(
                InsurancePlan(
                    tier = "Basic",
                    basePremium = basicRate,
                    maxCoverage = 300,
                    maxClaimsPerWeek = 1,
                    description = "Essential micro-income cushion for part-time delivery partners.",
                    targetWorker = "Part-Time Gig Riders",
                    features = listOf(
                        "Up to ₹300 weekly coverage",
                        "1 automated claim per week",
                        "24/7 Satellite weather oracle",
                        "Instant UPI bank settlement"
                    )
                ),
                InsurancePlan(
                    tier = "Pro",
                    basePremium = proRate,
                    maxCoverage = 800,
                    maxClaimsPerWeek = 2,
                    description = "Balanced coverage for full-time gig workers facing frequent urban disruptions.",
                    targetWorker = "Full-Time Delivery Partners",
                    badge = "MOST POPULAR",
                    features = listOf(
                        "Up to ₹800 weekly coverage",
                        "2 automated claims per week",
                        "Monsoon rainfall & AQI triggers",
                        "Zero-touch parametric payout",
                        "Soft-review fraud tolerance"
                    )
                ),
                InsurancePlan(
                    tier = "Elite",
                    basePremium = eliteRate,
                    maxCoverage = 1500,
                    maxClaimsPerWeek = 3,
                    description = "Maximum security for high-risk zones, severe monsoons, and curfew-prone sectors.",
                    targetWorker = "High-Risk Zone Champions",
                    badge = "MAX PROTECTION",
                    features = listOf(
                        "Up to ₹1,500 weekly coverage",
                        "3 automated claims per week",
                        "Full disruption coverage (Rain, AQI, Curfew, App Crash)",
                        "Priority Razorpay UPI transfer",
                        "Dedicated underwriter escalation"
                    )
                )
            )
        }
    }
}
