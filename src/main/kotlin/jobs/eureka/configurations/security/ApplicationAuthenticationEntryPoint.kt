package jobs.eureka.configurations.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class ApplicationAuthenticationEntryPoint: AuthenticationEntryPoint {

    @Autowired
    lateinit var exceptionHandler: SecurityExceptionHandler

    @Throws(IOException::class, ServletException::class)
    override fun commence(request: HttpServletRequest,
                 response: HttpServletResponse,
                 authenticationException: AuthenticationException) {

        exceptionHandler.handleExceptions(request,response,authenticationException)
    }
}