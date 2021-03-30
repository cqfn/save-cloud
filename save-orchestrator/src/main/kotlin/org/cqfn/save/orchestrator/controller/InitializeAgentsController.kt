package org.cqfn.save.orchestrator.controller

import org.cqfn.save.entities.Execution
import org.cqfn.save.orchestrator.service.AgentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

typealias Status = Mono<ResponseEntity<HttpStatus>>

/**
 * Controller used to starts agents with needed information
 */
@RestController
class InitializeAgentsController {
    /**
     * agentService
     */
    @Autowired
    lateinit var agentService: AgentService

    /**
     * @param execution
     * @return OK if everything went fine.
     */
    @PostMapping("/initializeAgents")
    fun initialize(@RequestBody execution: Execution): Status {
        // TODO initialization
        return Mono.just(ResponseEntity.ok(HttpStatus.OK))
    }
}
