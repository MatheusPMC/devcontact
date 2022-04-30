package com.devcontact.core.port

import com.devcontact.infra.entity.UserEntity

interface UserRepositoryPort {
    fun postUserRepository(userEntity: UserEntity): UserEntity
    fun putUserRepository(userEntity: UserEntity): UserEntity
    fun delUserRepository(id: String): String
}