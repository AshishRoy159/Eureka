package jobs.eureka.services.impl

import jobs.eureka.repositories.UserRepository
import jobs.eureka.services.IUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserServiceImpl : IUserService {

    @Autowired
    lateinit private var userRepository: UserRepository

    override fun sayHello(): String? {
        return userRepository.sayHello()
    }
}