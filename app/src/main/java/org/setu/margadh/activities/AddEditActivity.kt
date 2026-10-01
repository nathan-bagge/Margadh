package org.setu.margadh.activities

import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.setu.margadh.AppData
import org.setu.margadh.models.MarketModel

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

        createUserInterface()

        editingId = intent.getLongExtra("id", -1L)

        if (editingId != -1L) {
            loadExistingMarket(editingId)
        }
    }

    private fun createUserInterface() {

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 32, 32, 32)
        }

        nameInput = EditText(this).apply { hint = "Market name" }
        countyInput = EditText(this).apply { hint = "County" }
        daysInput = EditText(this).apply { hint = "Opening days (e.g. Sat 9am-2pm)" }
        descriptionInput = EditText(this).apply { hint = "Description" }

        // SIGNED lets you type a minus sign: Irish longitudes are negative
        latInput = EditText(this).apply {
            hint = "Latitude (e.g. 52.2593)"
            inputType = InputType.TYPE_CLASS_NUMBER or
                    InputType.TYPE_NUMBER_FLAG_DECIMAL or
                    InputType.TYPE_NUMBER_FLAG_SIGNED
        }

        lngInput = EditText(this).apply {
            hint = "Longitude (e.g. -7.1101)"
            inputType = InputType.TYPE_CLASS_NUMBER or
                    InputType.TYPE_NUMBER_FLAG_DECIMAL or
                    InputType.TYPE_NUMBER_FLAG_SIGNED
        }

        val saveButton = Button(this).apply {
            text = "Save"
            setOnClickListener { saveMarket() }
        }

        val cancelButton = Button(this).apply {
            text = "Cancel"
            setOnClickListener { finish() }
        }

        root.addView(nameInput)
        root.addView(countyInput)
        root.addView(daysInput)
        root.addView(descriptionInput)
        root.addView(latInput)
        root.addView(lngInput)
        root.addView(saveButton)
        root.addView(cancelButton)

        setContentView(root)
    }

    private fun loadExistingMarket(id: Long) {

        val market = AppData.markets.findOne(id)

        if (market == null) {
            Toast.makeText(this, "Market not found", Toast.LENGTH_SHORT).show()
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
            nameInput.error = "Name is required"
            return
        }

        val lat = latInput.text.toString().toDoubleOrNull()
        if (lat == null) {
            latInput.error = "Enter a valid number"
            return
        }

        val lng = lngInput.text.toString().toDoubleOrNull()
        if (lng == null) {
            lngInput.error = "Enter a valid number"
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
            Toast.makeText(this, "Market created", Toast.LENGTH_SHORT).show()
        } else {
            AppData.markets.update(market.copy(id = editingId))
            Toast.makeText(this, "Market updated", Toast.LENGTH_SHORT).show()
        }

        finish()
    }
}