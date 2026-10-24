package com.gigshield.mobile.domain.model

enum class DisruptionType(
    val title: String,
    val triggerSource: String,
    val defaultThreshold: String,
    val expectedImpact: String
) {
    HEAVY_RAIN(
        title = "Heavy Rain",
        triggerSource = "Open-Meteo Satellite API",
        defaultThreshold = "> 10mm precipitation",
        expectedImpact = "Flooded delivery arteries, order cancellations"
    ),
    HIGH_AQI(
        title = "High AQI",
        triggerSource = "Air Quality Monitor",
        defaultThreshold = "AQI > 400 Hazardous",
        expectedImpact = "Respiratory hazard forcing riders indoors"
    ),
    CURFEW(
        title = "Curfew",
        triggerSource = "Civic Alert Feeds",
        defaultThreshold = "Section 144 Enacted",
        expectedImpact = "Movement restricted, complete platform zero"
    ),
    APP_CRASH(
        title = "App Crash",
        triggerSource = "Delivery Platform Server Status",
        defaultThreshold = "Downtime > 45 minutes",
        expectedImpact = "Order dispatch engine outage"
    )
}
