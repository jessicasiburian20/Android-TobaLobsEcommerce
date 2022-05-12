package j012.tobalobsecommerce.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import j012.tobalobsecommerce.MainActivity
import j012.tobalobsecommerce.R
import j012.tobalobsecommerce.adapter.AdapterProduk
import j012.tobalobsecommerce.adapter.AdapterSlider
import j012.tobalobsecommerce.app.ApiConfig
import j012.tobalobsecommerce.model.Produk
import j012.tobalobsecommerce.model.ResponseModel
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class HomeFragment : Fragment() {

    lateinit var vpSlider: ViewPager
    lateinit var rvProduk: RecyclerView
    lateinit var rvProdukTerlaris: RecyclerView
    lateinit var rvLobster: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_home, container, false)

        init(view)
        getProduk()
        return view
    }

    fun displayProduk(){

        var arrSlider = ArrayList<Int>()
        arrSlider.add(R.drawable.slide1)
        arrSlider.add(R.drawable.slide2)
        arrSlider.add(R.drawable.slide3)

        val adapterSlider = AdapterSlider(arrSlider, activity)
        vpSlider.adapter = adapterSlider

        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL

        val layoutManager2 = LinearLayoutManager(activity)
        layoutManager2.orientation = LinearLayoutManager.HORIZONTAL

        val layoutManager3 = LinearLayoutManager(activity)
        layoutManager3.orientation = LinearLayoutManager.HORIZONTAL


        rvProduk.adapter = AdapterProduk(requireActivity(), listProduk)
        rvProduk.layoutManager = layoutManager

        rvProdukTerlaris.adapter = AdapterProduk(requireActivity(),listProduk)
        rvProdukTerlaris.layoutManager = layoutManager2

        rvLobster.adapter = AdapterProduk(requireActivity(),listProduk)
        rvLobster.layoutManager = layoutManager3
    }

    private var listProduk:ArrayList<Produk> = ArrayList()

    fun getProduk(){
        ApiConfig.instanceRetrofit.getProduk().enqueue(object : Callback<ResponseModel> {
            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {

            }

            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                val res = response.body()!!
                if (res.success == 1) {
                    val arrayProduk = ArrayList<Produk>()
                    for(p in res.produks){
                        p.diskon = 10000
                        arrayProduk.add(p)
                    }
                    listProduk = arrayProduk
                    displayProduk()
                }
            }
        })
    }

    fun init(view: View) {
        vpSlider = view.findViewById(R.id.vp_slider)
        rvProduk = view.findViewById(R.id.rv_produk)
        rvProdukTerlaris = view.findViewById(R.id.rv_ProdukTerlaris)
        rvLobster = view.findViewById(R.id.rv_Lobster)
    }

}

