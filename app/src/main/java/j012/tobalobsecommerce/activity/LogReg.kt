package j012.tobalobsecommerce.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import j012.tobalobsecommerce.R
import j012.tobalobsecommerce.helper.SharePref
import kotlinx.android.synthetic.main.activity_log_reg.*


class LogReg : AppCompatActivity() {

    lateinit var s: SharePref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_reg)

        s = SharePref(this)

        mainButton()
    }

    private fun mainButton() {
        btn_prosesLogin.setOnClickListener {
            s.setStatusLogin(true)
            startActivity(Intent(this, Login::class.java))
        }

        btn_register.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }
    }
}