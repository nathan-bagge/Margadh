package org.setu.margadh.activities

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.setu.margadh.AppData

class MainActivity : AppCompatActivity() {

    private lateinit var listLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createUserInterface()
    }

    override fun onResume() {
        super.onResume()
        if (::listLayout.isInitialized) {
            displayMarkets()
        }
    }

    private fun createUserInterface() {

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 32, 32, 32)
        }

        val title = TextView(this).apply {
            text = "Margadh"
            textSize = 28f
            gravity = Gravity.CENTER
        }

        val addButton = Button(this).apply {
            text = "Add Market"
            setOnClickListener {
                val intent = Intent(this@MainActivity, AddEditActivity::class.java)
                startActivity(intent)
            }
        }

        listLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }

        root.addView(title)
        root.addView(addButton)
        root.addView(listLayout)

        setContentView(root)

        displayMarkets()
    }

    private fun displayMarkets() {

        listLayout.removeAllViews()

        val markets = AppData.markets.findAll()

        if (markets.isEmpty()) {
            val emptyText = TextView(this).apply {
                text = "No markets yet."
                textSize = 18f
                setPadding(0, 40, 0, 40)
            }
            listLayout.addView(emptyText)
            return
        }

        for (market in markets) {

            val marketLayout = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(0, 20, 0, 20)
            }

            val marketName = TextView(this).apply {
                text = "${market.id}: ${market.name}"
                textSize = 20f
            }

            val marketDetails = TextView(this).apply {
                text = "${market.county} | Open: ${market.openingDays}"
                textSize = 16f
            }

            val marketDescription = TextView(this).apply {
                text = market.description
                textSize = 16f
            }

            val coordinates = TextView(this).apply {
                text = "Lat: ${market.lat}, Lng: ${market.lng}"
                textSize = 14f
            }

            val editButton = Button(this).apply {
                text = "Edit"
                setOnClickListener {
                    val intent = Intent(this@MainActivity, AddEditActivity::class.java)
                    intent.putExtra("id", market.id)
                    startActivity(intent)
                }
            }

            val deleteButton = Button(this).apply {
                text = "Delete"
                setOnClickListener {
                    AppData.markets.delete(market.id)
                    displayMarkets()
                }
            }

            marketLayout.addView(marketName)
            marketLayout.addView(marketDetails)
            marketLayout.addView(marketDescription)
            marketLayout.addView(coordinates)
            marketLayout.addView(editButton)
            marketLayout.addView(deleteButton)

            listLayout.addView(marketLayout)
        }
    }
}