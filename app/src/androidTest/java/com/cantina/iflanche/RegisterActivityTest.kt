package com.cantina.iflanche

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.RootMatchers.isPlatformPopup
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.cantina.iflanche.screen.RegisterActivity
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
    fun shouldFailWhenAllFieldsAreEmpty() {
        // Click the register button to trigger validation
        onView(withId(R.id.btn_register)).perform(click())

        // Verify that all error messages are displayed
        checkTextInputLayoutError(R.id.tf_name_register, "Por favor, preencha o nome")
        checkTextInputLayoutError(R.id.tf_email_register, "Email inválido")
        checkTextInputLayoutError(
            R.id.tf_password_register,
            "A senha deve ter pelo menos 6 caracteres"
        )
        checkTextInputLayoutError(R.id.tf_password_confirm_register, "As senhas não coincidem")
        checkTextInputLayoutError(
            R.id.tf_dropdown_user_type_register,
            "Selecione um tipo de usuário"
        )
    }

    @Test
    fun shouldHideErrorOnFieldClick() {
        // Click the register button to trigger the error
        onView(withId(R.id.btn_register)).perform(click())

        onView(withId(R.id.tf_name_register)).perform(click())
        checkTextInputLayoutError(R.id.tf_name_register, null)

        onView(withId(R.id.tf_dropdown_user_type_register)).perform(click())
        checkTextInputLayoutError(R.id.tf_dropdown_user_type_register, null)

        onView(withId(R.id.tf_email_register)).perform(click())
        checkTextInputLayoutError(R.id.tf_email_register, null)

        onView(withId(R.id.tf_password_register)).perform(scrollTo(), click())
        checkTextInputLayoutError(R.id.tf_password_register, null)

        onView(withId(R.id.tf_password_confirm_register)).perform(scrollTo(), click())
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

    @Test
    fun shouldSelectUserType() {
        // Click on the user type dropdown
        onView(withId(R.id.tf_options_user_type_register)).perform(click())

        // Select an item from the dropdown
        onView(withText("Funcionário")).inRoot(isPlatformPopup()).perform(click())

        // Verify that the selected item is correctly set
        onView(withId(R.id.tf_options_user_type_register))
            .check(matches(withText("Funcionário")))
    }

    @Test
    fun shouldFinishActivityOnBackButtonClick() {
        // Click the back button
        onView(withId(R.id.imb_back)).perform(click())

        // Verify that the activity is finished
        assert(activityRule.activity.isFinishing)
    }

    @Test
    fun shouldRegisterSuccessfully() {
        // Fill in the registration form
        onView(withId(R.id.tf_name_register_content)).perform(typeText("John Doe"))
        onView(withId(R.id.tf_email_register_content)).perform(typeText("john.doe@example.com"))
        onView(withId(R.id.tf_options_user_type_register)).perform(click())
        onView(withText("Funcionário")).inRoot(isPlatformPopup()).perform(click())
        onView(withId(R.id.tf_password_register_content)).perform(typeText("password123"))
        onView(withId(R.id.tf_password_confirm_register_content)).perform(
            scrollTo(),
            typeText("password123")
        )
        onView(withId(R.id.btn_register)).perform(scrollTo(), click())

        // Verify that the progress bar is displayed
        onView(withId(R.id.progressBarRegister)).check(matches(isDisplayed()))
    }

    @Test
    fun shouldFailWhenEmailIsInvalid() {
        // Fill in the registration form with an invalid email
        onView(withId(R.id.tf_name_register_content)).perform(typeText("John Doe"))
        onView(withId(R.id.tf_email_register_content)).perform(typeText("invalid-email"))
        onView(withId(R.id.tf_options_user_type_register)).perform(click())
        onView(withText("Aluno")).inRoot(isPlatformPopup()).perform(click())
        onView(withId(R.id.tf_password_register_content)).perform(typeText("password123"))
        onView(withId(R.id.tf_password_confirm_register_content)).perform(
            scrollTo(),
            typeText("password123")
        )

        // Click the register button to trigger validation
        onView(withId(R.id.btn_register)).perform(scrollTo(), click())

        // Verify that the email error message is displayed
        checkTextInputLayoutError(R.id.tf_email_register, "Email inválido")
    }

    @Test
    fun shouldFailWhenPasswordsDoNotMatch() {
        // Fill in the registration form with mismatched passwords
        onView(withId(R.id.tf_name_register_content)).perform(typeText("John Doe"))
        onView(withId(R.id.tf_email_register_content)).perform(typeText("john.doe@example.com"))
        onView(withId(R.id.tf_password_register_content)).perform(typeText("password123"))
        onView(withId(R.id.tf_password_confirm_register_content)).perform(
            scrollTo(),
            typeText("mismatchPassword")
        )
        onView(withId(R.id.tf_options_user_type_register)).perform(click())
        onView(withText("Aluno")).inRoot(isPlatformPopup()).perform(click())

        // Click the register button to trigger validation
        onView(withId(R.id.btn_register)).perform(click())

        // Verify that the confirm password error message is displayed
        checkTextInputLayoutError(
            R.id.tf_password_confirm_register,
            "As senhas não coincidem"
        )
    }

    @Test
    fun shouldFailWhenPasswordIsTooShort() {
        // Fill in the registration form with a short password
        onView(withId(R.id.tf_name_register_content)).perform(typeText("John Doe"))
        onView(withId(R.id.tf_email_register_content)).perform(typeText("john.doe@example.com"))
        onView(withId(R.id.tf_password_register_content)).perform(typeText("123"))
        onView(withId(R.id.tf_password_confirm_register_content)).perform(
            scrollTo(),
            typeText("123")
        )
        onView(withId(R.id.tf_options_user_type_register)).perform(click())
        onView(withText("Funcionário")).inRoot(isPlatformPopup()).perform(click())

        // Click the register button to trigger validation
        onView(withId(R.id.btn_register)).perform(click())

        // Verify that the password error message is displayed
        checkTextInputLayoutError(
            R.id.tf_password_register,
            "A senha deve ter pelo menos 6 caracteres"
        )
    }

}