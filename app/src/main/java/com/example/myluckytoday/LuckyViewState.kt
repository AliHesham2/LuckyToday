package com.example.myluckytoday

sealed class LuckyViewState {
    //ideal
    object Ideal:LuckyViewState()
    //text
    data class LuckyText(val text:String):LuckyViewState()
    //error
    data class Error(val error:String):LuckyViewState()
}