//References:
//- Android Developers, Set up the app bar: https://developer.android.com/develop/ui/views/components/appbar/setting-up
//- Android Developers, Add menus: https://developer.android.com/develop/ui/views/components/menus
//- Android Developers, Principles of navigation: https://developer.android.com/guide/navigation/principles
//- Android Developers, Dialogs: https://developer.android.com/develop/ui/views/components/dialogs

package org.setu.margadh.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.setu.margadh.R
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.setu.margadh.AppData
import org.setu.margadh.adapters.MarketAdapter
import org.setu.margadh.models.MarketModel
import org.setu.margadh.adapters.OnMarketListener
import androidx.appcompat.app.AlertDialog
import timber.log.Timber
import android.view.Menu
import android.view.MenuItem

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
                    confirmDelete(market)
                    return true
                }
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Use the layout's MaterialToolbar as the app bar "Set up the app bar"
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setTitle(R.string.markets_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onResume() {
        super.onResume()
        if (::adapter.isInitialized) {
            adapter.updateMarkets(AppData.markets.findAll())
            Timber.d("Showing ${AppData.markets.findAll().size} markets")
        }
    }

    // Options menu inflated from XML and handled by item id "Add menus"
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_market_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.item_add -> {
                Timber.i("Add tapped from toolbar")
                startActivity(Intent(this, AddEditActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    // Up returns to the existing previous screen, behaving the same as Back to menu "Principles of navigation"
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun editMarket(market: MarketModel) {
        Timber.d("Editing market id=${market.id}, name=${market.name}")
        val intent = Intent(this, AddEditActivity::class.java)
        intent.putExtra("id", market.id)
        startActivity(intent)
    }

    private fun deleteMarket(market: MarketModel) {
        AppData.markets.delete(market.id)
        Timber.i("Deleted market id=${market.id}")
        adapter.updateMarkets(AppData.markets.findAll())
    }

    // AlertDialog.Builder pattern from Android Developers, "Dialogs"
    private fun confirmDelete(market: MarketModel) {
        Timber.d("Asking to delete id=${market.id}")
        AlertDialog.Builder(this)
            .setTitle(R.string.delete_title)
            .setMessage(getString(R.string.delete_message, market.name))
            .setPositiveButton(R.string.delete) { _, _ ->
                deleteMarket(market)
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }
}
