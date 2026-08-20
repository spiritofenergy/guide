package com.kodex.guide.domain.role

import com.kodex.guide.domain.model.Permission
import com.kodex.guide.domain.model.UserRole
import javax.inject.Inject

class DefaultRolePermissionChecker @Inject constructor() : RolePermissionChecker {

    override fun hasPermission(
        role: UserRole,
        permission: Permission
    ): Boolean {
        return permission.isGrantedBy(role)
    }

    override fun canAccess(
        role: UserRole,
        requiredRole: UserRole
    ): Boolean {
        return role.hasAccessTo(requiredRole)
    }

    override fun canCreatePost(role: UserRole): Boolean {
        return hasPermission(role, Permission.CREATE_POST)
    }

    override fun canModerate(role: UserRole): Boolean {
        return hasPermission(role, Permission.MODERATE_CONTENT)
    }
}