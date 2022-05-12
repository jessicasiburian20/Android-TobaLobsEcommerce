package j012.tobalobsecommerce.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import j012.tobalobsecommerce.R
import j012.tobalobsecommerce.activity.LogReg
import j012.tobalobsecommerce.activity.Pengiriman
import j012.tobalobsecommerce.adapter.AdapterKeranjang
import j012.tobalobsecommerce.helper.Helper
import j012.tobalobsecommerce.helper.SharePref
import j012.tobalobsecommerce.model.Produk
import j012.tobalobsecommerce.room.MyDatabase

class KeranjangFragment : Fragment() {
    lateinit var myDb : MyDatabase
    lateinit var s : SharePref

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_keranjang, container, false)

        myDb = MyDatabase.getInstance(requireActivity())!!
        s = SharePref(requireActivity())

        init(view)
        mainButton()

        return view
    }

    lateinit var adapter : AdapterKeranjang
    var listProduk = ArrayList<Produk>()
    private fun displayProduk(){
        listProduk = myDb.daoKeranjang().getAll() as ArrayList
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        adapter = AdapterKeranjang(requireActivity(), listProduk, object : AdapterKeranjang.Listeners{
            override fun onUpdate() {
                totalharga()
            }

            override fun onDelete(position: Int) {
                listProduk.removeAt(position)
                adapter.notifyDataSetChanged()
                totalharga()
            }

        })
        rvProduk.adapter = adapter
        rvProduk.layoutManager = layoutManager

    }


    var totalharga = 0
    fun totalharga(){
        val listProduk = myDb.daoKeranjang().getAll() as ArrayList
        totalharga = 0
        var isSelectedAll = true
        for(produk in listProduk){
            if (produk.selected){
                val harga = Integer.valueOf(produk.harga)
                totalharga += (harga * produk.jumlah)
            } else{
                isSelectedAll = false
            }
        }
        ckAll.isChecked = isSelectedAll
        tvTotal.text = Helper().Rupiah(totalharga)
    }


    private fun mainButton() {
        btndelete.setOnClickListener{
            val listDelete = ArrayList<Produk>()
            for (p in listProduk){
                if (p.selected) listDelete.add(p)
            }

            delete(listDelete)
        }

        btnBayar.setOnClickListener{
            if (s.getStatusLogin()){

                var isThereProduk = false

                for (p in listProduk){
                    if (p.selected) isThereProduk = true
                }

                if (isThereProduk){
                    val intent = Intent(requireActivity(), Pengiriman::class.java)
                    intent.putExtra("extra", ""+totalharga)
                    startActivity(intent)
                }else{
                    Toast.makeText(requireContext(), "Tidak ada produk yang terpilih" , Toast.LENGTH_SHORT).show()
                }
            } else{
                requireActivity().startActivity(Intent(requireActivity(), LogReg::class.java))
            }
        }

        ckAll.setOnClickListener {
            for (i in listProduk.indices){
                val produk = listProduk[i]
                produk.selected = ckAll.isChecked
                listProduk[i] = produk
            }
            adapter.notifyDataSetChanged()
        }
    }


    private fun delete(data: ArrayList<Produk>){
        CompositeDisposable().add(Observable.fromCallable { myDb.daoKeranjang().delete(data) }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    listProduk.clear()
                    listProduk.addAll(myDb.daoKeranjang().getAll() as ArrayList)
                    adapter.notifyDataSetChanged()
                })
    }

    lateinit var btndelete : ImageView
    lateinit var rvProduk : RecyclerView
    lateinit var tvTotal : TextView
    lateinit var btnBayar : TextView
    lateinit var ckAll : CheckBox

    private fun init(view: View) {
        btndelete = view.findViewById(R.id.btn_delete)
        rvProduk = view.findViewById(R.id.rv_produk)
        tvTotal = view.findViewById(R.id.tv_total)
        btnBayar = view.findViewById(R.id.btn_bayar)
        ckAll = view.findViewById(R.id.cb_all)

    }

    override fun onResume() {
        displayProduk()
        totalharga()
        super.onResume()
    }
}