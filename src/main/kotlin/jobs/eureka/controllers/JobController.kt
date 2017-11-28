package jobs.eureka.controllers

import jobs.eureka.services.IUserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class JobController {

    @GetMapping("/jobs")
    fun showJobs(): String {
        return "jobs"
    }

}