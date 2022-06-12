package com.orbitalsonic.appfinderpackage

import android.app.Activity
import android.app.SearchManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log


object GeneralUtils {

    private val generalTag = "generalTag"

    fun Activity?.openPlayStoreApp(packageName:String) {
        this?.let {
            try {
                it.startActivity(
                    Intent(
                        "android.intent.action.VIEW",
                        Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                    )
                )
            }catch (e:Exception){
                Log.e(generalTag,e.message.toString())
            }
        }

    }


    fun Activity?.shareApp(packageName:String) {
        this?.let {
            try {
                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, it.getString(R.string.app_name))
                sendIntent.putExtra(
                    Intent.EXTRA_TEXT,
                    "https://play.google.com/store/apps/details?id=$packageName"
                )
                sendIntent.type = "text/plain"
                it.startActivity(sendIntent)
            }catch (e:Exception){
                Log.e(generalTag,e.message.toString())
            }

        }

    }

    fun Activity?.copyClipboardData(mData:String){
        this?.let {
            try {
                val clipboard = it.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip: ClipData = ClipData.newPlainText(
                    "simple text",
                    mData
                )
                clipboard.setPrimaryClip(clip)
            }catch (e:Exception){
                Log.e(generalTag,e.message.toString())
            }
        }

    }

    fun Activity?.shareTextData(mData: String){
        this?.let {
            try {
                val mIntent = Intent(Intent.ACTION_SEND)
                mIntent.type = "text/plain"
                mIntent.putExtra(Intent.EXTRA_SUBJECT, "Data")
                mIntent.putExtra(Intent.EXTRA_TEXT, mData)
                it.startActivity(Intent.createChooser(mIntent, "Choose to share"))
            }catch (e:Exception){
                Log.e(generalTag,e.message.toString())
            }
        }

    }

    fun Activity?.searchData(mData: String) {
        this?.let {
           try {
               val mIntent = Intent(Intent.ACTION_WEB_SEARCH)
               mIntent.putExtra(SearchManager.QUERY, mData)
               it.startActivity(mIntent)
           } catch (e:Exception){
               Log.e(generalTag,e.message.toString())
           }
        }

    }

    fun Activity?.translateDate(mData: String) {
        this?.let {
            try {
                val url =
                    "https://translate.google.com/#view=home&op=translate&sl=auto&tl=en&text=$mData"
                val mIntent = Intent(Intent.ACTION_VIEW)
                mIntent.data = Uri.parse(url)
                it.startActivity(mIntent)
            }catch (e:Exception){
                Log.e(generalTag,e.message.toString())
            }
        }

    }

    fun Activity?.appInfo(mPackage: String){
        this?.let {
            try {
                val mIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                mIntent.data = Uri.parse("package:$mPackage")
                it.startActivity(mIntent)
            }catch (e:Exception){
                Log.e(generalTag,e.message.toString())
            }
        }

    }

    fun Activity?.openAppInDevice(mPackage: String){
        this?.let {
            try {
               val mIntent = packageManager.getLaunchIntentForPackage(mPackage)
                it.startActivity(mIntent)
            }catch (e:Exception){
                Log.e(generalTag,e.message.toString())
            }
        }

    }

}