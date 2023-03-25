package com.example.readble3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.PriorityQueue;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ImageView image_view;
    private Button button_text, take_new;
    private Bitmap mSelectedImage;
    private TextView read_text;
    //private Bitmap mSelectedImage =  BitmapFactory.decodeFile("C:\\Users\\dutta\\AndroidStudioProjects\\hacking-amirite\\app\\src\\main\\assets\\Please_walk_on_the_grass.jpg");
    //private GraphicOverlay mGraphicOverlay;
    // Max width (portrait mode)
    private Integer mImageMaxWidth;
    // Max height (portrait mode)
    private Integer mImageMaxHeight;
    private Bundle extras;
    private String mobileTxt;
    private String emailTxt;
    private String nameTxt;

    Menu speedMenu;

    TextToSpeech textToSpeech;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (textToSpeech!=null)
        {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }
    /**
     * Number of results to show in the UI.
     */
    private static final int RESULTS_TO_SHOW = 3;
    /**
     * Dimensions of inputs.
     */
    private static final int DIM_BATCH_SIZE = 1;
    private static final int DIM_PIXEL_SIZE = 3;
    private static final int DIM_IMG_SIZE_X = 224;
    private static final int DIM_IMG_SIZE_Y = 224;

    private final PriorityQueue<Map.Entry<String, Float>> sortedLabels =
            new PriorityQueue<>(
                    RESULTS_TO_SHOW,
                    new Comparator<Map.Entry<String, Float>>() {
                        @Override
                        public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float>
                                o2) {
                            return (o1.getValue()).compareTo(o2.getValue());
                        }
                    });
    /* Preallocated buffers for storing image data. */
    private final int[] intValues = new int[DIM_IMG_SIZE_X * DIM_IMG_SIZE_Y];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        extras = getIntent().getExtras();
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        image_view = findViewById(R.id.image_view);

        button_text = findViewById(R.id.button_text);
        take_new = findViewById(R.id.take_new);
        take_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, com.example.readble3.CameraView.class);
                startActivity(i);
            }
        });
        read_text = findViewById(R.id.read_text);
        // mGraphicOverlay = findViewById(R.id.graphic_overlay);
        button_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    setImage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        if((ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
                || (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED))
        {
            Log.i(TAG, "trying to request");
            ActivityCompat.requestPermissions
                    (MainActivity.this, new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },225);
        }
        else
            Log.i(TAG, "Permissions are granted");


        mobileTxt = getIntent().getStringExtra("mobileTxt");
        emailTxt = getIntent().getStringExtra("emailTxt");
        nameTxt = getIntent().getStringExtra("nameTxt");

        Button chat_button = findViewById(R.id.chat_button);
        chat_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ChatMainActivity.class);
                intent.putExtra("emailTxt",emailTxt);
                intent.putExtra("mobileTxt",mobileTxt);
                intent.putExtra("nameTxt",nameTxt);
                startActivity(intent);

            }
        });





    }
    private void setImage() throws IOException {
        //mSelectedImage= getBitmapFromAsset(this, "handwriting_test.jpg");
        //ContentValues contentValues = new ContentValues();
        //contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "1661045619776");
        //contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
        //Uri.Builder builder = new Uri.Builder();
        //builder.appendPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString())
//                .appendPath("1661045619776.jpg");
//        Log.i(TAG, builder.build().toString());
//        mSelectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), builder.build());

        final BitmapFactory.Options options = new BitmapFactory.Options();
        //options.inScaled = true;
        options.inSampleSize = 3;
        options.inJustDecodeBounds = false;
        File file = new File("/storage/emulated/0/Pictures/"+String.valueOf(extras.getLong("ts"))+".jpg");
        if(file.exists())
            Log.i(TAG, "yes file exists");
        else
            Log.i(TAG, "no, file does not exist");
        mSelectedImage = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        if (mSelectedImage == null)
            Log.i(TAG, "but bitmap is null");
        else
            Log.i(TAG, "bitmap is not null");
        Pair<Integer, Integer> targetedSize = getTargetedWidthHeight();

        int targetWidth = targetedSize.first;
        int maxHeight = targetedSize.second;

        // Determine how much to scale down the image
        float scaleFactor =
                Math.max(
                        (float) mSelectedImage.getWidth() / (float) targetWidth,
                        (float) mSelectedImage.getHeight() / (float) maxHeight);

        Bitmap resizedBitmap =
                Bitmap.createScaledBitmap(
                        mSelectedImage,
                        (int) (mSelectedImage.getWidth() / scaleFactor),
                        (int) (mSelectedImage.getHeight() / scaleFactor),
                        true);

        image_view.setImageBitmap(resizedBitmap);
        mSelectedImage = resizedBitmap;

        runTextRecognition();
    }
    private void runTextRecognition() {

        InputImage image = InputImage.fromBitmap(mSelectedImage, 0);
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        button_text.setEnabled(false);
        recognizer.process(image)
                .addOnSuccessListener(
                        new OnSuccessListener<Text>() {
                            @Override
                            public void onSuccess(Text texts) {
                                button_text.setEnabled(true);
                                processTextRecognitionResult(texts);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Task failed with an exception
                                button_text.setEnabled(true);
                                e.printStackTrace();
                            }
                        });
    }

    @SuppressLint("SetTextI18n")
    private void processTextRecognitionResult(Text texts) {
        List<Text.TextBlock> blocks = texts.getTextBlocks();
        if (blocks.size() == 0) {
            showToast("No text found");
            return;
        }
        //mGraphicOverlay.clear();
        for (int i = 0; i < blocks.size(); i++) {
            List<Text.Line> lines = blocks.get(i).getLines();
            for (int j = 0; j < lines.size(); j++) {
                List<Text.Element> elements = lines.get(j).getElements();
                for (int k = 0; k < elements.size(); k++) {
                    //GraphicOverlay.Graphic textGraphic = new TextGraphic(mGraphicOverlay, elements.get(k));
                    //mGraphicOverlay.add(textGraphic);
                    if(i==0 && j==0 && k==0)
                        read_text.setText(elements.get(k).getText() + " ");

                    else
                        read_text.append(elements.get(k).getText() + " ");

                }
                read_text.append("\n");
                textToSpeech=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int i) {
                        if(i!=TextToSpeech. ERROR) {
                            textToSpeech.setLanguage(Locale.US);
                        }
                    }
                });
                read_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String txt_speak = read_text.getText().toString();
                        textToSpeech.speak(txt_speak,TextToSpeech.QUEUE_FLUSH,null,null);
                        Toast.makeText(MainActivity.this, "", Toast.LENGTH_LONG).show();
                    }
                });

            }
        }
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    // Functions for loading images from app assets.

    // Returns max image width, always for portrait mode. Caller needs to swap width / height for
    // landscape mode.
    private Integer getImageMaxWidth() {
        if (mImageMaxWidth == null) {
            // Calculate the max width in portrait mode. This is done lazily since we need to
            // wait for
            // a UI layout pass to get the right values. So delay it to first time image
            // rendering time.
            mImageMaxWidth = image_view.getWidth();
        }

        return mImageMaxWidth;
    }

    // Returns max image height, always for portrait mode. Caller needs to swap width / height for
    // landscape mode.
    private Integer getImageMaxHeight() {
        if (mImageMaxHeight == null) {
            mImageMaxHeight =
                    image_view.getHeight();
        }

        return mImageMaxHeight;
    }

    // Gets the targeted width / height.
    private Pair<Integer, Integer> getTargetedWidthHeight() {
        int targetWidth;
        int targetHeight;
        int maxWidthForPortraitMode = getImageMaxWidth();
        int maxHeightForPortraitMode = getImageMaxHeight();
        targetWidth = maxWidthForPortraitMode;
        targetHeight = maxHeightForPortraitMode;
        return new Pair<>(targetWidth, targetHeight);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.speedIs0_5x:
                Log.d(TAG, "Clicked on About!");
                textToSpeech.setSpeechRate(0.5F);
                // Code for About goes here
                break;
            case R.id.speedIs0_75x:
                Log.d(TAG, "Clicked on Help!");
                textToSpeech.setSpeechRate(0.75F);

                // Code for Help goes here
                break;
            case R.id.speedIs1x:
                Log.d(TAG, "User signed out");
                textToSpeech.setSpeechRate(1.0F);

                // SignOut method call goes here
                break;
            case R.id.speedIs1_5x:
                textToSpeech.setSpeechRate(1.5F);

                break;
            case R.id.speedIs2_0x:
                textToSpeech.setSpeechRate(2.0F);

                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }
}


