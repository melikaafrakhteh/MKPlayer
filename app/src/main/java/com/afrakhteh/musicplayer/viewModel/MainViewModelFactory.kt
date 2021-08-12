package com.afrakhteh.musicplayer.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.afrakhteh.musicplayer.model.repository.MusicRepository
import com.afrakhteh.musicplayer.model.repository.MusicRepositoryImpl

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(val repository: MusicRepositoryImpl) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainActivityViewModel(repository) as T
    }
}