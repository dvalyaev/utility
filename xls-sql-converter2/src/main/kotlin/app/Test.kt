import java.math.BigDecimal

var a = 5
infix fun BigDecimal.eq(other: BigDecimal) = this.compareTo(other)==0
 fun BigDecimal.equals(other: Any?) = this.compareTo(BigDecimal(other.toString()))==0

 fun BigDecimal.isZero() = this.signum() == 0

private val FIND_WAVES_BATCH_FOR_CONTAINER: String = """
              SELECT orderKey, loc, sum(QTY) itemsCount 
            FROM wmwhse1.PICKDETAIL 
                WHERE id = :containerId 
            GROUP BY ORDERKEY, LOC
            """.trimIndent()

fun main() {
    var func: () -> Unit = {
        val t = a
        println(t)}
    a = 10
    func()

    a.run {  }

    val f: (String) -> Int = { fun1(it, "abc") }

    println(f("tttt"))

    val list: List<CharSequence> = listOf("A").filterIsInstance<CharSequence>()

    var t: String? = null


//    when {
//        t.isNullOrBlank() -> {
//            println("error")
//            throw RuntimeException()
//        }
//        else -> println(t)
//    }

    println(FIND_WAVES_BATCH_FOR_CONTAINER)

    var x = BigDecimal("1.0")
    var y = ++x
    println(BigDecimal("1.0") eq BigDecimal("1.00"))
    println(BigDecimal("0.0000").isZero())
    println(BigDecimal("0.0000") eq BigDecimal.ZERO)
    println(BigDecimal("0.0000") > BigDecimal.ZERO)

    val s: String? = "dgs"
    s.takeUnless { it.isNullOrBlank() }

    println("2" + 2)



    val numbers = listOf("1", "2", "3", "4", "5")

    println(numbers.chunked(2))
    }

fun fun1( a: String,  b: String): Int = a.length + b.length

fun fun2(a: String): Int = a.length
