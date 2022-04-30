package com.devcontact.core.service

import com.devcontact.core.port.ContactRepositoryPort
import com.devcontact.core.port.ContactServicePort
import com.devcontact.infra.entity.UserContactEntity
import io.micronaut.context.annotation.Prototype

@Prototype
class ContactService(
    private val contactRepositoryPort: ContactRepositoryPort
): ContactServicePort {

    override fun postContact(userContactEntity: UserContactEntity): UserContactEntity {
        var repository = contactRepositoryPort.postContactRepository(userContactEntity)
        return repository
    }
}