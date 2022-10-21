package de.holundaio.camunda8template.process

import io.camunda.zeebe.client.ZeebeClient
import org.springframework.stereotype.Component
import java.util.concurrent.Future
import java.util.concurrent.FutureTask

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
    }

    fun start(businessKey: String): Future<BusinessProcessInstance> {
        val zeebeFuture = zeebe
            .newCreateInstanceCommand()
            .bpmnProcessId(BPMN_PROCESS_ID)
            .latestVersion()
            .variables(ProcessVariables(businessKey))
            .send()
        return FutureTask { BusinessProcessInstance(zeebeFuture.join().processInstanceKey) }
    }

    data class ProcessVariables(
        val businessKey: String,
        val jobResult: Boolean? = null
    )
}