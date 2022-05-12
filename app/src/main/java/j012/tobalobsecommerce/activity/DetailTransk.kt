package j012.tobalobsecommerce.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog
import j012.tobalobsecommerce.R
import j012.tobalobsecommerce.adapter.AdapterProdukTransaksi
import j012.tobalobsecommerce.app.ApiConfig
import j012.tobalobsecommerce.helper.Helper
import j012.tobalobsecommerce.model.DetailTrans
import j012.tobalobsecommerce.model.ResponseModel
import j012.tobalobsecommerce.model.TransaksiModel
import kotlinx.android.synthetic.main.activity_detail_transk.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class DetailTransk : AppCompatActivity() {

    var transk = TransaksiModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_transk)
        Helper().settoolbar(this, toolbar, "Detail Transaksi")


        val json  = intent.getStringExtra("transaksi")
        transk = Gson().fromJson(json, TransaksiModel::class.java)
        setData(transk)
        dispProduk(transk.details)
        mainButton()
    }

    private fun mainButton(){
        btn_batal.setOnClickListener{
            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Batalkan Transaksi?")
//                    .setContentText("Transaksi akan dibatalkan")
                    .setConfirmText("Ya")
                    .setConfirmClickListener {
                        it.dismissWithAnimation()
                        btlTransk()
                    }
                    .setCancelText("Tutup")
                    .setCancelClickListener {
                        it.dismissWithAnimation()
                    }.show()
        }
    }

    fun btlTransk(){
        val loading = SweetAlertDialog(this@DetailTransk, SweetAlertDialog.PROGRESS_TYPE)
        loading.setTitleText("Loading...").show()
        ApiConfig.instanceRetrofit.btlcheckout(transk.id).enqueue(object : Callback<ResponseModel> {
            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                error(t.message.toString())
            }

            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                loading.dismiss()
                val res = response.body()!!
                if (res.success == 1) {

                    SweetAlertDialog(this@DetailTransk, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Berhasil...")
                            .setContentText("Transaksi Berhasil Dibatalkan")
                            .setConfirmClickListener {
                                it.dismissWithAnimation()
                                onBackPressed()
                            }
                            .show()
//                    Toast.makeText(this@DetailTransk, "Transaksi telah dibatalkan", Toast.LENGTH_SHORT).show()
//
////                    dispRiwayat(res.transaksis)
                } else{
                    error(res.message)
                }
            }
        })
    }

    fun error(pesan: String){
        SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText(pesan)
                .show()
    }

    fun setData(t: TransaksiModel) {
        tv_status.text = t.status

        val formatBaru = "d MMMM yyyy, kk:mm:ss"
        tv_tgl.text = Helper().convertTanggal(t.created_at, formatBaru)

        tv_penerima.text = t.name + " - " + t.phone
        tv_alamat.text = t.detail_lokasi
        tv_kodeUnik.text = Helper().Rupiah(t.kode_unik)
        tv_totalBelanja.text = Helper().Rupiah(t.kode_unik)
        tv_ongkir.text = Helper().Rupiah(t.ongkir)
        tv_total.text = Helper().Rupiah(t.total_transfer)

        if (t.status != "MENUNGGU") div_footer.visibility = View.GONE

        var color = getColor(R.color.menunggu)
        if (t.status == "SELESAI") color = getColor(R.color.selesai)
        else if (t.status == "BATAL") color = getColor(R.color.batal)

        tv_status.setTextColor(color)
    }

    fun dispProduk(transaksis: ArrayList<DetailTrans>){
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        rv_produk.adapter = AdapterProdukTransaksi(transaksis)
        rv_produk.layoutManager = layoutManager
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}