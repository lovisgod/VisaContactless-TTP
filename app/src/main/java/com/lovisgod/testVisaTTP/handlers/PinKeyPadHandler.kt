package com.lovisgod.testVisaTTP.handlers

import android.view.TextureView
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatButton
import com.lovisgod.testVisaTTP.R

object PinKeyPadHandler {

    fun handleKeyButtonClick(keyBoardClick: KeyBoardClick, view: View) {
        view.findViewById<TextureView>(R.id.btn1).clickThis { keyBoardClick.onNumClick("1") }
        view.findViewById<TextureView>(R.id.btn2).clickThis { keyBoardClick.onNumClick("2") }
        view.findViewById<TextureView>(R.id.btn3).clickThis { keyBoardClick.onNumClick("3") }
        view.findViewById<TextureView>(R.id.btn4).clickThis { keyBoardClick.onNumClick("4") }
        view.findViewById<TextureView>(R.id.btn5).clickThis { keyBoardClick.onNumClick("5") }
        view.findViewById<TextureView>(R.id.btn6).clickThis { keyBoardClick.onNumClick("6") }
        view.findViewById<TextureView>(R.id.btn7).clickThis { keyBoardClick.onNumClick("7") }
        view.findViewById<TextureView>(R.id.btn8).clickThis { keyBoardClick.onNumClick("8") }
        view.findViewById<TextureView>(R.id.btn9).clickThis { keyBoardClick.onNumClick("9") }
        view.findViewById<TextureView>(R.id.btn0).clickThis { keyBoardClick.onNumClick("0") }
        view.findViewById<ImageView>(R.id.btnBack).clickThis { keyBoardClick.onBackSpace() }
//        view.findViewById<ImageView>(R.id.btn_clear).clickThis { keyBoardClick.onClear() }
        view.findViewById<AppCompatButton>(R.id.btnConfirm).clickThis { keyBoardClick.onSubmitButtonClick() }

    }

    fun View.clickThis(whatToDo: () -> Unit) {
        this.setOnClickListener {
            whatToDo()
        }
    }

}

interface KeyBoardClick {
    fun onNumClick(num: String)
    fun onSubmitButtonClick()
    fun onBackSpace()
    fun onClear()
}