package com.dev.freshie.feature.multimodal

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.freshie.R
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.internal.NoOpContinuation.context
import kotlinx.coroutines.launch
//import kotlin.coroutines.jvm.internal.CompletedContinuation.context

class PhotoReasoningViewModel(
    private val generativeModel: GenerativeModel
) : ViewModel() {

    private val _uiState: MutableStateFlow<PhotoReasoningUiState> =
        MutableStateFlow(PhotoReasoningUiState.Initial)
    val uiState: StateFlow<PhotoReasoningUiState> =
        _uiState.asStateFlow()

    fun reason(
        //userInput: String,
        selectedImages: List<Bitmap>
    ) {
        _uiState.value = PhotoReasoningUiState.Loading
        val prompt = "Look at the image(s), and then answer the following question: is the given image(s) is of fruits or vegetables? if yes then check if the food item is fresh or spoiled. Answer only in 'Fresh' or 'Spoil' and if the image is not of fruits or vegetables simply say 'Image is not a fruit or vegetable'"
        var testReport = "0 "

        val image : Bitmap = selectedImages.get(0)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val inputContent = content {

                    /*for (bitmap in selectedImages) {
                        image(bitmap)
                        //testReport += "added images"
                    }*/
                    //val drawable1 = LocalContext.current.resources.getDrawable(R.drawable.food)
                    image(image)
                    text(prompt)
                    //testReport += " , prompt given "
                }

                //var outputContent = ""

                val outputContent = generativeModel.generateContent(
                    content {
                        for (bitmap in selectedImages) {
                            image(bitmap ?: return@content)
                        }
                        //image(selectedImages.get(0) ?: return@content)
                        text(prompt)
                    }
                )
                _uiState.value = PhotoReasoningUiState.Success(outputContent.text ?: "Something went wrong")

                /*var fullResponse = "0 "
                generativeModel.generateContentStream(inputContent).collect { response ->
                    //print(response.text)
                    *//*fullResponse += response.text
                    outputContent += response.text*//*
                    _uiState.value = PhotoReasoningUiState.Success(outputContent)
                }*/


                /*generativeModel.generateContentStream(inputContent)
                    .collect { response ->
                        outputContent += response.text
                        testReport += " , prompt sent "
                        _uiState.value = PhotoReasoningUiState.Success(outputContent)
                        testReport += " , output generated "
                    }*/
            } catch (e: Exception) {
                _uiState.value = PhotoReasoningUiState.Error(e.localizedMessage ?: "")
                testReport += " , exception occur "
            }
        }
    }
}
