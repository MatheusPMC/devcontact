package com.devcontact.core.port

import com.devcontact.core.service.KeycloakService
import com.devcontact.entry.dto.UserRequest
import com.devcontact.infra.entity.UserEntity

interface UserServicePort {
    fun createUser(userRequest: UserRequest): UserEntity
    fun updateUser(userPutData: KeycloakService.UserPutdata): UserEntity
}