package com.example.myluckytoday

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var text:TextView
    private lateinit var viewModel:LuckyViewModel
    private var flag = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button = findViewById<MaterialButton>(R.id.button)
         text = findViewById(R.id.LuckyText)
        viewModel = ViewModelProvider(this).get(LuckyViewModel::class.java)
        render()
        button.setOnClickListener {
            //send
            if (flag == 0){
                flag++
                lifecycleScope.launch {
                    viewModel.intentChannel.send(LuckyIntent.ShowLucky)}
            }}
    }
    private fun render(){
        lifecycleScope.launchWhenStarted {
            viewModel.data.collect {
                when(it){
                    is LuckyViewState.Ideal ->{text.text = this@MainActivity.resources.getString(R.string.IDEAL) }
                    is LuckyViewState.LuckyText -> {text.text = it.text}
                    is LuckyViewState.Error -> { text.text = it.error }
                }
            }
        }
    }
}