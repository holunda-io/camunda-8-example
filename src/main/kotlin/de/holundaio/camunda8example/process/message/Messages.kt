package de.holundaio.camunda8example.process.message

abstract class Message(
    val name: String,
    val correlationKey: String,
    val variables: Any
)

class MessageA(
    businessKey: String,
    messageAVariables: Variables
) : Message("Message_A", businessKey, messageAVariables) {
    class Variables(
        val businessData: String
    )
}

class MessageB(
    businessKey: String,
    messageBVariables: Variables
) : Message("Message_B", businessKey, messageBVariables) {
    class Variables(
        val moreData: String
    )
}