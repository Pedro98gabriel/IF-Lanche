package com.cantina.iflanche

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.google.android.material.textfield.TextInputLayout
import org.hamcrest.Description
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegisterActivityTest {

    @get:Rule
    val activityRule = ActivityTestRule(RegisterActivity::class.java)

    @Test
    fun shouldShowAndHideErrorOnNameField() {
        // Click the register button to trigger the error
        onView(withId(R.id.btn_register)).perform(click())
        checkTextInputLayoutError(R.id.tf_name_register, "Por favor, preencha o nome")

        // Click on the name field to remove the error
        onView(withId(R.id.tf_name_register)).perform(click())
        checkTextInputLayoutError(R.id.tf_name_register, null)
    }

    @Test
    fun shouldShowAndHideErrorOnEmailField() {
        // Click the register button to trigger the error
        onView(withId(R.id.btn_register)).perform(click())
        checkTextInputLayoutError(R.id.tf_email_register, "Email inválido")

        // Click on the name field to remove the error
        onView(withId(R.id.tf_email_register)).perform(click())
        checkTextInputLayoutError(R.id.tf_email_register, null)
    }

    @Test
    fun shouldShowAndHideErrorOnUserTypeField() {
        // Click the register button to trigger the error
        onView(withId(R.id.btn_register)).perform(click())
        checkTextInputLayoutError(
            R.id.tf_dropdown_user_type_register,
            "Selecione um tipo de usuário"
        )

        // Click on the name field to remove the error
        onView(withId(R.id.tf_dropdown_user_type_register)).perform(click())
        checkTextInputLayoutError(R.id.tf_dropdown_user_type_register, null)
    }

    @Test
    fun shouldShowAndHideErrorOnPasswordField() {
        // Click the register button to trigger the error
        onView(withId(R.id.btn_register)).perform(click())
        checkTextInputLayoutError(
            R.id.tf_password_register, "A senha deve ter pelo menos 6 caracteres"
        )
        // Click on the name field to remove the error
        onView(withId(R.id.tf_password_register)).perform(click())
        checkTextInputLayoutError(R.id.tf_password_register, null)
    }

    @Test
    fun shouldShowAndHideErrorOnConfirmPasswordField() {
        // Click the register button to trigger the error
        onView(withId(R.id.btn_register)).perform(click())
        checkTextInputLayoutError(
            R.id.tf_password_confirm_register, "As senhas não coincidem"
        )
        // Click on the name field to remove the error
        onView(withId(R.id.tf_password_confirm_register)).perform(click())
        checkTextInputLayoutError(R.id.tf_password_confirm_register, null)
    }

    private fun checkTextInputLayoutError(viewId: Int, expectedErrorText: String?) {
        onView(withId(viewId)).check(matches(object :
            BoundedMatcher<View, TextInputLayout>(TextInputLayout::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("with error: $expectedErrorText")
            }

            override fun matchesSafely(textInputLayout: TextInputLayout): Boolean {
                return textInputLayout.error == expectedErrorText
            }
        }))
    }
}