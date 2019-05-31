package com.narims.myapplication.presentation.mvp.itemlist

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.narims.myapplication.App
import com.narims.myapplication.di.DaggerMainComponent
import com.narims.myapplication.di.DataModule
import com.narims.myapplication.repo.WeatherRepo
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@InjectViewState
class ItemListPresenter : MvpPresenter<ItemListView>() {
    @Inject
    internal lateinit var weatherRepo: WeatherRepo
    private var getCachedItemsDisposable: Disposable? = null
    private var getQueryChangesDisposable: Disposable? = null
    private var getQueryCacheDisposable: Disposable? = null

    private var isFirstCacheFetch = true

    init {
        DaggerMainComponent.builder()
            .dataModule(DataModule(App.instance))
            .build()
            .inject(this)
    }

    companion object {
        const val INPUT_CHANGES_TIMEOUT = 300L
        const val DEFAULT_COUNTRY = "Kazakhstan"
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        fetchWeatherCache()
        getQueryCacheDisposable = weatherRepo.getQueryCache()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                viewState.setSearchQuery(it)
                viewState.registerTextChanges()
            }, {
                viewState.registerTextChanges()
            })
    }

    fun setQueryTextChanges(observable: Observable<CharSequence>) {
        getQueryChangesDisposable?.dispose()
        getQueryChangesDisposable = observable.debounce(INPUT_CHANGES_TIMEOUT, TimeUnit.MILLISECONDS)
            .subscribe {
                if (it.length > 2)
                    queryWeather(it.toString())
                else
                    weatherRepo.removeAll()
                        .observeOn(Schedulers.io()).subscribe()
            }
    }

    private fun fetchWeatherCache() {
        getCachedItemsDisposable = weatherRepo
            .getCachedItems()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (!isFirstCacheFetch)
                    viewState.updateListWithDiff(it)
                else {
                    viewState.updateList(it)
                    if (it.isNotEmpty()) {
                        val item = it[0]
                        val cacheQuery = item.queryCache
                        cacheQuery?.let { query ->
                            queryWeather(query)
                        }
                    }
                    isFirstCacheFetch = false
                }
            }
    }


    override fun onDestroy() {
        getCachedItemsDisposable?.dispose()
        getQueryChangesDisposable?.dispose()
        getQueryCacheDisposable?.dispose()
        super.onDestroy()
    }

    private fun queryWeather(query: String) {
        weatherRepo.queryWeatherItem("$query, $DEFAULT_COUNTRY")
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

}