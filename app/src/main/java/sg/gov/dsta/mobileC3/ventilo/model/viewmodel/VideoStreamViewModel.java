package sg.gov.dsta.mobileC3.ventilo.model.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import io.reactivex.SingleObserver;
import sg.gov.dsta.mobileC3.ventilo.model.task.TaskModel;
import sg.gov.dsta.mobileC3.ventilo.model.videostream.VideoStreamModel;
import sg.gov.dsta.mobileC3.ventilo.repository.TaskRepository;
import sg.gov.dsta.mobileC3.ventilo.repository.VideoStreamRepository;

public class VideoStreamViewModel extends AndroidViewModel {

    private VideoStreamRepository repository;

    public VideoStreamViewModel(Application application) {
        super(application);
        repository = new VideoStreamRepository(application);
    }

    public LiveData<List<VideoStreamModel>> getAllVideoStreamsLiveDataForUser(String userId) {
        return repository.getAllVideoStreamsLiveDataForUser(userId);
    }

    public LiveData<List<String>> getAllVideoStreamNamesLiveDataForUser(String userId) {
        return repository.getAllVideoStreamNamesLiveDataForUser(userId);
    }

    public void getAllVideoStreamsForUser(String userId, SingleObserver singleObserver) {
        repository.getAllVideoStreamsForUser(userId, singleObserver);
    }

    public void getVideoStreamUrlForUserByName(String userId, String videoName,
                                             SingleObserver singleObserver) {
        repository.getVideoStreamUrlForUserByName(userId, videoName, singleObserver);
    }

    public void addVideoStream(VideoStreamModel videoStreamModel,
                               SingleObserver singleObserver) {
        repository.addVideoStream(videoStreamModel, singleObserver);
    }

    public void updateVideoStream(VideoStreamModel videoStreamModel) {
        repository.updateVideoStream(videoStreamModel);
    }

    public void deleteVideoStream(long videoStreamId) {
        repository.deleteVideoStream(videoStreamId);
    }
}