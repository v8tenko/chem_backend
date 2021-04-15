package com.example

import kotlin.math.ln
import kotlin.math.pow

typealias ChemReq = List<List<Double>>

object Solver {

/*
    sample
    val n = listOf(listOf(1, 1), listOf(1, 1)).map { it.map(Int::toDouble) }
    val a = n.new(17.45, 31.46, 18.82, 30.0)
    val b = n.new(60.46, 3.39, 58.38, 10.71)
    val dc = n.new(-1.117, 0.0, -15.61, 0.0)
    val dd = n.new(0.0, -3.77, 0.0, 0.33)
    val dh = n.new(-74.85, 0.0, -115.9, -241.84)
    val ds = n.new(186.19, 205.03, 218.8, 188.74)
    val t = 723.0
 */

    fun solve(elements: ChemReq, da: ChemReq,
              db: ChemReq, dc: ChemReq, dd: ChemReq,
              dh: ChemReq, ds: ChemReq,
              t: Double
    ): ChemRes {
        // входные данные
        // считаем коэффициенты
        val pa = calc(elements, da)
        val pb = calc(elements, db)
        val pc = calc(elements, dc)
        val pd = calc(elements, dd)
        val h = calc(elements, dh) * 1e3
        val s = calc(elements, ds)
        val n = elements.map { it.sum() }.let { it[1] - it[0] }
        val r = 8.314

        println("Коэффициенты:")
        println("a: $pa \nb: $pb \nc: $pc \nc': $pd")

        val a = pa
        val b = pb / 1e3
        val c = pc / 1e6
        val d = pd * 1e5

        val H = (h +
                a * (t - 298.0) +
                (b / 2.0) * (t.pow(2) - 298.0.pow(2)) +
                (c / 3.0) * (t.pow(3) - 298.0.pow(3)) -
                d * (1.0 / t - 1.0 / 298.0)) / 1e3
        println("Расчеты:")
        println("h: ${(h / 1e3).out()} кДж/моль")
        println("H: ${H.out()} кДж/моль")
        val S = s +
                a * ln(t / 298.0) +
                b * (t - 298.0) +
                (c / 2) * (t.pow(2) - 298.0.pow(2)) -
                (d / 2) * (1.0 / (t.pow(2)) - 1 / (298.0.pow(2)))
        println("s: ${s.out()} кДж/моль")
        println("S: ${S.out()} Дж/K*моль")
        val G_298 = (h - 298.0 * s) / 1e3
        println("G (298): ${G_298.out()} кДж/мол")
        val G = (H * 1e3 - t * S) / 1e3
        println("G : ${G.out()} кДж/мол")
        val U_298 = (h - n * r * 298.0) / 1e3
        println("U (298): ${U_298.out()} кДж/мол")
        val U = (H * 1e3 - n * r * t) / 1e3
        println("U: ${U.out()} кДж/мол")
        return ChemRes(
            pa.out(), pb.out(), pc.out(), pd.out(),
            (h / 1e3).out(), H.out(), s.out(), S.out(),
            G_298.out(), G.out(), U_298.out(), U.out()
        )
    }

    /**
     * @param lv - значения слева
     * @param lc - коэфы слева
     */
    fun calc(lc: List<Double>, lv: List<Double>, rc: List<Double>, rv: List<Double>): Double {
        return (rc.indices).map { i -> rc[i] * rv[i] }.sum() - (lc.indices).map { i -> lc[i] * lv[i] }.sum()
    }

    fun calc(elements: List<List<Double>>, d: List<List<Double>>) = calc(elements[0], d[0], elements[1], d[1])

    fun List<List<Double>>.new(vararg values: Double): List<List<Double>> =
        listOf(values.take(get(0).size), values.takeLast(get(1).size))

    fun Double.out() = "%.2f".format(this)

}

data class ChemRes(
    val a: String,
    val b: String,
    val c: String,
    val d: String,
    val h298: String,
    val h: String,
    val s298: String,
    val s: String,
    val g298: String,
    val g: String,
    val u298: String,
    val u: String
)
