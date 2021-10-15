package com.ionutv.gameoflife

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

class MainViewModel : ViewModel() {

    private val _gridState = MutableLiveData<MutableSet<Pair<Int, Int>>>(mutableSetOf())
    val gridState: LiveData<MutableSet<Pair<Int, Int>>>
        get() = _gridState

    fun play(gridSize : Int) = viewModelScope.launch {
        withContext(Dispatchers.Default)
        {
            while (isActive) {
                val state = _gridState.value ?: mutableSetOf()
                delay(50)
                if (state.isEmpty()) {
                    cancel()
                }
                _gridState.postValue(getNextScreen(state,gridSize))
            }
        }
    }

    fun onGridUpdate(gridState: MutableSet<Pair<Int, Int>>, cell: Pair<Int, Int>) {
        _gridState.value = mutableSetOf<Pair<Int, Int>>()
            .apply {
                addAll(gridState)
                if (contains(cell)) {
                    remove(cell)
                } else {
                    add(cell)
                }
            }
    }

    fun resetGrid() {
        _gridState.value = mutableSetOf()
    }

}