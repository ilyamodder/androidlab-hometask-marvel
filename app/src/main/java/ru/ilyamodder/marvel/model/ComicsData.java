package ru.ilyamodder.marvel.model;

import java.util.Date;
import java.util.List;

/**
 * Created by ilya on 16.11.16.
 */

public class ComicsData {
    private Data mData;

    public class Data {
        List<Cartoon> mResults;
    }

    public class Cartoon {
        private int mId;
        private String mTitle;
        private Date mModified;
        private String mFormat;
        private String mDescription;
        private String mResourceURI;

        public int getId() {
            return mId;
        }

        public String getTitle() {
            return mTitle;
        }

        public Date getModified() {
            return mModified;
        }

        public String getFormat() {
            return mFormat;
        }

        public String getDescription() {
            return mDescription;
        }

        public String getResourceURI() {
            return mResourceURI;
        }
    }

    public List<Cartoon> getList() {
        return mData.mResults;
    }
}
