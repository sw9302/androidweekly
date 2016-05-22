package com.icephone.sw.androidweekly.db;

import android.net.Uri;

/**
 * Created by sw on 2016/5/22.
 */
public class DatabaseTable {

    public static final String AUTHORITY = "com.icephone.sw.androidweekly.dbcache";

    public interface BaseTable{
        public static String _ID = "_id";
    }

    public static class GCTable implements BaseTable{
        public static final String TABLE_NAME = "gc_data";
        public static final Uri URI_GCTABLE = Uri.parse(
                "content://" + AUTHORITY + "/" + TABLE_NAME);

        public static final String TYPE = "type";
        public static final String CONTENT = "content";

        public static final int TYPE_WEEKLY = 1;
    }
}
