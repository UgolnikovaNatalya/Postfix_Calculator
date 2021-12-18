package com.example.view


import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import javafx.stage.FileChooser
import tornadofx.*
import java.io.File
import java.lang.IndexOutOfBoundsException
import java.util.*
import kotlin.math.*

/**
 * Данная программа является утилитой для калькулятора на базе ОС Windows.
 * Так же эта утилита может быть запущена на машине, где установлена JAVA.
 * Для работы программы необходимо запустить файл программы CalculateTornado.
 */
/**
 * Main view основной класс, где описан интерфейс и работа программы.
 * @property resultText поле в котором отображаются вычисления, произведенные после чтения файла
 * @property resultText2 информационное поле, которое сообщает о загрузке/не загрузке файла и ошибках в нем

 * @property root переопределение параметра для создания интерфейса, настройки отображения элементов интерфейса
 * @property vbox способ отображения интерфейса
 * @property label поле для отображения информации
 * @property scrollpane панель позволяющая прокручивать информацию в случае, если она не помещается целиком
 * @property button кнопка, при нажатии на которую запускаются методы openFile(), parseLine(), calculating()
 * @exception Exception обработка ошибки в случае, если в документе есть неверная запись
 *
 * @constructor Create empty Main view
 * @author Угольникова Наталья
 */

class MainView : View("Калькулятор") {

    private var resultText = SimpleStringProperty()
    private var resultText2 = SimpleStringProperty()

    override val root = vbox {

        alignment = Pos.CENTER
        spacing = 20.0
        paddingAll = 15

        label(
            text = "Загрузите файл с разрешением (.txt)"
        )

        button("Загрузить") {
            useMaxWidth = true
            action {
                val openedFile = openFile()
                if (openedFile != null) {
                    parseLine(openedFile)
//                    val time = measureTime { parseLine(openedFile) }
//                    println("Time: $time")
                }
            }
        }

        scrollpane {
            label(resultText) {
                paddingAll = 15
                bind(resultText)
            }
        }

        label(resultText2) {
            bind(resultText2)
        }
    }

    /**
     * Open file функция для загрузки файла.
     * @property FileChooserMode.Single - параметр, ограничивающий одновременную загрузку нескольких файлов
     * @property "Text file *.txt" - параметр описывающий расширение, в котором необходимо загрузить файл
     * @param <ef> переменная, содержит в себе массив из загруженных файлов
     * @property <fn> переменная, которая содержит выбранный файл
     * @exception IndexOutOfBoundsException обработка исключения в случае, если файл не будет загружен
     * @throws Exception выводит на экран информацию о том, что файл не был загружен
     *
     * @return fn функция возвращает загруженный файл
     */

     private fun openFile(): File? {
        try {
            val ef = arrayOf(FileChooser.ExtensionFilter("Text file *.txt", "*.txt"))
            val fn: List<File> = chooseFile("Выберите файл", ef, null, FileChooserMode.Single)

            return if (fn.isNotEmpty()) {
                resultText2.set("Файл загружен.")
                fn.get(0)
            } else {
                resultText2.set("Файл не был загружен.")
                null
            }
        } catch (e: NullPointerException) {
            throw Exception("")
        }

    }

    /**
     * Parse line функция чтения файла по строкам
     * @param file параметр, который принимает функция для его дальнейшего чтения и вычисления
     * @property result переменная, являющаяся изменяемым листом типа String. Хранит в себе строки из файла
     * @property str переменная типа String, сохраняющая значения каждой строки файла и результат ее вычисления
     * @property Math.round математическая функция для округления чисел до сотых
     * @property calculating вызов функции вычисления математических выражений
     * @return result возвращает прочтенный и вычесленный лист типа String
     */

    private fun parseLine(file: File): List<String> {

        val result = mutableListOf<String>()
        var str: String = ""

        try {
            file.useLines { lines ->
                lines.forEach {
                    str += "$it = "
                    resultText.set(str)
                    str += "${Math.round(calculating(it) * 1000.0) / 1000.0}\n"
                    result.add(it)
                }
            }

        } catch (e: NullPointerException) {
            resultText2.set("Ошибка в файле!")
            throw Exception("")
        }
        resultText.set(str)
        return result

    }

    /**
     * Calculating функция вычисления математических выражений в постфиксной записи
     * @param str параметр, который принимает функция для дальнейших вычислений
     * @property strings переменная, являющаяся Stack типа Double (тип записи LIFO)
     * @throws EmptyStackException обработка исключений, в случае ошибочной записи значений в файле
     *
     * @return stack.pop возвращает последнее значение, сохраненное в Stack
     */


    private fun calculating(str: String): Double {

        val strings = str.split(" ".toRegex()).toTypedArray()
        val stack = Stack<Double>()

        try {
            for (i in strings.indices) {
                if (isNumber(strings[i])) {
                    stack.push(strings[i].toDouble())
                } else {
                    val tmp1 = stack.pop()
                    when (strings[i]) {
                        "+" -> {
                            val tmp2 = stack.pop()
                            stack.push(tmp1 + tmp2)
                        }
                        "-" -> {
                            val tmp2 = stack.pop()
                            stack.push(tmp2 - tmp1)
                        }
                        "*" -> {
                            val tmp2 = stack.pop()
                            stack.push(tmp1 * tmp2)
                        }
                        "/" -> {
                            val tmp2 = stack.pop()
                            stack.push(tmp2 / tmp1)
                        }
                        "sin" -> stack.push(sin(tmp1))
                        "cos" -> stack.push(cos(tmp1))
                    }
                }
            }

        } catch (e: EmptyStackException) {
            resultText2.set("Ошибка в файле!")
            throw Exception("")
        }
        return stack.pop()
    }

    /**
     * Is number функция проверки соответствия знака из файла типу Double
     *
     * @param string на вход принимает переменную со значением String
     * @return возвращает true, если значение соответствует типу Boolean
     */
    private fun isNumber(string: String): Boolean {
        return try {
            string.toDouble()
            true
        } catch (ex: NumberFormatException) {
            false
        }
    }
}
