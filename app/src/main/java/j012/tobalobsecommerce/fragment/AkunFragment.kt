package j012.tobalobsecommerce.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import j012.tobalobsecommerce.R
import j012.tobalobsecommerce.activity.Riwayat
import j012.tobalobsecommerce.helper.SharePref

class AkunFragment : Fragment() {


    lateinit var s: SharePref
    lateinit var btnLogout: TextView
    lateinit var tvNama: TextView
    lateinit var tvEmail: TextView
    lateinit var tvPhone: TextView
    lateinit var btnRiwayat: RelativeLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_akun, container, false)
        init(view)

        s = SharePref(requireActivity())

        mainButton()
        setData()
        return view
    }

    fun mainButton() {
        btnLogout.setOnClickListener {
            s.setStatusLogin(false)
        }

        btnRiwayat.setOnClickListener {
            startActivity(Intent(requireActivity(), Riwayat::class.java))
        }
    }

    fun setData() {
        if (s.getUser() == null) {
            return
        }
        val user = s.getUser()!!
        tvNama.text = user.name
        tvEmail.text = user.email
        tvPhone.text = user.phone

    }

    private fun init(view: View) {
        btnLogout = view.findViewById(R.id.btn_logout)
        tvNama = view.findViewById(R.id.tv_nama)
        tvEmail = view.findViewById(R.id.tv_email)
        tvPhone = view.findViewById(R.id.tv_phone)
        btnRiwayat = view.findViewById(R.id.btn_riwayat)
    }
}
