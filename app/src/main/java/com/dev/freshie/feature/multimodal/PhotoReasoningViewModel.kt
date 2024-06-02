package com.dev.freshie.feature.multimodal

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PhotoReasoningViewModel(
    private val generativeModel: GenerativeModel,
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
        //val prompt = context.getString(R.string.photo_reasoning_prompt)
        val prompt = "Objective:\n" +
                "To generate detailed responses about the freshness of fruits and vegetables in provided images.\n" +
                "\n" +
                "Instructions:\n" +
                "1. For each image given as input to the model, follow steps 2 and 3. For multiple images, provide the output for each file with its position. \n" +
                "   Example for two files:\n" +
                "\n" +
                "   1. <output of the first image>\n" +
                "\n" +
                "   2. <output of the second image>\n" +
                "\n" +
                "\n" +
                "2. Identify the Subject:\n" +
                "   - Check if the given image(s) is of any fruit or vegetable.\n" +
                "   - If not, return: `Not a picture of fruit or vegetable`.\n" +
                "   - If yes, proceed to step 3.\n" +
                "\n" +
                "3. Assess Freshness:\n" +
                "   - Determine if the fruit or vegetable is spoiled or fresh.\n" +
                "   - If spoiled, output: `<fruit/vegetable name>: SPOILED`.\n" +
                "   - If fresh, output: `<fruit/vegetable name>: FRESH`.\n" +
                "\n" +
                "Examples:\n" +
                "\n" +
                "- Single Image Example:\n" +
                "  - Input: Image of a fresh Banana\n" +
                "  - Output: `Banana: FRESH`\n" +
                "\n" +
                "  - Input: Image of a spoiled Apple\n" +
                "  - Output: `Apple: SPOILED`\n" +
                "\n" +
                "- Multiple Images Example:\n" +
                "  - Input: First image of a spoiled Banana, second image of a fresh Apple\n" +
                "  - Output:\n" +
                "    1. Banana: SPOILED\n" +
                "    2. Apple: FRESH\n" +
                "\n" +
                "Combined Example:\n" +
                "If two photos are given, the first one is of a spoiled Banana and the second one is of a fresh Apple, then the output should be:\n" +
                "1. Banana: SPOILED\n" +
                "2. Apple: FRESH\n"
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
