package com.kodex.guide.domain.model

enum class UserRole(val level: Int) {
    ANONYMOUS(0),   // Не авторизован (может смотреть, добавлять в память телефона)
    USER(10),       // Обычный пользователь (может смотреть, добавлять в память телефона, писать отзывы)
    BUSINESS(30),   // Бизнес-аккаунт (может публиковать объявления)
    PREMIUM(50),   // Премиум-аккаунт (может публиковать объявления в дорогих рубриках)
    ADMIN(100);     // Администратор (модерация, удаление)

    fun hasAccessTo(required: UserRole): Boolean = this.level >= required.level
}