package ru.ilyamodder.marvel;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.ilyamodder.marvel.adapter.ComicsAdapter;
import ru.ilyamodder.marvel.provider.DataProvider;
import ru.ilyamodder.marvel.service.LoadingService;
import ru.ilyamodder.marvel.sqlite.RequestsTable;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private ContentObserver mContentObserver;
    private long requestId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        requestId = Math.abs(new Random().nextLong());

        mContentObserver = new ContentObserver(new Handler()) {
            @Override
            public void onChange(boolean selfChange, Uri uri) {
                getLoaderManager().getLoader(R.id.request_state_loader).forceLoad();
            }
        };

        getContentResolver().registerContentObserver(
                ContentUris.withAppendedId(DataProvider.REQUESTS_URI, requestId), true, mContentObserver);
        LoadingService.loadComics(this, "2007-01-01,2007-12-31", requestId);

        getLoaderManager().initLoader(R.id.data_loader, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
                return new CursorLoader(MainActivity.this, DataProvider.COMICS_URI, null, null, null, null);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
                mRecyclerView.setAdapter(new ComicsAdapter(cursor));
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {

            }
        });

        getLoaderManager().initLoader(R.id.request_state_loader, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
                return new CursorLoader(MainActivity.this, ContentUris.withAppendedId(DataProvider.REQUESTS_URI, requestId), null, null, null, null);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
                cursor.moveToNext();
                if (cursor.getCount() < 1) {
                    return;
                }
                int code = cursor.getInt(cursor.getColumnIndex(RequestsTable.Columns.RESP_CODE));
                if (code == 200) {
                    getLoaderManager().getLoader(R.id.data_loader).forceLoad();
                } else if (code == 409) {
                    Toast.makeText(MainActivity.this, R.string.request_error, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, R.string.loading_error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getContentResolver().unregisterContentObserver(mContentObserver);
    }
}
