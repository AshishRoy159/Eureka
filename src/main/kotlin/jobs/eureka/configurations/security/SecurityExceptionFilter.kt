package jobs.eureka.configurations.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class SecurityExceptionFilter: Filter {

    @Autowired
    lateinit var exceptionHandler: SecurityExceptionHandler

    override fun destroy() {}

    override fun init(p0: FilterConfig) {}

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        try {
            chain.doFilter(request, response)
        } catch (e: Exception) {
            exceptionHandler.handleExceptions(request as HttpServletRequest, response as HttpServletResponse, e)
        }
    }
}