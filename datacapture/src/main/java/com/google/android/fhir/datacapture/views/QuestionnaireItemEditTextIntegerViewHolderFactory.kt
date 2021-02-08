/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.fhir.datacapture.views

import android.text.InputType
import androidx.core.text.isDigitsOnly
import com.google.fhir.r4.core.Extension
import com.google.fhir.r4.core.Integer
import com.google.fhir.r4.core.QuestionnaireResponse

object QuestionnaireItemEditTextIntegerViewHolderFactory :
    QuestionnaireItemEditTextViewHolderFactory() {
    override fun getQuestionnaireItemViewHolderDelegate() =
        object : QuestionnaireItemEditTextViewHolderDelegate(
            InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED,
            true
        ) {
            override fun getValue(text: String): QuestionnaireResponse.Item.Answer.Builder? {
                return text.toIntOrNull()?.let {
                    QuestionnaireResponse.Item.Answer.newBuilder()
                        .apply {
                            value = QuestionnaireResponse.Item.Answer.ValueX.newBuilder()
                                .setInteger(Integer.newBuilder().setValue(it).build())
                                .build()
                        }
                }
            }

            override fun getText(answer: QuestionnaireResponse.Item.Answer.Builder?): String {
                return answer?.value?.integer?.value?.toString() ?: ""
            }

            override fun validateMaxValue(extension: Extension, inputValue: String): Boolean {
                if (extension.value.hasInteger() &&
                        inputValue.isNotEmpty() &&
                        inputValue.isNotBlank()) {

                    if (!inputValue.isDigitsOnly()) {
                        return true
                    }

                    if (inputValue.toInt() > extension.value.integer.value) {
                        return true
                    }
                }
                return false
            }

            override fun validateMinValue(extension: Extension, inputValue: String): Boolean {
                if (extension.value.hasInteger() &&
                        inputValue.isNotEmpty() &&
                        inputValue.isNotBlank()) {

                    if (!inputValue.isDigitsOnly()) {
                        return true
                    }
                    if (inputValue.toInt() < extension.value.integer.value) {
                        return true
                    }
                }
                return false
            }
        }
}
