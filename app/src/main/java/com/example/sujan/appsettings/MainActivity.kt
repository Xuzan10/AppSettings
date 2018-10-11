package com.example.sujan.appsettings

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import com.example.sujan.appsettings.databinding.ActivityMainBinding
import android.R.menu
import android.support.v4.view.MenuItemCompat.getActionView
import android.content.Context.SEARCH_SERVICE
import android.app.SearchManager
import android.content.Context
import android.support.v7.widget.SearchView
import android.view.MenuInflater
import android.databinding.adapters.SearchViewBindingAdapter.setOnQueryTextListener
import android.text.TextUtils


class MainActivity : AppCompatActivity(), AppAdapter.AppClick {


    lateinit var binding: ActivityMainBinding
    lateinit var appAdapter: AppAdapter
    var appList: MutableList<ApplicationInfo> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        getAppList()
    }

    private fun getAppList() {
        val packageManager: PackageManager = getPackageManager()
        appList = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        // Remove system apps
        val it = appList.iterator()
        while (it.hasNext()) {
            val appInfo = it.next()
            if (appInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0) {
                it.remove()
            }
        }
        appAdapter = AppAdapter(this, appList, this)
        binding.appRecycler.layoutManager = LinearLayoutManager(this)
        binding.appRecycler.adapter = appAdapter
    }

    override fun appClicked(position: Int) {
        val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:${appList[position].packageName}")
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search, menu)

        val searchItem = menu!!.findItem(R.id.action_search)

        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                s.toLowerCase()
                val newList: MutableList<ApplicationInfo> = arrayListOf()
                appList.forEach {
                    val appName = it.loadLabel(packageManager).toString().toLowerCase()
                    if (appName.contains(s))
                        newList.add(it)
                }
                appAdapter.filter(newList)
                return true
            }
        })
        return true
    }
}
