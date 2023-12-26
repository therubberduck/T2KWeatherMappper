package mock

interface IRandom {
    /**
     * Gets the next random Double value between 0 (exclusive) and 1 (inclusive)
     */
    fun rand(): Double
}