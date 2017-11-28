package jobs.eureka.configurations

import jobs.eureka.configurations.security.ApplicationAuthenticationFailureHandler
import jobs.eureka.configurations.security.ApplicationAuthenticationFilter
import jobs.eureka.configurations.security.ApplicationAuthenticationSuccessHandler
import jobs.eureka.configurations.security.SecurityExceptionFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.access.channel.ChannelProcessingFilter
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler
import org.springframework.web.bind.annotation.RequestMethod
import java.io.IOException
import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Configuration
class SecurityConfiguration: WebSecurityConfigurerAdapter() {

    @Autowired
    lateinit var entryPoint: AuthenticationEntryPoint

    @Autowired
    lateinit var logoutSuccessHandler: LogoutSuccessHandler

    @Autowired
    lateinit var userService: UserDetailsService

    @Autowired
    lateinit var authenticationSuccessHandler: ApplicationAuthenticationSuccessHandler

    @Autowired
    lateinit var authenticationFailureHandler: ApplicationAuthenticationFailureHandler

    @Autowired
    lateinit var exceptionFilter: SecurityExceptionFilter

    override fun configure(httpSecurity: HttpSecurity) {
        httpSecurity.authorizeRequests()
                .antMatchers("/swagger-ui.html", "/configuration/**"
                        ,"/swagger-resources/**","/v2/api-docs/**").permitAll()
                .antMatchers("/public/**","/topic/**", "/webSockConnect/**").permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .anyRequest().authenticated().and()
                .exceptionHandling().authenticationEntryPoint(entryPoint)
                .and().logout().logoutSuccessHandler(logoutSuccessHandler)
                .and().csrf().disable()

        httpSecurity.addFilterBefore(getApplicationAuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)

        httpSecurity.addFilterBefore(corsFilter(), ChannelProcessingFilter::class.java)

        httpSecurity.addFilterAfter(exceptionFilter, ChannelProcessingFilter::class.java)
    }

    override fun configure(web: WebSecurity) {
        web.ignoring().antMatchers("/webjars/springfox-swagger-ui/**").and().ignoring()
                .antMatchers("/configuration/ui").and().ignoring()
                .antMatchers("/swagger-resources").and().ignoring()
                .antMatchers("/v2/api-docs").and().ignoring()
                .antMatchers("/public/**").and().ignoring()
                .antMatchers("/webSockConnect/**").and().ignoring()
                .antMatchers("/topic/**").and().ignoring()
    }

    @Throws(Exception::class)
    fun configureGlobal(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userService).passwordEncoder(BCryptPasswordEncoder())
    }

    @Throws(Exception::class)
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    @Bean
    @Throws(Exception::class)
    fun getApplicationAuthenticationFilter(): ApplicationAuthenticationFilter {
        val authenticationFilter = ApplicationAuthenticationFilter()
        authenticationFilter.setAuthenticationManager(authenticationManager())
        authenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler)
        authenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler)

        return authenticationFilter
    }

    @Bean
    fun corsFilter(): Filter {
        return object: Filter {

            @Throws(IOException::class, ServletException::class)
            override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
                val response = response as HttpServletResponse
                val request = request as HttpServletRequest

                response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200")
                response.setHeader("Access-Control-Allow-Credentials", "true")
                response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE")
                response.setHeader("Access-Control-Allow-Headers",
                        "X-Requested-With, Content-Type, Authorization, Origin, Accept," +
                                " Access-Control-Request-Method, Access-Control-Request-Headers")

                if(request.method.equals(RequestMethod.OPTIONS.name)) {
                    response.writer.println("Success")
                } else {
                    chain.doFilter(request, response)
                }

            }
            override fun destroy() {}

            override fun init(filterConfig: FilterConfig) {}
        }
    }
}

