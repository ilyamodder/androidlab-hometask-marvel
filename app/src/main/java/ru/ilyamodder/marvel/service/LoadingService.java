package ru.ilyamodder.marvel.service;

import android.app.IntentService;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.ilyamodder.marvel.model.ComicsData;
import ru.ilyamodder.marvel.network.Api;
import ru.ilyamodder.marvel.provider.DataProvider;
import ru.ilyamodder.marvel.sqlite.RequestsTable;
import ru.ilyamodder.marvel.sqlite.ComicsTable;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * helper methods.
 */
public class LoadingService extends IntentService {
    private static final String ACTION_GET_COMICS = "ru.ilyamodder.contentprovider.service.action.GET_COMICS";
    private static final String EXTRA_DATE_RANGE = "date_range";
    private static final String EXTRA_KEY = "key";
    public static final int COUNT = 10;

    public LoadingService() {
        super("LoadingService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void loadComics(Context context, String dateRange, long uniqueKey) {
        Intent intent = new Intent(context, LoadingService.class);
        intent.setAction(ACTION_GET_COMICS);
        intent.putExtra(EXTRA_DATE_RANGE, dateRange);
        intent.putExtra(EXTRA_KEY, uniqueKey);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_GET_COMICS.equals(action)) {
                handleActionGetWeather(intent.getStringExtra(EXTRA_DATE_RANGE), intent.getLongExtra(EXTRA_KEY, -1));
            }
        }
    }

    /**
     * Handle action in the provided background thread with the provided
     * parameters.
     */
    private void handleActionGetWeather(String dateRange, final long key) {
        Api.getService().getComics(dateRange, COUNT).enqueue(new Callback<ComicsData>() {
            @Override
            public void onResponse(Call<ComicsData> call, Response<ComicsData> response) {
                ComicsData data = response.body();
                ContentValues cv = new ContentValues();
                cv.put(RequestsTable.Columns._ID, key);
                cv.put(RequestsTable.Columns.RESP_CODE, response.code());
                cv.put(RequestsTable.Columns.URL, call.request().url().toString());
                getContentResolver().insert(DataProvider.REQUESTS_URI, cv);
                getContentResolver().delete(DataProvider.COMICS_URI, null, null);

                if (response.isSuccessful()) {
                    List<ContentValues> weatherValues = new ArrayList<>();
                    for (ComicsData.Cartoon cartoon : data.getList()) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(ComicsTable.Columns.DATE_MODIFIED, cartoon.getModified().getTime());
                        contentValues.put(ComicsTable.Columns.DESCRIPTION, cartoon.getDescription());
                        contentValues.put(ComicsTable.Columns.FORMAT, cartoon.getFormat());
                        contentValues.put(ComicsTable.Columns._ID, cartoon.getId());
                        contentValues.put(ComicsTable.Columns.URL, cartoon.getResourceURI());
                        contentValues.put(ComicsTable.Columns.TITLE, cartoon.getTitle());
                        weatherValues.add(contentValues);
                    }
                    getContentResolver().bulkInsert(DataProvider.COMICS_URI, weatherValues.toArray(new ContentValues[0]));
                    getContentResolver().notifyChange(ContentUris.withAppendedId(DataProvider.REQUESTS_URI, key), null);
                } else {
                    try {
                        Log.e("error", new String(response.errorBody().bytes()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ComicsData> call, Throwable t) {
                Log.e("service", "fail");
                t.printStackTrace();
                ContentValues cv = new ContentValues();
                cv.put(RequestsTable.Columns._ID, key);
                cv.put(RequestsTable.Columns.RESP_CODE, 0);
                cv.put(RequestsTable.Columns.URL, call.request().url().toString());
                getContentResolver().insert(DataProvider.REQUESTS_URI, cv);
            }
        });
    }
}
