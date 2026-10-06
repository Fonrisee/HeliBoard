package helium314.keyboard.latin

import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions

object OfflineTranslator {
    private val cache = HashMap<String, Translator>()

    // from/to: "ru", "en"
    fun translate(text: String, from: String, to: String, onResult: (String?) -> Unit) {
        val translator = cache.getOrPut("$from-$to") {
            Translation.getClient(
                TranslatorOptions.Builder()
                    .setSourceLanguage(from)
                    .setTargetLanguage(to)
                    .build()
            )
        }
        translator.downloadModelIfNeeded(DownloadConditions.Builder().build())
            .addOnSuccessListener {
                translator.translate(text)
                    .addOnSuccessListener { onResult(it) }
                    .addOnFailureListener { onResult(null) }
            }
            .addOnFailureListener { onResult(null) }
    }
}
