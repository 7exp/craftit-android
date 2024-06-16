package com.sevenexp.craftit.data.response.items

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "fyp")
data class FypItems(
    @PrimaryKey
    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("image")
    val image: String,

    @field:SerializedName("totalStep")
    val totalStep: Int,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("id_user")
    val idUser: String,

    @field:SerializedName("created_by")
    val createdBy: String,

    @field:SerializedName("image_user")
    val imageUser: String,

    @field:SerializedName("likes")
    val likes: Int,

    @field:SerializedName("tags")
    val tagsItems: List<String>
)
