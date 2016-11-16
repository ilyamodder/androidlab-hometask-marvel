package ru.ilyamodder.marvel.sqlite;

import android.provider.BaseColumns;

/**
 * Created by ilya on 02.11.16.
 */

public interface ComicsTable {
    String NAME = "weather";

    interface Columns extends BaseColumns {
        String TITLE = "title";
        String DATE_MODIFIED = "modified";
        String FORMAT = "format";
        String DESCRIPTION = "description";
        String URL = "url";
    }
}
