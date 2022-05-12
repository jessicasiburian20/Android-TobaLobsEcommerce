package j012.tobalobsecommerce.room

import androidx.room.*
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import j012.tobalobsecommerce.model.Alamat
import j012.tobalobsecommerce.model.Produk

@Dao
interface DaoAlamat {

    @Insert(onConflict = REPLACE)
    fun insert(data: Alamat)

    @Delete
    fun delete(data: Alamat)

    @Update
    fun update(data: Alamat): Int

    @Query("SELECT * from alamat ORDER BY id ASC")
    fun getAll(): List<Alamat>

    @Query("SELECT * FROM alamat WHERE id = :id LIMIT 1")
    fun getProduk(id: Int): Alamat

    @Query("SELECT * FROM alamat WHERE isSelected = :status LIMIT 1")
    fun getbyStatus(status: Boolean): Alamat?

    @Query("DELETE FROM alamat")
    fun deleteAll(): Int
}