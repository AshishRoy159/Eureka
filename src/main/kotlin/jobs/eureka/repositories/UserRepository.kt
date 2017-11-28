package jobs.eureka.repositories

import org.springframework.stereotype.Repository

@Repository
class UserRepository {

    fun sayHello(): String? {
        return "hello"
    }
}