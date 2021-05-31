package me.rerere.polymartapp.model.resource

sealed class ResourceDetail(

) {

    object Loading : ResourceDetail() {
    }
}