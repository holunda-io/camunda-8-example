package de.holundaio.camunda8example.service

import org.springframework.stereotype.Service

@Service
class SomeBusinessService {
    fun myOperation(businessKey: String?): Boolean {
        return true
    }
}
