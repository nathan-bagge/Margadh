package org.setu.margadh.main

import org.setu.margadh.models.MarketMemStore
import org.setu.margadh.models.MarketModel

val store = MarketMemStore()

fun main() {
    println("=== Margadh: Local Markets of Ireland ===")
    var input: Int
    do {
        input = menu()
        when (input) {
            1 -> addMarket()
            2 -> listMarkets()
            3 -> updateMarket()
            4 -> deleteMarket()
            5 -> searchMarket()
            0 -> println("\nSlán! Exiting Margadh.")
            else -> println("\nInvalid option. Please try again.")
        }
    } while (input != 0)
}

fun menu(): Int {
    println("\n----------------------------------")
    println(" MARGADH MENU")
    println("----------------------------------")
    println(" 1. Add Market")
    println(" 2. List All Markets")
    println(" 3. Update a Market")
    println(" 4. Delete a Market")
    println(" 5. Search Market by ID")
    println(" 0. Exit")
    print("\nEnter option: ")
    return readlnOrNull()?.toIntOrNull() ?: -1
}

fun readText(prompt: String): String {
    print(prompt)
    return readlnOrNull()?.trim().orEmpty()
}

fun printMarket(m: MarketModel) =
    println("ID: ${m.id} | ${m.name} (${m.county}) | Open: ${m.openingDays} | ${m.description}")

fun addMarket() {
    println("\n--- Add Market ---")
    val name = readText("Market name: ")
    val county = readText("County: ")
    val days = readText("Opening days (e.g. Sat 9-2): ")
    val description = readText("Description: ")

    if (name.isNotEmpty()) {
        val market = MarketModel(name = name, county = county, openingDays = days, description = description)
        store.create(market)
        println("Market added with ID: ${market.id}")
    } else {
        println("Name cannot be empty. Creation cancelled.")
    }
}

fun listMarkets() {
    println("\n--- All Markets ---")
    val markets = store.findAll()
    if (markets.isEmpty()) println("No markets stored yet.")
    else markets.forEach { printMarket(it) }
}

fun updateMarket() {
    println("\n--- Update Market ---")
    listMarkets()
    if (store.findAll().isEmpty()) return

    print("\nEnter ID of market to update: ")
    val id = readlnOrNull()?.toLongOrNull()
    val existing = id?.let { store.findOne(it) }

    if (existing != null) {
        val name = readText("New name: ")
        val county = readText("New county: ")
        val days = readText("New opening days: ")
        val description = readText("New description: ")

        if (name.isNotEmpty()) {
            store.update(existing.copy(name = name, county = county, openingDays = days, description = description))
            println("Market updated successfully.")
        } else {
            println("Name cannot be empty. Update cancelled.")
        }
    } else {
        println("Market with ID $id not found.")
    }
}

fun deleteMarket() {
    println("\n--- Delete Market ---")
    listMarkets()
    if (store.findAll().isEmpty()) return

    print("\nEnter ID of market to delete: ")
    val id = readlnOrNull()?.toLongOrNull()
    when {
        id == null -> println("Invalid ID entered.")
        store.delete(id) -> println("Market $id deleted.")
        else -> println("Market $id not found.")
    }
}

fun searchMarket() {
    println("\n--- Search Market ---")
    print("Enter ID: ")
    val id = readlnOrNull()?.toLongOrNull()
    if (id == null) {
        println("Invalid ID entered.")
        return
    }
    store.findOne(id)?.let { printMarket(it) } ?: println("No market found with ID $id.")
}