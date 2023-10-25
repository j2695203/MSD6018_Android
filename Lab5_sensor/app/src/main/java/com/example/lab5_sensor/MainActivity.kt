package com.example.lab5_sensor

import android.annotation.SuppressLint
import android.app.Activity
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.Display
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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

        setContent {
            Lab5_sensorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // get sensor data
                    val graReading by graFlow.collectAsStateWithLifecycle(
                        floatArrayOf(0.0f, 0.0f, 0.0f),
                        lifecycleOwner = this@MainActivity
                    )
                    Text("Gravity reading: %.5f %.5f %.5f".format(graReading[0], graReading[1], graReading[2]))

                    // set marble's default position
                    var position by remember { mutableStateOf(Offset(100f, 100f)) } // default start position

                    // set boundary and marble's position
                    BoxWithConstraints (
                        modifier = Modifier.fillMaxSize()
                    ) {
                        val maxWidth = maxWidth.value - 50
                        val maxHeight = maxHeight.value - 50
                        var offsetX = position.x - graReading[0]
                        var offsetY = position.y + graReading[1]

                        // modify position if it's out of boundary
                        if (offsetX < 0 || offsetX > maxWidth || offsetY < 0 || offsetY > maxHeight) {
                            offsetX = offsetX.coerceIn(0f, maxWidth)
                            offsetY = offsetY.coerceIn(0f, maxHeight)
                        }
                        position = Offset(offsetX, offsetY)

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
