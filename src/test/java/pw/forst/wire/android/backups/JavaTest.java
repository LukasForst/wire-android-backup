package pw.forst.wire.android.backups;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.UUID;

import static pw.forst.wire.android.backups.steps.DatabaseDecryptKt.decryptDatabase;
import static pw.forst.wire.android.backups.steps.DecryptKt.initSodium;
import static pw.forst.wire.android.backups.steps.OrchestrateKt.decryptAndExtract;
import static pw.forst.wire.android.backups.steps.UnzipKt.extractBackup;
import static pw.forst.wire.android.backups.utils.LibLoadingKt.addLibraryPath;

public class JavaTest {

    private final String dbFile = "testing-assets/backup.android_wbu";
    private final String userId = "199a7b4b-4342-4de6-b4cb-f39852ee445b";
    private final String password = "a";

    @Test
    public void testJavaExecution_decryptAndExtract() {
        addLibraryPath("libs");
        initSodium();

        Assertions.assertNotNull(decryptAndExtract(dbFile, password, userId));
    }

    @Test
    public void testJavaExecution_decrypt_extract() {
        addLibraryPath("libs");
        initSodium();

        File decrypted = decryptDatabase(
                new File(dbFile),
                password.getBytes(),
                UUID.fromString(userId)
        );
        Assertions.assertNotNull(decrypted);
        extractBackup(decrypted, UUID.fromString(userId), ".");
    }
}
