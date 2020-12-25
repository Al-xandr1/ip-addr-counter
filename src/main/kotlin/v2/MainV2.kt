package v2

import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.system.exitProcess

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
            ?: throw IllegalArgumentException("File name not specified")

    val filePath = Paths.get(fileName)
    if (!filePath.toFile().isFile) {
        println("Specified file '$fileName' is not a file or does not exist. ")
        exitProcess(-1)
    }

    val startTime = System.currentTimeMillis()

    //todo OPTIMIZE Is it possible to parallelize file reading?
    val uniq = Files.newBufferedReader(filePath).useLines { it ->
        val bbs = BigBitSet(TOTAL_IPS)
        it.iterator().forEach { s ->
            try {
                bbs.set(s.toIp().toLong())
            } catch (e: Exception) {
                println("$s is not an IP address")
            }
        }
        val s1 = System.currentTimeMillis()
        println("before cardinality ${s1 - startTime} millis")
        bbs.cardinality()
    }

    val executionTime = (System.currentTimeMillis() - startTime) * 1.0 / 1000

    val fileSizeGigabyte = (File(filePath.toString()).length() / 1024 / 1024).toDouble() / 1024
    println("End. ${fileSizeGigabyte}G file processed: $uniq unique ips, $executionTime sec. For 120G ~ ${executionTime * (120 / fileSizeGigabyte)} sec.")
}
