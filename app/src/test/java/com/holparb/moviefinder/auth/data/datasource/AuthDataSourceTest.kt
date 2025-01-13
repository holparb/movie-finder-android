package com.holparb.moviefinder.auth.data.datasource

import com.holparb.moviefinder.core.domain.util.Result
import com.holparb.moviefinder.core.domain.util.errors.NetworkError
import com.holparb.moviefinder.testutils.JsonReader
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class AuthDataSourceTest {
    private var isSuccess: Boolean? = null
        get() = field ?: throw IllegalStateException("Mock has not beet initialized")

    private fun givenSuccess() {
        isSuccess = true
    }

    private fun givenFailure() {
        isSuccess = false
    }

    private lateinit var client: HttpClient
    private lateinit var mockEngine: MockEngine
    private lateinit var authDataSource: AuthDataSource

    @Before
    fun setup() {
        mockEngine = MockEngine { request ->
            if (request.url.encodedPath.contains("/authentication/").not()) {
                respond(
                    content = "Error",
                    status = HttpStatusCode.BadRequest,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
            val statusCode =
                if (isSuccess == true) HttpStatusCode.OK else HttpStatusCode.InternalServerError
            val content =
                if (isSuccess == true) JsonReader.readJsonFile("RequestTokenResponse.json")
                else JsonReader.readJsonFile("AuthFailedResponse.json")
            respond(
                content = content,
                status = statusCode,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        client = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(
                    json = Json {
                        ignoreUnknownKeys = true
                    }
                )
            }
        }

        authDataSource = AuthDataSource(client)
    }

    @Test
    fun token_generated_successfully() = runBlocking {
        givenSuccess()
        val response = authDataSource.getRequestToken()
        Assert.assertTrue(response is Result.Success)
        Assert.assertTrue((response as Result.Success).data.success)
        Assert.assertTrue(response.data.requestToken.isNotBlank())
    }

    @Test
    fun token_generation_failed() = runBlocking {
        givenFailure()
        val response = authDataSource.getRequestToken()
        Assert.assertTrue(response is Result.Error)
        Assert.assertEquals(NetworkError.SERVER_ERROR, (response as Result.Error).error)
    }
}