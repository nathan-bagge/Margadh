package org.setu.margadh.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.setu.margadh.R
import org.setu.margadh.models.MarketModel

class MarketAdapter(
    private var markets: List<MarketModel>,
    private val onEdit: (MarketModel) -> Unit,
    private val onDelete: (MarketModel) -> Unit
) : RecyclerView.Adapter<MarketAdapter.MarketViewHolder>() {

    class MarketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameText: TextView = itemView.findViewById(R.id.nameText)
        val detailsText: TextView = itemView.findViewById(R.id.detailsText)
        val descriptionText: TextView = itemView.findViewById(R.id.descriptionText)
        val coordinatesText: TextView = itemView.findViewById(R.id.coordinatesText)
        val editButton: Button = itemView.findViewById(R.id.editButton)
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MarketViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.item_market,
                parent,
                false)
        return MarketViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MarketViewHolder,
        position: Int) {
        val market = markets[position]

        holder.nameText.text = "${market.id}: ${market.name}"
        holder.detailsText.text = "${market.county} | Open: ${market.openingDays}"
        holder.descriptionText.text = market.description
        holder.coordinatesText.text = "Lat: ${market.lat}, Lng: ${market.lng}"

        holder.editButton.setOnClickListener { onEdit(market) }
        holder.deleteButton.setOnClickListener { onDelete(market) }
    }

    override fun getItemCount(): Int = markets.size

    fun updateMarkets(newMarkets: List<MarketModel>) {
        markets = newMarkets
        notifyDataSetChanged()
    }
}