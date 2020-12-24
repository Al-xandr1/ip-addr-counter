package v2

inline fun <T> Iterable<T>.sumByLong(selector: (T) -> Long): Long {
    var sum: Long = 0
    for (element in this) {
        sum += selector(element)
    }
    return sum
}

fun Long.toIntChecked(): Int {
    val int = toInt()
    assert(int.toLong() == this)
    return int
}
