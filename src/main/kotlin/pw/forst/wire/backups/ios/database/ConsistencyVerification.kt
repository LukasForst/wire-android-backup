package pw.forst.wire.backups.ios.database

import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import pw.forst.wire.backups.ios.database.model.PrimaryKeys
import pw.forst.wire.backups.ios.database.model.REACTION_ENTITY_TYPE
import pw.forst.wire.backups.ios.database.model.SYSTEM_MESSAGE_ENTITY_TYPE
import pw.forst.wire.backups.ios.database.model.USER_ENTITY_TYPE
import pw.forst.wire.backups.ios.model.IosDatabaseDto


@Suppress("unused") // we want it to run inside the transaction
internal fun Transaction.verifyConsistency() {
    val existingMap = PrimaryKeys
        .select {
            PrimaryKeys.entityKey.inList(expectedEntityTypes.values) and
                    PrimaryKeys.name.inList(expectedEntityTypes.keys)
        }.associate { it[PrimaryKeys.entityKey] to it[PrimaryKeys.name] }

    expectedEntityTypes.forEach { (entityTypeNumber, entityName) ->
        require(existingMap[entityName] == entityTypeNumber) {
            "Database model does not match expected scheme! Probably version mismatch."
        }
    }
}

internal fun verifyDatabaseMetadata(db: IosDatabaseDto) {
    val currentSupport = supportedAppVersions.getValue(expectedEntityTypes)
    require(currentSupport.contains(db.modelVersion)) {
        "Unsupported version of export! This tool supports $currentSupport, but export is ${db.modelVersion}"
    }
}

private val supportedAppVersions = mapOf(
    mapOf("SystemMessage" to 14, "Reaction" to 18, "User" to 22) to setOf("2.81.0"),
    mapOf("SystemMessage" to 15, "Reaction" to 19, "User" to 23) to setOf("2.82.0")
)

private val expectedEntityTypes = mapOf(
    "SystemMessage" to SYSTEM_MESSAGE_ENTITY_TYPE,
    "Reaction" to REACTION_ENTITY_TYPE,
    "User" to USER_ENTITY_TYPE
)
