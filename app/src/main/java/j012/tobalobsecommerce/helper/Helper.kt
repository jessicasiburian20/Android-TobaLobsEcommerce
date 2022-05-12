package j012.tobalobsecommerce.helper

import android.app.Activity

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.toolbar.*
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class Helper {
    fun Rupiah(string: String) :String{
        return NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(Integer.valueOf(string))
    }

    fun Rupiah(value: Int) :String{
        return NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(value)
    }


    fun settoolbar(activity: Activity, toolbar: Toolbar, title: String){
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        activity.supportActionBar!!.title = title
        activity.supportActionBar!!.setDisplayShowHomeEnabled(true)
        activity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    fun convertTanggal(tgl: String, formatBaru: String, fromatLama: String = "yyyy-MM-dd kk:mm:ss") :String{
        val dateFormat = SimpleDateFormat(fromatLama)
        val confert = dateFormat.parse(tgl)
        dateFormat.applyPattern(formatBaru)
        return dateFormat.format(confert)
    }
}