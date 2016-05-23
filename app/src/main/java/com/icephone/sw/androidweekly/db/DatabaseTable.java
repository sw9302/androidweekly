package com.icephone.sw.androidweekly.db;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by sw on 2016/5/22.
 */
public class DatabaseTable {

    public static final String AUTHORITY = "com.icephone.sw.androidweekly.dbcache";

    public interface BaseTable{
        public static String _ID = "_id";
    }
    //虚拟表，主要用来执行SQL
    public static final class SQLExecutor implements BaseColumns {

        public static final String TABLE_NAME = "sqlexecutor";

        /**
         * 查询或更新单个app的信息，需要加上app_id
         */
        public static final Uri URI_SQL_EXECUTOR = Uri
                .parse("content://" + AUTHORITY + "/" + TABLE_NAME);

        public static final String SQL = "sql";


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
