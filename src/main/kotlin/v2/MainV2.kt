package v2

import java.nio.file.Files
import java.nio.file.Paths
import kotlin.system.exitProcess

const val DEFAULT_TEST_FILE = "/Users/Alexander/IdeaProjects/ips/ips.txt"
const val TOTAL_IPS = 4_294_967_296L // 0..TOTAL_IPS-1 or 0.0.0.0..255.255.255.255
val IP_REEXP =
    Regex("(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)")

fun main(str: Array<String>) {
    println("Start...")
    val fileName =
        str.takeIf { it.isNotEmpty() }
            ?.get(0)
            ?.takeIf { it.isNotBlank() }
            ?.trim()
            ?: DEFAULT_TEST_FILE

    val filePath = Paths.get(fileName)
    if (!filePath.toFile().isFile) {
        println("Specified file '$fileName' is not a file or does not exist. ")
        exitProcess(-1)
    }

    val startTime = System.currentTimeMillis()

    val uniq = Files.newBufferedReader(filePath).useLines { it ->
        val bbs = BigBitSet(TOTAL_IPS)
        it.iterator().forEach { s ->
            val ip = s.toIp()
            if (ip != null) {
                bbs.set(ip.toLong())
            } else {
                println("$s is not an IP address")
            }
        }
        bbs.cardinality()
    }

    val executionTime = (System.currentTimeMillis() - startTime) * 1.0 / 1000

    println("End. 2G file processed: $uniq unique ips, $executionTime sec. For 120G ~ ${executionTime * 60} sec.")
}
