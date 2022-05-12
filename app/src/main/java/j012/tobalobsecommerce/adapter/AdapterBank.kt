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
import j012.tobalobsecommerce.model.Bank
import j012.tobalobsecommerce.model.Produk
import j012.tobalobsecommerce.model.rajaongkir.Costs
import j012.tobalobsecommerce.model.rajaongkir.Result
import j012.tobalobsecommerce.room.MyDatabase
import j012.tobalobsecommerce.util.Config
import java.text.NumberFormat
import java.util.*

class AdapterBank(var data: ArrayList<Bank>, var listener: Listeners):RecyclerView.Adapter<AdapterBank.Holder>() {

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNama = view.findViewById<TextView>(R.id.tv_nama)
        val layout = view.findViewById<LinearLayout>(R.id.layout)
        val image = view.findViewById<ImageView>(R.id.image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.itembank, parent, false)
        return Holder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val a = data[position]
        holder.tvNama.text = a.nama
        holder.image.setImageResource(a.image)
        holder.layout.setOnClickListener {
        listener.onClicked(a, holder.adapterPosition)
        }
    }

    interface Listeners{
        fun onClicked(data:Bank, index:Int)
    }

    override fun getItemCount(): Int {
        return data.size
    }

}