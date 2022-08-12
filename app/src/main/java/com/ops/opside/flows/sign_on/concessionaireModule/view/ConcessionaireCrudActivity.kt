package com.ops.opside.flows.sign_on.concessionaireModule.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ops.opside.common.entities.share.TianguisSE
import com.ops.opside.common.utils.animateOnPress
import com.ops.opside.common.adapters.SwipeToDeleteCallback
import com.ops.opside.databinding.ActivityConcessionaireCrudBinding
import com.ops.opside.flows.sign_on.concessionaireModule.adapters.TianguisParticipatingAdapter

class ConcessionaireCrudActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityConcessionaireCrudBinding
    private lateinit var mAdapter: TianguisParticipatingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityConcessionaireCrudBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.apply {
            btnClose.animateOnPress()
            btnClose.setOnClickListener { onBackPressed() }
        }

        initRecyclerView()
    }

    private fun initRecyclerView() {
        val tianguis = mutableListOf<TianguisSE>()

        for (i in 1..7){
            tianguis.add(TianguisSE(i.toLong(), "Tianguis $i", "",
                "",0.0,0.0,0))
        }

        mAdapter = TianguisParticipatingAdapter(tianguis)

        var linearLayoutManager: RecyclerView.LayoutManager
        linearLayoutManager = LinearLayoutManager(this)

        mBinding.rvParticipatingTianguis.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = mAdapter
        }

        val swipeHandler = object : SwipeToDeleteCallback(this) {

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                mAdapter.tianguis.removeAt(viewHolder.adapterPosition)
                mAdapter.notifyItemRemoved(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(mBinding.rvParticipatingTianguis)
    }
}