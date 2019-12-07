package io.felipe.appetit.ui.sign_in

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import io.felipe.appetit.R
import io.felipe.appetit.database.Database
import io.felipe.appetit.database.PrefsDb
import io.felipe.appetit.database.User
import io.felipe.appetit.ui.MainActivity
import kotlinx.android.synthetic.main.activity_sign_in.*


class SignInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        // Hack para alternar entre as Dicas (hint)
        inputLayoutEmail.hint = getString(R.string.sign_in_email_complete_hint)
        signInEmail.setOnFocusChangeListener { _, hasFocus ->
            checkHint(
                signInEmail,
                inputLayoutEmail,
                getString(R.string.sign_in_email_field_hint),
                getString(R.string.sign_in_email_complete_hint),
                hasFocus
            )
        }
        // Hack para alternar entre as Dicas (hint)
        inputLayoutPassword.hint = getString(R.string.sign_in_password_complete_hint)
        signInPassword.setOnFocusChangeListener { _, hasFocus ->
            checkHint(
                signInPassword,
                inputLayoutPassword,
                getString(R.string.sign_in_password_field_hint),
                getString(R.string.sign_in_password_complete_hint),
                hasFocus
            )
        }
        // Hora de fazer Login!
        signInBtn.setOnClickListener {
            val email = signInEmail.text?.toString() ?: ""
            val password = signInPassword.text?.toString() ?: ""
            if (validate(email, password)) {
                doSignIn(email, password)
            }
        }
    }

    private fun doSignIn(email: String, password: String) {
        val db = PrefsDb.init(this@SignInActivity).getDatabase()
        val user = getUser(db, email, password)
        if (user == null) {
            snackIt("Email ou Senha incorretos")
        } else {
            startActivity(Intent(this@SignInActivity, MainActivity::class.java))
        }
    }

    private fun getUser(
        db: Database,
        email: String,
        password: String
    ): User? = db.users?.filter { it.password == password }?.filter { it.email == email }?.first()

    private fun validate(email: String, password: String): Boolean {
        if (email.isEmpty() || password.isEmpty()) {
            snackIt(getString(R.string.sign_in_empty_fields_error))
            return false
        }
        if (!email.contains("@") || !email.contains(".")) {
            snackIt("Formato de email inválido")
            return false
        }
        return true
    }

    private fun snackIt(msg: String) {
        Snackbar.make(signInRootParent, msg, Snackbar.LENGTH_LONG).show()
    }

    private fun checkHint(
        v1: TextInputEditText,
        v2: TextInputLayout,
        msg1: String,
        msg2: String,
        hasFocus: Boolean
    ) =
        if (hasFocus) {
            v2.hint = msg1
        } else {
            if (v1.text?.toString()?.isNotEmpty() == true) {
                v2.hint = msg1
            } else {
                v2.hint = msg2
            }
        }
}
