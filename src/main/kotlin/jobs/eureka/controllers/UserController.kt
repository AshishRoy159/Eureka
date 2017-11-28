package jobs.eureka.controllers

import jobs.eureka.services.IUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController {

    @Autowired
    lateinit private var userService: IUserService

    @GetMapping("/hello")
    fun sayHello(): String? {
        return userService.sayHello()
    }

    @GetMapping("/hi")
    fun sayHi(): String {
        return "Hi"
    }
}