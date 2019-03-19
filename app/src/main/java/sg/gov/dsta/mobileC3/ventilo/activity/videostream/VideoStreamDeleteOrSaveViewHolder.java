package sg.gov.dsta.mobileC3.ventilo.activity.videostream;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import lombok.Data;
import sg.gov.dsta.mobileC3.ventilo.R;
import sg.gov.dsta.mobileC3.ventilo.util.component.C2OpenSansRegularEditTextView;
import sg.gov.dsta.mobileC3.ventilo.util.constant.FragmentConstants;
import sg.gov.dsta.mobileC3.ventilo.util.constant.SharedPreferenceConstants;

@Data
public class VideoStreamDeleteOrSaveViewHolder extends RecyclerView.ViewHolder {

    private ImageView imgVideoStreamDelete;
    private C2OpenSansRegularEditTextView etvVideoStreamName;
    private C2OpenSansRegularEditTextView etvVideoStreamURL;
    private ImageView imgVideoStreamEditOrSave;

    private VideoStreamDeleteOrSaveRecyclerAdapter mAdapter;

    protected VideoStreamDeleteOrSaveViewHolder(View itemView, VideoStreamDeleteOrSaveRecyclerAdapter adapter) {
        super(itemView);

        imgVideoStreamDelete = itemView.findViewById(R.id.img_video_stream_delete);
        etvVideoStreamName = itemView.findViewById(R.id.etv_video_stream_name);
        etvVideoStreamURL = itemView.findViewById(R.id.etv_video_stream_url);
        imgVideoStreamEditOrSave = itemView.findViewById(R.id.img_video_stream_edit_save);

        imgVideoStreamDelete.setOnClickListener(deleteVideoStreamItemListener);
        imgVideoStreamEditOrSave.setOnClickListener(editOrSaveVideoStreamItemListener);

        mAdapter = adapter;
    }

