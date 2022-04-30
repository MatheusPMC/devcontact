package com.devcontact.core.service

import com.devcontact.commons.extensions.TestLogging
import com.devcontact.commons.extensions.logger
import com.devcontact.commons.handler.KeycloakException
import com.devcontact.core.config.KeycloakConfiguration
import com.devcontact.core.port.KeyclockSevicePort
import com.devcontact.entry.dto.LoginRequest
import com.devcontact.entry.dto.UserRequest
import com.google.gson.JsonParser
import com.nimbusds.jose.jwk.JWK
import com.squareup.okhttp.MediaType
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.RequestBody
import io.jsonwebtoken.Jwts
import io.micronaut.context.annotation.Prototype
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.RSAPublicKeySpec
import java.time.Instant

@Prototype
class KeycloakService(
    private val keycloakConfiguration: KeycloakConfiguration,
    private val keycloakCacheService: KeycloakCacheService

): KeyclockSevicePort, TestLogging {

    val client = OkHttpClient()

        override fun getTokenUser(user: LoginRequest): UserToken {
            val mediaType = MediaType.parse("application/x-www-form-urlencoded")
            val body = RequestBody.create(mediaType,
                "username=${user.usuario}&password=${user.senha}&grant_type=password&client_id=${keycloakConfiguration.clientId}&client_secret=${keycloakConfiguration.clientSecret}")
            val request = Request.Builder()
                .url(keycloakConfiguration.accessTokenAdminCliUrl)
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build()
            val response = client.newCall(request).execute()

            val responseBodyToString = response.body().string()
            val responseBodyToJson = JsonParser.parseString(responseBodyToString)

            println(responseBodyToJson.asJsonObject["access_token"].asString)
            if (responseBodyToString.contains("access_token")) {
                logger().info("getAccessTokenAdminCli - access_token capturado!")
                var token = responseBodyToJson.asJsonObject["access_token"].asString
                var user = getUser(token, null)
//            var user = userRepositoryPort.getOneUserRepository(user.usuario)
                var responseUser = UserToken(user.sub, token)

                return responseUser
            } else {
                logger().error("getAccessTokenAdminCli - access_token nÃ£o capturado!")
                throw java.lang.RuntimeException("KeycloakSingUpService - Token not received")
            }
        }


        //TODO GET USER
         fun getUser(token: String?, password: String?): Test {
            val mediaType = MediaType.parse("application/json")
            val body = RequestBody.create(mediaType, "{}")
            val request = Request.Builder()
                .url("http://localhost:8080/realms/login/protocol/openid-connect/userinfo")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer $token")
                .build()

            val response = client.newCall(request).execute()
            val responseBodyToString = response.body().string()
            val responseBodyToJson = JsonParser.parseString(responseBodyToString)

            var test = Test()
            test.sub = responseBodyToJson.asJsonObject["sub"].asString
            test.email_verified = responseBodyToJson.asJsonObject["email_verified"].asBoolean
            test.name = responseBodyToJson.asJsonObject["name"].asString
            test.preferred_username = responseBodyToJson.asJsonObject["preferred_username"].asString
            test.given_name = responseBodyToJson.asJsonObject["given_name"].asString
            test.family_name = responseBodyToJson.asJsonObject["family_name"].asString
            test.email = responseBodyToJson.asJsonObject["email"].asString
            test.password = password

            println("entrou no response" + test)

            return test
        }


        //TODO: Create User

        override fun signUp(user: UserRequest): UserRequest {
            logger().info("signUp - Inicio do serviÃ§o keycloak")

            val tokenAdminCliCache = keycloakCacheService.readTokenAdminCliCache()

            logger().info("signUp - Verificando o Token do Admin Cli (CACHE)")
            val accessToken = if (tokenAdminCliCache != null && this.verifyExpTokenAdminCli(tokenAdminCliCache)) {
                logger().info("signUp - Token do admin cli (cache) Ã© vÃ¡lido")
                tokenAdminCliCache
            } else {
                logger().info("signUp - Token do admin cli (cache) invÃ¡lido, gerando um novo...")
                keycloakCacheService.deleteTokenAdminCliCache()

                val accessTokenAdminCli = this.getAccessTokenAdminCli()
                keycloakCacheService.saveTokenAdminCliCache(accessTokenAdminCli)

                println(accessTokenAdminCli)
                accessTokenAdminCli
            }

            val mediaType = MediaType.parse("application/json")
            val body = RequestBody.create(mediaType, "{\"firstName\":\"${user.firstName}\"," +
                    "\"lastName\":\"${user.lastName}\"," +
                    "\"username\":\"${user.userName}\"," +
                    "\"email\":\"${user.email}\"," +
                    "\"emailVerified\":${true}," +
                    "\"credentials\":[{\"type\":\"password\",\"value\":\"${user.password}\",\"temporary\":false}],\"enabled\":true}")
            val request = Request.Builder()
                .url(keycloakConfiguration.usersRegisterUrl)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer $accessToken")
                .build()

            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                throw KeycloakException("KeycloakSingUpService - NÃ£o foi possÃ­vel cadastrar o usuÃ¡rio")
            }

            var result = getTokenUser(LoginRequest(user.userName, user.password))
            println(result)
            var testResult =  getUser(result?.token, user.password)
            println(testResult)

//            userRepositoryPort.postUserRepository(testResult)


            logger().info("signUp - usuÃ¡rio registrado no keycloak!")
            return UserRequest(user.userName, user.firstName, user.lastName, user.email, user.password)
        }
