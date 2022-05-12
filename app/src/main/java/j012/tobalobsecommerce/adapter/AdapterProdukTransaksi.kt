package j012.tobalobsecommerce.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import j012.tobalobsecommerce.R
import j012.tobalobsecommerce.helper.Helper
import j012.tobalobsecommerce.model.DetailTrans
import j012.tobalobsecommerce.model.TransaksiModel
import j012.tobalobsecommerce.util.Config
import java.util.*

class AdapterProdukTransaksi(var data: ArrayList<DetailTrans>) : RecyclerView.Adapter<AdapterProdukTransaksi.Holder>() {

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val imgProduk = view.findViewById<ImageView>(R.id.img_produk)
        val tvNama = view.findViewById<TextView>(R.id.tv_nama)
        val tvHarga = view.findViewById<TextView>(R.id.tv_harga)
        val tvJumlah = view.findViewById<TextView>(R.id.tv_jumlah)
        val tvtotalHarga = view.findViewById<TextView>(R.id.tv_totalHarga)
        val layout = view.findViewById<CardView>(R.id.layout)
    }

    lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        context = parent.context
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_produk_transaksi, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {

        val a = data[position]

        val name = a.produk.name
        val p = a.produk
        holder.tvNama.text = name
        holder.tvHarga.text = Helper().Rupiah(p.harga)
        holder.tvtotalHarga.text = Helper().Rupiah(a.total_harga)
        holder.tvJumlah.text = a.total_item.toString() + " Items"

        holder.layout.setOnClickListener {
//            listener.onClicked(a)
        }

        val gambar = Config.productUrl + p.gambar
        Picasso.get()
            .load(gambar)
            .placeholder(R.drawable.ic_produk)
            .error(R.drawable.ic_produk)
            .into(holder.imgProduk)
    }

    interface Listeners {
        fun onClicked(data: DetailTrans)
    }

}