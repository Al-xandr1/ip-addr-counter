package v1

import java.io.BufferedReader
import java.io.File
import java.nio.file.Files
import java.util.*

class SortingIterator(partitions: List<File>, val keyExtractor: (String) -> (String)) : Iterator<String> {

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