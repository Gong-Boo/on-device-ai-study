package com.parkjin.mlkit.reply

import com.google.mlkit.nl.smartreply.SmartReply
import com.google.mlkit.nl.smartreply.SmartReplySuggestionResult
import com.google.mlkit.nl.smartreply.TextMessage
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class ReplyGenerator {

    private val replyGenerator = SmartReply.getClient()

    suspend fun generate(messages: List<TextMessage>): String? {
        return suspendCancellableCoroutine { coroutine ->
            replyGenerator.suggestReplies(messages)
                .addOnSuccessListener { result ->
                    coroutine.resume(
                        if (result.status == SmartReplySuggestionResult.STATUS_SUCCESS) {
                            result.suggestions.firstOrNull()?.text
                        } else {
                            null
                        }
                    )
                }
                .addOnFailureListener(coroutine::resumeWithException)
                .addOnCanceledListener(coroutine::cancel)
        }
    }
}
