package de.holundaio.camunda8example

import io.camunda.zeebe.spring.client.EnableZeebeClient
import io.camunda.zeebe.spring.client.annotation.Deployment
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableZeebeClient
@Deployment(resources = ["classpath*:/models/*.*"])
class Camunda8ExampleApplication

fun main(args: Array<String>) {
	runApplication<Camunda8ExampleApplication>(*args)
}
