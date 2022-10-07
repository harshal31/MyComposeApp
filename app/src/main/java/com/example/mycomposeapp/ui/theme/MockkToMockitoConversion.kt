package com.example.mycomposeapp.ui.theme

import android.os.Build
import androidx.annotation.RequiresApi
import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.StandardOpenOption

object MockkToMockitoConversion {
    private val mockMap = mutableMapOf(Mocks.EVERY to 0, Mocks.VERIFY to 0, Mocks.JUST_RUN to 0)
    private val writeList = mutableListOf<String>()
    private val mockObjectContainer = mutableListOf<String>()
    private var containerStr = ""
    private var importMockitoOnlyOnce = true

    @RequiresApi(Build.VERSION_CODES.O)
    fun File.convertMockkToMockito() {
        val readFile = readLines()
        val isJavaExtension = extension == "java"

        for (i in readFile) {
            val trimLine = i.trim()

            if (trimLine.contains("import io.mockk", true)) {
                insertMockitoImport(isJavaExtension)
                continue
            } else if (trimLine.contains("@Mockk")) {
                writeList.add(i.trimEnd().removeSuffix("K"))
                continue
            } else if (trimLine.contains("MockKAnnotations.init(this)")) {
                replaceMockkAnnotationIntoMockitoAnnotation(i, isJavaExtension)
                continue
            }else if (trimLine.startsWith("every {", true) && (i.contains("returns", true) || i.contains("throws", true)) && trimLine.length > 6) {
                writeList.add(trimLine.replaceEveryIntoMockitoWhen(isJavaExtension))
                continue
            } else if (trimLine.startsWith("verify(exactly", true) && trimLine.length > 6 && trimLine.endsWith("}", true)) {
                writeList.add(trimLine.replaceVerifyIntoMockitoVerify(isJavaExtension = isJavaExtension))
                continue
            } else if (trimLine.substringAfter("=").trim().startsWith("mockk<") && i.contains("<(.*?)>".toRegex())) {
                replaceMockkIntoMockitoMock(i, isJavaExtension)
                continue
            } else if (trimLine.startsWith("mockkObject(") && trimLine.contains("\\((.*?)\\)".toRegex())) {
                replaceMockkObjectIntoMockitoObject(i, isJavaExtension)
                continue
            }  else if (trimLine.startsWith("justRun {") && trimLine.length > 6 && trimLine.endsWith("}", true)) {
                writeList.add(trimLine.replaceJustRunIntoMockitoDoNothing(isJavaExtension))
                continue
            }

            if (trimLine.startsWith("every {", true) || trimLine.startsWith("} returns", true) || trimLine.startsWith("} throws")) {
                handEveryMock(trimLine, isJavaExtension)
                continue
            } else if ((mockMap[Mocks.EVERY] ?: 0) > 0) {
                handEveryMock(trimLine, isJavaExtension)
                continue
            }

            if (trimLine.startsWith("verify(exactly", true)) {
                containerStr += trimLine
                mockMap[Mocks.VERIFY] = mockMap[Mocks.VERIFY]?.plus(1) ?: 0
                continue
            } else if ((mockMap[Mocks.VERIFY] ?: 0) > 0) {
                if (trimLine.startsWith("}", true)) {
                    mockMap[Mocks.VERIFY] = 0
                    containerStr += trimLine
                    writeList.add(containerStr.replaceVerifyIntoMockitoVerify(isJavaExtension = isJavaExtension))
                    containerStr = ""
                } else {
                    containerStr += trimLine
                }
                continue
            }

            if (trimLine.startsWith("justRun {", true)) {
                containerStr += trimLine
                mockMap[Mocks.JUST_RUN] = mockMap[Mocks.JUST_RUN]?.plus(1) ?: 0
                continue
            } else if ((mockMap[Mocks.JUST_RUN] ?: 0) > 0) {
                if (trimLine.startsWith("}", true)) {
                    mockMap[Mocks.JUST_RUN] = 0
                    containerStr += trimLine
                    writeList.add(containerStr.replaceJustRunIntoMockitoDoNothing(isJavaExtension))
                    containerStr = ""
                } else {
                    containerStr += trimLine
                }
                continue
            }
            writeList.add(i)
        }

        Files.newBufferedWriter(this.toPath(), StandardOpenOption.TRUNCATE_EXISTING)
        Files.write(this.toPath(), writeList, StandardCharsets.UTF_8)
        writeList.clear()
        mockObjectContainer.clear()
    }

    private fun handEveryMock(i: String, isJavaExtension: Boolean) {
        if (i.contains("returns")) {
            mockMap[Mocks.EVERY] = 0
            containerStr += i
            writeList.add(containerStr.replaceEveryIntoMockitoWhen(isJavaExtension))
            containerStr = ""
        } else {
            mockMap[Mocks.EVERY] = mockMap[Mocks.EVERY]?.plus(1) ?: 0
            containerStr  += i
        }
    }

    private fun String.replaceEveryIntoMockitoWhen(isJavaExtension: Boolean): String {
        val splitStrByDot = extractStringBetweenCurlyBraces()
        val mockitoWhen = if (isJavaExtension) "when" else "`when`"
        val splitZeroCamelCase = splitStrByDot[0].toCamelCase()
        val whenObject = if (mockObjectContainer.contains(splitZeroCamelCase)) splitZeroCamelCase else splitStrByDot[0]
        val throwOrReturn = this.split(" ").thenReturnOrThenThrow()

        return formatAsPerExtension("        Mockito.$mockitoWhen($whenObject.${splitStrByDot[1]}).$throwOrReturn", isJavaExtension)
    }

