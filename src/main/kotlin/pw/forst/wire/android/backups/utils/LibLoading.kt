package pw.forst.wire.android.backups.utils


/**
 * Adds the specified path to the java library path
 *
 * @param pathToAdd the path to add
 */
fun addLibraryPath(pathToAdd: String) {
    val usrPathsField = ClassLoader::class.java.getDeclaredField("usr_paths")
        .apply { isAccessible = true }

    // get array of paths
    @Suppress("UNCHECKED_CAST") // it is safe as we know that this field contains array of strings
    val paths = usrPathsField.get(null) as Array<String>

    // check if the path to add is already present
    if (paths.contains(pathToAdd)) return

    with(paths + pathToAdd) {
        // add the new path
        usrPathsField.set(null, this)
        // set property
        System.setProperty("java.library.path", joinToString(":"))
    }
}
