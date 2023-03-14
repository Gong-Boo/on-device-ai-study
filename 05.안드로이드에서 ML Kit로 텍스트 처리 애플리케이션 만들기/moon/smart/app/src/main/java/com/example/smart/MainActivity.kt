package com.example.smart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.mlkit.nl.smartreply.SmartReply
import com.google.mlkit.nl.smartreply.TextMessage

class MainActivity : AppCompatActivity() {

    var outputText = ""
    var conversation: ArrayList<TextMessage> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val generateButton = findViewById<Button>(R.id.generate)

        initializeConversation()
        generateButton.setOnClickListener {
            generate()
        }
    }

    fun initializeConversation() {

        val conversationView = findViewById<TextView>(R.id.output)

        val friendName: String = "Nizhoni"
        addConversationItem("Hi, good morning!")
        addConversationItem("Oh, hey -- how are you?", friendName)
        addConversationItem("Just got up, thinking of heading out for breakfast")
        addConversationItem("Want to meet up?", friendName)
        addConversationItem("Sure, what do you fancy?")
        addConversationItem("Just coffee, or do you want to eat?", friendName)

        conversationView.text = outputText
    }

    fun generate() {
        val txtResult = findViewById<TextView>(R.id.result)

        val smartReplyGenerator = SmartReply.getClient()
        smartReplyGenerator.suggestReplies(conversation)
            .addOnSuccessListener { result ->
                txtResult.setText(result.suggestions[0].text)
            }
    }



    private fun addConversationItem(item: String) {
        outputText += "Me : $item\n"
        conversation.add(
            TextMessage.createForLocalUser(
                item, System.currentTimeMillis()
            )
        )
    }

    private fun addConversationItem(item: String, who: String) {
        outputText += who + " : " + item + "\n"
        conversation.add(
            TextMessage.createForRemoteUser(
                item, System.currentTimeMillis(), who
            )
        )
    }
}