package jobs.eureka.configurations.security

import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class ApplicationLogoutSuccessHandler: LogoutSuccessHandler {

    override fun onLogoutSuccess(request: HttpServletRequest,
                                 response: HttpServletResponse,
                                 authentication: Authentication) {

        response.status = HttpServletResponse.SC_OK
        response.writer.flush()
    }
}