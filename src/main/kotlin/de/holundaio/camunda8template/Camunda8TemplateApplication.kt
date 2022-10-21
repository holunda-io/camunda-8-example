package de.holundaio.camunda8template

import io.camunda.zeebe.spring.client.EnableZeebeClient
import io.camunda.zeebe.spring.client.annotation.Deployment
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableZeebeClient
@Deployment(resources = ["classpath*:/models/*.*"])
class Camunda8TemplateApplication

fun main(args: Array<String>) {
	runApplication<Camunda8TemplateApplication>(*args)
}
