package com.example.sujan.appsettings

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.example.sujan.appsettings.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), AppAdapter.AppClick {


    lateinit var binding: ActivityMainBinding
    lateinit var appAdapter: AppAdapter
    var appList: MutableList<ApplicationInfo> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        appAdapter = AppAdapter(this)
        binding.appRecycler.layoutManager = LinearLayoutManager(this)
        binding.appRecycler.adapter = appAdapter

        getAppList()
    }

    private fun getAppList() {
        val packageManager: PackageManager = getPackageManager()
        appList = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        // Remove system apps
        val it = appList.iterator()
        while (it.hasNext()) {
            val appInfo = it.next()
            if (appInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0 || appInfo.name.isNullOrEmpty()) {
                it.remove()
            }
        }
        appAdapter.swapData(appList)
    }

    override fun appClicked(position: Int) {
        val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:${appList[position].packageName}")
        startActivity(intent)
    }


}
