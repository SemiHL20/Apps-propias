package semi.android.agecurious

import android.annotation.SuppressLint
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.DecimalFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class ResultsActivity : AppCompatActivity() {

    private lateinit var yearsResult: TextView
    private lateinit var monthsResult: TextView
    private lateinit var weeksResult: TextView
    private lateinit var daysResult: TextView
    private lateinit var hoursResult: TextView
    private lateinit var minutesResult: TextView
    private lateinit var secondsResult: TextView
    private lateinit var againButton: Button

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private var seconds: Long = 0
    private var minutes: Float = 0.0F
    private var hours: Float = 0.0F
    private var days: Float = 0.0F
    private var weeks: Float = 0.0F
    private var months: Float = 0.0F
    private var years: Float = 0.0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.results_activity)

        val selectedDate = intent.getStringExtra("selectedDate")

        val date = dateFormat.parse(selectedDate)

        initializeViews()
        setUpListeners()

        calculateAge(date)
    }

    @SuppressLint("DefaultLocale")
    private fun initializeViews() {
        yearsResult = findViewById(R.id.years_result)
        monthsResult = findViewById(R.id.months_result)
        weeksResult = findViewById(R.id.weeks_result)
        daysResult = findViewById(R.id.days_result)
        hoursResult = findViewById(R.id.hours_result)
        minutesResult = findViewById(R.id.minutes_result)
        secondsResult = findViewById(R.id.seconds_result)
        againButton = findViewById(R.id.again_button)

    }

    private fun setUpListeners() {
        againButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    @SuppressLint("DefaultLocale")
    fun calculateAge(date: Date) {
        val currentDate = Calendar.getInstance()
        val birthDate = Calendar.getInstance()
        birthDate.time = date

        val diffInMillis = currentDate.timeInMillis - birthDate.timeInMillis
        seconds = TimeUnit.MILLISECONDS.toSeconds(diffInMillis)
        minutes = seconds / 60.0F
        hours = minutes / 60.0F
        days = hours / 24.0F
        weeks = days / 7.0F
        months = days / 30.416666F
        years = months / 12.0F

        val decimalFormat = DecimalFormat("#,###.##")
        yearsResult.text = decimalFormat.format(years)
        monthsResult.text = decimalFormat.format(months)
        weeksResult.text = decimalFormat.format(weeks)
        daysResult.text = decimalFormat.format(days)
        hoursResult.text = decimalFormat.format(hours)
        minutesResult.text = decimalFormat.format(minutes)
        secondsResult.text = decimalFormat.format(seconds)
    }
}