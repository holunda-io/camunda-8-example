package org.example.camunda.process.solution

import com.fasterxml.jackson.annotation.JsonInclude
import org.apache.commons.lang3.builder.MultilineRecursiveToStringStyle
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle

@JsonInclude(JsonInclude.Include.NON_NULL)
class ProcessVariables {
    var businessKey: String? = null
        private set
    var result: Boolean? = null
        private set

    fun setBusinessKey(businessKey: String?): ProcessVariables {
        this.businessKey = businessKey
        return this
    }

    fun setResult(result: Boolean?): ProcessVariables {
        this.result = result
        return this
    }

    override fun toString(): String {
        return ToStringBuilder.reflectionToString(
            this,
            object : MultilineRecursiveToStringStyle() {
                fun withShortPrefixes(): ToStringStyle {
                    this.isUseShortClassName = true
                    this.isUseIdentityHashCode = false
                    return this
                }
            }.withShortPrefixes()
        )
    }
}
