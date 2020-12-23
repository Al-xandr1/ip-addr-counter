package sbp.tcpf.mapper.cache

import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import kotlin.system.exitProcess

const val DEFAULT_TEST_FILE = "/Users/Alexander/IdeaProjects/ip-addr-counter/src/main/resources/ips_uniq.txt"
const val DEFAULT_LINES_PER_PART = 10_000 //todo сделать динамически выбираемой

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

    val partitionsTmpDir = filePath.buildTmpDir("parts")

    val startTime = System.currentTimeMillis()
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
                } while (line != null && (++totalLine % DEFAULT_LINES_PER_PART) != 0L)

                if (tree.isNotEmpty()) {
                    partitionsTmpDir
                        .buildTmpPartitionFile(
                            if (totalLine % DEFAULT_LINES_PER_PART == 0L) totalLine / DEFAULT_LINES_PER_PART
                            else totalLine / DEFAULT_LINES_PER_PART + 1
                        )
                        .let { partFile -> FileOutputStream(partFile.toFile()) }
                        .bufferedWriter()
                        .use {
                            tree.forEach { k, v ->
                                it.write("$k $v")
                                it.newLine()
                            }
                        }
                }

            } while (line != null)
        }

    val partitions = partitionsTmpDir.toFile().listFiles().toList()

    var uniq = 0L

    val sortingIterator = SortingIterator(partitions) { s -> s.split(" ")[0] }
    while (sortingIterator.hasNext()) {
        sortingIterator.next()
        uniq++
    }

    println("End: $uniq uniq ips, ${(System.currentTimeMillis() - startTime)*1.0 / 1000} sec")
}

private fun Path.buildTmpDir(tmpDirName: String): Path =
    parent
        .let {
            if (it != null) Files.createTempDirectory(it, tmpDirName)
            else Files.createTempDirectory(tmpDirName)
        }!!
//        .apply { toFile().deleteOnExit() }

private fun Path.buildTmpPartitionFile(partition: Long): Path =
    let { Files.createTempFile(it, "part_${partition}_", ".txt") }
//        .apply { toFile().deleteOnExit() }


private class SortingIterator(partitions: List<File>, val keyExtractor: (String) -> (String)) : Iterator<String> {

    private val readers = partitions.map { Files.newBufferedReader(it.toPath()) }.toMutableList()

    private var next: String? = null

    private val tree: TreeMap<String, MutableList<BufferedReader>> = TreeMap()

    init {
        readers.mapNotNull { buildPair(it) }
            .forEach { appendPair(it) }
    }

    private fun buildPair(reader: BufferedReader): Pair<String, BufferedReader>? {
        val head = reader.readLine()
        return if (head != null) Pair(keyExtractor(head), reader)
        else {
            reader.close()
            null
        }
    }

    private fun appendPair(it: Pair<String, BufferedReader>) {
        tree.compute(it.first) { _, oldReaders ->
            if (oldReaders != null) {
                oldReaders.add(it.second)
                oldReaders
            } else mutableListOf(it.second)
        }
    }

    override fun hasNext(): Boolean = tree.isNotEmpty()

    override fun next(): String {
        if (!hasNext()) throw NoSuchElementException("No data any more")

        val min = tree.pollFirstEntry()!!
        next = min.key
        min.value.mapNotNull { buildPair(it) }.forEach { appendPair(it) }

        return next ?: throw NoSuchElementException("We could not compute next element :(")
    }
}