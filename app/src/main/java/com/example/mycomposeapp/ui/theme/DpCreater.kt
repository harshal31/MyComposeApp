/**
 * Copyright 2022 Lenovo, All Rights Reserved.
 */
package com.example.mycomposeapp.ui.theme

import java.io.File
import kotlin.system.measureTimeMillis


infix fun IntRange.writeDpFromSelectedRangeIntoFile(file: File) {
    val time = measureTimeMillis {
        val list = mutableListOf<String>()
        for (i in this) {
            transformWordIntoDp(i, list)
        }
        val kl = list.joinToString("\n")
        file.appendText("import androidx.compose.ui.unit.dp\n")
        file.appendText(kl)
    }
    println("File write within $time milli-seconds.")
}


fun transformWordIntoDp(i: Int, list: MutableList<String>) {
    val word = numberToWords(i)
    val str = "val ${word}_dp = $i.dp"
    if (str.endsWith("9.dp", true)) {
        list.add(str.plus("\n"))
    } else {
        list.add(str)
    }
}

fun numberToWords(i: Int): String {
    val single = mutableMapOf(1 to "one", 2 to "two", 3 to "three",
        4 to "four", 5 to "five", 6 to "six", 7 to "seven", 8 to "eight", 9 to "nine", 10 to "ten",
        11 to "eleven", 12 to "twelve", 13 to "thirteen", 14 to "fourteen", 15 to "fifteen", 16 to "sixteen",
        17 to "seventeen", 18 to "eighteen", 19 to "nineteen"
    )
    val power = mapOf(2 to "twenty", 3 to "thirty",4 to "forty",5 to "fifty",
        6 to "sixty",7 to "seventy",8 to "eighty",9 to "ninety"
    )
    val powerTen = mapOf(3 to "hundred", 4 to "thousand")
    val builder = StringBuilder()
    val strI = i.toString()

    if (strI.length == 1) {
        builder.append(single[i])
    } else if(strI.length == 2){
        val res = single[i]
        if (res != null) {
            builder.append(res)
        } else {
            val (first, second) = Pair(strI[0].toString().toInt(), strI[1].toString().toInt())
            builder.append("${power[first]}${if (second != 0) "_".plus(single[second]) else ""}")
        }
    } else {
        val subString = strI.substring(1)
        if (subString.length == 2) {
            if (subString[0] == '0') {
                var k = single[subString[1].toString().toInt()] ?: ""
                if (k.isNotEmpty()) {
                    k = "_".plus(k)
                }
                builder.append("${single[strI[0].toString().toInt()]}_${powerTen[strI.length]}${k}")
            } else {
                val (first, second) = Pair(subString[0].toString().toInt(), subString[1].toString().toInt())
                var k = single[subString.toInt()] ?: "${power[first]}${if (second != 0) "_".plus(single[second]) else ""}"
                if (k.startsWith("_") || k.isNotEmpty()) {
                    k = "_".plus(k)
                }
                builder.append("${single[strI[0].toString().toInt()]}_${powerTen[strI.length]}${k}")
            }
        } else {
            val sub = i.toString()
            var zc = 0
            sub.forEachIndexed { index, _ ->
                val lSub = sub.substring(index)
                if (lSub[0] != '0') {
                    if (lSub.length == 2) {
                        val (first, second) = Pair(lSub[0].toString().toInt(), lSub[1].toString().toInt())
                        var k = single[lSub.toInt()] ?: "${power[first]}${if (second != 0) "_".plus(single[second]) else ""}"
                        if (k.isNotEmpty()) {
                            k = "_".plus(k)
                        }
                        builder.append(k)
                    } else if (lSub.length > 2)  {
                        val j = single[lSub[0].toString().toInt()] ?: ""
                        if (j.isNotEmpty()) {
                            builder.append(j)
                            builder.append("_")
                        }
                    }
                    builder.append(powerTen[lSub.length] ?: "")
                } else {
                    ++zc
                    if (zc == 2) {
                        val k = single[sub[sub.lastIndex].toString().toInt()]
                        if (k.isNullOrEmpty().not()) {
                            builder.append("_")
                            builder.append(k)
                        }
                    }
                }
            }
        }
    }

    return builder.toString()
}