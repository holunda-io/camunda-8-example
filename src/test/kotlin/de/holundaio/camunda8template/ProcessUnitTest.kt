package org.example.camunda.process.solution

import io.camunda.zeebe.process.test.api.ZeebeTestEngine
import io.camunda.zeebe.process.test.assertions.BpmnAssert
import io.camunda.zeebe.process.test.inspections.InspectionUtility
import io.camunda.zeebe.spring.test.ZeebeSpringTest
import io.camunda.zeebe.spring.test.ZeebeTestThreadSupport
import org.example.camunda.process.solution.facade.ProcessController
import org.example.camunda.process.solution.service.MyService
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.time.Duration

/**
 * @see      https://docs.camunda.io/docs/components/best-practices/development/testing-process-definitions/.writing-process-tests-in-java
 */
@SpringBootTest(classes = [ProcessApplication::class]) // will deploy BPMN & DMN models
@ZeebeSpringTest
class ProcessUnitTest {
    @Autowired
    private val processController: ProcessController? = null

    @Autowired
    private val engine: ZeebeTestEngine? = null

    @MockBean
    private val myService: MyService? = null
    @Test
    @Throws(Exception::class)
    fun testHappyPath() {
        // define mock behavior
        Mockito.`when`(myService!!.myOperation(ArgumentMatchers.anyString())).thenReturn(true)

        // prepare data
        val variables = ProcessVariables().setBusinessKey("23")

        // start a process instance
        processController!!.startProcessInstance(variables)

        // wait for process to be started
        engine!!.waitForIdleState(Duration.ofSeconds(1))
        val processInstance = InspectionUtility.findProcessInstances().findLastProcessInstance().get()
        BpmnAssert.assertThat(processInstance).isStarted

        // check that service task has been completed
        ZeebeTestThreadSupport.waitForProcessInstanceHasPassedElement(processInstance, "Task_InvokeService")
        Mockito.verify(myService).myOperation("23")

        // check that process is ended with the right result
        ZeebeTestThreadSupport.waitForProcessInstanceCompleted(processInstance)
        BpmnAssert.assertThat(processInstance)
            .isCompleted
            .hasPassedElement("Task_InvokeService")
            .hasVariableWithValue("result", true)

        // ensure no other side effects
        Mockito.verifyNoMoreInteractions(myService)
    }
}
