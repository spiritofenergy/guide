package com.kodex.guide.domain.role

import com.kodex.guide.domain.model.Permission
import com.kodex.guide.domain.model.UserRole

interface RolePermissionChecker {
    fun hasPermission(
        role: UserRole,
        permission: Permission
    ): Boolean

    fun canAccess(
        role: UserRole,
        requiredRole: UserRole
    ): Boolean

    fun canCreatePost(role: UserRole): Boolean

    fun canModerate(role: UserRole): Boolean
}