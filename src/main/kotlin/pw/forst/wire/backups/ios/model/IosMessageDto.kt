package pw.forst.wire.backups.ios.model

import pw.forst.wire.backups.android.database.converters.ExportDate
import java.util.UUID

/**
 * Message from iOS backup with protobuf and envelope.
 */
data class IosMessageDto(
    /**
     * Id of the message.
     */
    val id: Int,
    /**
     * Who sent the message.
     */
    val senderUUID: UUID,
    /**
     * In which conversation was message posted.
     */
    val conversationUUID: UUID,
    /**
     * When was the conversation sent.
     */
    val time: ExportDate,
    /**
     * Raw protobuf from the server.
     */
    val protobuf: ByteArray
) {
    @Suppress("DuplicatedCode") // because we need it ofc...
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
