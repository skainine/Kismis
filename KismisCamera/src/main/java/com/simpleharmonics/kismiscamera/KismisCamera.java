package com.simpleharmonics.kismiscamera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.core.view.GestureDetectorCompat;

import com.camerakit.CameraKitView;

import java.io.File;

public class KismisCamera extends FrameLayout {

    private static final String TAG = "KismisCamera";
    private final long MAX_DURATION = 15;

    private ProgressBar pbCountdown;
    private CameraKitView cameraKitView;
    private ImageButton ibTrigger;

    private Context contextHolder;
    private RecordingStatus recordingStatus = RecordingStatus.STOPPED;
    private boolean flag = false;

    private final CountDownTimer countDownTimer = new CountDownTimer(1000 * MAX_DURATION, 100) {

        @Override
        public void onTick(long millisUntilFinished) {
            long progress = MAX_DURATION * 10 - (millisUntilFinished / 100);
            pbCountdown.setProgress((int) progress);
        }

        @Override
        public void onFinish() {
            Log.i(TAG, "Kismis: countDownTimer: onFinish: Time up");
            stopRecording();
        }
    };

    public KismisCamera(Context context) {
        super(context);
        initialize(context);
    }

    public KismisCamera(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public KismisCamera(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initialize(Context context) {
        contextHolder = context;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View inflate = layoutInflater.inflate(R.layout.layout_kismis_camera, KismisCamera.this);

        pbCountdown = inflate.findViewById(R.id.ovc);
        cameraKitView = inflate.findViewById(R.id.tgf);
        ibTrigger = inflate.findViewById(R.id.gvp);
        ibTrigger.setOnClickListener(v -> {
            if (recordingStatus == RecordingStatus.RECORDING) {
                pauseRecording();
            } else {
                startRecording();
            }
        });

        GestureDetectorCompat gestureDetector = new GestureDetectorCompat(context,
                new GestureDetector.OnGestureListener() {
                    @Override
                    public boolean onDown(MotionEvent e) {
                        return false;
                    }

                    @Override
                    public void onShowPress(MotionEvent e) {

                    }

                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        return false;
                    }

                    @Override
                    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                        if (flag && distanceY > 0) {
                            flag = false;
//                            chooseVideo();
                        }
                        return true;
                    }

                    @Override
                    public void onLongPress(MotionEvent e) {

                    }

                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                        return false;
                    }
                });

        KismisCamera.this.setOnTouchListener((view, event) -> gestureDetector.onTouchEvent(event));

//        KismisCamera.this.
//                this.addCameraListener(new CameraListener() {
//            @Override
//            public void onVideoTaken(@NonNull VideoResult result) {
//                super.onVideoTaken(result);
//                getVideoApproval(result.getFile());
//            }
//
//            @Override
//            public void onVideoRecordingStart() {
//                super.onVideoRecordingStart();
//                activateButton();
//                countDownTimer.start();
//                isRecording = true;
//            }
//
//            @Override
//            public void onVideoRecordingEnd() {
//                super.onVideoRecordingEnd();
//                countDownTimer.cancel();
//                resetButton();
//                pbCountdown.setProgress(0);
//                isRecording = false;
//            }
//        });
    }

    private void startRecording() {
        File currentVideoFile = CustomTools.getVideoFile(contextHolder);
        if (currentVideoFile == null) {
            Log.e(TAG, "Kismis: startRecording: videoFile is null");
            CustomTools.showToast(contextHolder, "External storage not found");
        } else {
            startVideo(currentVideoFile);
        }
    }

    private void pauseRecording() {
        pauseVideo();
    }

    private void stopRecording() {
        stopVideo();
    }

//    private void getVideoApproval(final File currentVideoFile) {
//        Intent intent = new Intent(CameraActivity.this, VideoApprovalActivity.class);
//        intent.putExtra("VideoUriString", String.valueOf(Uri.fromFile(currentVideoFile)));
//        startActivity(intent);
//    }
//
//    public void chooseVideo() {
//        Intent intent = new Intent(contextHolder, VideoPickerActivity.class);
//        startActivity(intent);
//    }

    public void startVideo(File videoFile) {
        countDownTimer.start();
        activateButton();
    }

    public void pauseVideo() {
        pauseButton();
    }

    public void stopVideo() {
        resetButton();
    }

    private void activateButton() {
        ibTrigger.setImageDrawable(contextHolder.getDrawable(R.drawable.ic_pause_white_24));
        recordingStatus = RecordingStatus.RECORDING;
    }

    private void pauseButton() {
        ibTrigger.setImageDrawable(contextHolder.getDrawable(R.drawable.ic_play_arrow_white_24));
        recordingStatus = RecordingStatus.PAUSED;
    }

    private void resetButton() {
        pbCountdown.setProgress(0);
        ibTrigger.setImageDrawable(null);
        recordingStatus = RecordingStatus.STOPPED;
    }

    public void onStart() {
        cameraKitView.onStart();
    }

    public void onResume() {
        cameraKitView.onResume();
    }

    public void onPause() {
        cameraKitView.onPause();
    }

    public void onStop() {
        cameraKitView.onStop();
    }

    enum RecordingStatus {
        STOPPED,
        PAUSED,
        RECORDING,
    }
}
