package j012.tobalobsecommerce.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import j012.tobalobsecommerce.R
import j012.tobalobsecommerce.app.ApiAlamat
import j012.tobalobsecommerce.app.ApiConfig
import j012.tobalobsecommerce.helper.Helper
import j012.tobalobsecommerce.model.Alamat
import j012.tobalobsecommerce.model.ModelAlamat
import j012.tobalobsecommerce.model.ResponseModel
import j012.tobalobsecommerce.room.MyDatabase
import j012.tobalobsecommerce.util.ApiKEY
import kotlinx.android.synthetic.main.activity_add_alamat.*
import kotlinx.android.synthetic.main.activity_add_alamat.edt_nama
import kotlinx.android.synthetic.main.activity_add_alamat.edt_phone
import kotlinx.android.synthetic.main.activity_add_alamat.pb
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddAlamat : AppCompatActivity() {

    var provinsi = ModelAlamat.Provinsi()
    var kota = ModelAlamat.Provinsi()
    var kecamatan = ModelAlamat()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_alamat)
        Helper().settoolbar(this, toolbar, "Tambah ALamat")

        mainButton()

        getProvinsi()

    }

    private fun mainButton(){
        btn_simpan.setOnClickListener{
            simpan()
        }
    }

    private fun simpan() {
        when {
            edt_nama.text.isEmpty() -> {
                error(edt_nama)
                return
            }
            edt_type.text.isEmpty() -> {
                error(edt_type)
                return
            }
            edt_phone.text.isEmpty() -> {
                error(edt_phone)
                return
            }
            edt_alamat.text.isEmpty() -> {
                error(edt_alamat)
                return
            }
            edt_kodePos.text.isEmpty() -> {
                error(edt_kodePos)
                return
            }
        }

        if (provinsi.province_id == "0"){
            toast("Silahkan Pilih Provinsi")
            return
        }
        if (kota.city_id == "0"){
            toast("Silahkan Pilih Kota")
            return
        }
//        if (kecamatan.id == 0){
//            toast("Silahkan Pilih Kecamatan")
//            return
//        }

        val alamat = Alamat()

        alamat.name = edt_nama.text.toString()
        alamat.type = edt_type.text.toString()
        alamat.phone = edt_phone.text.toString()
        alamat.alamat = edt_alamat.text.toString()
        alamat.kodepos = edt_kodePos.text.toString()

        alamat.id_provinsi = Integer.valueOf(provinsi.province_id)
        alamat.provinsi = provinsi.province

        alamat.id_kota = Integer.valueOf(kota.city_id)
        alamat.kota = kota.city_name

//        alamat.id_kecamatan = kecamatan.id
//        alamat.kecamatan = kecamatan.nama

        insert(alamat)

    }

    fun toast(string: String){
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
    }

    private fun error(editText: EditText){
        editText.error = "Tidak boleh kosong"
        editText.requestFocus()
    }

    private fun getProvinsi(){

        ApiAlamat.instanceRetrofit.getProvinsi(ApiKEY.key).enqueue(object : Callback<ResponseModel> {
            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {

            }

            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                if (response.isSuccessful){
                    pb.visibility = View.GONE
                    div_provinsi.visibility= View.VISIBLE

                    val res = response.body()!!
                    val arrayString = ArrayList<String>()
                    arrayString.add("Pilih Provinsi")

                    val listProvinsi = res.rajaongkir.results
                    for (prov in listProvinsi){
                        arrayString.add(prov.province)
                    }


                    val adapter = ArrayAdapter<Any>(this@AddAlamat, R.layout.itemspinner, arrayString.toTypedArray())
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spn_provinsi.adapter = adapter

                    spn_provinsi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            if (position !=0){
                                provinsi = listProvinsi[position - 1]
                                val idProv = provinsi.province_id
                                getKota(idProv)
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {

                        }
                    }

                }else{
                    Log.d("Error", "gagal memuat data:"+response.message())
                }
            }
        })
    }

    private fun getKota(id:String){
        pb.visibility = View.VISIBLE
        ApiAlamat.instanceRetrofit.getKota(ApiKEY.key, id).enqueue(object : Callback<ResponseModel> {
            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
            }
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                if (response.isSuccessful){
                    pb.visibility = View.GONE
                    div_kota.visibility= View.VISIBLE

                    val res = response.body()!!
                    val listArray = res.rajaongkir.results
                    val arrayString = ArrayList<String>()
                    arrayString.add("Pilih Kota/Kabupaten")
                    for (kota in listArray){
                        arrayString.add(kota.type+ " " +kota.city_name)
                    }
                    val adapter = ArrayAdapter<Any>(this@AddAlamat, R.layout.itemspinner, arrayString.toTypedArray())
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spn_kota.adapter = adapter

                    spn_kota.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            if (position !=0){
                                kota = listArray[position - 1]
                                val kodepos = kota.postal_code
                                edt_kodePos.setText(kodepos)
//                                getKecamatan(idKota)
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {
                        }
                    }

                }else{
                    Log.d("Error", "gagal memuat data:"+response.message())
                }
            }
        })
    }

    private fun getKecamatan(id:Int){
        pb.visibility = View.VISIBLE
        ApiAlamat.instanceRetrofit.getKecamatan(id).enqueue(object : Callback<ResponseModel> {
            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
            }
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                if (response.isSuccessful) {
                    pb.visibility = View.GONE
                    div_kecamatan.visibility = View.VISIBLE
                    val res = response.body()!!
                    val listKec = res.kecamatan

                    val arryString = ArrayList<String>()
                    arryString.add("Pilih Kecamatan")
                    for (data in listKec) {
                        arryString.add(data.nama)
                    }

                    val adapter = ArrayAdapter<Any>(this@AddAlamat, R.layout.itemspinner, arryString.toTypedArray())
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spn_kecamatan.adapter = adapter
                    spn_kecamatan.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            if (position !=0){
                                kecamatan = listKec[position - 1]
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {
                        }
                    }
                } else {
                    Log.d("Error", "gagal memuat data:" + response.message())
                }
            }
        })
    }

    private fun insert(data: Alamat){
        val myDb = MyDatabase.getInstance(this)!!
        if (myDb.daoAlamat().getbyStatus(true) ==null){
           data.isSelected = true

        }
        CompositeDisposable().add(Observable.fromCallable { myDb.daoAlamat().insert(data) }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    toast("Alamat Berhasil Ditambah")
                    onBackPressed()
                })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}