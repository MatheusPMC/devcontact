package com.devcontact.entry.controller

import com.devcontact.core.port.KeyclockSevicePort
import com.devcontact.core.port.UserServicePort
import com.devcontact.core.service.KeycloakService
import com.devcontact.entry.dto.LoginRequest
import com.devcontact.entry.dto.UserRequest
import com.devcontact.infra.entity.UserEntity
import io.micronaut.http.HttpResponse
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule

@Controller("/api/v1/")
class UserController(
    private val keycloakServicePort: KeyclockSevicePort,
    private val userServicePort: UserServicePort
) {

    @Post("login")
    @Secured(SecurityRule.IS_ANONYMOUS)
    fun loginAccount(@Body user: LoginRequest): MutableHttpResponse<KeycloakService.UserToken>? {
        println(user)
        var test = keycloakServicePort.loginUserKc(user)
        return HttpResponse.ok(test).status(200).body(test)
    }

    @Post("register")
    @Secured(SecurityRule.IS_ANONYMOUS)
    fun createAccount(@Body user: UserRequest): MutableHttpResponse<UserEntity>? {
        println(user)
        var result = userServicePort.createUser(user)
        return HttpResponse.ok(result).status(200).body(result)
    }

    @Put("put")
    @Secured(SecurityRule.IS_ANONYMOUS)
    fun putAccount(@Body user: KeycloakService.UserPutdata): MutableHttpResponse<UserEntity>? {
        println(user)
        var result = userServicePort.updateUser(user)
        return HttpResponse.ok(result).status(200).body(result)
    }

    @Delete("user/{sub}")
    @Secured(SecurityRule.IS_ANONYMOUS)
    fun deleteAccount(@PathVariable sub: String): MutableHttpResponse<String>? {
        println(sub)
        var result = userServicePort.deleteUser(sub)
        return HttpResponse.ok(result).status(200).body(result)
    }
}