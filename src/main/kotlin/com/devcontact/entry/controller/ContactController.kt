package com.devcontact.entry.controller

import com.devcontact.core.port.ContactServicePort
import com.devcontact.infra.entity.UserContactEntity
import io.micronaut.http.HttpResponse
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.security.annotation.Secured
import java.util.*

@Controller("/api/v1/")
class ContactController(
    private val contactServicePort: ContactServicePort
) {

    @Get("/{sub}")
    @Secured("viewer")
    @Produces
    fun getAllContacts(@PathVariable sub: String): MutableHttpResponse<List<UserContactEntity>>? {
        var result = contactServicePort.getAllContacts(sub)
        return HttpResponse.ok(result).status(200)
    }

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