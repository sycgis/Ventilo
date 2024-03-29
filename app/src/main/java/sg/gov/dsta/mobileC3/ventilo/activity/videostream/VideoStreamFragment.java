package sg.gov.dsta.mobileC3.ventilo.activity.videostream;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.VideoView;

import sg.gov.dsta.mobileC3.ventilo.R;
import sg.gov.dsta.mobileC3.ventilo.util.CustomKeyboardUtil;
import sg.gov.dsta.mobileC3.ventilo.util.ValidationUtil;
import sg.gov.dsta.mobileC3.ventilo.util.component.C2LatoBlackButton;
import sg.gov.dsta.mobileC3.ventilo.util.component.C2LatoItalicLightEditTextView;

public class VideoStreamFragment extends Fragment {

    private static final String VIDEO_STREAM_TAG = "VideoStream";
    private VideoView mVideoViewOne;
    private VideoView mVideoViewTwo;
    private C2LatoItalicLightEditTextView mEtvFirstVideoURLLink;
    private C2LatoItalicLightEditTextView mEtvSecondVideoURLLink;
    private LinearLayout mLinearLayoutFirstVideoConfirm;
    private LinearLayout mLinearLayoutSecondVideoConfirm;
    private C2LatoBlackButton mImgBtnFirstVideoConfirm;
    private C2LatoBlackButton mImgBtnSecondVideoConfirm;

    private MediaController mMediaControllerOne;
    private MediaController mMediaControllerTwo;

    private boolean mIsVisibleToUser;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootVideoStreamView = inflater.inflate(R.layout.fragment_video_stream, container, false);
        initUI(rootVideoStreamView);

        return rootVideoStreamView;
    }

    private void initUI(View rootVideoStreamView) {
        initURLLinkUI(rootVideoStreamView);
        initVideoOneStream(rootVideoStreamView);
        initVideoTwoStream(rootVideoStreamView);
    }

    private void initURLLinkUI(View rootVideoStreamView) {
        mEtvFirstVideoURLLink = rootVideoStreamView.findViewById(R.id.etv_first_video_url_title_detail);
        mEtvFirstVideoURLLink.addTextChangedListener(firstVideoTextWatcher);

        mLinearLayoutFirstVideoConfirm = rootVideoStreamView.findViewById(R.id.layout_first_video_confirm);
        mImgBtnFirstVideoConfirm = rootVideoStreamView.findViewById(R.id.btn_first_video_stream_link);
        mImgBtnFirstVideoConfirm.setOnClickListener(onFirstVideoConfirmClickListener);

        mEtvSecondVideoURLLink = rootVideoStreamView.findViewById(R.id.etv_second_video_url_title_detail);
        mEtvSecondVideoURLLink.addTextChangedListener(secondVideoTextWatcher);
        mLinearLayoutSecondVideoConfirm = rootVideoStreamView.findViewById(R.id.layout_second_video_confirm);
        mImgBtnSecondVideoConfirm = rootVideoStreamView.findViewById(R.id.btn_second_video_stream_link);
        mImgBtnSecondVideoConfirm.setOnClickListener(onSecondVideoConfirmClickListener);
    }

    private TextWatcher firstVideoTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            mLinearLayoutFirstVideoConfirm.setVisibility(View.VISIBLE);
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

    private TextWatcher secondVideoTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            mLinearLayoutSecondVideoConfirm.setVisibility(View.VISIBLE);
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

    private View.OnClickListener onFirstVideoConfirmClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (ValidationUtil.validateEditTextField(mEtvFirstVideoURLLink,
                    getString(R.string.error_empty_video_ur_link_detail))) {
                setVideoOneStreamURL(mEtvFirstVideoURLLink.getText().toString().trim());
                mLinearLayoutFirstVideoConfirm.setVisibility(View.GONE);
                hideKeyboard();
            }
        }
    };

    private View.OnClickListener onSecondVideoConfirmClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (ValidationUtil.validateEditTextField(mEtvSecondVideoURLLink,
                    getString(R.string.error_empty_video_ur_link_detail))) {
                setVideoTwoStreamURL(mEtvSecondVideoURLLink.getText().toString().trim());
                mLinearLayoutSecondVideoConfirm.setVisibility(View.GONE);
                hideKeyboard();
            }
        }
    };

    private void initVideoOneStream(View rootVideoStreamView) {
        mVideoViewOne = rootVideoStreamView.findViewById(R.id.video_view_stream_one);
        mMediaControllerOne = new MediaController(getActivity());
        mVideoViewOne.setMediaController(mMediaControllerOne);
    }

    private void initVideoTwoStream(View rootVideoStreamView) {
        mVideoViewTwo = rootVideoStreamView.findViewById(R.id.video_view_stream_two);
        mMediaControllerOne = new MediaController(getActivity());
        mVideoViewOne.setMediaController(mMediaControllerOne);
    }

    // Test URL is "rtsp://184.72.239.149/vod/mp4:BigBuckBunny_175k.mov"
    private void setVideoOneStreamURL(String urlLink) {
        try {
            Uri video = Uri.parse(urlLink);

            mVideoViewOne.setVideoURI(video);

            mVideoViewOne.post(new Runnable() {
                @Override
                public void run() {
//                    mMediaControllerOne.show(0);
                }
            });
        } catch (RuntimeException e) {
            Log.d(VIDEO_STREAM_TAG, "Video Stream One - Invalid URI");
        }

        mVideoViewOne.start();
    }

    // Test URL is "http://archive.org/download/SampleMpeg4_201307/sample_mpeg4.mp4"
    private void setVideoTwoStreamURL(String urlLink) {
        mMediaControllerTwo = new MediaController(getActivity());
        mVideoViewTwo.setMediaController(mMediaControllerTwo);

        try {
            Uri video = Uri.parse(urlLink);
            mVideoViewTwo.setVideoURI(video);

            mVideoViewTwo.post(new Runnable() {
                @Override
                public void run() {
//                    mMediaControllerTwo.show(0);
                }
            });
        } catch (RuntimeException e) {
            Log.d(VIDEO_STREAM_TAG, "Video Stream Two - Invalid URI");
        }

        mVideoViewTwo.start();
    }

    private void onVisible() {
        mEtvFirstVideoURLLink.requestFocus();
        CustomKeyboardUtil.showKeyboard(getActivity());
    }

    private void onInvisible() {
        hideKeyboard();
    }

    private void hideKeyboard() {
        mEtvFirstVideoURLLink.clearFocus();
        mEtvSecondVideoURLLink.clearFocus();
        CustomKeyboardUtil.hideKeyboard(getActivity(), mEtvFirstVideoURLLink);
        CustomKeyboardUtil.hideKeyboard(getActivity(), mEtvSecondVideoURLLink);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mIsVisibleToUser = isVisibleToUser;
        if (isResumed()) { // fragment has been created at this point
            if (mIsVisibleToUser) {
                onVisible();
            } else {
                onInvisible();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mIsVisibleToUser) {
            onVisible();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mIsVisibleToUser) {
            onInvisible();
        }
    }
}
