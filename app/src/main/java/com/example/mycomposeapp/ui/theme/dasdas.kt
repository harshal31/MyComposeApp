
package com.example.mycomposeapp.ui.theme

import java.io.File
import kotlin.system.measureTimeMillis


object AutoDpCreator {

    private val units = mutableMapOf(
        1 to "one", 2 to "two", 3 to "three", 4 to "four", 5 to "five", 6 to "six", 7 to "seven", 8 to "eight", 9 to "nine",
        10 to "ten", 11 to "eleven", 12 to "twelve", 13 to "thirteen", 14 to "fourteen", 15 to "fifteen", 16 to "sixteen",
        17 to "seventeen", 18 to "eighteen", 19 to "nineteen"
    )
    private val tens = mapOf(
        2 to "twenty", 3 to "thirty", 4 to "forty", 5 to "fifty", 6 to "sixty", 7 to "seventy", 8 to "eighty", 9 to "ninety"
    )
    private val hundreds = mapOf(3 to "hundred", 4 to "thousand", 5 to "thousand")
    private val builder = StringBuilder()


    private fun IntRange.writeDpFromSelectedRangeIntoFile1(file: File, separator: String = "_", withConst: Boolean = false) {
        val timeTakenToWriteFile = measureTimeMillis {
            if (file.readText().contains("package com.example.mycomposeapp.ui.theme")) {
                println("package there")
            }

            if (this.first == 0 || this.last == 0) {
                throw Exception("Please provide valid IntRange, like 1..10")
            }


            file.appendText("\nimport androidx.compose.ui.unit.dp\n\n")
            for (i in this) {
                file.appendText(numberIntoFormattedDpWord(i, separator, withConst))
            }
        }

        println("File write within $timeTakenToWriteFile mili seconds.")
    }

    private fun numberIntoFormattedDpWord(number: Int, separator: String = "_", withConst: Boolean = false): String {
        val str = number.toString()
        when {
            str.length <= 2 -> convertIntToWordWhichContainDoubleDigit(number, separator, withConst)
            str.length == 3 -> convertIntToWordWhichContainTripleDigit(number, separator, withConst)
            str.length == 4 -> convertIntToWordWhichContainFourDigit(number, separator, withConst)
            str.length == 5 -> convertIntToWordWhichContainFiveDigit(number, separator, withConst)
        }
        println(builder.toString())
        return appendNumberAfterEqualWithDotDp(number)
    }

    private fun convertIntToWordWhichContainFiveDigit(i: Int, separator: String = "_", withConst: Boolean = false) {
        if (i == 0) { handleValueWhichSumIsZero(withConst); return }
        val number = i.toString()
        val numberSubStr = number.substring(0..1).toInt()
        units[numberSubStr]?.let {
            formatStringToRespectiveUnit(it, hundreds[number.length], separator = separator)
            checkLengthOfRemainingNumberAndFormWord(i, 2, separator, withConst)
        } ?: kotlin.run {

        }
    }

    private fun convertIntToWordWhichContainFourDigit(i: Int, separator: String = "_", withConst: Boolean = false) {
        if (i == 0) { handleValueWhichSumIsZero(withConst); return }
        val number = i.toString()
        formatStringToRespectiveUnit(units.getValue(number[0]), hundreds[number.length], separator = separator)
        checkLengthOfRemainingNumberAndFormWord(i, 1, separator, withConst)
    }

    private fun convertIntToWordWhichContainTripleDigit(i: Int, separator: String = "_", withConst: Boolean = false) {
        if (i == 0) { handleValueWhichSumIsZero(withConst); return }
        val number = i.toString()
        formatStringToRespectiveUnit(units.getValue(number[0]), hundreds[number.length], separator = separator)
        checkLengthOfRemainingNumberAndFormWord(i, 1, separator, withConst)
    }

    private fun convertIntToWordWhichContainDoubleDigit(i: Int, separator: String = "_", withConst: Boolean = false) {
        if (i == 0) { handleValueWhichSumIsZero(withConst); return }
        val unitStr = units[i]
        if (unitStr != null) {
            formatLastTwoDigitNumber(unitStr, separator, withConst)
        } else {
            val str = i.toString()
            val (ten, unit) = Pair(str[0].toString().toInt(), str[1].toString().toInt())
            val k = if (unit != 0) separator+(units[unit] ?: "") else ""
            val result = "${tens[ten]}$k"
            formatLastTwoDigitNumber(result, separator, withConst)
        }
    }

    private fun formatLastTwoDigitNumber(value: String, separator: String = "_", withConst: Boolean = false) {
        builder.apply {
            val variableField = if(withConst) "const val " else "val "
            setRange(0, 0,variableField)
            append(value, separator, "dp", " = ")
        }
    }

    private fun appendNumberAfterEqualWithDotDp(i: Int): String {
        val str = builder.apply { append(i, ".", "dp", "\n")
            if (i.toString().endsWith("9")) append("\n")
        }.toString()

        builder.clear()
        return str
    }

    private fun sumIntFromSpecifiedIndex(i: Int, index: Int = 1): Int {
        val str = i.toString()
        val subStr = str.substring(index)
        return if (subStr.toInt() < 10) {
            subStr.map { it.toString().toInt() }.sum()
        } else {
            subStr.toInt()
        }
    }

    private fun formatStringToRespectiveUnit(vararg values: String?, separator: String = "_") {
        builder.append(values.joinToString(separator, postfix = separator))
    }

    private fun Map<Int, String>.getValue(from: Char): String {
        return this[from.toString().toInt()] ?: ""
    }

    private fun handleValueWhichSumIsZero(withConst: Boolean = false) {
        val variableField = if(withConst) "const val " else "val "
        builder.apply { setRange(0,0, variableField); deleteAt(builder.lastIndex); append(" = ") }
    }

    /**
     * This method is useful to format 2, 3, 4 and as per requirement digits
     */
    private fun checkLengthOfRemainingNumberAndFormWord(number: Int, subStrFromIndex: Int = 1, separator: String = "_",  withConst: Boolean = false) {
        val result = sumIntFromSpecifiedIndex(number, subStrFromIndex)
        if (result.toString().length <= 2) convertIntToWordWhichContainDoubleDigit(result, separator, withConst)
        else convertIntToWordWhichContainTripleDigit(result, separator, withConst)
    }

    fun asd(i: Int, separator: String = "_") {
        if(i < 100) {
            convertIntToWordWhichContainDoubleDigit(i, separator)
        } else {
            if (i.toString().length < 5) {
                builder.append(units[i.toString()[0].digitToInt()])
                builder.append(separator)
                val k = hundreds[i.toString().length]
                if (k != null) {
                    builder.append(k)
                    builder.append("_")
                }

                checkLengthOfRemainingNumberAndFormWord(i, 1, separator)
            } else {

            }
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
//        (1..9999).writeDpFromSelectedRangeIntoFile1(File("/Users/harshalchaudhari/AndroidStudioProjects/Compose/app/src/main/java/com/example/mycomposeapp/ui/theme/Utils.kt"), withConst = true)
    }



}