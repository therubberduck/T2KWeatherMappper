package mock

import kotlin.math.abs
import kotlin.random.Random


class Random: IRandom {
    private val rand = Random.Default

    override fun rand(): Double {
        return abs(rand.nextDouble() - 1)
    }
}