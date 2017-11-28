package jobs.eureka

import org.springframework.http.HttpStatus

class APIException: RuntimeException {

    lateinit var exceptionMessage: String
    lateinit var status: HttpStatus

    constructor(message: String, httpStatus: HttpStatus) {
        this.exceptionMessage = message
        this.status = httpStatus
    }
    constructor(message: String) {
        this.exceptionMessage = message
    }
    constructor(httpStatus: HttpStatus) {
        this.status = httpStatus
    }

    fun getHttpStatus(): HttpStatus {
        return status;
    }

    fun setHttpStatus(status: HttpStatus) {
        this.status = status
    }

    fun getException(): String {
        return exceptionMessage
    }

    fun setException(message: String) {
        this.exceptionMessage = message
    }

}
