package com.devcontact.core.port

import com.devcontact.infra.entity.UserContactEntity

interface ContactServicePort {
    fun getAllContacts(sub: String): List<UserContactEntity>
    fun getOneContact(userContactEntity: UserContactEntity): UserContactEntity?
    fun postContact(userContactEntity: UserContactEntity): UserContactEntity
    fun putContact(userContactEntity: UserContactEntity): UserContactEntity
}