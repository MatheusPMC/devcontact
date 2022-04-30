package com.devcontact.infra.entity

import com.devcontact.commons.annotations.NoArg
import io.micronaut.core.annotation.Introspected
import java.time.LocalDateTime

@Introspected
@NoArg
data class UserEntity(
    var sub: String? = "",
    var email_verified: Boolean? = false,
    var name: String? = "",
    var preferred_username: String? = "",
    var given_name: String? = "",
    var family_name: String? = "",
    var email:String? = "",
    var password: String? = "",
    var createdAt: LocalDateTime? = null,
    var updatedAt: LocalDateTime? = null
)