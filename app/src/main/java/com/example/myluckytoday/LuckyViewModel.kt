package com.example.myluckytoday

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import java.lang.Exception

class LuckyViewModel(app:Application) :AndroidViewModel(app) {
     val intentChannel = Channel<LuckyIntent>(Channel.UNLIMITED)
    private var _luckyText = mutableListOf(app.resources.getString(R.string.your_lucky_today1),app.resources.getString(R.string.your_lucky_today2),app.resources.getString(R.string.your_lucky_today3))
    private var _data = MutableStateFlow<LuckyViewState>(LuckyViewState.Ideal)
    val data :MutableStateFlow<LuckyViewState>
        get() = _data
    init {
        processIntent()
    }

    //process
    private fun processIntent(){
        viewModelScope.launch {
            intentChannel.consumeAsFlow().collect {
                when(it){
                   is LuckyIntent.ShowLucky ->{
                       _luckyText.shuffle()
                       showLuckyText( _luckyText[0])}
                }
            }
        }
    }
    //reduce
    private fun showLuckyText(s: String) {
        viewModelScope.launch {
            _data.value =
            try {
                LuckyViewState.LuckyText(s)
            }catch (e:Exception){
                LuckyViewState.Error(e.message!!)
            }
        }
    }


}