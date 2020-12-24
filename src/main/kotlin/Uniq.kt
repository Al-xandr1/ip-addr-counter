package sbp.tcpf.mapper.cache

import java.nio.file.Path

fun uniq(filePath: Path): Long {
    val t1 = System.currentTimeMillis()
    val partitionsTmpDir = Partitioner(filePath).partition()
    val t2 = System.currentTimeMillis()
    println("Splitting ${t2 - t1} millis")

    val partitions = partitionsTmpDir.toFile().listFiles()!!.toList()

    var uniq = 0L
    val sortingIterator = SortingIterator(partitions) { s -> s.split(" ")[0] }
    while (sortingIterator.hasNext()) {
        sortingIterator.next()
        uniq++
    }
    val t3 = System.currentTimeMillis()
    println("Sorting ${t3 - t2} millis")
    return uniq
}