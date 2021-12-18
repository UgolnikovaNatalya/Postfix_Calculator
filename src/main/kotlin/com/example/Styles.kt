package com.example

import javafx.scene.text.FontWeight
import tornadofx.Stylesheet
import tornadofx.box
import tornadofx.cssclass
import tornadofx.px

/**
 * Styles класс, задающий стиль текста интерфейса,
 * размер, отступы и стиль текста в заголовке программы, а так же
 * стиль текста для окон, выводящих информацию
 *
 * @constructor Create empty Styles
 */

class Styles : Stylesheet() {
    companion object {
        val heading by cssclass()
    }

    init {
        label and heading {
            padding = box(10.px)
            fontSize = 20.px
            fontWeight = FontWeight.BOLD
        }
    }
}