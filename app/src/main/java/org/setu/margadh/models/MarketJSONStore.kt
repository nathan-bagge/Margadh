//References:
//- Gson User Guide: https://github.com/google/gson/blob/main/UserGuide.md
//- Android "Access app-specific files": https://developer.android.com/training/data-storage/app-specific

package org.setu.margadh.models

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import timber.log.Timber
import java.io.File

private const val JSON_FILE = "markets.json"

class MarketJSONStore(context: Context) : MarketStore {

    // Pretty printing keeps the JSON file human-readable (Gson User Guide: "Compact Vs. Pretty Printing")
    private val gson: Gson = GsonBuilder().setPrettyPrinting().create()
    // TypeToken keeps the List<MarketModel> type so Gson can rebuild MarketModel objects (Gson User Guide: "Serializing and Deserializing Generic Types")
    private val listType = object : TypeToken<ArrayList<MarketModel>>() {}.type
    // App-specific internal storage (Android Developers: "Access app-specific files")
    private val file = File(context.filesDir, JSON_FILE)

    private var markets = ArrayList<MarketModel>()

    init {
        if (file.exists()) {
            deserialize()
        }
    }

    override fun findAll(): List<MarketModel> = markets


    override fun findOne(id: Long): MarketModel? = markets.find { it.id == id }

    override fun create(market: MarketModel) {
        // Next id = highest saved id + 1, so ids stay unique after the app restarts
        market.id = (markets.maxOfOrNull { it.id } ?: 0L) + 1
        markets.add(market)
        serialize()
        Timber.d("JSON store: created id=${market.id}")
    }

    override fun update(market: MarketModel): Boolean {
        val found = findOne(market.id) ?: return false
        found.name = market.name
        found.description = market.description
        found.county = market.county
        found.openingDays = market.openingDays
        found.openingHours = market.openingHours
        found.lat = market.lat
        found.lng = market.lng
        serialize()
        return true
    }

    override fun delete(id: Long): Boolean {
        val removed = markets.removeIf { it.id == id }
        if (removed) serialize()
        Timber.d("JSON store: delete id=$id success=$removed")
        return removed
    }

    private fun serialize() {
        file.writeText(gson.toJson(markets, listType))
        Timber.d("JSON store: saved ${markets.size} markets")
    }

    private fun deserialize() {
        try {
            markets = gson.fromJson(file.readText(), listType) ?: ArrayList()
            Timber.i("JSON store: loaded ${markets.size} markets")
        } catch (e: Exception) {
            Timber.e(e, "JSON store: could not read $JSON_FILE, starting empty")
            markets = ArrayList()
        }
    }
}

