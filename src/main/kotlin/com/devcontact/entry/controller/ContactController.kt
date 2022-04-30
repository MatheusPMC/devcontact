package com.devcontact.entry.controller

import com.devcontact.core.port.ContactServicePort
import com.devcontact.infra.entity.UserContactEntity
import io.micronaut.http.HttpResponse
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Produces
import io.micronaut.security.annotation.Secured
import java.util.*

@Controller("/api/v1/")
class ContactController(
    private val contactServicePort: ContactServicePort
) {

    @Post("contact")
    @Secured("viewer")
    @Produces
    fun postContact(@Body userContactEntity: UserContactEntity): MutableHttpResponse<UserContactEntity>? {
        userContactEntity.contactId = UUID.randomUUID().toString()
        println(userContactEntity)
        val result = contactServicePort.postContact(userContactEntity)
        return HttpResponse.created(result).status(201)
    }
}