package j012.tobalobsecommerce.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
import j012.tobalobsecommerce.room.MyDatabase
import j012.tobalobsecommerce.util.Config
import java.text.NumberFormat
import java.util.*

class AdapterAlamat(var data:ArrayList<Alamat>, var listener:Listeners):RecyclerView.Adapter<AdapterAlamat.Holder>() {

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNama = view.findViewById<TextView>(R.id.tv_nama)
        val tvPhone = view.findViewById<TextView>(R.id.tv_phone)
        val tvAlamat = view.findViewById<TextView>(R.id.tv_alamat)
        val layout = view.findViewById<CardView>(R.id.layout)
        val rdbutton = view.findViewById<RadioButton>(R.id.rd_alamat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.itemalamat, parent, false)
        return Holder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {

        val x = data[position]

        holder.rdbutton.isChecked = x.isSelected
        holder.tvNama.text = x.name
        holder.tvPhone.text = x.phone
        holder.tvAlamat.text = x.alamat + ", " +x.kota + ", " +x.kecamatan + ", " + x.kodepos + ", (" +x.type+")"

        holder.rdbutton.setOnClickListener {
            x.isSelected = true
            listener.onClicked(x)
        }

        holder.layout.setOnClickListener{
            x.isSelected = true
            listener.onClicked(x)
        }
    }



    interface Listeners{
        fun onClicked(data:Alamat)
    }

    override fun getItemCount(): Int {
        return data.size
    }

}