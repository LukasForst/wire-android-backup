package pw.forst.wire.backups.ios

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

@Disabled
class GenericMessageConverterKtTest {
    @Test
    fun `test obtain some protobufs from ios backup`() {
        val bufs = obtainProtobufsForDatabase("/Users/lukas/work/wire/android-db-decryption/store.wiredatabase")
        assertTrue { bufs.isNotEmpty() }
        print(bufs)
    }
}
