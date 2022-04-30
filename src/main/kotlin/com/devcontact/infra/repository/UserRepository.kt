package com.devcontact.infra.repository

import com.devcontact.core.port.UserRepositoryPort
import com.devcontact.infra.entity.UserEntity
import com.mongodb.client.MongoClient
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import io.micronaut.context.annotation.Prototype
import java.time.LocalDateTime

@Prototype
class UserRepository(
    private val mongoClient: MongoClient
): UserRepositoryPort {
    
    override fun postUserRepository(userEntity: UserEntity): UserEntity {
        userEntity.createdAt = LocalDateTime.now()
        getCollection().insertOne(userEntity)
        return userEntity
    }

    override fun putUserRepository(userEntity: UserEntity): UserEntity {
        userEntity.updatedAt = LocalDateTime.now()
        getCollection()
            .findOneAndUpdate(
                Filters.eq("sub", userEntity.sub),
            listOf(
                Updates.set("email", userEntity.email),
                Updates.set("email_verified", userEntity.email_verified),
                Updates.set("family_name", userEntity.family_name),
                Updates.set("given_name", userEntity.given_name),
                Updates.set("name", userEntity.name),
                Updates.set("password", userEntity.password),
                Updates.set("preferred_username", userEntity.preferred_username),
                Updates.set("updatedAt", userEntity.updatedAt),
            )
            )
        return userEntity
    }

    override fun delUserRepository(id: String): String {
        var result = getCollection()
            .deleteOne(
                Filters.eq("sub", id)
            ).deletedCount

        return if (result.toInt() == 1) {
            "User-apagado-com-sucesso!"
        } else {
            "User-não-Encontrado"
        }
    }

    private fun getCollection() =
        mongoClient
            .getDatabase("dev")
            .getCollection("user", UserEntity::class.java)

}