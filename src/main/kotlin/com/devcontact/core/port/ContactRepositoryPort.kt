package com.devcontact.core.port

import com.devcontact.infra.entity.UserContactEntity

interface ContactRepositoryPort {
    fun getAllContactsRepository(sub: String): List<UserContactEntity>
    fun getOneContactRepository(userContactEntity: UserContactEntity): UserContactEntity?
    fun postContactRepository(userContactEntity: UserContactEntity): UserContactEntity
    fun putContactRepository(userContactEntity: UserContactEntity): UserContactEntity
    fun delContactRepository(id: String): String
}