package jobs.eureka.configurations.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class ApplicationAuthenticationFailureHandler: SimpleUrlAuthenticationFailureHandler() {

    @Autowired
    lateinit var exceptionHandler: SecurityExceptionHandler

    override fun onAuthenticationFailure(request: HttpServletRequest,
                                         response: HttpServletResponse, exception: AuthenticationException) {
        exceptionHandler.handleExceptions(request,response,exception)
    }
}