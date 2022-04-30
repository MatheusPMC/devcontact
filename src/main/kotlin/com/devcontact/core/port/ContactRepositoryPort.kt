package com.devcontact.core.port

import com.devcontact.infra.entity.UserContactEntity

interface ContactRepositoryPort {
    fun postContactRepository(userContactEntity: UserContactEntity): UserContactEntity
}