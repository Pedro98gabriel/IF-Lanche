package com.cantina.iflanche

import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.AutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

object HelperFunctions {
    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun addTextWatcherAndFocusListener(view: View, textInputLayout: TextInputLayout) {
        when (view) {
            is TextInputEditText -> {
                view.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        textInputLayout.error = null
                    }

                    override fun afterTextChanged(s: Editable?) {}
                })

                view.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        textInputLayout.error = null
                    }
                }
            }

            is AutoCompleteTextView -> {
                view.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        textInputLayout.error = null
                    }

                    override fun afterTextChanged(s: Editable?) {}
                })

                view.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        textInputLayout.error = null
                    }
                }
            }
        }
    }

    fun clearFocusFromAllFields(fields: List<View>) {
        fields.forEach { it.clearFocus() }
    }
}