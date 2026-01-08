package com.drinkwater.reminder.core.domain.model

/**
 * Weight unit for measurements
 * 
 * Domain model - represents core business concept
 * Used for converting between metric and imperial systems
 */
enum class WeightUnit {
    KG,
    LBS;
    
    companion object {
        /**
         * Conversion factor from LBS to KG
         */
        const val LBS_TO_KG = 0.453592f
        
        /**
         * Conversion factor from KG to LBS
         */
        const val KG_TO_LBS = 2.20462f
    }
    
    /**
     * Convert weight to kilograms
     */
    fun toKg(weight: Float): Float = when (this) {
        KG -> weight
        LBS -> weight * LBS_TO_KG
    }
    
    /**
     * Convert weight to pounds
     */
    fun toLbs(weight: Float): Float = when (this) {
        KG -> weight * KG_TO_LBS
        LBS -> weight
    }
}
