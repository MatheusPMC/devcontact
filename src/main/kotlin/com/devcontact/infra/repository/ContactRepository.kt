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

    override fun getOneContactRepository(userContactEntity: UserContactEntity): UserContactEntity? {
        println(userContactEntity.contactId)
        var contactEntity = getCollection().find(
            Filters.eq("contactId", userContactEntity.contactId)).toList().firstOrNull()
        println(contactEntity)
        return contactEntity
    }


    override fun postContactRepository(contactEntity: UserContactEntity): UserContactEntity {
        getCollection().insertOne(contactEntity)
        return contactEntity
    }

    override fun putContactRepository(contactEntity: UserContactEntity): UserContactEntity {
         getCollection()
            .replaceOne(
                Filters.eq("contactId", contactEntity.contactId),
                contactEntity
            )

        return contactEntity
    }

    override fun delContactRepository(id: String): String {
        var result = getCollection()
            .deleteOne(
                Filters.eq("contactId", id)
            ).deletedCount

        return if (result.toInt() == 1) {
            "Disciplina-apagada-com-sucesso!"
        } else {
            "Disciplina-não-Encontrada"
        }

    }

    private fun getCollection() =
        mongoClient
            .getDatabase("dev")
            .getCollection("contact", UserContactEntity::class.java)

}