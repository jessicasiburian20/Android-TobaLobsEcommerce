package j012.tobalobsecommerce

import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import j012.tobalobsecommerce.activity.LogReg
import j012.tobalobsecommerce.activity.Login
import j012.tobalobsecommerce.fragment.AkunFragment
import j012.tobalobsecommerce.fragment.HomeFragment
import j012.tobalobsecommerce.fragment.KeranjangFragment
import j012.tobalobsecommerce.helper.SharePref

class MainActivity : AppCompatActivity() {

    val fragmentHome: Fragment = HomeFragment()
    val fragmentAkun: Fragment = AkunFragment()
    val fragmentKeranjang : Fragment = KeranjangFragment()
    val fm: FragmentManager = supportFragmentManager
    var active: Fragment = fragmentHome

    private lateinit var menu: Menu
    private lateinit var menuItem: MenuItem
    private lateinit var bottomNavigationView: BottomNavigationView


    private var statusLogin = false
    private lateinit var s:SharePref


    private var daridetail : Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        s = SharePref(this)

        setUpBottomNav()
        LocalBroadcastManager.getInstance(this).registerReceiver(message, IntentFilter("event:keranjang"))

    }

    val message : BroadcastReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            daridetail = true
        }

    }

    fun setUpBottomNav() {

        fm.beginTransaction().add(R.id.container, fragmentHome).show(fragmentHome).commit()
        fm.beginTransaction().add(R.id.container, fragmentKeranjang).hide(fragmentKeranjang).commit()
        fm.beginTransaction().add(R.id.container, fragmentAkun).hide(fragmentAkun).commit()

        bottomNavigationView = findViewById(R.id.nav_view)
        menu = bottomNavigationView.menu

        menuItem = menu.getItem(0)
        menuItem.isChecked = true

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->

            when(item.itemId){
                R.id.navigation_home ->{
                    callFragment(0, fragmentHome)
                }
                R.id.navigation_keranjang ->{
                    callFragment(1, fragmentKeranjang)
                }
                R.id.navigation_akun ->{
                    if (s.getStatusLogin()){
                        callFragment(2, fragmentAkun)
                    } else {
                        startActivity(Intent(this, LogReg::class.java))
                    }

                }
            }
            false
        }
    }


    fun callFragment(int: Int, fragment: Fragment){
        menuItem = menu.getItem(int)
        menuItem.isChecked = true
        fm.beginTransaction().hide(active).show(fragment).commit()
        active = fragment
    }


    override fun onResume() {
        if(daridetail) {
            daridetail = false
            callFragment(1, fragmentKeranjang)
        }
        super.onResume()
    }
 }
