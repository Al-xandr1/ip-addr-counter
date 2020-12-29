package v2

import java.util.concurrent.atomic.AtomicLongArray

class AtomicBitSet(nbits: Int) {
    /**
     * The internal field corresponding to the serialField "bits".
     */
    private lateinit var words: AtomicLongArray

    init {
        if (nbits < 0) throw NegativeArraySizeException("nbits < 0: $nbits")
        initWords(nbits)
    }

    private fun initWords(nbits: Int) {
        words = AtomicLongArray(wordIndex(nbits - 1) + 1)
    }

    private fun check(wordIndex: Int) {
        val wordsRequired = wordIndex + 1
        if (words.length() < wordsRequired) {
            throw IndexOutOfBoundsException("array capacity is not enough: words.length()=${words.length()}, wordsRequired=$wordsRequired")
        }
    }

    /**
     * Sets the bit at the specified index to `true`.
     *
     * @param  bitIndex a bit index
     * @throws IndexOutOfBoundsException if the specified index is negative
     * @since  JDK1.0
     */
    fun set(bitIndex: Int) {
        if (bitIndex < 0) throw IndexOutOfBoundsException("bitIndex < 0: $bitIndex")
        val wordIndex = wordIndex(bitIndex)
        check(wordIndex)
        do {
            val word = words[wordIndex]
            val update = word or (1L shl bitIndex)
        } while (!words.compareAndSet(wordIndex, word, update))
    }

    /**
     * Returns the number of bits set to `true` in this `BitSet`.
     *
     * @return the number of bits set to `true` in this `BitSet`
     * @since  1.4
     */
    fun cardinality(): Int {
        var sum = 0
        for (i in 0 until words.length()) sum += java.lang.Long.bitCount(words[i])
        return sum
    }


    companion object {
        /*
     * BitSets are packed into arrays of "words."  Currently a word is
     * a long, which consists of 64 bits, requiring 6 address bits.
     * The choice of word size is determined purely by performance concerns.
     */
        private const val ADDRESS_BITS_PER_WORD = 6

        /**
         * Given a bit index, return word index containing it.
         */
        private fun wordIndex(bitIndex: Int): Int = bitIndex shr ADDRESS_BITS_PER_WORD
    }
}