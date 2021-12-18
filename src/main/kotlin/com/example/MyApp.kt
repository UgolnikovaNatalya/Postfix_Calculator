package com.example

import com.example.view.MainView
import javafx.stage.Stage
import tornadofx.App

class MyApp: App(MainView::class, Styles::class)
{
    override fun start(stage: Stage) {
        with(stage){
            width = 600.0
            height = 400.0
        }
        super.start(stage)
    }
}