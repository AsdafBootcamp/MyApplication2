package com.narims.myapplication.presentation.adapter

import android.content.Intent
import android.os.Bundle
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.narims.myapplication.R
import com.narims.myapplication.model.WeatherItem
import com.narims.myapplication.presentation.ItemDetailActivity
import com.narims.myapplication.presentation.ItemDetailFragment
import com.narims.myapplication.presentation.mvp.itemlist.ItemListActivity
import com.narims.myapplication.util.WeatherItemsDiffCallback
import kotlinx.android.synthetic.main.item_list_content.view.*

class WeatherItemRecyclerViewAdapter(
    private val parentActivity: ItemListActivity,
    private var items: ArrayList<WeatherItem>,
    private val twoPane: Boolean
) :
    RecyclerView.Adapter<WeatherItemRecyclerViewAdapter.ViewHolder>() {

    private val onClickListener: View.OnClickListener

    init {
        onClickListener = View.OnClickListener { v ->
            val item = v.tag as WeatherItem
            if (twoPane) {
                val fragment = ItemDetailFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(ItemDetailFragment.ARG_WEATHER_ITEM, item)
                    }
                }
                parentActivity.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit()
            } else {
                val intent = Intent(v.context, ItemDetailActivity::class.java).apply {
                    putExtra(ItemDetailFragment.ARG_WEATHER_ITEM, item)
                }
                v.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_content, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.idView.text = item.temperature
        holder.contentView.text = item.cityName

        with(holder.itemView) {
            tag = item
            setOnClickListener(onClickListener)
        }
    }

    override fun getItemCount() = items.size

    fun updateWithDiffUtil(newList: List<WeatherItem>) {
        val diffResult = DiffUtil.calculateDiff(
            WeatherItemsDiffCallback(newList, this.items), false
        )
        items.clear()
        items.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    fun update(newList: List<WeatherItem>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val idView: TextView = view.id_text
        val contentView: TextView = view.content
    }
}