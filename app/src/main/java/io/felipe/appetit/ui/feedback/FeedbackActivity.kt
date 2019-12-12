package io.felipe.appetit.ui.feedback

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.felipe.appetit.R
import io.felipe.appetit.ui.new_sale.NewSaleActivity
import kotlinx.android.synthetic.main.activity_feedback.*

class FeedbackActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)

        feedbackAnotherSaleButton.setOnClickListener {
            startActivity(Intent(this@FeedbackActivity, NewSaleActivity::class.java))
            finish()
        }
        feedbackBackButton.setOnClickListener {
            finish()
        }
    }
}
