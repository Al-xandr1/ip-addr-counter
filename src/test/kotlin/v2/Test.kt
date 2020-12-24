package v2

class Test {
    companion object {
        private const val ERROR = "\t\t\tERROR!!!"
        private const val SUCCESS = "\tsuccess!"

        private fun check(actual: Any?, expect: Any?) = if (actual != expect) ERROR else SUCCESS

        private fun Any.test(actual: Any?, expect: Any?) = println("$this $actual $expect ${check(actual, expect)}")

        private fun String.testIsIp(expect: Boolean) =
            println("$this ${this.isIp()} $expect ${check(this.isIp(), expect)}")

        private fun runTestsIsIp() {
            //ips
            "0.0.0.0".testIsIp(true)
            "0.0.0.000".testIsIp(true)
            "0.0.0.01".testIsIp(true)
            "0.0.0.255".testIsIp(true)
            "37.230.210.51".testIsIp(true)

            //not ips
            "0.0.0.0000".testIsIp(false)
            "0.0.0.-1".testIsIp(false)
            "0.0.0.256".testIsIp(false)
            "0.0.0.d".testIsIp(false)
            "999.0.0.0".testIsIp(false)
            println()
        }

        private fun String.testToIp(expect: IP?) = test(toIp(), expect)

        private fun runTestsStringToIp() {
            //ips
            "0.0.0.0".testToIp(IP(0, 0, 0, 0))
            "0.0.0.000".testToIp(IP(0, 0, 0, 0))
            "0.0.0.01".testToIp(IP(0, 0, 0, 1))
            "0.0.0.255".testToIp(IP(0, 0, 0, 255))
            "37.230.210.51".testToIp(IP(37, 230, 210, 51))

            //not ips
            "0.0.0.0000".testToIp(null)
            "0.0.0.-1".testToIp(null)
            "0.0.0.256".testToIp(null)
            "0.0.0.d".testToIp(null)
            "999.0.0.0".testToIp(null)
            println()
        }

        private fun IP.testToLong(expect: Long) = test(toLong(), expect)

        private fun runTestsToLong() {
            IP(0, 0, 0, 0).testToLong(0)
            IP(0, 0, 0, 1).testToLong(1)
            IP(0, 0, 0, 255).testToLong(255)
            IP(37, 230, 210, 51).testToLong(635884083)
            IP(1, 1, 1, 1).testToLong(16843009)
            IP(255, 255, 255, 255).testToLong(4294967295)
            println()
        }

        private fun Long.testToIp(expect: IP) = test(toIp(), expect)

        private fun runTestsLongToIp() {
            0L.testToIp(IP(0, 0, 0, 0))
            1L.testToIp(IP(0, 0, 0, 1))
            255L.testToIp(IP(0, 0, 0, 255))
            635884083L.testToIp(IP(37, 230, 210, 51))
            16843009L.testToIp(IP(1, 1, 1, 1))
            4294967295L.testToIp(IP(255, 255, 255, 255))
            println()
        }

        private fun BigBitSet.testBigBitSetCardinality(expect: Long) {
            LongRange(0, nbits - 1L).forEach { set(it) }
            test(this.cardinality(), expect)
        }

        private fun runTestsCardinality() {
            BigBitSet(TOTAL_IPS).testBigBitSetCardinality(TOTAL_IPS)
            BigBitSet(Integer.MAX_VALUE.toLong()).testBigBitSetCardinality(Integer.MAX_VALUE.toLong())
            BigBitSet(1).testBigBitSetCardinality(1)
        }

        fun run() {
            runTestsIsIp()
            runTestsStringToIp()
            runTestsToLong()
            runTestsLongToIp()
            runTestsCardinality()
        }
    }
}

fun main() {
    Test.run()
}