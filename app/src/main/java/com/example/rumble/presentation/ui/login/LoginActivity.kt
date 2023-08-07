package com.example.rumble.presentation.ui.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.rumble.R
import com.example.rumble.databinding.ActivityLoginBinding
import com.example.rumble.infrastructure.api.LoginCredentials
import com.example.rumble.presentation.ui.main.MainActivity
import com.example.rumble.presentation.ui.login.LoginViewModel.Event.Login
import com.example.rumble.presentation.ui.utils.storeValue
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityLoginBinding

    private val viewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.usernameEditText.text = viewBinding.usernameEditText.editableText
        viewBinding.passwordEditText.text = viewBinding.passwordEditText.editableText

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.loginViewState.collect(::renderStates)
            }
        }

        viewBinding.send.setOnClickListener {
            viewBinding.indicator.visibility = View.VISIBLE
            viewModel.onEvent(Login(LoginCredentials("greta.frucht@rn.de","SuperSecurePW123!")))
            //TODO dont forget to remove this line
            //viewModel.onEvent(Login(LoginCredentials(viewBinding.usernameEditText.text.toString(),viewBinding.passwordEditText.text.toString())))
        }
    }

    private fun renderStates(state: LoginViewModel.LoginState) {
        when (state) {
            is LoginViewModel.LoginState.FailedLogin -> {
                viewBinding.indicator.visibility = View.INVISIBLE
                Toast.makeText(this, state.error, Toast.LENGTH_SHORT).show()
            }

            is LoginViewModel.LoginState.LunchMain -> {
                viewBinding.indicator.visibility = View.INVISIBLE

                storeValue(KEY_TOKEN, state.profile.accessToken)
                storeValue(KEY_SUBSCRIPTIONS, state.profile.activeSubscriptions)

                val intent = MainActivity.newIntent(this, state.profile)
                startActivity(intent)
            }

            is LoginViewModel.LoginState.NoResults -> {
                viewBinding.indicator.visibility = View.INVISIBLE
                Toast.makeText(this, R.string.login_error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {

        const val KEY_TOKEN = "Token"
        const val KEY_SUBSCRIPTIONS = "Subscription"
    }
}