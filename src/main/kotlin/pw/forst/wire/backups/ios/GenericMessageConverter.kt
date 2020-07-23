package pw.forst.wire.backups.ios

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Returns List of wrapped byte arrays for generic message protobuf.
 */
fun obtainProtobufsForDatabase(pathToDatabaseFile: String): List<GenericMessageDto> =
    transaction(Database.connect("jdbc:sqlite:$pathToDatabaseFile")) {
        GenericMessageData.selectAll().map {
            GenericMessageDto(it[GenericMessageData.proto].bytes)
        }
    }

data class GenericMessageDto(
    /**
     * Byte representation of protobuf.
     */
    val protobuf: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GenericMessageDto

        if (!protobuf.contentEquals(other.protobuf)) return false

        return true
    }

    override fun hashCode(): Int {
        return protobuf.contentHashCode()
    }
}
