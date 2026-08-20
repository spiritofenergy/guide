package com.kodex.guide.domain.model

enum class Permission(val requiredRole: UserRole) {
    // Просмотр - всем
    VIEW_CATALOG(UserRole.ANONYMOUS),
    VIEW_DETAILS(UserRole.ANONYMOUS),
    SAVE_POST(UserRole.ANONYMOUS),

    // комментарии - только авторизованным
    ADD_COMMENTS(UserRole.USER),
    CREATE_POST(UserRole.BUSINESS),

    // Публикация - только BUSINESS

    EDIT_OWN_POST(UserRole.BUSINESS),
    DELETE_OWN_POST(UserRole.BUSINESS),

    // Публикация - только PREMIUM
    CREATE_POST_PREMIUM(UserRole.PREMIUM),
    EDIT_OWN_POST_PREMIUM(UserRole.PREMIUM),
    DELETE_OWN_POST_PREMIUM(UserRole.PREMIUM),

    // Админка
    MODERATE_CONTENT(UserRole.ADMIN),
    DELETE_ANY_POST(UserRole.ADMIN),
    VIEW_STATISTICS(UserRole.ADMIN);

    fun isGrantedBy(role: UserRole): Boolean = role.hasAccessTo(requiredRole)

}