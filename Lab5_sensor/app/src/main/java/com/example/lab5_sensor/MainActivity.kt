package com.example.lab5_sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.lab5_sensor.ui.theme.Lab5_sensorTheme
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        val gravity = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)
        val graFlow : Flow<FloatArray> = getGraData(gravity, sensorManager)
        val vm = ViewModelProvider(this)[ScreenOrientationViewModel::class.java]

        setContent {
            Lab5_sensorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // get gravity sensor data
                    val graReading by graFlow.collectAsStateWithLifecycle(
                        floatArrayOf(0.0f, 0.0f, 0.0f),
                        lifecycleOwner = this@MainActivity
                    )
                    Text("Gravity reading: %.5f %.5f %.5f".format(graReading[0], graReading[1], graReading[2]))

                    // get screen orientation, update to view model
                    val windowManager = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                    vm.screenOrientation.value = windowManager.defaultDisplay.rotation

                    // marble rolling in the box
                    BoxWithConstraints (
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // box boundary
                        val maxWidth = maxWidth.value - 50
                        val maxHeight = maxHeight.value - 50

                        // marble's start position
                        var position by remember { mutableStateOf(Offset(100f, 100f)) }
                        // marble's offset position, depending on screen orientation
                        vm.screenOrientation.observe(this@MainActivity){o ->
                            position = when (o) {
                                // landscape orientation
                                1 -> {
                                    Offset(
                                        position.x + graReading[1] / 2,
                                        position.y + graReading[0] / 2
                                    )
                                }
                                3 -> {
                                    Offset(
                                        position.x - graReading[1] / 2,
                                        position.y - graReading[0] / 2
                                    )
                                }
                                // portrait orientation
                                else -> {
                                    Offset(
                                        position.x - graReading[0] / 2,
                                        position.y + graReading[1] / 2
                                    )
                                }
                            }
                        }
                        // modify position if it's out of boundary
                        position = Offset(
                            position.x.coerceIn(0f, maxWidth),
                            position.y.coerceIn(0f, maxHeight)
                        )

                        marble(pos = position)
                    }
                }
            }
        }
    }
}


fun getGraData(graSensor: Sensor?, sensorManager: SensorManager) : Flow<FloatArray>{
    return channelFlow {
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event !== null) {
                    Log.e("Gravity Sensor event!", event.values.toString())
                    var success = channel.trySend(event.values.copyOf()).isSuccess
                    Log.e("success?", success.toString())
                }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                //If you care :shrug:
            }
        }
        sensorManager.registerListener(listener, graSensor, SensorManager.SENSOR_DELAY_NORMAL)

        awaitClose {
            sensorManager.unregisterListener(listener)
        }
    }
}

@Composable
fun marble(pos: Offset) {
    Box(
        modifier = Modifier
            .offset(x = pos.x.dp, y = pos.y.dp)
            .size(50.dp)
            .background(Color.Magenta),
        contentAlignment = Alignment.Center
    ){

    }
}

class ScreenOrientationViewModel : ViewModel() {
    var screenOrientation = MutableLiveData<Int>()
}

fun calculateMarblePosition(context: BoxWithConstraintsScope, startPosition:Offset, graReading: FloatArray, screenOrientation: Int): Offset {
    val maxWidth = context.maxWidth.value - 50
    val maxHeight = context.maxHeight.value - 50
//    val startPosition = Offset(100f, 100f)
    val stepX = 1/2
    val stepY = 1/2

    val updatedX = when (screenOrientation) {
        1 -> startPosition.x + graReading[1] * stepX
        3 -> startPosition.x - graReading[1] * stepX
        else -> startPosition.x - graReading[0] * stepX
    }

    val updatedY = when (screenOrientation) {
        1 -> startPosition.y + graReading[0] * stepY
        3 -> startPosition.y - graReading[0] * stepY
        else -> startPosition.y + graReading[1] * stepY
    }

    // modify position if it's out of boundary
    return Offset(
        updatedX.coerceIn(0f, maxWidth),
        updatedY.coerceIn(0f, maxHeight)
    )
}