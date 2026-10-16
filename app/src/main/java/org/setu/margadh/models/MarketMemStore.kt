package org.setu.margadh.models

import java.util.concurrent.atomic.AtomicLong
import timber.log.Timber

class MarketMemStore : MarketStore {

    private val markets = ArrayList<MarketModel>()
    private val lastId = AtomicLong(0L)

    override fun findAll(): List<MarketModel> = markets

    override fun create(market: MarketModel) {
        market.id = lastId.incrementAndGet()
        markets.add(market)
        Timber.d("Store: created id=${market.id}")
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
        return true
    }

    override fun delete(id: Long): Boolean {
        val removed = markets.removeIf { it.id == id }
        Timber.d("Store: delete id=$id success=$removed")
        return removed
    }

    override fun findOne(id: Long): MarketModel? = markets.find { it.id == id }
}