    private View.OnClickListener deleteVideoStreamItemListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            removeSingleItemFromLocalDatabase(view.getContext(), getAdapterPosition());
            mAdapter.deleteItem(getAdapterPosition());
        }
    };

    private View.OnClickListener editOrSaveVideoStreamItemListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(view.getContext());
            SharedPreferences.Editor editor = pref.edit();

            String videoStreamInitials = SharedPreferenceConstants.HEADER_VIDEO_STREAM.
                    concat(SharedPreferenceConstants.SEPARATOR).concat(String.valueOf(getAdapterPosition()));

            String iconType = pref.getString(videoStreamInitials.concat(SharedPreferenceConstants.SEPARATOR).
                    concat(SharedPreferenceConstants.SUB_HEADER_VIDEO_STREAM_ICON_TYPE), FragmentConstants.KEY_VIDEO_STREAM_SAVE);

            if (iconType.equalsIgnoreCase(FragmentConstants.KEY_VIDEO_STREAM_EDIT)) {
                editor.putString(videoStreamInitials.concat(SharedPreferenceConstants.SEPARATOR).
                                concat(SharedPreferenceConstants.SUB_HEADER_VIDEO_STREAM_ICON_TYPE),
                        FragmentConstants.KEY_VIDEO_STREAM_SAVE);

                editor.apply();

                etvVideoStreamName.setEnabled(true);
                etvVideoStreamURL.setEnabled(true);
                imgVideoStreamEditOrSave.setImageDrawable(view.getContext().getDrawable(R.drawable.btn_save));

            } else {
                editor.putInt(videoStreamInitials.concat(SharedPreferenceConstants.SEPARATOR).
                        concat(SharedPreferenceConstants.SUB_HEADER_VIDEO_STREAM_ID), view.getId());
                editor.putString(videoStreamInitials.concat(SharedPreferenceConstants.SEPARATOR).
                                concat(SharedPreferenceConstants.SUB_HEADER_VIDEO_STREAM_NAME),
                        etvVideoStreamName.getText().toString().trim());
                editor.putString(videoStreamInitials.concat(SharedPreferenceConstants.SEPARATOR).
                                concat(SharedPreferenceConstants.SUB_HEADER_VIDEO_STREAM_URL),
                        etvVideoStreamURL.getText().toString().trim());
                editor.putString(videoStreamInitials.concat(SharedPreferenceConstants.SEPARATOR).
                                concat(SharedPreferenceConstants.SUB_HEADER_VIDEO_STREAM_ICON_TYPE),
                        FragmentConstants.KEY_VIDEO_STREAM_EDIT);

                editor.apply();

                etvVideoStreamName.setEnabled(false);
                etvVideoStreamURL.setEnabled(false);
                imgVideoStreamEditOrSave.setImageDrawable(view.getContext().getDrawable(R.drawable.btn_edit));
            }

        }
    };

    private void removeSingleItemFromLocalDatabase(Context context, int position) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        int totalNumberOfVideoStreams = pref.getInt(SharedPreferenceConstants.VIDEO_STREAM_TOTAL_NUMBER, 0);

        // Overwrite all fields of specified video stream item with next stored item fields
        // E.g. id(2) is replaced with id(3)
        for (int i = position; i < totalNumberOfVideoStreams - 1; i++) {
            String videoStreamInitialsOfCurrentPos = SharedPreferenceConstants.HEADER_VIDEO_STREAM.
                    concat(SharedPreferenceConstants.SEPARATOR).concat(String.valueOf(i));

            String videoStreamInitialsOfNextPos = SharedPreferenceConstants.HEADER_VIDEO_STREAM.
                    concat(SharedPreferenceConstants.SEPARATOR).concat(String.valueOf(i + 1));

            editor.putInt(videoStreamInitialsOfCurrentPos.concat(SharedPreferenceConstants.SEPARATOR).
                            concat(SharedPreferenceConstants.SUB_HEADER_VIDEO_STREAM_ID),
                    pref.getInt(videoStreamInitialsOfNextPos.concat(SharedPreferenceConstants.SEPARATOR).
                            concat(SharedPreferenceConstants.SUB_HEADER_VIDEO_STREAM_ID), SharedPreferenceConstants.DEFAULT_INT));
            editor.putString(videoStreamInitialsOfCurrentPos.concat(SharedPreferenceConstants.SEPARATOR).
                            concat(SharedPreferenceConstants.SUB_HEADER_VIDEO_STREAM_NAME),
                    pref.getString(videoStreamInitialsOfNextPos.concat(SharedPreferenceConstants.SEPARATOR).
                            concat(SharedPreferenceConstants.SUB_HEADER_VIDEO_STREAM_NAME), SharedPreferenceConstants.DEFAULT_STRING));
            editor.putString(videoStreamInitialsOfCurrentPos.concat(SharedPreferenceConstants.SEPARATOR).
                            concat(SharedPreferenceConstants.SUB_HEADER_VIDEO_STREAM_URL),
                    pref.getString(videoStreamInitialsOfNextPos.concat(SharedPreferenceConstants.SEPARATOR).
                            concat(SharedPreferenceConstants.SUB_HEADER_VIDEO_STREAM_URL), SharedPreferenceConstants.DEFAULT_STRING));
            editor.putString(videoStreamInitialsOfCurrentPos.concat(SharedPreferenceConstants.SEPARATOR).
                            concat(SharedPreferenceConstants.SUB_HEADER_VIDEO_STREAM_ICON_TYPE),
                    pref.getString(videoStreamInitialsOfNextPos.concat(SharedPreferenceConstants.SEPARATOR).
                            concat(SharedPreferenceConstants.SUB_HEADER_VIDEO_STREAM_ICON_TYPE), SharedPreferenceConstants.DEFAULT_STRING));
        }

        // Safely remove all fields of video stream item at the back of the queue
        String videoStreamInitials = SharedPreferenceConstants.HEADER_VIDEO_STREAM.concat(SharedPreferenceConstants.SEPARATOR).
                concat(String.valueOf(totalNumberOfVideoStreams));

        editor.remove(videoStreamInitials.concat(SharedPreferenceConstants.SEPARATOR).
                concat(SharedPreferenceConstants.SUB_HEADER_VIDEO_STREAM_ID));
        editor.remove(videoStreamInitials.concat(SharedPreferenceConstants.SEPARATOR).
                concat(SharedPreferenceConstants.SUB_HEADER_VIDEO_STREAM_NAME));
        editor.remove(videoStreamInitials.concat(SharedPreferenceConstants.SEPARATOR).
                concat(SharedPreferenceConstants.SUB_HEADER_VIDEO_STREAM_URL));
        editor.remove(videoStreamInitials.concat(SharedPreferenceConstants.SEPARATOR).
                concat(SharedPreferenceConstants.SUB_HEADER_VIDEO_STREAM_ICON_TYPE));

        // Decrement total number of video stream items by one, and store it
        editor.putInt(SharedPreferenceConstants.VIDEO_STREAM_TOTAL_NUMBER, totalNumberOfVideoStreams - 1);

        editor.apply();
    }
}