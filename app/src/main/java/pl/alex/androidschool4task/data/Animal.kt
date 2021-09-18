package pl.alex.androidschool4task.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "animal_table")
@Parcelize
data class Animal(
    val name: String,
    val age: Int,
    val nickName: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) : Parcelable
