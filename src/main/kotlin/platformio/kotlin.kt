package platformio

fun <T1, T2, T3, T4> tuple(t1: T1, t2: T2, t3: T3, t4: T4): NTuple4<T1, T2, T3, T4> {
    return NTuple4(t1, t2, t3, t4)
}

data class NTuple4<T1, T2, T3, T4>(val t1: T1, val t2: T2, val t3: T3, val t4: T4)

infix fun <T1, T2, T3, T4> Triple<T1, T2, T3>.then(t4: T4): NTuple4<T1, T2, T3, T4> {
    return NTuple4(this.first, this.second, this.third, t4)
}
