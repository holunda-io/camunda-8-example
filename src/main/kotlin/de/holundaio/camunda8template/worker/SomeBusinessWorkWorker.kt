package de.holundaio.camunda8template.worker

import de.holundaio.camunda8template.service.SomeBusinessService
import io.camunda.zeebe.spring.client.annotation.JobWorker
import io.camunda.zeebe.spring.client.annotation.VariablesAsType
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class SomeBusinessStuffWorker(private val someBusinessService: SomeBusinessService) {
    @JobWorker(type = "someBusinessStuff")
    fun doSomeBusinessStuff(@VariablesAsType variables: InputVariables): OutputVariables {
        LOG.info("Invoking businessService with variables: $variables")
        val result = someBusinessService.myOperation(variables.businessKey)
        return OutputVariables(result)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(SomeBusinessStuffWorker::class.java)
    }

    class InputVariables {
        var businessKey: String? = null
    }

    data class OutputVariables (
        val result: Boolean
    )
}
