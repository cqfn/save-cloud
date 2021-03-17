package org.cqfn.save.backend.controllers

import org.cqfn.save.backend.service.TestExecutionService
import org.cqfn.save.entities.TestExecution
import org.springframework.dao.DataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

/**
 * Controller to work with test execution
 *
 * @param testExecutionService service for test execution
 */
@RestController
class TestExecutionController(private val testExecutionService: TestExecutionService) {
    /**
     * @param testExecutions list of test executions
     * @return response
     */
    @PostMapping(value = ["/saveTestResult"])
    fun saveTestResult(@RequestBody testExecutions: List<TestExecution>): ResponseEntity<String> {
        return try {
            if (testExecutionService.saveTestResult(testExecutions).isEmpty()) {
                ResponseEntity.status(HttpStatus.OK).body("Saved")
            } else {
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Some ids don't exist")
            }
        } catch (exception: DataAccessException) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error to save")
        }
    }
}