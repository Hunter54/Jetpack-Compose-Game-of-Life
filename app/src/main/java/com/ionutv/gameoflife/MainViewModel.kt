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

    fun play(state: Array<Array<Boolean>>) = viewModelScope.launch {
        while (true) {
            delay(100)
            if (state.isEmpty()) {
                cancel()
            }
            _gridState.value = getNextScreen(state)
        }
    }

    fun onGridUpdate(gridState : Array<Array<Boolean>>,row: Int, col: Int) {
        val grid = gridState.copyOf()
        grid[row][col] = !grid[row][col]
        _gridState.value = grid
    }

    fun resetGrid() {
        _gridState.value = Array(gridsize){ Array(gridsize) {false} }
    }

}