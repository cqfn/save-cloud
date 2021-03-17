package org.cqfn.save.entities

import org.cqfn.save.execution.ExecutionStatus
import java.time.LocalDateTime
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Entity
class Execution(
    var projectId: Long,
    var startTime: LocalDateTime,
    var endTime: LocalDateTime,
    @Enumerated(EnumType.STRING)
    var status: ExecutionStatus,
    var testSuiteIds: String,
    @Id @GeneratedValue var id: Long
)
