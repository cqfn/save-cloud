/**
 * Classes for configuration properties of orchestrator
 */

package org.cqfn.save.orchestrator.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

/**
 * Class for properties
 *
 * @property backendUrl url of save-backend
 * @property testResources configuration for test resources
 * @property docker configuration for docker API
 * @property agentsCount a number of agents to start for every [Execution]
 * @property agentLogs path to folder to store agent logs
 */
@ConstructorBinding
@ConfigurationProperties(prefix = "orchestrator")
data class ConfigProperties(
    val backendUrl: String,
    val testResources: TestResources,
    val docker: DockerSettings,
    val agentsCount: Int,
    val agentLogs: String,
) {
    val agentLogsFolder = System.getenv("user.home") + agentLogs
}

/**
 * @property basePath path to the root directory, where all test resources are stored
 */
data class TestResources(
    val basePath: String,
)

/**
 * @property host hostname of docker daemon
 */
data class DockerSettings(
    val host: String,
)
