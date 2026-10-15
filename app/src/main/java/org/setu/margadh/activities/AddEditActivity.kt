package org.setu.margadh.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.setu.margadh.AppData
import org.setu.margadh.R
import org.setu.margadh.models.MarketModel
import timber.log.Timber

class AddEditActivity : AppCompatActivity() {

    private lateinit var nameInput: EditText
    private lateinit var countyInput: EditText
    private lateinit var daysInput: EditText
    private lateinit var descriptionInput: EditText
    private lateinit var latInput: EditText
    private lateinit var lngInput: EditText

    private var editingId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_edit)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        nameInput = findViewById(R.id.nameInput)
        countyInput = findViewById(R.id.countyInput)
        daysInput = findViewById(R.id.daysInput)
        descriptionInput = findViewById(R.id.descriptionInput)
        latInput = findViewById(R.id.latInput)
        lngInput = findViewById(R.id.lngInput)

        findViewById<Button>(R.id.saveButton).setOnClickListener { saveMarket() }
        findViewById<Button>(R.id.cancelButton).setOnClickListener { finish() }

        editingId = intent.getLongExtra("id", -1L)

        if (editingId != -1L) {
            findViewById<TextView>(R.id.formTitle).text = getString(R.string.edit_market)
            loadExistingMarket(editingId)
        }
    }

    private fun loadExistingMarket(id: Long) {

        val market = AppData.markets.findOne(id)

        if (market == null) {
            Timber.w("Market id=$id not found")
            Toast.makeText(this, R.string.market_not_found, Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        nameInput.setText(market.name)
        countyInput.setText(market.county)
        daysInput.setText(market.openingDays)
        descriptionInput.setText(market.description)
        latInput.setText(market.lat.toString())
        lngInput.setText(market.lng.toString())
    }

    private fun saveMarket() {

        val name = nameInput.text.toString().trim()

        if (name.isEmpty()) {
            Timber.w("Save blocked: name empty")
            nameInput.error = getString(R.string.error_name_required)
            return
        }

        val lat = latInput.text.toString().toDoubleOrNull()

        if (lat == null) {
            Timber.w("Save blocked: invalid lat")
            latInput.error = getString(R.string.error_invalid_number)
            return
        }

        val lng = lngInput.text.toString().toDoubleOrNull()

        if (lng == null) {
            Timber.w("Save blocked: invalid lng")
            lngInput.error = getString(R.string.error_invalid_number)
            return
        }

        val market = MarketModel(
            name = name,
            county = countyInput.text.toString().trim(),
            openingDays = daysInput.text.toString().trim(),
            description = descriptionInput.text.toString().trim(),
            lat = lat,
            lng = lng
        )

        if (editingId == -1L) {
            AppData.markets.create(market)
            Timber.i("Created market id=${market.id}, name=${market.name}")
            Toast.makeText(this, R.string.market_created, Toast.LENGTH_SHORT).show()
        } else {
            AppData.markets.update(market.copy(id = editingId))
            Timber.i("Updated market id=$editingId")
            Toast.makeText(this, R.string.market_updated, Toast.LENGTH_SHORT).show()
        }

        finish()
    }
}