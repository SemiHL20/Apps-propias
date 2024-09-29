package semi.android.agecurious

import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var datePicker: DatePicker
    private lateinit var checkButton: Button

    private val calendar: Calendar = Calendar.getInstance()
    private var year = calendar.get(Calendar.YEAR)
    private var month = calendar.get(Calendar.MONTH)
    private var day = calendar.get(Calendar.DAY_OF_MONTH)

    private var selectedDate = "$day/$month/$year"


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeViews()
        setUpListeners()
    }

    private fun initializeViews() {
        datePicker = findViewById(R.id.date_of_birth)
        checkButton = findViewById(R.id.check_button)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setUpListeners() {
        checkButton.setOnClickListener {
            val intent = Intent(this, ResultsActivity::class.java)
            intent.putExtra("selectedDate", selectedDate)
            startActivity(intent)
        }

        datePicker.setOnDateChangedListener { _, _, _, _ ->
            day = datePicker.dayOfMonth
            month = datePicker.month + 1 // Meses en DatePicker van de 0 a 11
            year = datePicker.year
            selectedDate = "$day/$month/$year"
        }
    }
}