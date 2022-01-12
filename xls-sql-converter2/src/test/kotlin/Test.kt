import org.junit.jupiter.api.Test
import kotlin.math.pow
import kotlin.math.roundToInt

class Test {

    @Test
    fun `Calc income`() {

        var s = 0.0
        var lin = 0.0
        val addPerMonth = 150 // k
        val percent = 2.0
        val k = 1 + (percent / 100)
        for (i in 1..(12 * 10).toInt()) {
            s = (s + addPerMonth) * k
            lin += addPerMonth
        }

        println("S: ${s.toInt()}k")
        println("lin: ${lin.toInt()}k")
        println("s/lin: ${(s / lin)}")
        println("income after: ${s * percent / 100}")

        println("k per year: ${k.pow(12) - 1}")
    }

    @Test
    fun `Inflation`() {

        val money = 74e6
        val inflation = 0.08 // per year
        val k = 0.26 // per year
        val pureK = k - inflation

        println("Чистая доходность: ${pureK}")
        println("Money to spend per month: ${(money * pureK / 12 / 1000).roundToInt()}k")

        for(i in 1..100) {
            val profitability = calcProfitability(i * 1e6, inflation, 1000e3)
            if (profitability < 0.1) break
            println("$i mln -> ${(profitability * 1000).toInt() / 10.0}%")
        }

        /*

        капитал и доходность, чтобы иметь в месяц 100к на траты при инфляции 8%:
        5  млн 32%
        10 млн 20%
        20 млн 14%
        30 млн 12%
        40 млн 11%
        50 млн 10.4%
        60 млн 10%

        */
    }

    fun calcProfitability(initialMoney: Double, inflation: Double, requiredMonthIncome: Double): Double {
        return (requiredMonthIncome * 12 / initialMoney) + inflation
    }
}
