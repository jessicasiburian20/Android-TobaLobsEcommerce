package j012.tobalobsecommerce.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import j012.tobalobsecommerce.R
import j012.tobalobsecommerce.adapter.AdapterAlamat
import j012.tobalobsecommerce.adapter.AdapterProduk
import j012.tobalobsecommerce.helper.Helper
import j012.tobalobsecommerce.model.Alamat
import j012.tobalobsecommerce.model.Produk
import j012.tobalobsecommerce.room.MyDatabase
import kotlinx.android.synthetic.main.activity_list_alamat.*
import kotlinx.android.synthetic.main.toolbar.*

class ListAlamat : AppCompatActivity() {

    lateinit var myDb : MyDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_alamat)
        Helper().settoolbar(this, toolbar, "Pilih ALamat")
        myDb = MyDatabase.getInstance(this)!!
        mainButton()
    }

    private fun displayalamat(){
        val arrayList = myDb.daoAlamat().getAll() as ArrayList

            if (arrayList.isEmpty()) div_kosong.visibility = View.VISIBLE
            else div_kosong.visibility = View.GONE

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        rv_alamat.adapter = AdapterAlamat(arrayList, object : AdapterAlamat.Listeners{

            override fun onClicked(data: Alamat) {
                if (myDb.daoAlamat().getbyStatus(true) !=null){
                    val alamatActive = myDb.daoAlamat().getbyStatus(true)!!
                    alamatActive.isSelected = false
                    updateActive(alamatActive, data)
                }
            }
        })
        rv_alamat.layoutManager = layoutManager
    }

    private fun updateActive(dataActive: Alamat, dataNonActive: Alamat){
        CompositeDisposable().add(Observable.fromCallable { myDb.daoAlamat().update(dataActive) }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    updateNonActive(dataNonActive)
                })
    }

    private fun updateNonActive(data: Alamat){
        data.isSelected = true
        CompositeDisposable().add(Observable.fromCallable { myDb.daoAlamat().update(data) }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    onBackPressed()
                })
    }

    override fun onResume() {
        displayalamat()
        super.onResume()
    }

    private fun mainButton(){
        btn_tambahAlamat.setOnClickListener{
            startActivity(Intent(this, AddAlamat::class.java))
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}