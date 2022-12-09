package de.holundaio.camunda8example.facade

import de.holundaio.camunda8example.process.BusinessProcess
import de.holundaio.camunda8example.process.BusinessProcess.Companion.BPMN_PROCESS_ID
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/business-process")
class BusinessProcessController(private val businessProcess: BusinessProcess) {

    @PostMapping("/start")
    fun startBusinessProcessInstance(@RequestBody businessKey: String) {
        LOG.info(
            "Starting process `$BPMN_PROCESS_ID` with business key: `$businessKey`"
        )
        businessProcess.start(businessKey)
    }

    @PostMapping("/message/some-business-message/{someBusinessKey}")
    fun someBusinessMessage(
        @PathVariable someBusinessKey: String,
        @RequestBody businessData: String
    ) {
        LOG.info(
            "Publishing some business message with correlation key `$someBusinessKey` and data: $businessData"
        )
        businessProcess.correlateSomeBusinessMessage(someBusinessKey, businessData)
    }

    @PostMapping("/message/another-business-message/{anotherBusinessKey}")
    fun correlateAnotherBusinessMessage(
        @PathVariable anotherBusinessKey: String,
        @RequestBody businessData: String
    ) {
        LOG.info(
            "Publishing another business message with correlation key `$anotherBusinessKey` and data: $businessData"
        )
        businessProcess.correlateAnotherBusinessMessage(anotherBusinessKey, businessData)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(BusinessProcessController::class.java)
    }
}
