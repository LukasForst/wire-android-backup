package pw.forst.wire.backups.utils

inline fun <T, NT, V> Pair<List<T>, V>.mapLeft(block: (T) -> NT): Pair<List<NT>, V> =
    Pair(this.first.map(block), this.second)

inline fun <T, V, NV> Pair<T, List<V>>.mapRight(block: (V) -> NV): Pair<T, List<NV>> =
    Pair(this.first, this.second.map(block))

inline fun <T, V, NT, NV> Pair<List<T>, List<V>>.mapPair(leftBlock: (T) -> NT, rightBlock: (V) -> NV): Pair<List<NT>, List<NV>> =
    Pair(this.first.map(leftBlock), this.second.map(rightBlock))

inline fun <T, NT, V> Pair<T, V>.letLeft(block: (T) -> NT): Pair<NT, V> =
    letPair(block, { it })

inline fun <T, V, NV> Pair<T, V>.letRight(block: (V) -> NV): Pair<T, NV> =
    letPair({ it }, block)

inline fun <T, V, NT, NV> Pair<T, V>.letPair(leftBlock: (T) -> NT, rightBlock: (V) -> NV): Pair<NT, NV> =
    Pair(leftBlock(this.first), rightBlock(this.second))
