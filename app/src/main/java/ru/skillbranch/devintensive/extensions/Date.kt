package ru.skillbranch.devintensive.extensions

import java.text.SimpleDateFormat
import java.util.*

const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR


fun Date.format(pattern:String = "HH:mm:ss dd.MM.yy"):String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.add(value:Int, units:TimeUnits = TimeUnits.SECOND) : Date {
    var time = this.time

    time += when(units){
        TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE -> value * MINUTE
        TimeUnits.HOUR -> value * HOUR
        TimeUnits.DAY -> value * DAY
    }
    this.time = time

    return this
}
fun Date.humanizeDiff(date: Date = Date()): String {
    var difference = date.time - this.time

    fun diff(type: Long, forOne: String = "", forTwoToFour: String = "", other: String = "") : String {
        if (difference < 0) {
            difference = -difference
        }

        return when {
            difference/type in 5..20 -> other
            (difference/type)%10  == 1L  -> forOne
            (difference/type)%10 in 2..4  -> forTwoToFour
            else -> other
        }
    }

    if (difference < 0) {
        return when(-difference) {
            in 0..1*SECOND -> "только что"
            in 1*SECOND..45*SECOND -> "через несколько ${TimeUnits.SECOND.MANY}"
            in 45*SECOND..75*SECOND -> "через ${TimeUnits.MINUTE.ONE}"
            in 75*SECOND..45*MINUTE -> "через ${-difference/ MINUTE}" +
                    " ${diff(MINUTE, TimeUnits.MINUTE.ONE, TimeUnits.MINUTE.FEW, TimeUnits.MINUTE.MANY)}"
            in 45*MINUTE..75*MINUTE -> "через ${TimeUnits.HOUR.ONE}"
            in 75*MINUTE..22*HOUR -> "через ${-difference/ HOUR}" +
                    " ${diff(HOUR, TimeUnits.HOUR.ONE, TimeUnits.HOUR.FEW, TimeUnits.HOUR.MANY)}"
            in 22*HOUR..26*HOUR -> "через ${TimeUnits.DAY.ONE}"
            in 26*HOUR..360*DAY -> "через ${-difference/ DAY}" +
                    " ${diff(DAY, TimeUnits.DAY.ONE, TimeUnits.DAY.FEW, TimeUnits.DAY.MANY)}"
            else -> "более чем через год"
        }
    } else {
        return when(difference) {
            in 0..1*SECOND -> "только что"
            in 1*SECOND..45*SECOND -> "несколько ${TimeUnits.SECOND.MANY} назад"
            in 45*SECOND..75*SECOND -> "${TimeUnits.MINUTE.ONE} назад"
            in 75*SECOND..45*MINUTE -> "${difference/ MINUTE}" +
                    " ${diff(MINUTE, TimeUnits.MINUTE.ONE, TimeUnits.MINUTE.FEW, TimeUnits.MINUTE.MANY)} назад"
            in 45*MINUTE..75*MINUTE -> "${TimeUnits.HOUR.ONE} назад"
            in 75*MINUTE..22*HOUR -> "${difference/ HOUR}" +
                    " ${diff(HOUR, TimeUnits.HOUR.ONE, TimeUnits.HOUR.FEW, TimeUnits.HOUR.MANY)} назад"
            in 22*HOUR..26*HOUR -> "${TimeUnits.DAY.ONE} назад"
            in 26*HOUR..360*DAY -> "${difference/ DAY}" +
                    " ${diff(DAY, TimeUnits.DAY.ONE, TimeUnits.DAY.FEW, TimeUnits.DAY.MANY)} назад"
            else -> "более года назад"
        }
    }
}

enum class TimeUnits(val ONE: String, val FEW: String, val MANY: String) {
    SECOND("секунду", "секунды", "секунд"),
    MINUTE("минуту", "минуты", "минут"),
    HOUR("час", "часа", "часов"),
    DAY("день", "дня", "дней");
}