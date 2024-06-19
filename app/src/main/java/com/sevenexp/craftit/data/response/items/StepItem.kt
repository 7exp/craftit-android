package com.sevenexp.craftit.data.response.items

import com.google.gson.annotations.SerializedName

data class StepItem(

    @field:SerializedName("id_handicraft")
    val idHandicraft: String,

    @field:SerializedName("image")
    val image: String,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("step_number")
    val stepNumber: Int,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("updatedAt")
    val updatedAt: String
)
