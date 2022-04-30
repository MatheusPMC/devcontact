package com.devcontact.core.port

import com.devcontact.core.service.KeycloakService
import com.devcontact.entry.dto.LoginRequest
import com.devcontact.entry.dto.UserRequest
import com.devcontact.infra.entity.UserEntity

interface KeyclockSevicePort {
    fun getTokenUser(user: LoginRequest): KeycloakService.UserToken
//    fun getUser(token: String?, password: String?): KeycloakService.Test
    fun signUp(user: UserRequest): UserEntity
    fun putUser(user: KeycloakService.UserPutdata): KeycloakService.UserPutdata
    fun deleteUser(sub: String)
}