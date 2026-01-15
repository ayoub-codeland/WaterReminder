package waterly.drinkwater.reminder

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform