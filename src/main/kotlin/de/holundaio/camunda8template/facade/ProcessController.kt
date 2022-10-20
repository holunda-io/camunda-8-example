package org.example.camunda.process.solution.facade

import io.camunda.zeebe.client.ZeebeClient
import org.example.camunda.process.solution.ProcessConstants
import org.example.camunda.process.solution.ProcessVariables
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/process")
class ProcessController(private val zeebe: ZeebeClient) {
    @PostMapping("/start")
    fun startProcessInstance(@RequestBody variables: ProcessVariables) {
        LOG.info(
            "Starting process `" + ProcessConstants.BPMN_PROCESS_ID + "` with variables: " + variables
        )
        zeebe
            .newCreateInstanceCommand()
            .bpmnProcessId(ProcessConstants.BPMN_PROCESS_ID)
            .latestVersion()
            .variables(variables)
            .send()
    }

    @PostMapping("/message/{messageName}/{correlationKey}")
    fun publishMessage(
        @PathVariable messageName: String?,
        @PathVariable correlationKey: String?,
        @RequestBody variables: ProcessVariables?
    ) {
        LOG.info(
            "Publishing message `{}` with correlation key `{}` and variables: {}",
            messageName,
            correlationKey,
            variables
        )
        zeebe
            .newPublishMessageCommand()
            .messageName(messageName)
            .correlationKey(correlationKey)
            .variables(variables)
            .send()
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(ProcessController::class.java)
    }
}
