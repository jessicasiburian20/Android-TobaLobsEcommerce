package j012.tobalobsecommerce.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import j012.tobalobsecommerce.R
import j012.tobalobsecommerce.adapter.AdapterBank
import j012.tobalobsecommerce.app.ApiConfig
import j012.tobalobsecommerce.helper.Helper
import j012.tobalobsecommerce.model.Bank
import j012.tobalobsecommerce.model.CheckOut
import j012.tobalobsecommerce.model.ResponseModel
import j012.tobalobsecommerce.model.TransaksiModel
import kotlinx.android.synthetic.main.activity_pembayaran.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Pembayaran : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pembayaran)
        Helper().settoolbar(this, toolbar, "Pembayaran")

        dispBank()
    }

    fun dispBank(){
        val arrBank = ArrayList<Bank>()
        arrBank.add(Bank("BCA", "13425678987654", "Jessica", R.drawable.logo_bca))
        arrBank.add(Bank("BRI", "1234567876543498", "Jessica", R.drawable.logo_bri))
        arrBank.add(Bank("Mandiri", "09876675344345", "Jessica", R.drawable.logo_mandiri))


        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        rv_data.layoutManager = layoutManager
        rv_data.adapter = AdapterBank(arrBank, object :AdapterBank.Listeners{
            override fun onClicked(data: Bank, index: Int) {
                bayar(data)
            }

        })
    }

    fun bayar(bank:Bank){
        val json = intent.getStringExtra("extra")!!.toString()
        val checkOut = Gson().fromJson(json, CheckOut::class.java)
        checkOut.bank = bank.nama
        ApiConfig.instanceRetrofit.checkout(checkOut).enqueue(object : Callback<ResponseModel> {
            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {

//                Toast.makeText(this@Pengiriman, "Error:" + t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                val respon = response.body()!!
                if (respon.success == 1) {
                    val jsBank = Gson().toJson(bank, Bank::class.java)
                    val jsTransaksiModel = Gson().toJson(respon.transaksi, TransaksiModel::class.java)
                    val jsCheckOut = Gson().toJson(checkOut, CheckOut::class.java)

                    val intent = Intent(this@Pembayaran, Transfer::class.java)
                    intent.putExtra("bank", jsBank)
                    intent.putExtra("transaksi", jsTransaksiModel)
                    intent.putExtra("checkOut", jsCheckOut)

                    startActivity(intent)

                } else {
                    Toast.makeText(this@Pembayaran, "Error:" + respon.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}