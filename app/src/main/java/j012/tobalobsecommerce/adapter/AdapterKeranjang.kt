package j012.tobalobsecommerce.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
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
import java.util.*

class AdapterKeranjang(var activity: Activity, var data:ArrayList<Produk>, var listener :Listeners):RecyclerView.Adapter<AdapterKeranjang.Holder>() {

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNama = view.findViewById<TextView>(R.id.tv_nama)
        val tvHarga = view.findViewById<TextView>(R.id.tv_harga)
        val imgProduk = view.findViewById<ImageView>(R.id.img_produk)
        val layout = view.findViewById<CardView>(R.id.layout)
        val btnHapus = view.findViewById<ImageView>(R.id.btn_delete)
        val btnTambah = view.findViewById<ImageView>(R.id.btn_tambah)
        val btnKurang = view.findViewById<ImageView>(R.id.btn_kurang)

        val chekbox = view.findViewById<CheckBox>(R.id.checkBox)
        val tvJumlh = view.findViewById<TextView>(R.id.tv_jumlah)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.itemkeranjang, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        val produk = data[position]
        val harga = Integer.valueOf(produk.harga)

        holder.tvNama.text = produk.name
        holder.tvHarga.text = Helper().Rupiah(harga * produk.jumlah)

        var jumlah = data[position].jumlah
        holder.tvJumlh.text = jumlah.toString()

        holder.chekbox.isChecked = produk.selected
        holder.chekbox.setOnCheckedChangeListener { buttonView, isChecked ->
            produk.selected = isChecked
            update(produk)
        }


        val gambar = Config.productUrl + data[position].gambar
        Picasso.get()
                .load(gambar)
                .placeholder(R.drawable.ic_produk)
                .error(R.drawable.ic_produk)
                .into(holder.imgProduk)


        holder.btnTambah.setOnClickListener{
            jumlah++
            produk.jumlah = jumlah
            update(produk)
            holder.tvJumlh.text = jumlah.toString()
            holder.tvHarga.text = Helper().Rupiah(harga * jumlah)
        }

        holder.btnKurang.setOnClickListener{
            if (jumlah <= 1) return@setOnClickListener
            jumlah--
            produk.jumlah = jumlah
            update(produk)
            holder.tvJumlh.text = jumlah.toString()
            holder.tvHarga.text = Helper().Rupiah(harga * jumlah)
        }

        holder.btnHapus.setOnClickListener{
            delete(produk)
            listener.onDelete(position)
        }
    }

    interface Listeners {
        fun onUpdate()
        fun onDelete(position: Int)
    }

    private fun update(data: Produk){

        val myDb = MyDatabase.getInstance(activity)
        CompositeDisposable().add(Observable.fromCallable { myDb!!.daoKeranjang().update(data) }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    listener.onUpdate()
                })
    }

    private fun delete(data: Produk){

        val myDb = MyDatabase.getInstance(activity)
        CompositeDisposable().add(Observable.fromCallable { myDb!!.daoKeranjang().delete(data) }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                })
    }


    override fun getItemCount(): Int {
        return data.size
    }

}