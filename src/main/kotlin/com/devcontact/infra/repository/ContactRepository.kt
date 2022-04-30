package com.devcontact.infra.repository

import com.devcontact.core.port.ContactRepositoryPort
import com.devcontact.infra.entity.UserContactEntity
import com.mongodb.client.MongoClient
import io.micronaut.context.annotation.Prototype

@Prototype
class ContactRepository(
    private val mongoClient: MongoClient
): ContactRepositoryPort {

    override fun postContactRepository(contactEntity: UserContactEntity): UserContactEntity {
        getCollection().insertOne(contactEntity)
        return contactEntity
    }

    private fun getCollection() =
        mongoClient
            .getDatabase("dev")
            .getCollection("contact", UserContactEntity::class.java)

}