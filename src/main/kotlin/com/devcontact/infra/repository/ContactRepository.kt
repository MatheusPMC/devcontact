package com.devcontact.infra.repository

import com.devcontact.core.port.ContactRepositoryPort
import com.devcontact.infra.entity.UserContactEntity
import com.mongodb.client.MongoClient
import com.mongodb.client.model.Filters
import io.micronaut.context.annotation.Prototype

@Prototype
class ContactRepository(
    private val mongoClient: MongoClient
): ContactRepositoryPort {
    override fun getAllContactsRepository(sub: String): List<UserContactEntity> {
        println(sub)
        val result = getCollection()
            .find(Filters.eq("sub", sub)).toList()
        println(result)
        return result
    }

    override fun postContactRepository(contactEntity: UserContactEntity): UserContactEntity {
        getCollection().insertOne(contactEntity)
        return contactEntity
    }

    private fun getCollection() =
        mongoClient
            .getDatabase("dev")
            .getCollection("contact", UserContactEntity::class.java)

}