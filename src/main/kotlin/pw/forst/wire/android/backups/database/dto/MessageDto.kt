package pw.forst.wire.android.backups.database.dto

import java.util.UUID

data class MessageDto(
    val id: UUID,
    val conversationId: UUID,
    val userId: UUID,
    // TODO WTF?
    val time: String,
    // TODO is this formatted or what?
    val content: String,
    val quote: UUID? = null
)

data class AssetMessageDto(
    val messageId: UUID,
    // TODO again, what is that?
    val time: String,
    val assetKey: String,
    val assetToken: String,
    /**
     * ID of the Asset2
     */
    val key: UUID,
    val sha: ByteArray,
    /**
     * probably can be found as second 32 bytes field in proto file
     */
    val otrKey: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AssetMessageDto

        if (messageId != other.messageId) return false
        if (time != other.time) return false
        if (assetKey != other.assetKey) return false
        if (assetToken != other.assetToken) return false
        if (key != other.key) return false
        if (!sha.contentEquals(other.sha)) return false
        if (!otrKey.contentEquals(other.otrKey)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = messageId.hashCode()
        result = 31 * result + time.hashCode()
        result = 31 * result + assetKey.hashCode()
        result = 31 * result + assetToken.hashCode()
        result = 31 * result + key.hashCode()
        result = 31 * result + sha.contentHashCode()
        result = 31 * result + otrKey.contentHashCode()
        return result
    }
}