//
//
//        //TODO: Put User
//
//
//        override fun putUser(user: UserPutdata): UserPutdata {
//
//            logger().info("signUp - Inicio do serviÃ§o keycloak")
//
//            var accessToken = getToken()
//
//            val mediaType = MediaType.parse("application/json")
//            val body = RequestBody.create(mediaType,
//                "{\n\t\"email\" : \"${user.email}\",\n\t\"lastName\": \"${user.lastName}\",\n\t\"firstName\": \"${user.firstName}\",\n\t\"credentials\": [{ \n\t\t\"type\": \"password\",\n\t\t\"value\": \"${user.password}\"\n}]\t\n}")
//            val request = Request.Builder()
//                .url("http://localhost:8080/admin/realms/login/users/${user.sub}")
//                .put(body)
//                .addHeader("Content-Type", "application/json")
//                .addHeader("Authorization", "Bearer $accessToken")
//                .build()
//
//            val response = client.newCall(request).execute()
//            println(response.body())
//
//            return user
//        }
//
//
//        //TODO: Delete User
//        override fun deleteUser(sub: String) {
//            val client = OkHttpClient()
//
//            var accessToken = getToken()
//
//
//            val request = Request.Builder()
//                .url("http://localhost:8080/admin/realms/login/users/$sub")
//                .delete(null)
//                .addHeader("Authorization", "Bearer ${accessToken}")
//                .build()
//
//            val response = client.newCall(request).execute()
//        }
//
//
//
//
//        private fun getToken(): String {
//            val tokenAdminCliCache = keycloakCacheService.readTokenAdminCliCache()
//
//            logger().info("signUp - Verificando o Token do Admin Cli (CACHE)")
//            val accessToken = if (tokenAdminCliCache != null && this.verifyExpTokenAdminCli(tokenAdminCliCache)) {
//                logger().info("signUp - Token do admin cli (cache) Ã© vÃ¡lido")
//                tokenAdminCliCache
//            } else {
//                logger().info("signUp - Token do admin cli (cache) invÃ¡lido, gerando um novo...")
//                keycloakCacheService.deleteTokenAdminCliCache()
//
//                val accessTokenAdminCli = this.getAccessTokenAdminCli()
//                keycloakCacheService.saveTokenAdminCliCache(accessTokenAdminCli)
//
//                println(accessTokenAdminCli)
//                accessTokenAdminCli
//            }
//            return accessToken
//        }

        private fun getAccessTokenAdminCli(): String {
            logger().info("getAccessTokenAdminCli - capturando o access_token do admin-cli")

            val mediaType = MediaType.parse("application/x-www-form-urlencoded")
            val body = RequestBody.create(mediaType,
                "grant_type=${keycloakConfiguration.grantType}&client_id=${keycloakConfiguration.clientId}&client_secret=${keycloakConfiguration.clientSecret}")
            val request = Request.Builder()
                .url(keycloakConfiguration.accessTokenAdminCliUrl)
                .post(body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build()

            val response = client.newCall(request).execute()

            val responseBodyToString = response.body().string()
            val responseBodyToJson = JsonParser.parseString(responseBodyToString)

            if (responseBodyToString.contains("access_token")) {
                logger().info("getAccessTokenAdminCli - access_token capturado!")
                return responseBodyToJson.asJsonObject["access_token"].asString
            } else {
                logger().error("getAccessTokenAdminCli - access_token nÃ£o capturado!")
                throw KeycloakException("KeycloakSingUpService - Token not received")
            }
        }


        private fun verifyExpTokenAdminCli(token: String): Boolean {
            logger().info("verifyExpTokenAdminCli - verificando expiraÃ§Ã£o do token do admin-cli")

            try {
                val jwk = JWK.parse(this.getJWKCerts())

                val rsaKey = jwk.toRSAKey()

                val factory = KeyFactory.getInstance(jwk.keyType.value)
                val rsaPublicKeySpec = RSAPublicKeySpec(
                    rsaKey.modulus.decodeToBigInteger(), // n
                    rsaKey.publicExponent.decodeToBigInteger() //e
                )
                val publicKeySpec: PublicKey = factory.generatePublic(rsaPublicKeySpec)

                val claims = Jwts.parser().setSigningKey(publicKeySpec).parseClaimsJws(token)

                val expiration = claims.body["exp"] as Int
                val expirationInstant = Instant.ofEpochMilli(expiration.toLong())

                return Instant.now().isAfter(expirationInstant)


            } catch (e: Exception) {
                logger().error("verifyExpTokenAdminCli - token expirado ou invÃ¡lido")
                return false
            }
        }

        private fun getJWKCerts(): String {
            logger().info("getJWKCerts - obtendo certs do keycloak e gerando a chave pÃºblica (RSA)")
            // Obtendo o certificado para gerar a public key
            val request = Request.Builder()
                .url(keycloakConfiguration.certsRSAUrl)
                .get()
                .build()

            val response = client.newCall(request).execute()
            val responseJson = JsonParser.parseString(response.body().string())
            val keys = responseJson.asJsonObject["keys"].asJsonArray

            return keys[1].toString()
        }

        data class Test(
            var sub: String? = "",
            var email_verified: Boolean? = false,
            var name: String? = "",
            var preferred_username: String? = "",
            var given_name: String? = "",
            var family_name: String? = "",
            var email: String? = "",
            var password: String? = "",
        )

        data class UserPutdata(
            var sub: String = "",
            var userName: String = "",
            var firstName: String = "",
            var lastName: String = "",
            var email: String = "",
            var password: String = "",
            var token: String = "",
        )

        data class UserToken(
            var sub: String?,
            var token: String?,
        )
    }