package com.devcontact.core.service

import com.devcontact.core.port.ContactRepositoryPort
import com.devcontact.core.port.ContactServicePort
import com.devcontact.infra.entity.UserContactEntity
import io.micronaut.context.annotation.Prototype

@Prototype
class ContactService(
    private val contactRepositoryPort: ContactRepositoryPort
): ContactServicePort {
    override fun getAllContacts(sub: String): List<UserContactEntity> {
        var repository = contactRepositoryPort.getAllContactsRepository(sub)
        return repository
    }

    override fun getOneContact(userContactEntity: UserContactEntity): UserContactEntity? {
        val repository = contactRepositoryPort.getOneContactRepository(userContactEntity)
        return repository
    }

    override fun postContact(userContactEntity: UserContactEntity): UserContactEntity {
        var repository = contactRepositoryPort.postContactRepository(userContactEntity)
        return repository
    }

    override fun putContact(userContactEntity: UserContactEntity): UserContactEntity {
        val repository = contactRepositoryPort.putContactRepository(userContactEntity)
        return repository
    }
}