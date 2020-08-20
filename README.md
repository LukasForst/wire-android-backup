# Wire backup decryption

____

## Deprecated
The code from this library was merged with the generating code, thus the library is now deprecated in favour of https://github.com/wireapp/backup-export-tool
____

This tool allows to decrypt the backup produced by Wire clients.

## Include in project

The library is using [libsodium](https://github.com/jedisct1/libsodium) through 
[lazysodium](https://github.com/terl/lazysodium-java).
Necessary binaries are included in this repo [here](libs).
To include them in java one must specify `-Djna.library.path=libs` folder.

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
