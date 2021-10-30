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


@Composable
fun DisplayApp(
    scaffoldState: ScaffoldState,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel

) {
    var gridSize by remember { mutableStateOf(14f) }
    var job: Job? by rememberSaveable {
        mutableStateOf(null)
    }

    Scaffold(modifier = modifier.fillMaxSize(), scaffoldState = scaffoldState, topBar = {
        TopAppBar(title = {
            Text(text = "Game of Life")
        })
    }) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Select a few boxes and then click start"
            )

            GameGrid(
                viewModel, gridSize.toInt()
            )
            { gridState, cell ->
                viewModel.onGridUpdate(gridState, cell)
            }

            Controls(onPlay = {
                job?.cancel()
                job = viewModel.play(gridSize.toInt())
            }, onStop = {
                job?.let {
                    if (it.isActive) {
                        it.cancel(CancellationException("User canceled"))
                    }
                }
            },
                onReset = {
                    job?.let {
                        if (it.isActive) {
                            it.cancel(CancellationException("User canceled"))
                        }
                    }
                    viewModel.resetGrid()
                })
            SizeSlider(gridSize = gridSize) {
                gridSize = it
            }
        }
    }
}

@Composable
fun GameGrid(
    viewModel: MainViewModel,
    gridSize: Int,
    modifier: Modifier = Modifier,
    onUpdate: (state: MutableSet<Pair<Int, Int>>, cell: Pair<Int, Int>) -> Unit
) {
    val gridState by viewModel.gridState.observeAsState(mutableSetOf())
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, modifier =
        modifier.fillMaxWidth()
    ) {
        for (row in 0..gridSize) {
            Row {
                for (col in 0..gridSize) {
                    val color = if (gridState.contains(Pair(row, col)))
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
                                gridState.plus(Pair(row, col))
                                onUpdate(gridState, Pair(row, col))
                            })

                }
            }
        }
    }
}

@Composable
fun SizeSlider(gridSize: Float, onGridSizeUpdated: (gridSize: Float) -> Unit) {
    Text(text = "GridSize is ${gridSize.toInt()}")
    Slider(value = gridSize, steps = 14, valueRange = 5f..22f,
        modifier = Modifier.fillMaxWidth(0.75f),
        onValueChange = {
            onGridSizeUpdated(it)
        })
}

@Composable
fun Controls(
    onPlay: () -> Unit,
    onStop: () -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    var playColor by remember { mutableStateOf(false) }
    var stopColor by remember { mutableStateOf(true) }
    Row(

        modifier = modifier
            .padding(all = defaultSpacerSize)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            colors = ButtonDefaults.buttonColors(if (playColor) MaterialTheme.colors.secondary else MaterialTheme.colors.primary),
            onClick = {
                playColor = true
                stopColor = false
                onPlay()
            }) {
            Text(text = "Start")
        }
        Button(
            colors = ButtonDefaults.buttonColors(if (stopColor) MaterialTheme.colors.secondary else MaterialTheme.colors.primary),
            onClick = {
                playColor = false
                stopColor = true
                onStop()
            }) {
            Text(text = "Stop")
        }

        Button(onClick = {
            playColor = false
            stopColor = true
            onReset()
        }) {
            Text(text = "Reset")
        }
    }
}

