package v2

import v2.BigBitSet.Companion.MAX_SIZE
import java.util.*

/**
 * @param nbits полоколичество значений в массиве. Ограничено до MAX_SIZE = 10L * Integer.MAX_VALUE
 * @see MAX_SIZE
 */
class BigBitSet(val nbits: Long) {

    private var buckets: List<BitSet>

    init {
        require(nbits in 1..(10L * MAX_BUCKET_SIZE))

        val int = (nbits / MAX_BUCKET_SIZE).toInt()
        val reminder = nbits % MAX_BUCKET_SIZE
        val bucketsNum = int + (if (reminder == 0L) 0 else 1)
        assert(bucketsNum in 1..10)

        //todo MAX_TASK optimize size of the last bucket
        buckets = (1..bucketsNum).map { BitSet(MAX_BUCKET_SIZE) }
    }

    fun set(index: Long) {
        require(index in 0 until MAX_SIZE)

        val bucket = index / MAX_BUCKET_SIZE
        assert(bucket in buckets.indices)

        val localIndex = index - bucket * MAX_BUCKET_SIZE

        buckets[bucket.toIntChecked()].set(localIndex.toIntChecked())
    }

    fun cardinality(): Long = buckets.sumByLong { it.cardinality().toLong() }

    //todo MAX_TASK to implement other methods

    companion object {
        const val MAX_SIZE = 10L * Integer.MAX_VALUE
        const val MAX_BUCKET_SIZE = Integer.MAX_VALUE
    }
}