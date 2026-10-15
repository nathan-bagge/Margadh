package org.setu.margadh.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.setu.margadh.R
import android.content.Intent
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.setu.margadh.AppData
import org.setu.margadh.adapters.MarketAdapter
import org.setu.margadh.models.MarketModel
import org.setu.margadh.adapters.OnMarketListener

class MarketListActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MarketAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_market_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.marketsRecyclerView)

        adapter = MarketAdapter(
            markets = AppData.markets.findAll(),
            listener = object : OnMarketListener {
                override fun onMarketClick(market: MarketModel) {
                    editMarket(market)
                }

                override fun onMarketLongClick(market: MarketModel): Boolean {
                    deleteMarket(market)
                    return true
                }
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        findViewById<Button>(R.id.returnButton).setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        if (::adapter.isInitialized) {
            adapter.updateMarkets(AppData.markets.findAll())
        }
    }

    private fun editMarket(market: MarketModel) {
        val intent = Intent(this, AddEditActivity::class.java)
        intent.putExtra("id", market.id)
        startActivity(intent)
    }

    private fun deleteMarket(market: MarketModel) {
        AppData.markets.delete(market.id)
        adapter.updateMarkets(AppData.markets.findAll())
    }
}
