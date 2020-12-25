package v2

private const val P1_MASK = 0xFF000000L
private const val P2_MASK = 0x00FF0000L
private const val P3_MASK = 0x0000FF00L
private const val P4_MASK = 0x000000FFL

data class IP(val p1: Long, val p2: Long, val p3: Long, val p4: Long) {
    init {
        assert { p1 in 0..255 }
        assert { p2 in 0..255 }
        assert { p3 in 0..255 }
        assert { p4 in 0..255 }
    }
}

fun String.isIp() = this.matches(IP_REGEXP)

fun String.toIp(): IP {
    assert { isIp() }

    val bytes = sun.net.util.IPAddressUtil.textToNumericFormatV4(this)
    return IP(bytes[0].myToLong(), bytes[1].myToLong(), bytes[2].myToLong(), bytes[3].myToLong())
}

//todo OPTIMIZE replace arithmetic operation by bitwise
private fun Byte.myToLong(): Long {
    if (this < 0) {
        return (this + 256).toLong()
    }
    return toLong()
}

fun IP.toLong(): Long = this.p1.shl(24) + this.p2.shl(16) + this.p3.shl(8) + this.p4

fun Long.toIp(): IP = IP(and(P1_MASK).shr(24), and(P2_MASK).shr(16), and(P3_MASK).shr(8), and(P4_MASK))