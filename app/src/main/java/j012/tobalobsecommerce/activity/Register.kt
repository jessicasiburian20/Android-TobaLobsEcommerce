package j012.tobalobsecommerce.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import j012.tobalobsecommerce.MainActivity
import j012.tobalobsecommerce.R
import j012.tobalobsecommerce.app.ApiConfig
import j012.tobalobsecommerce.helper.SharePref
import j012.tobalobsecommerce.model.ResponseModel
import kotlinx.android.synthetic.main.activity_register.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Register : AppCompatActivity() {

      lateinit var s: SharePref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        s = SharePref(this)

        btn_register.setOnClickListener {
            register()
        }

        loginText.setOnClickListener {
            dataDummy()
        }
    }

    fun dataDummy() {
        edt_nama.setText("test")
        edt_email.setText("test@gmail.com")
        edt_phone.setText("0812345678")
        edt_password.setText("abcd1234")
    }

    fun register() {
        if (edt_nama.text.isEmpty()) {
            edt_nama.error = "Nama tidak boleh kosong"
            edt_nama.requestFocus()
            return
        } else if (edt_email.text.isEmpty()) {
            edt_email.error = "Email tidak boleh kosong"
            edt_email.requestFocus()
            return
        } else if (edt_phone.text.isEmpty()) {
            edt_phone.error = "Nomor Telepon tidak boleh kosong"
            edt_phone.requestFocus()
            return
        } else if (edt_password.text.isEmpty()) {
            edt_password.error = "Password tidak boleh kosong"
            edt_password.requestFocus()
            return
        }

        pb.visibility = View.VISIBLE
        ApiConfig.instanceRetrofit.register(edt_nama.text.toString(), edt_email.text.toString(), edt_phone.text.toString(), edt_password.text.toString()).enqueue(object : Callback<ResponseModel> {

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                pb.visibility = View.GONE
                Toast.makeText(this@Register, "Error:" + t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                pb.visibility = View.GONE
                val respon = response.body()!!
                if (respon.success == 1) {
                    s.setStatusLogin(true)
                    val intent = Intent(this@Register, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                    Toast.makeText(this@Register, "Selamat datang " + respon.user.name, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@Register, "Error:" + respon.message, Toast.LENGTH_SHORT).show()
                }

            }

        })
    }
}
