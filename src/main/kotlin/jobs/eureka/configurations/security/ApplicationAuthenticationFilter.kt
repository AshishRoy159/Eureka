package jobs.eureka.configurations.security

import com.fasterxml.jackson.databind.ObjectMapper
import jobs.eureka.APIException
import jobs.eureka.dto.LoginRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.validation.BindException
import org.springframework.validation.Validator
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class ApplicationAuthenticationFilter: AbstractAuthenticationProcessingFilter(AntPathRequestMatcher(login, loginHttpMethod)) {

    companion object {
        private val loginContentType = "application/json;charset=UTF-8"
        private val loginHttpMethod = "POST"
        private val login = "/login"
        private val LOGIN_REQUEST = "Login Request"
    }

    @Autowired
    lateinit var validator: Validator

    @Throws(AuthenticationException::class)
    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {

        if(loginHttpMethod != request.method) {
            throw AuthenticationServiceException("Authentication method not supported: "+request.method)
        }

        var contentType = request.contentType
        if(contentType != null) {
            contentType = contentType.replace(("\\s+").toRegex(), "")
        }
        if(loginContentType != contentType) {
            throw AuthenticationServiceException("Content Type "+request.contentType+" Not Supported")
        }

        val loginRequest = getLoginRequest(request)

        validateLoginRequest(loginRequest)

        val authRequest = UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password)
        authRequest.details = authenticationDetailsSource.buildDetails(request)

        return this.authenticationManager.authenticate(authRequest)
    }

    private fun getLoginRequest(request: HttpServletRequest): LoginRequest {
        var loginRequest: LoginRequest? = null

        try {
            loginRequest = ObjectMapper().readValue(request.reader, LoginRequest::class.java)
        } catch (e: Exception) {
            throw APIException("Invalid JSON format of credentials", HttpStatus.BAD_REQUEST)
        }

        return loginRequest

    }

    private fun validateLoginRequest(loginRequest: LoginRequest) {
        val bindException = BindException(loginRequest, LOGIN_REQUEST)

        validator.validate(loginRequest, bindException)
        if(bindException != null && bindException.hasErrors()) {
            throw APIException(bindException.fieldError.defaultMessage, HttpStatus.BAD_REQUEST)
        }
    }
}