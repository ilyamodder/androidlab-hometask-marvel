package ru.ilyamodder.marvel.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import ru.ilyamodder.marvel.sqlite.ComicsTable;
import ru.ilyamodder.marvel.sqlite.DBHelper;
import ru.ilyamodder.marvel.sqlite.RequestsTable;

/**
 * Created by ilya on 02.11.16.
 */

public class DataProvider extends ContentProvider {

    public static final String AUTHORITY = DataProvider.class.getCanonicalName();

    public static final Uri REQUESTS_URI = Uri.parse("content://" + AUTHORITY + "/" +
            RequestsTable.NAME);
    public static final Uri COMICS_URI = Uri.parse("content://" + AUTHORITY + "/" +
            ComicsTable.NAME);

    public static final String REQUEST_CONTENT_TYPE = "vnd.android.cursor.item/vnd."
            + AUTHORITY + "." + RequestsTable.NAME;
    public static final String REQUESTS_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + RequestsTable.NAME;

    public static final String CARTOON_CONTENT_TYPE = "vnd.android.cursor.item/vnd."
            + AUTHORITY + "." + ComicsTable.NAME;
    public static final String COMICS_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + ComicsTable.NAME;

    public static final int URI_REQUESTS = 0;
    public static final int URI_REQUEST = 1;
    public static final int URI_COMICS = 2;
    public static final int URI_CARTOON = 3;

    private static UriMatcher sMatcher;
    static {
        sMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sMatcher.addURI(AUTHORITY, RequestsTable.NAME, URI_REQUESTS);
        sMatcher.addURI(AUTHORITY, RequestsTable.NAME + "/#", URI_REQUEST);
        sMatcher.addURI(AUTHORITY, ComicsTable.NAME, URI_COMICS);
        sMatcher.addURI(AUTHORITY, ComicsTable.NAME + "/#", URI_CARTOON);
    }

    private DBHelper mDBHelper;
    private SQLiteDatabase mDb;

    @Override
    public boolean onCreate() {
        mDBHelper = new DBHelper(getContext());
        mDb = mDBHelper.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        switch (sMatcher.match(uri)) {
            case URI_REQUESTS:
                cursor = mDb.query(RequestsTable.NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case URI_REQUEST:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = RequestsTable.Columns._ID + "=" + id;
                } else {
                    selection += " AND " + RequestsTable.Columns._ID + "=" + id;
                }
                cursor = mDb.query(RequestsTable.NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case URI_COMICS:
                cursor = mDb.query(ComicsTable.NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Wrong uri: " + uri);

        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (sMatcher.match(uri)) {
            case URI_REQUESTS:
                return REQUESTS_CONTENT_TYPE;
            case URI_REQUEST:
                return REQUEST_CONTENT_TYPE;
            case URI_COMICS:
                return COMICS_CONTENT_TYPE;
            case URI_CARTOON:
                return CARTOON_CONTENT_TYPE;
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        long rowId;
        switch (sMatcher.match(uri)) {
            case URI_REQUESTS:
                rowId = mDb.insert(RequestsTable.NAME, null, values);
                return ContentUris.withAppendedId(REQUESTS_URI, rowId);
            case URI_COMICS:
                rowId = mDb.insert(ComicsTable.NAME, null, values);
                return ContentUris.withAppendedId(COMICS_URI, rowId);
            default:
                throw new IllegalArgumentException("Wrong uri: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        String table;
        String id;
        switch (sMatcher.match(uri)) {
            case URI_REQUESTS:
                table = RequestsTable.NAME;
                break;
            case URI_REQUEST:
                table = RequestsTable.NAME;
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = RequestsTable.Columns._ID + "=" + id;
                } else {
                    selection += " AND " + RequestsTable.Columns._ID + "=" + id;
                }
                break;
            case URI_CARTOON:
                table = ComicsTable.NAME;
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = ComicsTable.Columns._ID + "=" + id;
                } else {
                    selection += " AND " + ComicsTable.Columns._ID + "=" + id;
                }
                break;
            case URI_COMICS:
                table = ComicsTable.NAME;
                break;
            default:
                throw new IllegalArgumentException("Wrong uri: " + uri);
        }
        return mDb.delete(table, selection, selectionArgs);
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        String table;
        String id;
        switch (sMatcher.match(uri)) {
            case URI_REQUESTS:
                table = RequestsTable.NAME;
                break;
            case URI_REQUEST:
                table = RequestsTable.NAME;
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = RequestsTable.Columns._ID + "=" + id;
                } else {
                    selection += " AND " + RequestsTable.Columns._ID + "=" + id;
                }
                break;
            case URI_CARTOON:
                table = ComicsTable.NAME;
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = ComicsTable.Columns._ID + "=" + id;
                } else {
                    selection += " AND " + ComicsTable.Columns._ID + "=" + id;
                }
                break;
            case URI_COMICS:
                table = ComicsTable.NAME;
                break;
            default:
                throw new IllegalArgumentException("Wrong uri: " + uri);
        }
        return mDb.update(table, values, selection, selectionArgs);
    }
}
