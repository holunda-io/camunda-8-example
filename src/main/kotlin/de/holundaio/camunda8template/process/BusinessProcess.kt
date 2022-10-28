package de.holundaio.camunda8template.process

import io.camunda.zeebe.client.ZeebeClient
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture

@Component
class BusinessProcess(private val zeebe: ZeebeClient) {

    companion object {
        const val BPMN_PROCESS_ID = "business-process"
    }

    object FlowNodes {
        const val TASK_DO_SOME_BUSINESS_STUFF = "Task_DoSomeBusinessStuff"
    }

    object Messages {
        const val MESSAGE_A = "Message_A"
        const val MESSAGE_B = "Message_B"
    }

    fun start(businessKey: String): CompletableFuture<BusinessProcessInstance> {
        val zeebeFuture = zeebe
            .newCreateInstanceCommand()
            .bpmnProcessId(BPMN_PROCESS_ID)
            .latestVersion()
            .variables(ProcessVariables(businessKey))
            .send()
        return CompletableFuture.supplyAsync { BusinessProcessInstance(zeebeFuture.join().processInstanceKey) }
    }

    fun publishSomeBusinessMessage(businessKey: String, businessData: String) {
        publishMessage(Messages.MESSAGE_A, businessKey, ProcessVariables(businessKey, businessData = businessData))
    }

    fun publishAnotherBusinessMessage(businessKey: String, otherData: String) {
        publishMessage(Messages.MESSAGE_B, businessKey, ProcessVariables(businessKey, moreData = otherData))
    }

    private fun publishMessage(messageName: String, correlationKey: String, variables: ProcessVariables) {
        zeebe
            .newPublishMessageCommand()
            .messageName(messageName)
            .correlationKey(correlationKey)
            .variables(variables)
            .send()
    }

    data class ProcessVariables(
        val businessKey: String,
        val businessData: String? = null,
        val moreData: String? = null,
        val jobResult: Boolean? = null
    )
}