package j012.tobalobsecommerce.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.gson.Gson
import j012.tobalobsecommerce.MainActivity
import j012.tobalobsecommerce.R
import j012.tobalobsecommerce.helper.Helper
import j012.tobalobsecommerce.model.Bank
import j012.tobalobsecommerce.model.CheckOut
import j012.tobalobsecommerce.model.TransaksiModel
import j012.tobalobsecommerce.room.MyDatabase
import kotlinx.android.synthetic.main.activity_transfer.*
import kotlinx.android.synthetic.main.toolbar.*

class Transfer : AppCompatActivity() {
    var nominal = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transfer)
        Helper().settoolbar(this, toolbar, "Pembayaran")


        setValues()
        mainbutton()
    }


    fun mainbutton(){
        btn_copyNoRek.setOnClickListener {
            copyText(tv_nomorRekening.text.toString())
        }

        btn_copyNominal.setOnClickListener {
            copyText(nominal.toString())
        }

        btn_cekStatus.setOnClickListener {
            startActivity(Intent(this, Riwayat::class.java))
        }
    }

    fun copyText(text: String) {
        val copyManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val copyText = ClipData.newPlainText("text", text)
        copyManager.setPrimaryClip(copyText)
        Toast.makeText(this, "Berhasil Disalin", Toast.LENGTH_LONG).show()
    }


    fun setValues(){
        val jsBank = intent.getStringExtra("bank")
        val jsTransaksiModel = intent.getStringExtra("transaksi")
        val jsCheckOut = intent.getStringExtra("checkOut")

        val bank = Gson().fromJson(jsBank, Bank::class.java)
        val transaksi = Gson().fromJson(jsTransaksiModel, TransaksiModel::class.java)
        val checkOut = Gson().fromJson(jsCheckOut, CheckOut::class.java)

        val myDb = MyDatabase.getInstance(this)!!

        for (produk in checkOut.produks){
            myDb.daoKeranjang().deltbyID(produk.id)
        }

        tv_nomorRekening.text = bank.rekening
        tv_namaPenerima.text = bank.penerima
        image_bank.setImageResource(bank.image)

        nominal = Integer.valueOf(transaksi.total_transfer) + transaksi.kode_unik
        tv_nominal.text = Helper().Rupiah(nominal)
    }



    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
        super.onBackPressed()
    }
}