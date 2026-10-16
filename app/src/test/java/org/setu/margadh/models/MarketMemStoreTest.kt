//References:
//- Android Developers, Build local unit tests: https://developer.android.com/training/testing/local-tests
//- JUnit 4: https://junit.org/junit4/

package org.setu.margadh.models

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class MarketMemStoreTest {

    private lateinit var store: MarketMemStore

    // Runs before every test, so each test starts with a fresh store holding the same two markets
    @Before
    fun setUp() {
        store = MarketMemStore()
        store.create(MarketModel(name = "Waterford Farmers Market", county = "Waterford", lat = 52.2593, lng = -7.1101))
        store.create(MarketModel(name = "Dungarvan Market", county = "Waterford", lat = 52.0845, lng = -7.6255))
    }

    @Test
    fun create_assignsIncrementingIds() {
        val markets = store.findAll()
        assertEquals(1L, markets[0].id)
        assertEquals(2L, markets[1].id)
    }

    @Test
    fun findAll_returnsEveryMarket() {
        assertEquals(2, store.findAll().size)
    }

    @Test
    fun findOne_returnsMatchingMarket() {
        val market = store.findOne(2L)
        assertNotNull(market)
        assertEquals("Dungarvan Market", market?.name)
    }

    @Test
    fun findOne_returnsNullForMissingId() {
        assertNull(store.findOne(99L))
    }

    @Test
    fun update_changesExistingMarket() {
        val updated = MarketModel(id = 1L, name = "Waterford Market", county = "Waterford", openingDays = "Sat 9am-2pm")
        assertTrue(store.update(updated))

        val market = store.findOne(1L)
        assertEquals("Waterford Market", market?.name)
        assertEquals("Sat 9am-2pm", market?.openingDays)
    }

    @Test
    fun update_returnsFalseForMissingId() {
        assertFalse(store.update(MarketModel(id = 99L, name = "Nowhere")))
    }

    @Test
    fun delete_removesMarket() {
        assertTrue(store.delete(1L))
        assertEquals(1, store.findAll().size)
        assertNull(store.findOne(1L))
    }

    @Test
    fun delete_returnsFalseForMissingId() {
        assertFalse(store.delete(99L))
        assertEquals(2, store.findAll().size)
    }
}