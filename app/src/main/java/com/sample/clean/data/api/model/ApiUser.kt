package com.sample.clean.data.api.model

import com.google.gson.annotations.SerializedName
import com.sample.clean.domain.repo.model.User

data class ApiUser(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("email")
    val email: String = "",
    @SerializedName("avatar")
    val avatar: String = ""
)
// 单个对象转换
fun ApiUser.toDomain(): User {
    return User(
        id = this.id,
        name = this.name,
        email = this.email,
        avatar = this.avatar
    )
}

// 重点：List 转换
fun List<ApiUser>.toDomainList(): List<User> {
    return this.map { it.toDomain() }
}
