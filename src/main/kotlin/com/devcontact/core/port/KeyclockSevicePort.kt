package com.devcontact.core.port

import com.devcontact.core.service.KeycloakService
import com.devcontact.entry.dto.LoginRequest
import com.devcontact.entry.dto.UserRequest
import com.devcontact.infra.entity.UserEntity

interface KeyclockSevicePort {
    fun loginUserKc(user: LoginRequest): KeycloakService.UserToken
//    fun getUser(token: String?, password: String?): KeycloakService.Test
    fun createUserKc(user: UserRequest): UserEntity
    fun putUserKc(user: KeycloakService.UserPutdata): UserEntity
    fun deleteUserKc(sub: String): String
}