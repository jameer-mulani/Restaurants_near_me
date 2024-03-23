package com.deserve.restaurantsnearme.model.entity

data class Resource<out T>(
    val data: T? = null,
    val error: String? = null,
    val status: ResourceStatus? = null
) {
    companion object {
        fun <T> success(data: T): Resource<T> =
            Resource(data = data, error = null, status = ResourceStatus.SUCCESS)

        fun <T> failed(error: String): Resource<T> =
            Resource(data = null, error = error, status = ResourceStatus.FAILED)

        fun <T> loading(): Resource<T> =
            Resource(data = null, error = null, status = ResourceStatus.LOADING)


    }
}

enum class ResourceStatus {
    SUCCESS, LOADING, FAILED
}