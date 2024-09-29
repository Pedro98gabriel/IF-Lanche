package com.cantina.iflanche

import android.view.View
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.withId
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
    fun shouldShowErrorOnMandatoryField_whenNameFieldIsEmpty() {
        onView(withId(R.id.btn_register)).perform(click())
        checkTextInputLayoutError(R.id.tf_name_register, "Por favor, preencha o nome")
    }

    private fun checkTextInputLayoutError(viewId: Int, expectedErrorText: String) {
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