package j012.tobalobsecommerce.adapter

import android.app.Activity
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import j012.tobalobsecommerce.MainActivity
import j012.tobalobsecommerce.R
import j012.tobalobsecommerce.activity.DetailProduk
import j012.tobalobsecommerce.activity.Login
import j012.tobalobsecommerce.helper.Helper
import j012.tobalobsecommerce.model.Produk
import j012.tobalobsecommerce.util.Config
import java.text.NumberFormat
import java.util.*

class AdapterProduk(var activity: Activity, var data:ArrayList<Produk>):RecyclerView.Adapter<AdapterProduk.Holder>() {

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNama = view.findViewById<TextView>(R.id.tv_nama)
        val tvHarga = view.findViewById<TextView>(R.id.tv_harga)
        val tvHargaAsli = view.findViewById<TextView>(R.id.tv_hargaAsli)
        val imgProduk = view.findViewById<ImageView>(R.id.img_produk)
        val layout = view.findViewById<CardView>(R.id.layout)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.itemproduk, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        val a = data[position]

        var hargaAsli = Integer.valueOf(a.harga)
        var harga = Integer.valueOf(a.harga)

        if (a.diskon !=0){
            harga -= a.diskon
        }


        holder.tvHargaAsli.text = Helper().Rupiah(hargaAsli)
        holder.tvHargaAsli.paintFlags = holder.tvHargaAsli.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        holder.tvNama.text = data[position].name
        holder.tvHarga.text = Helper().Rupiah(harga)
        //holder.imgProduk.setImageResource(data[position].gambar)

        val gambar = Config.productUrl + data[position].gambar
        Picasso.get()
                .load(gambar)
                .placeholder(R.drawable.ic_produk)
                .error(R.drawable.ic_produk)
                .into(holder.imgProduk)

        holder.layout.setOnClickListener{
            val intent = Intent(activity, DetailProduk::class.java)
            val str = Gson().toJson(data[position], Produk::class.java)
            intent.putExtra("extra", str)

            activity.startActivity(intent)
        }



    }

    override fun getItemCount(): Int {
        return data.size
    }

}