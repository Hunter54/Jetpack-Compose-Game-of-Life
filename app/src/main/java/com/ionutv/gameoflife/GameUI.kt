package com.ionutv.gameoflife

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ionutv.gameoflife.ui.theme.boxPadding
import com.ionutv.gameoflife.ui.theme.defaultSpacerSize
import kotlinx.coroutines.*
import java.util.concurrent.CancellationException

const val gridsize = 14

@Composable
fun DisplayApp(scaffoldState: ScaffoldState, viewModel: MainViewModel, modifier: Modifier = Modifier) {
    val gridState by viewModel.gridState.observeAsState(Array(gridsize){ Array(gridsize) {false} })

    Scaffold(modifier = modifier.fillMaxSize(), scaffoldState = scaffoldState, topBar = {
        TopAppBar(title = {
            Text(text = "Game of Life")
        })
    }) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(defaultSpacerSize))
            Text(text = "Select a few boxes and then click start")
            Spacer(modifier = Modifier.height(defaultSpacerSize))
            GameGrid(
                state = gridState
            )
            { row, col ->
                viewModel.onGridUpdate(gridState,row ,col)
            }
            Controls(gridState,viewModel)

        }
    }
}

@Composable
fun GameGrid(state: Array<Array<Boolean>>, onUpdate: (row: Int, col : Int) -> Unit) {

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        for (row in 0 until gridsize) {
            Row {
                for (col in 0 until gridsize) {
                    val color = if (state[row] [col])
                        MaterialTheme.colors.secondary
                    else MaterialTheme.colors.primaryVariant

                    Divider(
                        thickness = defaultSpacerSize,
                        color = color,
                        modifier = Modifier
                            .padding(all = boxPadding)
                            .width(defaultSpacerSize)
                            .clickable {
                                onUpdate(row,col)
                            }
                    )
                }
            }
        }
    }
}

@Composable
fun Controls(state: Array<Array<Boolean>>,viewModel: MainViewModel,modifier: Modifier = Modifier) {
    var job : Job= remember{
        Job()
    }
    Row(
        modifier = modifier.padding(all = defaultSpacerSize),
        horizontalArrangement = Arrangement.Center
    ) {
        Button(onClick = {
            job.cancel()
            job = viewModel.play(state)
        }) {
            Text(text = "Start")
        }
        Spacer(Modifier.width(defaultSpacerSize))
        Button(onClick = {
            job.let {
                if (it.isActive) {
                    job.cancel(CancellationException("User canceled"))
                }
            }
        }) {
            Text(text = "Stop")
        }

        Spacer(Modifier.width(defaultSpacerSize))
        Button(onClick = {
            job.let {
                if (it.isActive) {
                    job.cancel(CancellationException("User canceled"))
                }
            }
            viewModel.resetGrid()
        }) {
            Text(text = "Reset")
        }
    }
}

