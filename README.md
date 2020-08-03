# Wire backup decryption

This tool allows to decrypt the backup produced by Wire clients.

## Include in project

One must install [libsodium](https://github.com/jedisct1/libsodium) in order to use this library.

Include to maven like this:
```xml
        <dependency>
            <groupId>pw.forst.wire</groupId>
            <artifactId>backups</artifactId>
            <version>some-latest-version</version>
        </dependency>
```

Or in gradle:
```kotlin
implementation("pw.forst.wire", "backups", "some-latest-version")
```

## Usage

Android usage:
````java
import pw.forst.wire.backups.android.database.dto.*;
import pw.forst.wire.backups.android.model.AndroidDatabaseExportDto;
import pw.forst.wire.backups.android.steps.ExportMetadata;
import pw.forst.wire.backups.api.DatabaseExport;

class DecryptionTest {
    public static void main(String[] args){
        final AndroidDatabaseExportDto exportDto = DatabaseExport.builder()
                        .forUserId("<some userid>")
                        .fromEncryptedExport("<path to backup>")
                        .withPassword("<password used during encryption>")
                        .toOutputDirectory("<output directory>")
                        .buildForAndroidBackup()
                        .exportDatabase();

        System.out.println(exportDto);     
    }
}
````

iOS usage:
````java
import pw.forst.wire.backups.api.DatabaseExport;
import pw.forst.wire.backups.ios.model.*;

class DecryptionTest {
    public static void main(String[] args){
        final IosDatabaseExportDto exportDto = DatabaseExport.builder()
                        .forUserId("<some userid>")
                        .fromEncryptedExport("<path to backup>")
                        .withPassword("<password used during encryption>")
                        .toOutputDirectory("<output directory>")
                        .buildForIosBackup()
                        .exportDatabase();

        System.out.println(exportDto);     
    }
}
````


## Supported versions
Current iOS model - `2.82.0`
