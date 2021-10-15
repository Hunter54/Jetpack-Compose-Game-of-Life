package com.ionutv.gameoflife

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

class MainViewModel : ViewModel() {

    private val _gridState = MutableLiveData<Array<Array<Boolean>>>()
    val gridState: LiveData<Array<Array<Boolean>>>
        get() = _gridState

    fun play(gridSize : Int) = viewModelScope.launch {
        withContext(Dispatchers.Default)
        {
            while (isActive) {
                val state = _gridState.value ?: Array(gridSize){ Array(gridSize) {false} }
                delay(50)
                if (state.isEmpty()) {
                    cancel()
                }
                _gridState.postValue(getNextScreen(gridSize,state))
            }
        }
    }

    fun onGridUpdate(gridState: Array<Array<Boolean>>, cell: Pair<Int, Int>) {
        gridState[cell.first][cell.second] = !gridState[cell.first][cell.second]
        _gridState.value = gridState.copyOf()
    }

    fun resetGrid(gridSize : Int) {
        _gridState.value = Array(maxGridSize){ Array(maxGridSize) {false} }
    }

}