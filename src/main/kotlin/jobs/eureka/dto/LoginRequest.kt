package jobs.eureka.dto

import java.io.Serializable

data class LoginRequest(val username: String, val password: String): Serializable {
}