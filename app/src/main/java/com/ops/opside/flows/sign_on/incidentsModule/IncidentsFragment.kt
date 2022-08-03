package com.ops.opside.flows.sign_on.incidentsModule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toolbar
import com.ops.opside.R
import com.ops.opside.databinding.FragmentIncidentsBinding

class IncidentsFragment : Fragment() {

    private var mBinding: FragmentIncidentsBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentIncidentsBinding.inflate(inflater, container, false)
        setToolbar()

        return binding.root
    }

    private fun setToolbar(){
        with(binding.toolbar.commonToolbar){
            this.setTitleTextColor(R.color.primaryTextColor)
            this.title = getString(R.string.bn_menu_incidents_opc5)
            this.inflateMenu(R.menu.incidents_toolbar_menu)
            this.setNavigationOnClickListener {
            }

            this.setOnMenuItemClickListener { menuItem ->
                when(menuItem.itemId){
                    R.id.create_incident -> {
                        Toast.makeText(activity, "Create Incident", Toast.LENGTH_SHORT).show()
                        true
                    }

                    R.id.see_incident -> {
                        Toast.makeText(activity, "See Incident", Toast.LENGTH_SHORT).show()
                        true
                    }

                    else -> false
                }
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}