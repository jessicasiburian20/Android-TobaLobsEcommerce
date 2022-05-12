package j012.tobalobsecommerce.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import j012.tobalobsecommerce.R
import j012.tobalobsecommerce.adapter.AdapterKurir
import j012.tobalobsecommerce.app.ApiAlamat
import j012.tobalobsecommerce.helper.Helper
import j012.tobalobsecommerce.helper.SharePref
import j012.tobalobsecommerce.model.CheckOut
import j012.tobalobsecommerce.model.rajaongkir.Costs
import j012.tobalobsecommerce.model.rajaongkir.ResponseModelOngkir
import j012.tobalobsecommerce.room.MyDatabase
import j012.tobalobsecommerce.util.ApiKEY
import kotlinx.android.synthetic.main.activity_add_alamat.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_pengiriman.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Pengiriman : AppCompatActivity() {
    lateinit var myDb : MyDatabase
    var totalHarga = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pengiriman)
        Helper().settoolbar(this, toolbar, "Pengiriman")
        myDb = MyDatabase.getInstance(this)!!

        totalHarga = Integer.valueOf(intent.getStringExtra("extra")!!)

        tv_totalBelanja.text = Helper().Rupiah(totalHarga)
        mainButton()
        setSpinner()
    }

    fun setSpinner(){
        val arrayString = ArrayList<String>()
        arrayString.add("JNE")
        arrayString.add("POS")
        arrayString.add("TIKI")

        val adapter = ArrayAdapter<Any>(this, R.layout.itemspinner, arrayString.toTypedArray())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spn_kurir.adapter = adapter

        spn_kurir.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position !=0){
                    getOngkir(spn_kurir.selectedItem.toString())
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

    }

    @SuppressLint("SetTextI18n")
    fun cekAlamat() {

        if (myDb.daoAlamat().getbyStatus(true) != null){
            div_alamat.visibility = View.VISIBLE
            div_kosong.visibility = View.GONE
            div_metodePengiriman.visibility = View.VISIBLE

            val x = myDb.daoAlamat().getbyStatus(true)!!
            tv_nama.text = x.name
            tv_phone.text = x.phone
            tv_alamat.text = x.alamat + ", " +x.kota + ", " + x.kodepos + ", (" +x.type+")"
            btn_tambahAlamat.text = "Ubah Alamat"



            getOngkir("JNE")
        } else{
            div_alamat.visibility = View.GONE
            div_kosong.visibility = View.VISIBLE
            btn_tambahAlamat.text = "Tambah Alamat"
        }
    }

    private fun mainButton(){
        btn_tambahAlamat.setOnClickListener{
            startActivity(Intent(this, ListAlamat::class.java))
        }

        btn_bayar.setOnClickListener{
            bayar()
        }
    }

    private fun bayar(){
        val user = SharePref(this).getUser()!!
        val x = myDb.daoAlamat().getbyStatus(true)!!

        val listProduk = myDb.daoKeranjang().getAll() as ArrayList

        var totalItem = 0
        var totalHarga = 0

        val produks = ArrayList<CheckOut.Item>()

        for (p in listProduk){
            if(p.selected){
                totalItem += p.jumlah
                totalHarga += (p.jumlah * Integer.valueOf(p.harga))

                val produk = CheckOut.Item()
                produk.id = ""+ p.id
                produk.total_item = ""+p.jumlah
                produk.total_harga = "" + (p.jumlah * Integer.valueOf(p.harga))
                produk.catatan = "catatan baruuu"

                produks.add(produk)
            }
        }

        val checkOut = CheckOut()
        checkOut.user_id = "" + user.id
        checkOut.total_item = "" + totalItem
        checkOut.total_harga = "" + totalHarga
        checkOut.name = x.name
        checkOut.phone = x.phone
        checkOut.jasa_pengiriman = jasakirim
        checkOut.detail_lokasi = tv_alamat.text.toString()
        checkOut.ongkir = ongkir
        checkOut.kurir = kurir
        checkOut.total_transfer = ""+(totalHarga + Integer.valueOf(ongkir))
        checkOut.produks = produks

        val json = Gson().toJson(checkOut, CheckOut::class.java)
        Log.d("Respon:", "json:" +json)
        val intent = Intent(this, Pembayaran::class.java)
        intent.putExtra("extra", json)
        startActivity(intent)



    }

    private fun getOngkir(kurir:String){

        val alamat = myDb.daoAlamat().getbyStatus(true)

        val origin = "501"
        val destination = "" +alamat!!.id_kota
        val weight = 1000

        ApiAlamat.instanceRetrofit.ongkir(ApiKEY.key, origin, destination, weight, kurir.toLowerCase()).enqueue(object : Callback<ResponseModelOngkir> {
            override fun onFailure(call: Call<ResponseModelOngkir>, t: Throwable) {
                Log.d("Error", "gagal memuat data:"+ t.message)
            }

            override fun onResponse(call: Call<ResponseModelOngkir>, response: Response<ResponseModelOngkir>) {
                if (response.isSuccessful){
                    Log.d("Success", "Berhasil memuat data")
                    val result = response.body()!!.rajaongkir.results
                    if (result.isNotEmpty()){
                        dispOngkir(result[0].code.toUpperCase(), result[0].costs)
                    }

                }else{
                    Log.d("Error", "gagal memuat data:"+response.message())
                }
            }
        })
    }


    var ongkir = ""
    var jasakirim = ""
    var kurir = ""
    private fun dispOngkir(_kurir :String, arrayList : ArrayList<Costs>){

        var arrayOngkir = ArrayList<Costs>()
        for (i in arrayList.indices){
            var ongkir = arrayList[i]
            if (i == 0){
                ongkir.isActive = true
            }
            arrayOngkir.add(ongkir)
        }

        setTotal(arrayOngkir[0].cost[0].value)
        ongkir = arrayOngkir[0].cost[0].value
        kurir = _kurir
        jasakirim = arrayOngkir[0].service

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        var adapter: AdapterKurir? = null
        adapter = AdapterKurir(arrayOngkir, _kurir, object : AdapterKurir.Listeners{
            override fun onClicked(data: Costs, index: Int) {
                val newarrayOngkir = ArrayList<Costs>()
                for(ongkir in arrayOngkir){
                    ongkir.isActive = data.description == ongkir.description
                    newarrayOngkir.add(ongkir)
                }
                arrayOngkir = newarrayOngkir
                adapter!!.notifyDataSetChanged()
                setTotal(data.cost[0].value)

                ongkir = data.cost[0].value
                kurir = _kurir
                jasakirim = data.service
            }
        })
        rv_metode.adapter = adapter
        rv_metode.layoutManager = layoutManager
    }

    fun setTotal(ongkir :String){
        tv_ongkir.text = Helper().Rupiah(ongkir)
        tv_total.text = Helper().Rupiah(Integer.valueOf(ongkir)+totalHarga)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onResume() {
        cekAlamat()

        super.onResume()
    }
}