package com.ops.opside.flows.sign_on.permissionModule.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.ops.opside.R
import com.ops.opside.common.entities.firestore.Modul
import com.ops.opside.common.entities.firestore.ModulsTree
import com.ops.opside.common.utils.Formaters.orFalse
import com.ops.opside.databinding.ItemPermissionBinding
import com.ops.opside.flows.sign_on.permissionModule.view.PermissionActivity

class PermissionAdapter(
    var mModules: ModulsTree,
    private val mActivity: PermissionActivity
) : RecyclerView.Adapter<PermissionAdapter.ViewHolder>() {

    private lateinit var context: Context
    private var mBeforeModules: ModulsTree = ModulsTree()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(
            R.layout.item_permission, parent,
            false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = mModules.moduls.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val modul = mModules.moduls[position]
        with(holder) {
            bind(modul)
        }
    }

    fun saveBeforeTree() {
        if (mBeforeModules.moduls.isEmpty().orFalse()) {
            mBeforeModules = ModulsTree(mModules.moduls)
        }
    }

    fun getPermissionTreeVisible(): ModulsTree {
        return mModules
    }

    fun resetPermissionToOrigin() {
        Log.d("Demo", mBeforeModules.toString())
        mModules = ModulsTree(mBeforeModules.moduls)

        Log.d("Demo", mModules.toString())
        Log.d("Demo", mBeforeModules.toString())

        notifyDataSetChanged()
    }

    fun checkAllPermissions() {
        mModules.moduls.map {
            it.access = true
            it.permissions = mutableListOf(true, true, true, true)
        }
        notifyDataSetChanged()
    }

    /** View Holder Inner Class **/
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemPermissionBinding.bind(view)

        fun bind(item: Modul) {
            binding.apply {

                cbRol.text = item.name

                cbCreate.isGone = item.access.not()
                cbRead.isGone = item.access.not()
                cbUpdate.isGone = item.access.not()
                cbDelete.isGone = item.access.not()

                cbRol.isChecked = item.access
                cbCreate.isChecked = item.permissions[0]
                cbRead.isChecked = item.permissions[1]
                cbUpdate.isChecked = item.permissions[2]
                cbDelete.isChecked = item.permissions[3]

                if (item.access) {
                    cvPermission.strokeColor = mActivity.getColor(R.color.secondaryColor)
                } else {
                    cvPermission.strokeColor = mActivity.getColor(R.color.primaryTextColor)
                }

                cbRol.setOnCheckedChangeListener { _, isChecked ->
                    item.access = isChecked

                    if (isChecked.not()){
                        cbCreate.isChecked = false
                        cbRead.isChecked = false
                        cbUpdate.isChecked = false
                        cbDelete.isChecked = false
                    }

                    cbCreate.isGone = isChecked.not()
                    cbRead.isGone = isChecked.not()
                    cbUpdate.isGone = isChecked.not()
                    cbDelete.isGone = isChecked.not()

                    if (isChecked) {
                        cvPermission.strokeColor = mActivity.getColor(R.color.secondaryColor)
                    } else {
                        cvPermission.strokeColor = mActivity.getColor(R.color.primaryTextColor)
                    }

                }

                cbCreate.setOnCheckedChangeListener{ _, isChecked -> item.permissions[0] = isChecked}
                cbRead.setOnCheckedChangeListener  { _, isChecked -> item.permissions[1] = isChecked}
                cbUpdate.setOnCheckedChangeListener{ _, isChecked -> item.permissions[2] = isChecked}
                cbDelete.setOnCheckedChangeListener{ _, isChecked -> item.permissions[3] = isChecked}
            }
        }
    }

}