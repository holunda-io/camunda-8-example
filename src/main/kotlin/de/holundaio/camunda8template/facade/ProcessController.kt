package de.holundaio.camunda8template.facade

import de.holundaio.camunda8template.process.BusinessProcess
import de.holundaio.camunda8template.process.BusinessProcess.Companion.BPMN_PROCESS_ID
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/business-process")
class ProcessController(private val businessProcess: BusinessProcess) {

    @PostMapping("/start")
    fun startBusinessProcessInstance(@RequestBody businessKey: String) {
        LOG.info(
            "Starting process `$BPMN_PROCESS_ID` with business key: `$businessKey`"
        )
        businessProcess.start(businessKey)
    }

    @PostMapping("/message/some-business-message/{correlationKey}")
    fun publishSomeBusinessMessage(
        @PathVariable correlationKey: String,
        @RequestBody businessData: String
    ) {
        LOG.info(
            "Publishing some business message with correlation key `$correlationKey` and data: $businessData"
        )
        businessProcess.publishSomeBusinessMessage(correlationKey, businessData)
    }

    @PostMapping("/message/another-business-message/{correlationKey}")
    fun publishAnotherBusinessMessage(
        @PathVariable correlationKey: String,
        @RequestBody businessData: String
    ) {
        LOG.info(
            "Publishing another business message with correlation key `$correlationKey` and data: $businessData"
        )
        businessProcess.publishAnotherBusinessMessage(correlationKey, businessData)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(ProcessController::class.java)
    }
}
