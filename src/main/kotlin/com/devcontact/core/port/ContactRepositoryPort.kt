package com.devcontact.core.port

import com.devcontact.infra.entity.UserContactEntity

interface ContactRepositoryPort {
    fun getAllContactsRepository(sub: String): List<UserContactEntity>
    fun postContactRepository(userContactEntity: UserContactEntity): UserContactEntity
}