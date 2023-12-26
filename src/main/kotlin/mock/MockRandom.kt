package mock

class MockRandom(private val value: Double): IRandom {
    override fun rand(): Double {
        return value
    }
}