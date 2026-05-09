package com.fongmi.android.tv.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fongmi.android.tv.App
import com.fongmi.android.tv.Setting
import com.fongmi.android.tv.api.SiteApi
import com.fongmi.android.tv.api.LiveApi
import com.fongmi.android.tv.bean.*
import com.fongmi.android.tv.config.ConfigManager
import com.fongmi.android.tv.db.AppDatabase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class HomeUiState(
    val sites: List<Site> = emptyList(),
    val vodList: List<Vod> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val db = AppDatabase.get(application)

    init {
        loadConfig()
    }

    fun loadConfig() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val url = Setting.configUrl
                if (url.isNotBlank()) {
                    val core = ConfigManager.loadConfig(url)
                    core?.let {
                        _uiState.update { it.copy(sites = it.sites, isLoading = false) }
                        ConfigManager.initSpiders(it.sites)
                        _uiState.update { it.copy(sites = it.sites) }
                    } ?: _uiState.update { it.copy(isLoading = false, error = "Load config failed") }
                } else {
                    _uiState.update { it.copy(isLoading = false) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun loadHomeContent(site: Site) {
        viewModelScope.launch {
            try {
                val result = SiteApi.homeContent(site, true)
                _uiState.update { it.copy(vodList = result.list) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }
}

data class VodUiState(
    val vod: Vod? = null,
    val result: Result? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class VodViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(VodUiState())
    val uiState: StateFlow<VodUiState> = _uiState.asStateFlow()

    fun detail(site: Site, id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val result = SiteApi.detail(site, id)
                val vod = result.list.firstOrNull()
                _uiState.update { it.copy(vod = vod, result = result, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun play(site: Site, flag: String, id: List<String>, vipFlags: List<String>) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val result = SiteApi.play(site, flag, id, vipFlags)
                _uiState.update { it.copy(result = result, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}

data class SearchUiState(
    val keyword: String = "",
    val results: Map<Site, List<Vod>> = emptyMap(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    fun search(sites: List<Site>, keyword: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(keyword = keyword, isLoading = true, results = emptyMap()) }
            val results = mutableMapOf<Site, List<Vod>>()
            sites.filter { it.canSearch() }.forEach { site ->
                try {
                    val result = SiteApi.search(site, keyword, quick = true)
                    results[site] = result.list
                } catch (_: Exception) {}
            }
            _uiState.update { it.copy(results = results, isLoading = false) }
        }
    }
}

data class LiveUiState(
    val groups: List<Group> = emptyList(),
    val currentChannel: Channel? = null,
    val currentGroup: Group? = null,
    val epg: Epg? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class LiveViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(LiveUiState())
    val uiState: StateFlow<LiveUiState> = _uiState.asStateFlow()

    fun loadLive(url: String, type: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val groups = LiveApi.parse(url, type)
                _uiState.update { it.copy(groups = groups, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun selectChannel(channel: Channel) {
        _uiState.update { it.copy(currentChannel = channel) }
    }

    fun selectGroup(group: Group) {
        _uiState.update { it.copy(currentGroup = group) }
    }
}
