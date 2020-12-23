package sbp.tcpf.mapper.cache

import java.nio.file.Path

fun uniq(filePath: Path): Long {
    val partitionsTmpDir = Partitioner(filePath).partition()
    val partitions = partitionsTmpDir.toFile().listFiles()!!.toList()

    var uniq = 0L
    val sortingIterator = SortingIterator(partitions) { s -> s.split(" ")[0] }
    while (sortingIterator.hasNext()) {
        sortingIterator.next()
        uniq++
    }
    return uniq
}