    private fun String.replaceVerifyIntoMockitoVerify(isMockitoTimes: Boolean = true, isJavaExtension: Boolean): String {
        val splitStrByDot = extractStringBetweenCurlyBraces()
        val intMatcher = "[0-9]+".toPattern().matcher(this)
        val howMayTimesFromStr = if (intMatcher.find()) (intMatcher.group() ?: "") else "0"
        val whichMockitoTimesStr = if (isMockitoTimes) "Mockito.times($howMayTimesFromStr)" else "VerificationModeFactory.times($howMayTimesFromStr)"
        val splitZeroCamelCase = splitStrByDot[0].toCamelCase()
        val verifyObject = if (mockObjectContainer.contains(splitZeroCamelCase)) splitZeroCamelCase  else splitStrByDot[0]

        return formatAsPerExtension("        Mockito.verify($verifyObject, $whichMockitoTimesStr).${splitStrByDot[1]}", isJavaExtension)
    }

    private fun String.replaceJustRunIntoMockitoDoNothing(isJavaExtension: Boolean): String {
        val splitStrByDot = extractStringBetweenCurlyBraces()
        val mockitoWhen = if (isJavaExtension) "when" else "`when`"
        val splitZeroCamelCase = splitStrByDot[0].toCamelCase()
        val whenObject = if (mockObjectContainer.contains(splitZeroCamelCase)) splitZeroCamelCase else splitStrByDot[0]

        return formatAsPerExtension("        Mockito.doNothing().$mockitoWhen($whenObject).(${splitStrByDot[1]})", isJavaExtension)
    }

    private fun replaceMockkAnnotationIntoMockitoAnnotation(i: String, isJavaExtension: Boolean) {
        val oldReplace = if (isJavaExtension) "MockKAnnotations.init(this);" else "MockKAnnotations.init(this)"
        val mockAnnotation = i.replace(oldReplace, formatAsPerExtension("MockitoAnnotations.initMocks(this)", isJavaExtension))
        writeList.add(mockAnnotation)
    }

    private fun insertMockitoImport(isJavaExtension: Boolean) {
        if (importMockitoOnlyOnce) {
            if (isJavaExtension) { writeList.add("import static org.mockito.Mockito.*;"); writeList.add("import org.mockito.Mockito.*;") }
            else writeList.add("import org.mockito.Mockito.*")
            importMockitoOnlyOnce = false
        }
    }

    private fun replaceMockkIntoMockitoMock(i: String, isJavaExtension: Boolean) {
        val mockkMatcher = "<(.*?)>".toPattern().matcher(i)
        if(mockkMatcher.find()) {
            val resultBetAngleBrace = mockkMatcher.group(1)
            val mockitoMockFormattingAsPerExtension = formatAsPerExtension(" Mockito.mock($resultBetAngleBrace)", isJavaExtension)
            val mockitoMock = i.replaceAfter("=", mockitoMockFormattingAsPerExtension)
            writeList.add(mockitoMock)
        }
    }

    private fun replaceMockkObjectIntoMockitoObject(i: String, isJavaExtension: Boolean) {
        val builder = StringBuilder()
        val mockkMatcher = "\\((.*?)\\)".toPattern().matcher(i)
        if(mockkMatcher.find()) {
            val resultBetAngleBrace = mockkMatcher.group(1) ?: ""
            val camelCase = resultBetAngleBrace.toCamelCase()
            mockObjectContainer.add(camelCase)
            if (isJavaExtension) {
                builder.append("        ", resultBetAngleBrace, " ",camelCase," = ", "Mockito.mock($resultBetAngleBrace.class);",  "\n")
                builder.append("        ", "UtilsKt.getFinalPrivateVariable(${resultBetAngleBrace}.class, \"INSTANCE\")", ".set(null, $camelCase);", "\n")
            } else {
                builder.append("        ", "val ", camelCase," = ", "Mockito.mock($resultBetAngleBrace::class);",  "\n")
                builder.append("        ", "UtilsKt.getFinalPrivateVariable(${resultBetAngleBrace}.class, \"INSTANCE\")", ".set(null, $camelCase);", "\n")
            }
            writeList.add(i.replace(i, builder.toString()))
        }
    }

    private fun String.toCamelCase(): String {
        var ct = 0
        return split("(?=\\p{Lu})".toRegex()).joinToString("") {
            if (it.isNotBlank() && ct == 0) {
                ++ct
                it.lowercase()
            } else {
                it.trim()
            }
        }
    }

    private fun String.extractStringBetweenCurlyBraces(): List<String> {
        val curlyMatcher = "\\{([^}]+)}".toPattern().matcher(this)
        val strBtnCurlyBraces = if (curlyMatcher.find()) curlyMatcher.group(1) ?: ""  else ""
        return strBtnCurlyBraces.split(".", limit = 2).map{ it.trim() }
    }

    private fun formatAsPerExtension(i: String, isJavaExtension: Boolean): String {
        val str = if (isJavaExtension) ";" else ""
        return i.plus(str)
    }

    private fun List<String>.thenReturnOrThenThrow(): String {
        return if (this[lastIndex - 1].equals("throws", true)) "thenThrow(${this.last().trim()})" else "thenReturn(${this.last().trim()})"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @JvmStatic
    fun main(args: Array<String>) {
        val k =  File("/Users/harshalchaudhari/AndroidStudioProjects/Compose/app/src/main/java/com/example/mycomposeapp/ui/theme/MockClass.txt")
        k.convertMockkToMockito()
    }
}

enum class Mocks { EVERY, VERIFY, JUST_RUN }
