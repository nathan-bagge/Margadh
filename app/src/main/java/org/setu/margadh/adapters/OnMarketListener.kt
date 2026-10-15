package org.setu.margadh.adapters

import org.setu.margadh.models.MarketModel

interface OnMarketListener {
    fun onMarketClick(market: MarketModel)
    fun onMarketLongClick(market: MarketModel): Boolean
}