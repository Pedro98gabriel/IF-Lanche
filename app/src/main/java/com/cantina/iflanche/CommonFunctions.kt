package com.cantina.iflanche

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

object CommonFunctions {
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

    fun clearFocusFromAllFields(fields: List<View>, context: Context) {
        fields.forEach { it.clearFocus() }
        hideKeyboard(fields.first(), context)
    }

    fun hideKeyboard(view: View, context: Context) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}