package jobs.eureka.configurations.security

import com.fasterxml.jackson.databind.ObjectMapper
import jobs.eureka.APIException
import org.springframework.security.core.AuthenticationException
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class SecurityExceptionHandler {

    public fun handleExceptions(request: HttpServletRequest, response: HttpServletResponse, e: Exception) {
        response.contentType = "application/json"
        if(e is APIException) {
            val apiE: APIException = e as APIException
            response.status = apiE.getHttpStatus().value()
        } else if (e is AuthenticationException) {
            response.status = HttpServletResponse.SC_UNAUTHORIZED
        } else {
            response.status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
        }

        ObjectMapper().writeValue(response.outputStream, e.message)
    }
}