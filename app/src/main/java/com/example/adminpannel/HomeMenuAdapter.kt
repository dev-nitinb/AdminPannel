package com.example.adminpannel

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import java.util.*


class HomeMenuAdapter (var context: Context,var onShareClickedListenerCallback: OnHomeMenuChangedListener,var onStartDragListener: StartDragListener, var alHomeMenu:ArrayList<HomeMenuModel>): RecyclerView.Adapter<HomeMenuAdapter.ViewHolder>(),
    ItemMoveCallback.ItemTouchHelperContract {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_home_menu, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return alHomeMenu.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var homeMenuName=alHomeMenu[position].name
        var homeMenuVisibility=alHomeMenu[position].visibility

        if(Config.adminPanel){
            holder.checkBox.visibility=View.VISIBLE
            holder.checkBox.isChecked = homeMenuVisibility

            if(!homeMenuVisibility){
                holder.parent_layout.alpha=0.5f
            }
        }
        else{
            holder.checkBox.visibility=View.GONE
        }
        holder.tvName.text = homeMenuName

        holder.checkBox.setOnClickListener { view ->
            alHomeMenu[position].visibility = (view as CompoundButton).isChecked
            //notifyItemChanged(position)
            onShareClickedListenerCallback!!.onHomeMenuChanged(alHomeMenu)
       }
        
        holder.parent_layout.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN && Config.adminPanel) {
                onStartDragListener.requestDrag(holder)
            }
            false
        }

    }

    fun getHomeMenuUpdatedArrayList():ArrayList<HomeMenuModel>{
        return alHomeMenu
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName = itemView.findViewById(R.id.tvName) as TextView
        val parent_layout = itemView.findViewById(R.id.parent_layout) as ConstraintLayout
        val checkBox = itemView.findViewById(R.id.checkbox) as CheckBox
    }

    interface OnHomeMenuChangedListener {
        fun onHomeMenuChanged(alHomeMenu:ArrayList<HomeMenuModel>)
    }

    interface StartDragListener {
        fun requestDrag(viewHolder: RecyclerView.ViewHolder?)
    }

    override fun onRowMoved(fromPosition: Int, toPosition: Int) {
            if (fromPosition < toPosition) {
                for (i in fromPosition until toPosition) {
                    Collections.swap(alHomeMenu, i, i + 1)
                }
            } else {
                for (i in fromPosition downTo toPosition + 1) {
                    Collections.swap(alHomeMenu, i, i - 1)
                }
            }
            notifyItemMoved(fromPosition, toPosition)
    }

    override fun onRowSelected(myViewHolder: RecyclerView.ViewHolder) {
        (myViewHolder as ViewHolder).tvName.setTextColor(Color.RED)
    }

    override fun onRowClear(myViewHolder: RecyclerView.ViewHolder) {
        (myViewHolder as ViewHolder).tvName.setTextColor(Color.BLACK)
    }

}