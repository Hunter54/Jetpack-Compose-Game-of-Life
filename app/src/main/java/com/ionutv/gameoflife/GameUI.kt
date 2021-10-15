package com.ionutv.gameoflife

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ionutv.gameoflife.ui.theme.boxPadding
import com.ionutv.gameoflife.ui.theme.defaultSpacerSize
import kotlinx.coroutines.*
import java.util.concurrent.CancellationException

val maxGridSize = 22

@Composable
fun DisplayApp(
    scaffoldState: ScaffoldState,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = androidx.lifecycle.viewmodel.compose.viewModel()

) {
    var gridSize by remember { mutableStateOf(14f) }

    Scaffold(modifier = modifier.fillMaxSize(), scaffoldState = scaffoldState, topBar = {
        TopAppBar(title = {
            Text(text = "Game of Life")
        })
    }) {
        Box(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(),
        ) {
            Text(text = "Select a few boxes and then click start",modifier = Modifier
                .height(defaultSpacerSize)
                .align(Alignment.TopCenter)
                .offset(y = defaultSpacerSize))

            GameGrid(
                viewModel, gridSize.toInt(),
                Modifier
                    .align(Alignment.Center)
                    .offset(y = -defaultSpacerSize)
            )
            { gridState, cell ->
                viewModel.onGridUpdate(gridState, cell)
            }
            Column(
                Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = -defaultSpacerSize),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Controls( onPlay = {
                    viewModel.play(gridSize.toInt())
                },
                onReset ={
                    viewModel.resetGrid(gridSize.toInt())
                })
                SizeSlider(gridsize = gridSize) {
                    gridSize = it
                }
            }
        }
    }
}

@Composable
fun GameGrid(
    viewModel: MainViewModel,
    gridSize: Int,
    modifier: Modifier = Modifier,
    onUpdate: (state: Array<Array<Boolean>>, cell: Pair<Int, Int>) -> Unit
) {
    val gridState by viewModel.gridState.observeAsState(Array(maxGridSize){ Array(maxGridSize) {false} })
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, modifier =
        modifier.fillMaxWidth()
    ) {
        for (row in 0 until gridSize) {
            Row {
                for (col in 0 until gridSize) {
                    val color = if (gridState[row][col])
                        MaterialTheme.colors.secondary
                    else MaterialTheme.colors.primaryVariant

                    Box(
                        Modifier
                            .fillMaxWidth(1f / gridSize)
                            .weight(1f)
                            .aspectRatio(1f)
                            .background(color)
                            .border(boxPadding, Color.Black)
                            .clickable {
                                onUpdate(gridState, Pair(row, col))
                            })

                }
            }
        }
    }
}

@Composable
fun SizeSlider(gridsize: Float, onGridSizeUpdated: (gridSize: Float) -> Unit) {
    Text(text = "GridSize is ${gridsize.toInt()}")
    Slider(value = gridsize,steps = 14, valueRange = 5f..22f,
        modifier = Modifier.fillMaxWidth(0.75f),
        onValueChange = {
            onGridSizeUpdated(it)
        })
}

@Composable
fun Controls( onPlay: () -> Job, modifier: Modifier = Modifier,onReset : () -> Unit) {
    var job: Job? by rememberSaveable {
        mutableStateOf(null)
    }
    Row(

        modifier = modifier
            .padding(all = defaultSpacerSize)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(onClick = {
            job?.cancel()
            job = onPlay()
        }) {
            Text(text = "Start")
        }
        Button(onClick = {
            job?.let {
                if (it.isActive) {
                    it.cancel(CancellationException("User canceled"))
                }
            }
        }) {
            Text(text = "Stop")
        }

        Button(onClick = {
            job?.let {
                if (it.isActive) {
                    it.cancel(CancellationException("User canceled"))
                }
            }
            onReset()
        }) {
            Text(text = "Reset")
        }
    }
}

