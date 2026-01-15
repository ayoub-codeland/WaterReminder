package waterly.drinkwater.reminder.features.settings.domain.usecase

import waterly.drinkwater.reminder.core.domain.model.WeightUnit

/**
 * Use case for converting weight between units
 * 
 * Encapsulates weight conversion business logic
 */
class ConvertWeightUseCase {
    /**
     * Convert weight from one unit to another
     * 
     * @param value Weight value to convert
     * @param from Source unit
     * @param to Target unit
     * @return Converted weight value
     */
    operator fun invoke(
        value: Float,
        from: WeightUnit,
        to: WeightUnit
    ): Float {
        if (from == to) return value
        
        return when {
            from == WeightUnit.KG && to == WeightUnit.LBS -> value * KG_TO_LBS
            from == WeightUnit.LBS && to == WeightUnit.KG -> value / KG_TO_LBS
            else -> value
        }
    }
    
    companion object {
        private const val KG_TO_LBS = 2.20462f
    }
}
