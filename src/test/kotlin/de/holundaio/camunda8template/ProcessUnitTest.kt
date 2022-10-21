package org.example.camunda.process.solution

import de.holundaio.camunda8template.Camunda8TemplateApplication
import de.holundaio.camunda8template.facade.ProcessController
import de.holundaio.camunda8template.process.BusinessProcess.FlowNodes.TASK_DO_SOME_BUSINESS_STUFF
import de.holundaio.camunda8template.process.BusinessProcess.ProcessVariables
import de.holundaio.camunda8template.service.SomeBusinessService
import io.camunda.zeebe.process.test.api.ZeebeTestEngine
import io.camunda.zeebe.process.test.assertions.BpmnAssert
import io.camunda.zeebe.process.test.inspections.InspectionUtility
import io.camunda.zeebe.spring.test.ZeebeSpringTest
import io.camunda.zeebe.spring.test.ZeebeTestThreadSupport
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.time.Duration

/**
 * @see https://docs.camunda.io/docs/components/best-practices/development/testing-process-definitions/.writing-process-tests-in-java
 */
@SpringBootTest(classes = [Camunda8TemplateApplication::class]) // will deploy BPMN & DMN models
@ZeebeSpringTest
class ProcessUnitTest {

    @Autowired
    private lateinit var processController: ProcessController

    @Autowired
    private lateinit var engine: ZeebeTestEngine

    @MockBean
    private val someBusinessService: SomeBusinessService? = null
    @Test
    @Throws(Exception::class)
    fun testHappyPath() {
        // define mock behavior
        Mockito.`when`(someBusinessService!!.myOperation(ArgumentMatchers.anyString())).thenReturn(true)

        // start a process instance
        processController.startProcessInstance("23")

        // wait for process to be started
        engine.waitForIdleState(Duration.ofSeconds(1))
        val processInstance = InspectionUtility.findProcessInstances().findLastProcessInstance().get()
        BpmnAssert.assertThat(processInstance).isStarted

        // check that service task has been completed
        ZeebeTestThreadSupport.waitForProcessInstanceHasPassedElement(processInstance, TASK_DO_SOME_BUSINESS_STUFF)
        Mockito.verify(someBusinessService).myOperation("23")

        // check that process is ended with the right result
        ZeebeTestThreadSupport.waitForProcessInstanceCompleted(processInstance)
        BpmnAssert.assertThat(processInstance)
            .isCompleted
            .hasPassedElement(TASK_DO_SOME_BUSINESS_STUFF)
            .hasVariableWithValue(ProcessVariables::jobResult.name, true)

        // ensure no other side effects
        Mockito.verifyNoMoreInteractions(someBusinessService)
    }
}
