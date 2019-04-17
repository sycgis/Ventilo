package sg.gov.dsta.mobileC3.ventilo.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import sg.gov.dsta.mobileC3.ventilo.database.DAO.VideoStreamDao;
import sg.gov.dsta.mobileC3.ventilo.database.VentiloDatabase;
import sg.gov.dsta.mobileC3.ventilo.model.videostream.VideoStreamModel;

public class VideoStreamRepository {

    private LiveData<List<VideoStreamModel>> mAllVideoStreamsForUser;
    private VideoStreamDao mVideoStreamDao;

    public VideoStreamRepository(Application application) {
        VentiloDatabase db = VentiloDatabase.getInstance(application);
        mVideoStreamDao = db.videoStreamDao();
    }

    public LiveData<List<VideoStreamModel>> getAllVideoStreamsLiveDataForUser(String userId) {
        return mVideoStreamDao.getAllVideoStreamsLiveDataForUser(userId);
    }

    public LiveData<List<String>> getAllVideoStreamNamesLiveDataForUser(String userId) {
        return mVideoStreamDao.getAllVideoStreamNamesLiveDataForUser(userId);
    }

    public void getAllVideoStreamsForUser(
            String userId, SingleObserver singleObserver) {
        QueryAllVideoStreamsAsyncTask task = new
                QueryAllVideoStreamsAsyncTask(mVideoStreamDao, singleObserver);
        task.execute(userId);
    }

    public void getVideoStreamUrlForUserByName(
            String userId, String videoName, SingleObserver singleObserver) {
        String[] paramsForGetUrl = {userId, videoName};
        QueryVideoStreamForUserByNameAsyncTask task = new
                QueryVideoStreamForUserByNameAsyncTask(mVideoStreamDao, singleObserver);
        task.execute(paramsForGetUrl);
    }

    public void addVideoStream(VideoStreamModel videoStreamModel,
                               SingleObserver singleObserver) {
        InsertAsyncTask task = new InsertAsyncTask(mVideoStreamDao, singleObserver);
        task.execute(videoStreamModel);
    }

    public void updateVideoStream(VideoStreamModel videoStreamModel) {
        UpdateAsyncTask task = new UpdateAsyncTask(mVideoStreamDao);
        task.execute(videoStreamModel);
    }

    public void deleteVideoStream(long videoStreamId) {
        DeleteAsyncTask task = new DeleteAsyncTask(mVideoStreamDao);
        task.execute(videoStreamId);
    }

    private static class QueryAllVideoStreamsAsyncTask extends
            AsyncTask<String, Void, Void> {

        private VideoStreamDao asyncVideoStreamDao;
        private SingleObserver asyncSingleObserver;

        QueryAllVideoStreamsAsyncTask(VideoStreamDao dao, SingleObserver singleObserver) {
            asyncVideoStreamDao = dao;
            asyncSingleObserver = singleObserver;
        }

        @Override
        protected Void doInBackground(final String... userId) {
            // Converts type Long to Observable, then to Single for RxJava use
            List<VideoStreamModel> allVideoStreamId =
                    asyncVideoStreamDao.getAllVideoStreamsForUser(userId[0]);

            if (allVideoStreamId == null) {
                allVideoStreamId = new ArrayList<>();
            }

            Observable<List<VideoStreamModel>> observableAllVideoStreamId =
                    Observable.just(allVideoStreamId);
            Single<List<VideoStreamModel>> singleInsertedVideoStreamId =
                    Single.fromObservable(observableAllVideoStreamId);

            singleInsertedVideoStreamId.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(asyncSingleObserver);

            return null;
        }
    }

    private static class QueryVideoStreamForUserByNameAsyncTask extends
            AsyncTask<String, Void, Void> {

        private VideoStreamDao asyncVideoStreamDao;
        private SingleObserver asyncSingleObserver;

        QueryVideoStreamForUserByNameAsyncTask(VideoStreamDao dao, SingleObserver singleObserver) {
            asyncVideoStreamDao = dao;
            asyncSingleObserver = singleObserver;
        }

        @Override
        protected Void doInBackground(final String... paramForGetUrl) {
            // Converts type Long to Observable, then to Single for RxJava use
            String videoStreamUrl = asyncVideoStreamDao.
                    getVideoStreamUrlForUserByName(paramForGetUrl[0], paramForGetUrl[1]);

            if (videoStreamUrl != null) {
                Observable<String> observableVideoStreamUrl =
                        Observable.just(videoStreamUrl);
                Single<String> singleVideoStreamUrl =
                        Single.fromObservable(observableVideoStreamUrl);

                singleVideoStreamUrl.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(asyncSingleObserver);
            }

            return null;
        }
    }

    private static class InsertAsyncTask extends AsyncTask<VideoStreamModel, Void, Void> {

        private VideoStreamDao asyncVideoStreamDao;
        private SingleObserver asyncSingleObserver;

        InsertAsyncTask(VideoStreamDao dao, SingleObserver singleObserver) {
            asyncVideoStreamDao = dao;
            asyncSingleObserver = singleObserver;
        }

        @Override
        protected Void doInBackground(final VideoStreamModel... videoStreamModel) {
            // Converts type Long to Observable, then to Single for RxJava use
            Long insertedVideoStreamId = asyncVideoStreamDao.
                    insertVideoStreamModel(videoStreamModel[0]);
            Observable<Long> observableInsertedVideoStreamId =
                    Observable.just(insertedVideoStreamId);
            Single<Long> singleInsertedVideoStreamId =
                    Single.fromObservable(observableInsertedVideoStreamId);

            singleInsertedVideoStreamId.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(asyncSingleObserver);

            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<VideoStreamModel, Void, Void> {

        private VideoStreamDao asyncVideoStreamDao;

        UpdateAsyncTask(VideoStreamDao dao) {
            asyncVideoStreamDao = dao;
        }

        @Override
        protected Void doInBackground(final VideoStreamModel... videoStreamModel) {
            asyncVideoStreamDao.updateVideoStreamModel(videoStreamModel[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<Long, Void, Void> {

        private VideoStreamDao asyncVideoStreamDao;

        DeleteAsyncTask(VideoStreamDao dao) {
            asyncVideoStreamDao = dao;
        }

        @Override
        protected Void doInBackground(final Long... videoStreamId) {
            asyncVideoStreamDao.deleteVideoStreamModel(videoStreamId[0]);
            return null;
        }
    }
}