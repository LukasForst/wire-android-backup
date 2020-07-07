package com.wire.integrations.backups.utils


/**
 * Adds the specified path to the java library path
 *
 * @param pathToAdd the path to add
 * @throws Exception
 */
@Suppress("UNCHECKED_CAST") // it is safe as usr
fun addLibraryPath(pathToAdd: String) {
    val usrPathsField = ClassLoader::class.java.getDeclaredField("usr_paths")
    usrPathsField.isAccessible = true

    //get array of paths
    val paths: Array<String> = usrPathsField.get(null) as Array<String>

    //check if the path to add is already present
    for (path in paths) {
        if (path == pathToAdd) {
            return
        }
    }

    //add the new path
    val newPaths = paths.copyOf(paths.size + 1)
    newPaths[newPaths.size - 1] = pathToAdd
    usrPathsField.set(null, newPaths)
}
