package com.sevenexp.craftit.data.source.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "recent")
data class RecentEntity(
    @PrimaryKey @field:SerializedName("id") val id: String,

    @field:SerializedName("image") val image: String,

    @field:SerializedName("totalStep") val totalStep: Int,

    @field:SerializedName("description") val description: String,

    @field:SerializedName("image_user") val imageUser: String,

    @field:SerializedName("tags") val tags: List<String>,

    @field:SerializedName("createdAt") val createdAt: String,

    @field:SerializedName("createdBy") val createdBy: String,

    @field:SerializedName("name") val name: String,

    @field:SerializedName("likes") val likes: Int
)
