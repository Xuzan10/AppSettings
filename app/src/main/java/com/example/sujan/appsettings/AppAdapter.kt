package com.example.sujan.appsettings

import android.content.Context
import android.content.pm.ApplicationInfo
import android.databinding.DataBindingUtil
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.sujan.appsettings.databinding.ItemAppBinding

class AppAdapter(val appClick: AppAdapter.AppClick) : RecyclerView.Adapter<AppAdapter.AppViewHolder>() {
    interface AppClick {
        fun appClicked(position: Int)
    }

    var appList: List<ApplicationInfo> = ArrayList()

    override fun getItemCount(): Int {
        return appList.size
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        holder.bind(appList[position])
    }

    fun swapData(appList: List<ApplicationInfo>) {
        this.appList = appList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): AppViewHolder {
        val binding: ItemAppBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_app, parent, false)
        return AppViewHolder(parent.context, binding)
    }


    inner class AppViewHolder(val context: Context, val binding: ItemAppBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.appName.setOnClickListener {
                appClick.appClicked(adapterPosition)
            }
        }

        fun bind(applicationInfo: ApplicationInfo) {
            binding.appName.text = getApplicationName(applicationInfo)
            binding.appIcon.setImageDrawable(getAppIcon(applicationInfo))
            binding.packageName.text = applicationInfo.name
        }

        private fun getApplicationName(applicationInfo: ApplicationInfo): String {
            return applicationInfo.loadLabel(context.packageManager).toString()
        }

        private fun getAppIcon(applicationInfo: ApplicationInfo): Drawable {
            return context.packageManager.getApplicationIcon(applicationInfo.packageName)
        }
    }
}