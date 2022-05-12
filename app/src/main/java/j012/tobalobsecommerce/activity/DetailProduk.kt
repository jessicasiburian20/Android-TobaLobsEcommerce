package j012.tobalobsecommerce.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import j012.tobalobsecommerce.R
import j012.tobalobsecommerce.helper.Helper
import j012.tobalobsecommerce.model.Produk
import j012.tobalobsecommerce.room.MyDatabase
import j012.tobalobsecommerce.util.Config
import kotlinx.android.synthetic.main.activity_detail_produk.*
import kotlinx.android.synthetic.main.itemproduk.*
import kotlinx.android.synthetic.main.itemproduk.tv_harga
import kotlinx.android.synthetic.main.itemproduk.tv_nama
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar.toolbar
import kotlinx.android.synthetic.main.toolbarcustom.*

class DetailProduk : AppCompatActivity() {

    lateinit var produk:Produk
    lateinit var myDb: MyDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_produk)

        myDb = MyDatabase.getInstance(this)!! // call database

        getInfo()
        mainButton()
        cekkeranjang()
    }

     private fun mainButton(){
        btn_keranjang.setOnClickListener{
            val data = myDb.daoKeranjang().getProduk(produk.id)
            if (data == null){
                insert()
            } else{
                data.jumlah += 1
                update(data)
            }


        }

        btn_favorit.setOnClickListener{

            val listData = myDb.daoKeranjang().getAll() // get All data
            for(note :Produk in listData){
                println("-----------------------")
                println(note.name)
                println(note.harga)
            }
        }

         btn_toKeranjang.setOnClickListener{
             val intent = Intent("event:keranjang")
             LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
             onBackPressed()
         }
    }

    private fun insert(){

        CompositeDisposable().add(Observable.fromCallable { myDb.daoKeranjang().insert(produk) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {

            cekkeranjang()

                Log.d("respons", "data inserted")
                Toast.makeText(this, "Berhasil menambahkan ke keranjang", Toast.LENGTH_SHORT).show()
            })
    }



    private fun update(data: Produk){

        CompositeDisposable().add(Observable.fromCallable { myDb.daoKeranjang().update(data) }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {

                    cekkeranjang()

                    Log.d("respons", "data inserted")
                    Toast.makeText(this, "Berhasil menambahkan ke keranjang", Toast.LENGTH_SHORT).show()
                })
    }


    private fun cekkeranjang(){
        val datakeranjang = myDb.daoKeranjang().getAll()
        if(datakeranjang.isNotEmpty()){
            div_angka.visibility = View.VISIBLE
            tv_angka.text = datakeranjang.size.toString()
        } else{
            div_angka.visibility = View.GONE
        }
    }




   private fun getInfo(){
        val data = intent.getStringExtra("extra")
        produk = Gson().fromJson<Produk>(data, Produk::class.java)
        tv_nama.text = produk.name
        tv_harga.text = Helper().Rupiah(produk.harga)
        tv_deskripsi.text = produk.deskripsi


        val gmbr = Config.productUrl + produk.gambar
        Picasso.get()
            .load(gmbr)
            .placeholder(R.drawable.ic_produk)
            .error(R.drawable.ic_produk)
            .resize(400, 400)
            .into(image)


       Helper().settoolbar(this, toolbar, produk.name)

    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}