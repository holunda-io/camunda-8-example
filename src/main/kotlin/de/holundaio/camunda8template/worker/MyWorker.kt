package org.example.camunda.process.solution.worker

import io.camunda.zeebe.spring.client.annotation.JobWorker
import io.camunda.zeebe.spring.client.annotation.VariablesAsType
import org.example.camunda.process.solution.ProcessVariables
import org.example.camunda.process.solution.service.MyService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class MyWorker(private val myService: MyService) {
    @JobWorker
    fun invokeMyService(@VariablesAsType variables: ProcessVariables): ProcessVariables? {
        LOG.info("Invoking myService with variables: $variables")
        val result = myService.myOperation(variables.businessKey)
        return ProcessVariables()
            .setResult(result) // new object to avoid sending unchanged variables
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(MyWorker::class.java)
    }
}
