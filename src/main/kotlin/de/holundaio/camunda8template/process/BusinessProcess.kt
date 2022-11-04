package de.holundaio.camunda8template.process

import de.holundaio.camunda8template.process.message.Message
import de.holundaio.camunda8template.process.message.MessageA
import de.holundaio.camunda8template.process.message.MessageB
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
        const val MESSAGE_CATCH_MESSAGE_A = "MessageCatch_A"
        const val MESSAGE_CATCH_MESSAGE_B = "MessageCatch_B"
        const val END_MESSAGE_A_PROCESSED = "End_Message_A_processed"
        const val END_MESSAGE_B_PROCESSED = "End_Message_B_processed"
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
        publishMessage(MessageA(businessKey, MessageA.Variables(businessData)))
    }

    fun publishAnotherBusinessMessage(businessKey: String, moreData: String) {
        publishMessage(MessageB(businessKey, MessageB.Variables(moreData)))
    }

    private fun publishMessage(message: Message) {
        zeebe
            .newPublishMessageCommand()
            .messageName(message.name)
            .correlationKey(message.correlationKey)
            .variables(message.variables)
            .send()
    }

    data class ProcessVariables(
        val businessKey: String,
        val businessData: String? = null,
        val moreData: String? = null,
        val jobResult: Boolean? = null
    )
}