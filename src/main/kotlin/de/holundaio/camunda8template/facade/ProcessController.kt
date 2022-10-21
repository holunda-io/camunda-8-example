package de.holundaio.camunda8template.facade

import de.holundaio.camunda8template.process.BusinessProcess
import de.holundaio.camunda8template.process.BusinessProcess.Companion.BPMN_PROCESS_ID
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/process")
class ProcessController(private val businessProcess: BusinessProcess) {

    @PostMapping("/start")
    fun startProcessInstance(@RequestBody businessKey: String) {
        LOG.info(
            "Starting process `$BPMN_PROCESS_ID` with business key: `$businessKey`"
        )
        businessProcess.start(businessKey)
    }

    @PostMapping("/message/{messageName}/{correlationKey}")
    fun publishMessage(
        @PathVariable messageName: String?,
        @PathVariable correlationKey: String?,
        @RequestBody variables: BusinessProcess.ProcessVariables?
    ) {
        LOG.info(
            "Publishing message `$messageName` with correlation key `$correlationKey` and variables: $variables"
        )
        // TODO move to BusinessProcess(Instance)
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
