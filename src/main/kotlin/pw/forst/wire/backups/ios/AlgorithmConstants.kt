package pw.forst.wire.backups.ios

// taken from rust version of libsodium
// https://docs.rs/libsodium-sys/0.2.5/src/libsodium_sys/sodium_bindings.rs.html#2463-2467
const val crypto_pwhash_argon2i_ALG_ARGON2I13 = 1

const val crypto_pwhash_argon2i_MEMLIMIT_INTERACTIVE = 33554432
const val crypto_pwhash_argon2i_OPSLIMIT_INTERACTIVE = 4

const val crypto_pwhash_argon2i_MEMLIMIT_MODERATE = 134217728
const val crypto_pwhash_argon2i_OPSLIMIT_MODERATE = 6
