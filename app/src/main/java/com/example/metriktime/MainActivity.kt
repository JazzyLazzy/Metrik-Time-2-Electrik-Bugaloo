package com.example.metriktime

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.metriktime.databinding.ActivityMainBinding
import com.example.metriktime.ui.theme.MetrikTimeTheme
import java.text.SimpleDateFormat
import java.util.TimeZone
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor

class MainActivity : ComponentActivity() {

    private lateinit var binding: ActivityMainBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*setContent {
            MetrikTimeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }*/
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val uiMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        when (uiMode) {
            Configuration.UI_MODE_NIGHT_YES -> {
                // Dark mode is active
                binding.container.setBackgroundColor(getColor(R.color.background_dark))
                binding.metrikTime.setTextColor(getColor(R.color.background_light))
            }
            else -> {
                // Light mode is active or unspecified
                binding.container.setBackgroundColor(getColor(R.color.background_light))
                binding.metrikTime.setTextColor(getColor(R.color.background_dark))
            }
        }
        val targetDate = "2024-12-21"
        val targetTime = "01:20:00"
        val targetTimeZone = "America/Los_Angeles"
        val targetDateTimeString = "$targetDate $targetTime"
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        dateFormat.timeZone = TimeZone.getTimeZone(targetTimeZone)
        val targetTimeUnixSeconds = dateFormat.parse(targetDateTimeString)?.time?.div(1000) ?: 0
        val handler = Handler(Looper.getMainLooper())
        val runnable: Runnable = object : Runnable {
            override fun run() {
                // do something
                val currentTimeUnixSeconds = System.currentTimeMillis() / 1000
                val timeDiff = currentTimeUnixSeconds - targetTimeUnixSeconds
                val metrikTimeDiff = timeDiff / 3.1556925
                var metrikYear = 0.0
                var metrikDay = 0.0
                var metrikHour = 0.0
                var metrikMinute = 0.0
                var metrikSecond = 0.0
                if (metrikTimeDiff < 0){
                    metrikYear = ceil(metrikTimeDiff / 10000000)
                    metrikDay = ceil(metrikTimeDiff / 100000) - 100 * metrikYear;
                    metrikHour = ceil(metrikTimeDiff / 10000) - 1000 * metrikYear - 10 * metrikDay;
                    metrikMinute = ceil(metrikTimeDiff / 100) - 100000 * metrikYear - 1000 * metrikDay - 100 * metrikHour;
                    metrikSecond = ceil(metrikTimeDiff / 1.0) - 10000000 * metrikYear - 100000 * metrikDay - 10000 * metrikHour - 100 * metrikMinute
                }else{
                    metrikYear = floor(metrikTimeDiff / 10000000)
                    metrikDay = floor(metrikTimeDiff / 100000) - 100 * metrikYear;
                    metrikHour = floor(metrikTimeDiff / 10000) - 1000 * metrikYear - 10 * metrikDay;
                    metrikMinute = floor(metrikTimeDiff / 100) - 100000 * metrikYear - 1000 * metrikDay - 100 * metrikHour;
                    metrikSecond = floor(metrikTimeDiff / 1.0) - 10000000 * metrikYear - 100000 * metrikDay - 10000 * metrikHour - 100 * metrikMinute
                }

                val metrikYearInt = metrikYear.toInt()
                val metrikDayInt = abs(metrikDay).toInt();
                val metrikSecondInt = abs(metrikSecond).toInt();
                val metrikMinuteInt = abs(metrikMinute).toInt();
                val metrikHourInt = abs(metrikHour).toInt();

                binding.metrikTime.text =
                    "$metrikYearInt-$metrikDayInt $metrikHourInt.$metrikMinuteInt.$metrikSecondInt"
                handler.postDelayed(this, 1000L) // 1 second delay
            }
        }
        handler.post(runnable)
    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MetrikTimeTheme {
        Greeting("Android")
    }
}