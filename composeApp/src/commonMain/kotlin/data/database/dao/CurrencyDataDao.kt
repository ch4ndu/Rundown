package data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import data.database.model.currency.CurrencyData
import kotlinx.coroutines.flow.Flow

@Dao
abstract class CurrencyDataDao : BaseDao<CurrencyData> {

    @Query("SELECT * FROM currency ORDER BY name ASC LIMIT :currencyLimit")
    abstract fun getPaginatedCurrency(currencyLimit: Int): Flow<MutableList<CurrencyData>>

    @Query("SELECT * FROM currency ORDER BY currencyId ASC")
    abstract fun getCurrency(): Flow<List<CurrencyData>>

    /* Sort currency according to their attributes
     * 1. Sort currency by their names in alphabetical order
     * 2. if currency has enabled = true as their attributes, put them at the top
     * 3. if currencyDefault = true, put the currency at the first row
     */
    @Query("SELECT * FROM (SELECT * FROM (SELECT * FROM currency ORDER BY name) ORDER BY enabled DESC) ORDER BY currencyDefault DESC")
    abstract suspend fun getSortedCurrency(): List<CurrencyData>

    @Query("DELETE FROM currency WHERE code = :currencyCode")
    abstract suspend fun deleteCurrencyByCode(currencyCode: String): Int

    @Query("SELECT * FROM currency WHERE code = :currencyCode")
    abstract suspend fun getCurrencyByCode(currencyCode: String): MutableList<CurrencyData>

    @Query("SELECT * FROM currency WHERE currencyDefault = :defaultCurrency")
    abstract suspend fun getDefaultCurrency(defaultCurrency: Boolean = true): CurrencyData

    @Query("SELECT * FROM currency WHERE currencyId =:currencyId")
    abstract suspend fun getCurrencyById(currencyId: Long): MutableList<CurrencyData>

    @Query("DELETE FROM currency WHERE currencyDefault =:defaultCurrency")
    abstract suspend fun deleteDefaultCurrency(defaultCurrency: Boolean = true): Int

    @Query("DELETE FROM currency")
    abstract suspend fun deleteAllCurrency(): Int

    @Query("SELECT * FROM currency WHERE name =:currencyName")
    abstract suspend fun getCurrencyByName(currencyName: String): MutableList<CurrencyData>

    @Update(entity = CurrencyData::class)
    abstract suspend fun updateDefaultCurrency(currencyData: CurrencyData)

    @Query("UPDATE currency SET currencyDefault =:currencyDefault WHERE name =:currencyName")
    abstract suspend fun changeDefaultCurrency(
        currencyName: String,
        currencyDefault: Boolean = true
    )

//    @Query("SELECT currency.name FROM bills JOIN currency ON bills.currency_id = currency.currencyId WHERE billId =:billId")
//    abstract suspend fun getCurrencyFromBill(billId: Long): String

    @Query("SELECT * FROM currency WHERE name LIKE :name")
    abstract suspend fun searchCurrency(name: String): List<CurrencyData>
}