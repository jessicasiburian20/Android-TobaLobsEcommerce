package j012.tobalobsecommerce.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import j012.tobalobsecommerce.R
import j012.tobalobsecommerce.adapter.AdapterRiwayat
import j012.tobalobsecommerce.app.ApiConfig
import j012.tobalobsecommerce.helper.Helper
import j012.tobalobsecommerce.helper.SharePref
import j012.tobalobsecommerce.model.DetailTrans
import j012.tobalobsecommerce.model.Produk
import j012.tobalobsecommerce.model.ResponseModel
import j012.tobalobsecommerce.model.TransaksiModel
import kotlinx.android.synthetic.main.activity_riwayat.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class Riwayat : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_riwayat)
        Helper().settoolbar(this, toolbar, "Riwayat Transaksi")

    }

    fun getRiwayat(){

        val id = SharePref(this).getUser()!!.id

            ApiConfig.instanceRetrofit.getRiwayat(id).enqueue(object : Callback<ResponseModel> {
                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {

                }

                override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                    val res = response.body()!!
                    if (res.success == 1) {
                        dispRiwayat(res.transaksis)
                    }
                }
            })
    }

    fun dispRiwayat(transaksis: ArrayList<TransaksiModel>){
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        rv_riwayat.adapter = AdapterRiwayat(transaksis, object : AdapterRiwayat.Listeners {
            override fun onClicked(data: TransaksiModel) {
                val json = Gson().toJson(data, TransaksiModel::class.java)
                val intent = Intent(this@Riwayat, DetailTransk::class.java)
                intent.putExtra("transaksi", json)
                startActivity(intent)
            }
        })
        rv_riwayat.layoutManager = layoutManager
    }

    override fun onResume(){
        getRiwayat()
        super.onResume()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}