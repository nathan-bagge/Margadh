package org.setu.margadh.models

interface MarketStore {
    fun findAll(): List<MarketModel>
    fun create(market: MarketModel)
    fun update(market: MarketModel): Boolean
    fun delete(id: Long): Boolean
    fun findOne(id: Long): MarketModel?
}