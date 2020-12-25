package v2

internal object MyAssertions {
    @JvmField
    @PublishedApi
    internal val ENABLED: Boolean = javaClass.desiredAssertionStatus()
}

fun assert(callable: () -> Boolean) {
    if (MyAssertions.ENABLED) {
        assert(callable())
    }
}

inline fun <T> Iterable<T>.sumByLong(selector: (T) -> Long): Long {
    var sum: Long = 0
    for (element in this) {
        sum += selector(element)
    }
    return sum
}

fun Long.toIntChecked(): Int {
    val int = toInt()
    assert {int.toLong() == this}
    return int
}