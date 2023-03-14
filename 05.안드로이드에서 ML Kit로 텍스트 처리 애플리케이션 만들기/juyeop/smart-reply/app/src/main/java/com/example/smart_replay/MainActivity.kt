package com.example.smart_replay

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import com.example.smart_replay.ui.theme.SmartreplayTheme
import com.google.mlkit.nl.smartreply.SmartReply
import com.google.mlkit.nl.smartreply.TextMessage

class MainActivity : ComponentActivity() {

    private val conversation = ArrayList<TextMessage>()
    private val inputText = mutableStateOf("")
    private val resultText = mutableStateOf("")
    private val onButtonClickListener = {
        generateReply()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartreplayTheme {
                MainScreen(
                    inputText = inputText.value,
                    resultText = resultText.value,
                    onButtonClickListener = onButtonClickListener
                )
            }
        }

        initConversation()
    }

    private fun initConversation() {
        val friendName = "jongwoo"

        addConversationItem("Hi")
        addConversationItem("Hi", friendName)
        addConversationItem("What your name")
        addConversationItem("my name is jongwoo",friendName)
        addConversationItem("oh your name is so good")
        addConversationItem("Thank you what your name?", friendName)
    }

    private fun addConversationItem(item: String) {
        inputText.value += "juyeop : $item\n"
        conversation.add(
            TextMessage.createForLocalUser(item, System.currentTimeMillis())
        )
    }

    private fun addConversationItem(item: String, who: String){
        inputText.value += "$who : $item\n"
        conversation.add(
            TextMessage.createForRemoteUser(item, System.currentTimeMillis(), who)
        )
    }

    private fun generateReply() {
        val client = SmartReply.getClient()
        client
            .suggestReplies(conversation)
            .addOnSuccessListener { result ->
                val str = StringBuilder()

                result.suggestions.map {
                    str.append("${it.text}\n")
                }

                resultText.value = str.toString()
            }
            .addOnFailureListener {
                Log.e("TAG", it.message.toString())
            }
    }
}
