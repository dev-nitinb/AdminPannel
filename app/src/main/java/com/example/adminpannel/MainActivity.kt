package com.example.adminpannel

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class MainActivity : AppCompatActivity(), HomeMenuAdapter.OnHomeMenuChangedListener, HomeMenuAdapter.StartDragListener {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: HomeMenuAdapter
    private lateinit var mProgressBar: ProgressBar
    private lateinit var switchAdminPanel: Switch
    private lateinit var llOption: LinearLayout
    private lateinit var llApprove: LinearLayout
    private lateinit var llReject: LinearLayout
    lateinit var touchHelper: ItemTouchHelper
    var alHomeMenu = ArrayList<HomeMenuModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initialize()

        switchAdminPanel.setOnCheckedChangeListener { buttonView, isChecked ->
            Config.adminPanel = isChecked
            if (Config.adminPanel) {
                llOption.visibility = View.VISIBLE
            } else {
                llOption.visibility = View.GONE
            }
            alHomeMenu = Gson().fromJson(
                Config.homeMenuJson,
                object : TypeToken<ArrayList<HomeMenuModel>>() {}.type
            )
            initializeRecylerView(alHomeMenu)
        }

        llApprove.setOnClickListener {
            alHomeMenu = mAdapter.getHomeMenuUpdatedArrayList()
            Config.homeMenuJson = Gson().toJson(alHomeMenu)
            switchAdminPanel.isChecked = false


        }

        llReject.setOnClickListener {
            alHomeMenu = Gson().fromJson(
                Config.homeMenuJson,
                object : TypeToken<ArrayList<HomeMenuModel>>() {}.type
            )
            initializeRecylerView(alHomeMenu)
        }
    }

    fun initialize() {
        mRecyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        mProgressBar = findViewById<ProgressBar>(R.id.progress_bar)
        switchAdminPanel = findViewById<Switch>(R.id.switchAdminPanel)
        llOption = findViewById<LinearLayout>(R.id.llOption)
        llApprove = findViewById<LinearLayout>(R.id.llApprove)
        llReject = findViewById<LinearLayout>(R.id.llReject)

        alHomeMenu = Gson().fromJson(
            Config.homeMenuJson,
            object : TypeToken<ArrayList<HomeMenuModel>>() {}.type
        )

        initializeRecylerView(alHomeMenu)
    }

    fun initializeRecylerView(alHomeMenu: ArrayList<HomeMenuModel>) {
        var filterHomeMenu = ArrayList<HomeMenuModel>()

        //if in general mode
        if (!Config.adminPanel) {
            for (i in 0 until alHomeMenu.size) {
                if (alHomeMenu[i].visibility) {
                    filterHomeMenu.add(alHomeMenu[i])
                }
            }
        }

        //for admin panel
        else {
            filterHomeMenu = alHomeMenu
        }

        mAdapter = HomeMenuAdapter(this, this as HomeMenuAdapter.OnHomeMenuChangedListener,this as HomeMenuAdapter.StartDragListener, filterHomeMenu)

        val callback: ItemTouchHelper.Callback = ItemMoveCallback(mAdapter)
        touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(mRecyclerView)

       val linearLayoutManager = LinearLayoutManager(this)
        //val linearLayoutManager = GridLayoutManager(this, 2)
        mRecyclerView!!.layoutManager = linearLayoutManager
        mRecyclerView!!.adapter = mAdapter
    }

    override fun onHomeMenuChanged(alHomeMenu: ArrayList<HomeMenuModel>) {
        initializeRecylerView(alHomeMenu)
    }

    override fun requestDrag(viewHolder: RecyclerView.ViewHolder?) {
        touchHelper.startDrag(viewHolder!!)
    }

}
