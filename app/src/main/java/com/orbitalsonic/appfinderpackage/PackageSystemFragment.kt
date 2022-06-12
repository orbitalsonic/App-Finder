package com.orbitalsonic.appfinderpackage

import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.orbitalsonic.appfinderpackage.GeneralUtils.appInfo
import com.orbitalsonic.appfinderpackage.GeneralUtils.copyClipboardData
import com.orbitalsonic.appfinderpackage.GeneralUtils.openAppInDevice
import com.orbitalsonic.appfinderpackage.GeneralUtils.openPlayStoreApp
import com.orbitalsonic.appfinderpackage.GeneralUtils.shareApp
import com.orbitalsonic.appfinderpackage.GeneralUtils.shareTextData
import com.orbitalsonic.appfinderpackage.databinding.DialogPackageBinding
import com.orbitalsonic.appfinderpackage.databinding.FragmentPackageSystemBinding
import kotlinx.coroutines.*

class PackageSystemFragment : BaseFragment<FragmentPackageSystemBinding>() {

    private lateinit var mAdapter: AdapterPackageFragment
    private val scope = CoroutineScope(Job() + Dispatchers.IO)
    private var job: Job? = null
    var packageList:ArrayList<PackageItem> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return getPersistentView(inflater, container, savedInstanceState, R.layout.fragment_package_system)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!hasInitializedRootView) {
            hasInitializedRootView = true

            createRecyclerView()
            Log.i("PackageTesting","${packageList.size}")
            job = scope.launch {
                try {
                     getInstalledApps()
                } catch (e: Exception){
                    Log.i("PackageTesting",e.toString())
                } finally {

                    if (job!!.isActive && !job!!.isCancelled){
                        scope.cancel()
                    }

                }
            }

        }


    }


    private fun createRecyclerView() {
        mAdapter = AdapterPackageFragment()
        binding.recyclerview.adapter = mAdapter
        binding.recyclerview.layoutManager = GridLayoutManager(context, 1)

        mAdapter.setOnItemClickListener(object : OnPackageClickListener {
            override fun onItemClick(position: Int) {
                showInfoDialog(mAdapter.currentList[position].packageName,mAdapter.currentList[position].titleName)
            }

        })

    }


    private fun getInstalledApps(){
        val packList: List<PackageInfo> = context?.packageManager?.getInstalledPackages(0)!!
        for (index in packList.indices) {
            val pack = packList[index]
            if (isSystemPackage(pack)) {
                val mAppName = pack.applicationInfo.loadLabel(context?.packageManager!!).toString()
                val mAppIcon = pack.applicationInfo.loadIcon(context?.packageManager!!)
                val mAppPackage = pack.applicationInfo.packageName
                packageList.add(
                    PackageItem(mAppPackage,mAppName,mAppIcon)
                )
            }
        }

        activity?.runOnUiThread {
            binding.animationView.visibility = View.GONE
            mAdapter.submitList(packageList)
        }
        Log.i("PackageTesting","${packList.size}")
        Log.i("PackageTesting","${packageList.size}")
    }


    private fun isSystemPackage(pkgInfo: PackageInfo): Boolean {
        return pkgInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
    }


    private fun showInfoDialog(mPackage: String, mTitle: String) {
        val mDialog = BottomSheetDialog(requireContext())
        val dialogBinding: DialogPackageBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context), R.layout.dialog_package, null, false
        )
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDialog.setContentView(dialogBinding.root)
        mDialog.setCanceledOnTouchOutside(true)

        dialogBinding.btnInfo.setOnClickListener {
            activity.appInfo(mPackage)
            mDialog.dismiss()
        }

        dialogBinding.btnOpenPlayStore.setOnClickListener {
            activity.openPlayStoreApp(mPackage)
            mDialog.dismiss()
        }
        dialogBinding.btnOpenApp.setOnClickListener {
            activity.openAppInDevice(mPackage)
            mDialog.dismiss()
        }

        dialogBinding.btnShareApp.setOnClickListener {
            activity.shareApp(mPackage)
            mDialog.dismiss()
        }

        dialogBinding.btnShareAppDetails.setOnClickListener {
            val details = "App name: $mTitle\nPackage Name: $mPackage"
            activity.shareTextData(details)
            mDialog.dismiss()
        }

        dialogBinding.btnCopyDetails.setOnClickListener {
            val details = "App name: $mTitle\nPackage Name: $mPackage"
            activity.copyClipboardData(details)
            showMessage("Details Copied!")
            mDialog.dismiss()
        }

        mDialog.show()
    }

}