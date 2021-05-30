package me.rerere.polymartapp.model.resource


import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("request")
    val request: Request,
    @SerializedName("response")
    val response: Response
) {
    data class Request(
        @SerializedName("action")
        val action: String,
        @SerializedName("parameters")
        val parameters: Parameters,
        @SerializedName("time")
        val time: Int,
        @SerializedName("timeElapsed")
        val timeElapsed: String
    ) {
        data class Parameters(
            @SerializedName("given")
            val given: Given,
            @SerializedName("used")
            val used: Used
        ) {
            data class Given(
                @SerializedName("limit")
                val limit: Any,
                @SerializedName("start")
                val start: Any
            )

            data class Used(
                @SerializedName("limit")
                val limit: Int,
                @SerializedName("start")
                val start: Int
            )
        }
    }

    data class Response(
        @SerializedName("more")
        val more: Boolean,
        @SerializedName("next_start")
        val nextStart: Int,
        @SerializedName("remaining")
        val remaining: Int,
        @SerializedName("result")
        val result: List<Result>,
        @SerializedName("result_count")
        val resultCount: Int,
        @SerializedName("success")
        val success: Boolean,
        @SerializedName("total")
        val total: Int
    ) {
        data class Result(
            @SerializedName("averageReview")
            val averageReview: String,
            @SerializedName("canDownload")
            val canDownload: Boolean,
            @SerializedName("creationTime")
            val creationTime: String,
            @SerializedName("currency")
            val currency: String,
            @SerializedName("donationLink")
            val donationLink: Any,
            @SerializedName("downloads")
            val downloads: String,
            @SerializedName("headerURL")
            val headerURL: String,
            @SerializedName("id")
            val id: String,
            @SerializedName("keepPercent")
            val keepPercent: String,
            @SerializedName("lastUpdateTime")
            val lastUpdateTime: String,
            @SerializedName("owner")
            val owner: Owner,
            @SerializedName("price")
            val price: String,
            @SerializedName("reviewCount")
            val reviewCount: String,
            @SerializedName("sourceCodeLink")
            val sourceCodeLink: Any,
            @SerializedName("subtitle")
            val subtitle: String,
            @SerializedName("supportedLanguages")
            val supportedLanguages: String,
            @SerializedName("supportedMinecraftVersions")
            val supportedMinecraftVersions: String,
            @SerializedName("supportedServerSoftware")
            val supportedServerSoftware: String,
            @SerializedName("team")
            val team: Any,
            @SerializedName("themeColorDark")
            val themeColorDark: String,
            @SerializedName("themeColorLight")
            val themeColorLight: String,
            @SerializedName("thumbnailURL")
            val thumbnailURL: String,
            @SerializedName("title")
            val title: String,
            @SerializedName("totalDownloads")
            val totalDownloads: String,
            @SerializedName("url")
            val url: String,
            @SerializedName("version")
            val version: String
        ) {
            data class Owner(
                @SerializedName("id")
                val id: String,
                @SerializedName("name")
                val name: String,
                @SerializedName("type")
                val type: String,
                @SerializedName("url")
                val url: String
            )
        }
    }
}