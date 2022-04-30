package com.devcontact.core.port

import com.devcontact.infra.entity.UserContactEntity


interface ContactServicePort {
    fun postContact(userContactEntity: UserContactEntity): UserContactEntity
}