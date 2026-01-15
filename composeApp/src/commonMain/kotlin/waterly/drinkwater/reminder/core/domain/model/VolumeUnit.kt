package waterly.drinkwater.reminder.core.domain.model

/**
 * Volume unit for liquid measurements
 * 
 * Domain model - represents core business concept
 * Used for displaying and tracking water intake
 */
enum class VolumeUnit {
    ML,
    OZ;
    
    companion object {
        /**
         * Conversion factor from OZ to ML
         */
        const val OZ_TO_ML = 29.5735f
        
        /**
         * Conversion factor from ML to OZ
         */
        const val ML_TO_OZ = 0.033814f
    }
    
    /**
     * Convert volume to milliliters
     */
    fun toMl(volume: Float): Float = when (this) {
        ML -> volume
        OZ -> volume * OZ_TO_ML
    }
    
    /**
     * Convert volume to ounces
     */
    fun toOz(volume: Float): Float = when (this) {
        ML -> volume * ML_TO_OZ
        OZ -> volume
    }
}
