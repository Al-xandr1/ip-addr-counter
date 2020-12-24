package sbp.tcpf.mapper.cache.v1

import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import kotlin.math.roundToInt

class Partitioner(private val filePath: Path) {

    fun partition(): Path {
        val sizeInGigabytes = Files.size(filePath) * 1.0 / 1024 / 1024 / 1024
        val rows = sizeInGigabytes * ROWS_IN_1_GIG
        val defaultLinesPerPart = (rows / MAX_FILES_COUNT).roundToInt()
        val partitionsTmpDir = filePath.buildTmpDir("parts")

        Files.newBufferedReader(filePath)
            .use {
                var totalLine: Long = 0
                do {
                    val tree = TreeMap<String, Int>()
                    var line: String?

                    do {
                        line = it.readLine()
                            ?.also { l ->
                                tree.compute(l) { _, oldCount -> if (oldCount != null) oldCount + 1 else 1 }
                            }
                    } while (line != null && (++totalLine % defaultLinesPerPart) != 0L)

                    if (tree.isNotEmpty()) {
                        partitionsTmpDir
                            .buildTmpPartitionFile(
                                if (totalLine % defaultLinesPerPart == 0L) totalLine / defaultLinesPerPart
                                else totalLine / defaultLinesPerPart + 1
                            )
                            .let { partFile -> FileOutputStream(partFile.toFile()) }
                            .bufferedWriter()
                            .use {
                                tree.forEach { (k, v) ->
                                    it.write("$k $v")
                                    it.newLine()
                                }
                            }
                    }

                } while (line != null)
            }

        return partitionsTmpDir
    }

    private fun Path.buildTmpDir(tmpDirName: String): Path =
        parent
            .let {
                if (it != null) Files.createTempDirectory(it, tmpDirName)
                else Files.createTempDirectory(tmpDirName)
            }!!
            .apply { toFile().deleteOnExit() }

    private fun Path.buildTmpPartitionFile(partition: Long): Path =
        let { Files.createTempFile(it, "part_${partition}_", ".txt") }
            .apply { toFile().deleteOnExit() }
}
