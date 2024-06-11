package com.sevenexp.craftit.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText


class ValidationTextWatcher(
    private val fieldType: FIELDTYPE,
    private val errorMessage: String,
    private val editText: EditText
) : TextWatcher {

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun afterTextChanged(s: Editable?) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (!s.isNullOrEmpty() && !isInputValid(s.toString(), fieldType)) {
            editText.error = errorMessage
        } else {
            editText.error = null
        }
    }

    private fun isInputValid(input: String, fieldType: FIELDTYPE): Boolean {
        val regex = when (fieldType) {
            FIELDTYPE.EMAIL -> android.util.Patterns.EMAIL_ADDRESS.pattern()
            FIELDTYPE.PASSWORD -> "^.{8,}$"
        }
        return input.matches(regex.toRegex())
    }
}

enum class FIELDTYPE {
    EMAIL, PASSWORD
}
