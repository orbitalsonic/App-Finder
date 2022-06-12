package com.orbitalsonic.appfinderpackage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.orbitalsonic.appfinderpackage.databinding.ItemPackageBinding

class AdapterPackageFragment:ListAdapter<PackageItem, AdapterPackageFragment.PackageViewHolder>(
    DATA_COMPARATOR
) {

    var mListener: OnPackageClickListener? = null

    fun setOnItemClickListener(listener: OnPackageClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemPackageBinding = DataBindingUtil.inflate(inflater,R.layout.item_package,parent,false)
        return PackageViewHolder(binding,mListener!!)

    }

    override fun onBindViewHolder(holder: PackageViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    class PackageViewHolder(binding:ItemPackageBinding, listener: OnPackageClickListener) : RecyclerView.ViewHolder(binding.root) {
        private val mBinding = binding

        init {

            mBinding.item.setOnClickListener{
                val mPosition = adapterPosition
                listener.onItemClick(mPosition)
            }

        }

        fun bind(mCurrentItem: PackageItem) {
           mBinding.packageData = mCurrentItem
            mBinding.ivAppIcon.setImageDrawable(mCurrentItem.appIcon)
        }

    }


    companion object {
        private val DATA_COMPARATOR = object : DiffUtil.ItemCallback<PackageItem>() {
            override fun areItemsTheSame(oldItem: PackageItem, newItem: PackageItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: PackageItem, newItem: PackageItem): Boolean {
                return oldItem.packageName==newItem.packageName
            }
        }
    }

}