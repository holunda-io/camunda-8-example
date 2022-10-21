package org.example.camunda.process.solution

import de.holundaio.camunda8template.Camunda8TemplateApplication
import de.holundaio.camunda8template.ProcessVariables
import io.camunda.zeebe.process.test.api.ZeebeTestEngine
import io.camunda.zeebe.process.test.assertions.BpmnAssert
import io.camunda.zeebe.process.test.inspections.InspectionUtility
import io.camunda.zeebe.spring.test.ZeebeSpringTest
import io.camunda.zeebe.spring.test.ZeebeTestThreadSupport
import de.holundaio.camunda8template.facade.ProcessController
import de.holundaio.camunda8template.service.SomeBusinessService
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
@SpringBootTest(classes = [Camunda8TemplateApplication::class]) // will deploy BPMN & DMN models
@ZeebeSpringTest
class ProcessUnitTest {
    @Autowired
    private val processController: ProcessController? = null

    @Autowired
    private val engine: ZeebeTestEngine? = null

    @MockBean
    private val someBusinessService: SomeBusinessService? = null
    @Test
    @Throws(Exception::class)
    fun testHappyPath() {
        // define mock behavior
        Mockito.`when`(someBusinessService!!.myOperation(ArgumentMatchers.anyString())).thenReturn(true)

        // prepare data
        val variables = ProcessVariables("23")

        // start a process instance
        processController!!.startProcessInstance(variables)

        // wait for process to be started
        engine!!.waitForIdleState(Duration.ofSeconds(1))
        val processInstance = InspectionUtility.findProcessInstances().findLastProcessInstance().get()
        BpmnAssert.assertThat(processInstance).isStarted

        // check that service task has been completed
        ZeebeTestThreadSupport.waitForProcessInstanceHasPassedElement(processInstance, "Task_DoSomeBusinessStuff")
        Mockito.verify(someBusinessService).myOperation("23")

        // check that process is ended with the right result
        ZeebeTestThreadSupport.waitForProcessInstanceCompleted(processInstance)
        BpmnAssert.assertThat(processInstance)
            .isCompleted
            .hasPassedElement("Task_DoSomeBusinessStuff")
            .hasVariableWithValue("jobResult", true)

        // ensure no other side effects
        Mockito.verifyNoMoreInteractions(someBusinessService)
    }
}
