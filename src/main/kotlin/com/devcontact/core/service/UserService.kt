package com.devcontact.core.service

import com.devcontact.core.port.KeyclockSevicePort
import com.devcontact.core.port.UserRepositoryPort
import com.devcontact.core.port.UserServicePort
import com.devcontact.entry.dto.UserRequest
import com.devcontact.infra.entity.UserEntity
import io.micronaut.context.annotation.Prototype

@Prototype
class UserService(
    private val keyclockSevicePort: KeyclockSevicePort,
    private val userRepositoryPort: UserRepositoryPort
): UserServicePort {

    override fun createUser(userRequest: UserRequest): UserEntity {
        var createdUser = keyclockSevicePort.signUp(userRequest)
        var repository = userRepositoryPort.postUserRepository(createdUser)
        return repository
    }
}