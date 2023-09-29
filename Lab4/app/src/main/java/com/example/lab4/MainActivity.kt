package com.example.lab4

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.example.lab4.ui.theme.Lab4Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.Date
import com.google.gson.Gson

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val vm: JokeViewModel by viewModels{  JokeViewModelFactory((application as JokeApplication).jokeRepository)}

        setContent {
            Lab4Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {


                    Column {
                        val vm: JokeViewModel by viewModels{  JokeViewModelFactory((application as JokeApplication).jokeRepository)}
                        val currentJoke by vm.currentJoke.observeAsState()

                        val scope = rememberCoroutineScope()
                        var job : Job? by remember {
                            mutableStateOf(null)
                        }

                        SearchArea{
                            job = scope.launch {
                                val joke = getJoke()
                                vm.checkJoke(joke.value)
                            }
//                            vm.checkJoke(it)
                        }
                        JokeDataDisplay(currentJoke)

//                        Spacer(modifier = Modifier.padding(32.dp))

                        Text("Previous Joke",
                            fontSize = 12.em,
                            lineHeight = 1.em)

                        Spacer(modifier = Modifier.padding(16.dp))

                        val allJoke by vm.allJoke.observeAsState()
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp),){
                            for(data in allJoke ?: listOf()){
                                item{
                                    JokeDataDisplay(data = data)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun JokeDataDisplay(data: JokeData?, modifier: Modifier = Modifier) {
    Surface(color=MaterialTheme.colorScheme.surface) {
        Text(
            text = if (data != null) "Date: ${data.timestamp} \n ${data.joke}\n" else "NO JOKES YET",
            modifier = modifier
        )
    }
}

@Composable
fun SearchArea(onClick: (joke: String) -> Unit){
    Row(modifier = Modifier.padding(16.dp)) {
        var joke: String by remember { mutableStateOf("default joke") }
        OutlinedTextField(
            value = joke,
            onValueChange = { joke = it },
            label = { Text("Joke") }
        )
        Button(onClick = {onClick(joke)}) {
            Text("Get Joke")
        }
    }
}

@Preview(showBackground = false)
@Composable
fun SearchAreaPreview(){
    Lab4Theme {
        SearchArea(onClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun JokeDataDisplayPreview() {
    Lab4Theme {
        JokeDataDisplay(JokeData(Date(), "Salt Lake"))
    }
}

suspend fun getJoke() : JString {
    return withContext(Dispatchers.IO) {

        val url: Uri = Uri.Builder().scheme("https")
            .authority("api.chucknorris.io")
            .appendPath("jokes")
            .appendPath("random").build()


        val conn = URL(url.toString()).openConnection() as HttpURLConnection
        conn.connect()
        val gson = Gson()
        val result = gson.fromJson(
            InputStreamReader(conn.inputStream, "UTF-8"),
            JString::class.java
        )
        Log.e("data!", gson.toJson(result).toString())
        result
    }
}

data class JString (var value: String)


