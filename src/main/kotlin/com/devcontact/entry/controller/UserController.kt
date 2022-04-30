package com.devcontact.entry.controller

import com.devcontact.core.port.KeyclockSevicePort
import com.devcontact.core.service.KeycloakService
import com.devcontact.entry.dto.LoginRequest
import com.devcontact.entry.dto.UserRequest
import com.devcontact.infra.entity.UserContactEntity
import io.micronaut.http.HttpResponse
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule

@Controller("/api/v1/")
class UserController(
    private val keycloakServicePort: KeyclockSevicePort
) {

    @Post("login")
    @Secured(SecurityRule.IS_ANONYMOUS)
    fun loginAccount(@Body user: LoginRequest): MutableHttpResponse<KeycloakService.UserToken>? {
        println(user)
        var test = keycloakServicePort.getTokenUser(user)
        return HttpResponse.ok(test).status(200).body(test)
    }

    @Post("register")
    @Secured(SecurityRule.IS_ANONYMOUS)
    fun createAccount(@Body user: UserRequest): HttpResponse<UserRequest>? {
        println(user)
        var result = keycloakServicePort.signUp(user)
        return HttpResponse.ok(result).status(200).body(result)
    }

    @Put("put")
    @Secured(SecurityRule.IS_ANONYMOUS)
    fun putAccount(@Body user: KeycloakService.UserPutdata): MutableHttpResponse<KeycloakService.UserPutdata>? {
        println(user)
        var result = keycloakServicePort.putUser(user)
        return HttpResponse.ok(result).status(200).body(user)
    }


}