package sbp.tcpf.mapper.cache

import java.nio.file.Paths
import kotlin.system.exitProcess

const val DEFAULT_TEST_FILE = "/Users/Alexander/IdeaProjects/ip-addr-counter/src/main/resources/ips.txt"
const val MAX_FILES_COUNT = 6500
const val ROWS_IN_1_GIG = 83_000_000

//todo проверить после shufl
//todo описать требования по памяти
//todo описать примерно время выполнения
//todo проверить на https://ecwid-vgv-storage.s3.eu-central-1.amazonaws.com/ip_addresses.zip

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
    var uniq = uniq(filePath)
    val executionTime = (System.currentTimeMillis() - startTime) * 1.0 / 1000

    println("End: $uniq uniq ips, $executionTime sec. For 120G ~ ${executionTime * 120} sec.")
}


