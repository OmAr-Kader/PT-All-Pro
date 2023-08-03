package com.pt.web

open class TextRecognizer(private val ctx: android.content.Context, private val webListener: ExtractorListener) {

    fun detectedText(scannerGallery: android.net.Uri) {
        ctx.runCatching {
            com.google.mlkit.vision.text.TextRecognition.getClient(
                com.google.mlkit.vision.text.latin.TextRecognizerOptions.DEFAULT_OPTIONS
            ).process(com.google.mlkit.vision.common.InputImage.fromFilePath(ctx, scannerGallery)).addOnSuccessListener {
                webListener.onTextExtracted(it?.text)
            }.addOnFailureListener {
                webListener.onTextExtracted(null)
            }
        }
    }

    @FunctionalInterface
    fun interface ExtractorListener {
        fun onTextExtracted(txt: String?)
    }
}