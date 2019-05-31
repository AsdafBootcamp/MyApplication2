package com.narims.myapplication.presentation.mvp.itemlist

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.jakewharton.rxbinding2.widget.RxTextView
import com.narims.myapplication.R
import com.narims.myapplication.model.WeatherItem
import com.narims.myapplication.presentation.adapter.WeatherItemRecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_item_list.*
import kotlinx.android.synthetic.main.item_list.*


/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [ItemDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class ItemListActivity : MvpAppCompatActivity(), ItemListView {
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false
    private lateinit var adapter: WeatherItemRecyclerViewAdapter
    @InjectPresenter
    internal lateinit var itemListPresenter: ItemListPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)

        setSupportActionBar(toolbar)
        toolbar.title = title

        if (item_detail_container != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }
        adapter = WeatherItemRecyclerViewAdapter(
            this,
            ArrayList(),
            twoPane
        )
        setupRecyclerView(item_list)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = adapter
    }

    override fun setSearchQuery(query: String) {
        etSearch.setText(query)
        etSearch.requestFocus()
        etSearch.setSelection(query.length)
    }

    override fun updateList(newList: List<WeatherItem>) {
        adapter.update(newList)
    }

    override fun updateListWithDiff(newList: List<WeatherItem>) {
        adapter.updateWithDiffUtil(newList)
    }

    override fun registerTextChanges() {
        itemListPresenter.setQueryTextChanges(RxTextView.textChanges(etSearch))
    }
}
