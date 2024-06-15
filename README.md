# FRESHIE - Veggie Freshness Check App

FRESHIE is an Android application designed to help users determine the freshness of their fruits and vegetables. By leveraging the Gemini API's photo reasoning model, FRESHIE can analyze images and provide instant feedback on whether the produce is fresh or spoiled.

## Features

- **Image Selection from Gallery:** Users can select images of fruits and vegetables from their device's gallery.
- **Real-Time Image Capture:** Users can capture images directly from the app using the device's camera.
- **Image Removal:** Easily remove unwanted images from the selection list with a simple close ("X") button.
- **Freshness Check:** Analyze images to determine if the produce is fresh or spoiled.

## How It Works

1. **Select or Capture Images:**
   - Users can either choose images from their gallery or take new photos using the camera.
   
2. **Review and Manage Selected Images:**
   - Selected images are displayed in a list, where users can review and remove any unnecessary ones.
   
3. **Analyze Images:**
   - The app sends the selected images to the Gemini photo reasoning model for analysis.
   - The model returns whether the produce is a fruit or vegetable, and if so, whether it is fresh or spoiled.

### Example Output

If two images are given (one of a spoiled banana and another of a fresh apple), the output will be:

1. Banana: SPOILED
2. Apple: FRESH


## Getting Started

### Prerequisites

- Android Studio
- Android device or emulator

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/FRESHIE.git

2. Open the project in Android Studio.
3. Build and run the project on your device or emulator.

### Usage

1. **Open the app.**
2. **Select images** from your gallery or **capture new photos** using the camera.
3. **Review** your selected images and remove any unnecessary ones using the close ("X") button.
4. **Tap the "Go" button** to analyze the images.
5. **View the results**, which indicate whether each fruit or vegetable is fresh or spoiled.

## Technical Details

### PhotoReasoningScreen Composable

The `PhotoReasoningScreen` composable function is responsible for generating the UI for selecting and displaying images, as well as for triggering the image analysis. It includes:

- Image selection from the gallery
- Real-time image capture using the camera
- Display of selected images with an option to remove them
- Invocation of the Gemini API for image analysis

### Gemini PhotoReasoning Model Prompt

The model prompt guides the image analysis process:

Objective:
To generate detailed responses about the freshness of fruits and vegetables in provided images.

Instructions:
1. For each image given as input to the model, follow steps 2 and 3. For multiple images, provide the output for each file with its position.
   Example for two files:
   ```
   1. <output of the first image>
   2. <output of the second image>
   ```
3. Identify the Subject:
   - Check if the given image(s) is of any fruit or vegetable.
   - If not, return: `Not a picture of fruit or vegetable`.
   - If yes, proceed to step 3.

4. Assess Freshness:
   - Determine if the fruit or vegetable is spoiled or fresh.
   - If spoiled, output: <fruit/vegetable name>: SPOILED.
   - If fresh, output: <fruit/vegetable name>: FRESH.

### Example Outputs

- Single Image Example:
  - Input: Image of a fresh Banana
  - Output: `Banana: FRESH`
  - Input: Image of a spoiled Apple
  - Output: `Apple: SPOILED`

- Multiple Images Example:
  - Input: First image of a spoiled Banana, second image of a fresh Apple
  - Output:
    ```
    1. Banana: SPOILED
    2. Apple: FRESH
    ```

## Contributing

Contributions are welcome! Please read the [CONTRIBUTING.md](CONTRIBUTING.md) for details on the code of conduct, and the process for submitting pull requests.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- Thanks to the Gemini API team for their powerful photo reasoning model.
- Special thanks to the contributors who have helped improve this project.
