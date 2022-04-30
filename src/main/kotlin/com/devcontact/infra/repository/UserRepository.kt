package com.devcontact.infra.repository

import com.devcontact.core.port.UserRepositoryPort
import com.devcontact.infra.entity.UserEntity
import com.mongodb.client.MongoClient
import io.micronaut.context.annotation.Prototype

@Prototype
class UserRepository(
    private val mongoClient: MongoClient
): UserRepositoryPort {



    override fun postUserRepository(userEntity: UserEntity): UserEntity {
        println("Repository " +userEntity)
        getCollection().insertOne(userEntity)
        return userEntity
    }

    private fun getCollection() =
        mongoClient
            .getDatabase("dev")
            .getCollection("user", UserEntity::class.java)

}