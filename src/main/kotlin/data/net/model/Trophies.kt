package data.net.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The response received from this endpoint is a list of trophy objects.
 */
@Serializable
data class TrophyResponse(
    val kind: String,
    val data: TrophyResponseData,
)

@Serializable
data class TrophyResponseData(
    val trophies: List<Trophy>,
)

@Serializable
data class Trophy(
    val kind: String,
    val data: TrophyData,
)

@Serializable
data class TrophyData(
    @SerialName("icon_70")
    val iconUrl: String,
    @SerialName("granted_at")
    val grantedAt: Long?,
    val name: String,
)