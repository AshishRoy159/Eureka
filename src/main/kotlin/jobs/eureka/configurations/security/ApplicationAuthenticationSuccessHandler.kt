package jobs.eureka.configurations.security

import jobs.eureka.dto.ApplicationUser
import org.springframework.security.core.Authentication
import org.springframework.security.web.DefaultRedirectStrategy
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession

@Component
@RestController
class ApplicationAuthenticationSuccessHandler: SimpleUrlAuthenticationSuccessHandler() {

    private val redirectStrategy = DefaultRedirectStrategy()

    override fun onAuthenticationSuccess(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication) {
        clearAuthenticationAttributes(request)
        val user = authentication.principal as ApplicationUser

        redirectStrategy.sendRedirect(request, response, "/public/login-info")
    }

    @GetMapping("/public/login-info")
    fun getLoginInfo(httpSession: HttpSession, response: HttpServletResponse): Map<String, Any> {
        val loginInfo = HashMap<String, Any>()

        loginInfo.put("JSESSIONID", httpSession.id)
        response.setHeader("JSESSIONID", httpSession.id)

        return loginInfo
    }
}