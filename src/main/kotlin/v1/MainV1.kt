package v1

import java.nio.file.Paths
import kotlin.system.exitProcess

const val MAX_FILES_COUNT = 6500
const val ROWS_IN_1_GIG = 81_000_000

//todo ускорить реализацию через распаралленивание

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
    var uniq = uniq(filePath)
    val executionTime = (System.currentTimeMillis() - startTime) * 1.0 / 1000

    println("End. 2G file processed: $uniq unique ips, $executionTime sec. For 120G ~ ${executionTime * 60} sec.")
}
