package pw.forst.wire.backups.ios

import pw.forst.wire.backups.android.database.converters.ExportDate
import java.util.UUID

data class IosMessageDto(
    val id: Int,
    val senderUUID: UUID,
    val conversationUUID: UUID,
    val time: ExportDate,
    val protobuf: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as IosMessageDto

        if (id != other.id) return false
        if (senderUUID != other.senderUUID) return false
        if (conversationUUID != other.conversationUUID) return false
        if (time != other.time) return false
        if (!protobuf.contentEquals(other.protobuf)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + senderUUID.hashCode()
        result = 31 * result + conversationUUID.hashCode()
        result = 31 * result + time.hashCode()
        result = 31 * result + protobuf.contentHashCode()
        return result
    }
}
