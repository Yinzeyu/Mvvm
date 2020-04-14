package com.yzy.example.component.main


import com.yzy.baselibrary.base.BaseLiveData
import com.yzy.baselibrary.base.BaseViewModel
import com.yzy.example.repository.GankRepository
import com.yzy.example.repository.bean.BaseDataBean
import com.yzy.example.repository.bean.GankAndroidBean

class DynViewModel : BaseViewModel<GankRepository>() {
    private var page = 1
    private var pageSize = 20
    private var articleBean: MutableList<GankAndroidBean> = mutableListOf()
    private val _bannerAndArticleResult: BaseLiveData<BaseUiModel<BaseDataBean<MutableList<GankAndroidBean>>>> =
        BaseLiveData()
    val uiState: BaseLiveData<BaseUiModel<BaseDataBean<MutableList<GankAndroidBean>>>> get() = _bannerAndArticleResult
    fun getAndroidSuspend(isRefresh: Boolean = false) {

        emitArticleUiState(true)
        val tempPage = if (isRefresh) 1 else page + 1
        launchOnlyresult1({ repository.getAndroidSuspend(pageSize, tempPage) }, {
            if (isRefresh) {
                articleBean.clear()
            }
            articleBean.addAll(it)
            val baseBean = BaseDataBean(hasMore = it.size < pageSize, bean = articleBean)
            emitArticleUiState(showLoading = false, success = baseBean)
            page++
        })
    }

    private fun emitArticleUiState(
        showLoading: Boolean = false,
        isRefresh: Boolean = false,
        success: BaseDataBean<MutableList<GankAndroidBean>>? = null
    ) {
        val uiModel = BaseUiModel(showLoading = showLoading, success = success)
        _bannerAndArticleResult.update(uiModel)
    }
}