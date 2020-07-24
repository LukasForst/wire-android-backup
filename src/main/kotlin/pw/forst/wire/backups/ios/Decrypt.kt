package pw.forst.wire.backups.ios

import org.libsodium.jni.Sodium
import java.io.File
import java.nio.ByteBuffer
import java.util.UUID

fun decrypt(databaseFile: File, password: String, userId: UUID): ByteArray =
    decrypt(ByteBuffer.wrap(databaseFile.readBytes()), password, userId)

fun decrypt(input: ByteBuffer, password: String, userId: UUID): ByteArray {
    // read and verifies the header, modifies input buffer
    val fileHeader = readHeader(input)
    // now we check the uuid
    val iosHashUserId = iosUuidHash(userId, fileHeader.salt)
    require(iosHashUserId.contentEquals(fileHeader.uuidHash)) { "UUID mismatch!" }
    // obtain key for decryption
    val key = deriveKey(password, fileHeader.salt)
    // create state and cipher header bytes
    val state = ByteArray(Sodium.crypto_secretstream_xchacha20poly1305_statebytes())
    val chachaHeader = ByteArray(Sodium.crypto_secretstream_xchacha20poly1305_headerbytes())
    // read header from the remaining input bytes
    input.get(chachaHeader)
    // initialize state with encryption header and key
    val initPullResult = Sodium.crypto_secretstream_xchacha20poly1305_init_pull(state, chachaHeader, key)
    require(initPullResult == 0) { "It was not possible to init state!" }

    // TODO and this is magic ladies and gentleman...
    // found after debugging test cases from iOS not sure whether this is correct
    // but now is the decryption result 0 and the decrypted data are valid
    state[32] = 0

    // read rest of the cipher text
    val cipherText = ByteArray(input.remaining())
    input.get(cipherText)
    // prepare decryption buffers
    // TODO allow decryption of larger files by using 1024*1024 buffer
    val decrypted = ByteArray(cipherText.size + Sodium.crypto_secretstream_xchacha20poly1305_abytes())
    val decryptedMessageLength = IntArray(1)
    val tag = ByteArray(1)
    // decrypt data
    val decryptionResult = Sodium.crypto_secretstream_xchacha20poly1305_pull(
        state, decrypted, decryptedMessageLength, tag, cipherText, cipherText.size,
        ByteArray(0), 0
    )
    require(decryptionResult == 0) { "Decryption failed" }
    return decrypted.take(decryptedMessageLength[0]).toByteArray()
}
