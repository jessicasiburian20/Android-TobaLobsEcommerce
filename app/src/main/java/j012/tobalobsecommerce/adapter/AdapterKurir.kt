package j012.tobalobsecommerce.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import j012.tobalobsecommerce.MainActivity
import j012.tobalobsecommerce.R
import j012.tobalobsecommerce.activity.DetailProduk
import j012.tobalobsecommerce.activity.Login
import j012.tobalobsecommerce.helper.Helper
import j012.tobalobsecommerce.model.Alamat
import j012.tobalobsecommerce.model.Produk
import j012.tobalobsecommerce.model.rajaongkir.Costs
import j012.tobalobsecommerce.model.rajaongkir.Result
import j012.tobalobsecommerce.room.MyDatabase
import j012.tobalobsecommerce.util.Config
import java.text.NumberFormat
import java.util.*

class AdapterKurir(var data:ArrayList<Costs>, var kurir:String, var listener:Listeners):RecyclerView.Adapter<AdapterKurir.Holder>() {

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNama = view.findViewById<TextView>(R.id.tv_nama)
        val tvLamaPengiriman = view.findViewById<TextView>(R.id.tv_lamaPengiriman)
        val tvBerat = view.findViewById<TextView>(R.id.tv_berat)
        val tvHarga = view.findViewById<TextView>(R.id.tv_harga)
        val layout = view.findViewById<LinearLayout>(R.id.layout)
        val rdbutton = view.findViewById<RadioButton>(R.id.rd)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.itemkurir, parent, false)
        return Holder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {

        val x = data[position]

        holder.rdbutton.isChecked = x.isActive

        holder.tvNama.text = kurir + " " +x.service
        val cost = x.cost[0]
        holder.tvLamaPengiriman.text = cost.etd + " hari kerja"
        holder.tvHarga.text = Helper().Rupiah(cost.value)
        holder.tvBerat.text = "1 kg x" + Helper().Rupiah(cost.value)
//        holder.tvAlamat.text = x.alamat + ", " +x.kota + ", " +x.kecamatan + ", " + x.kodepos + ", (" +x.type+")"
//
        holder.rdbutton.setOnClickListener {
            x.isActive = true
            listener.onClicked(x, holder.adapterPosition)
        }
//
//        holder.layout.setOnClickListener{
//            x.isSelected = true
//            listener.onClicked(x)
//        }
    }



    interface Listeners{
        fun onClicked(data:Costs, index:Int)
    }

    override fun getItemCount(): Int {
        return data.size
    }

}