package android.example.moviesdata;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by Ashok on 12/25/2015.
 */
public class MoviesProvider extends ContentProvider {

  public  static final String PROVIDER_NAME = "android.example.movies";
    public  static final String URL = "content://" + PROVIDER_NAME + "/movies";
    public  static final Uri CONTENT_URI = Uri.parse(URL);

    public static final String _ID = "_id";
    public static final String TITLE = "title";
    public static final String OVERVIEW = "overview";
    public static final String RATING = "rating";
    public static final String RELEASE_DATE = "relaseDate";
    public static final String POSTER = "poster";


    private static HashMap<String, String> MOVIESS_PROJECTION_MAP;

    static final int MOVIESS = 1;
    static final int MOVIES_ID = 2;

    static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "movies", MOVIESS);
        uriMatcher.addURI(PROVIDER_NAME, "movies/#", MOVIES_ID);
    }

    /**
     * Database specific constant declarations
     */
    private SQLiteDatabase db;
    static final String DATABASE_NAME = "MOVIES";
    static final String MOVIESS_TABLE_NAME = "FAVARATE_MOVIES";
    static final int DATABASE_VERSION = 4;
    static final String CREATE_DB_TABLE =
            " CREATE TABLE " + MOVIESS_TABLE_NAME +
                    " (_id INTEGER, " +
                    " title TEXT NOT NULL, " +
                    " overview TEXT NOT NULL, " +
                    " relaseDate TEXT NOT NULL, " +
                    " rating TEXT NOT NULL, " +
                    " poster TEXT NOT NULL);";

    /**
     * Helper class that actually creates and manages
     * the provider's underlying data repository.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {
     public    DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_DB_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + MOVIESS_TABLE_NAME);
            onCreate(db);
        }
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);

        /**
         * Create a write able database which will trigger its 
         * creation if it doesn't already exist.
         */
        db = dbHelper.getWritableDatabase();
        return (db == null) ? false : true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        /**
         * Add a new student record
         */
        long rowID = db.insert(MOVIESS_TABLE_NAME, null, values);

        /**
         * If record is added successfully
         */
        Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);

        if (rowID > 0) {

            if (null != getContext())
                getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        return _uri;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(MOVIESS_TABLE_NAME);
        switch (uriMatcher.match(uri)) {
            case MOVIESS:
                qb.setProjectionMap(MOVIESS_PROJECTION_MAP);
                break;

            case MOVIES_ID:
                qb.appendWhere(_ID + "=" + uri.getPathSegments().get(1));
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        if (sortOrder == null || sortOrder == "") {
            /**
             * By default sort on student names
             */
            sortOrder = TITLE;
        }
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        /**
         * register to watch a content URI for changes
         */
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;

        switch (uriMatcher.match(uri)) {
            case MOVIESS:
                count = db.delete(MOVIESS_TABLE_NAME, selection, selectionArgs);
                break;

            case MOVIES_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete(MOVIESS_TABLE_NAME, _ID + " = " + id +
                        (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = 0;

        switch (uriMatcher.match(uri)) {
            case MOVIESS:
                count = db.update(MOVIESS_TABLE_NAME, values, selection, selectionArgs);
                break;

            case MOVIES_ID:
                count = db.update(MOVIESS_TABLE_NAME, values, _ID + " = " + uri.getPathSegments().get(1) +
                        (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            /**
             * Get all student records 
             */
            case MOVIESS:
                return "vnd.android.cursor.dir/android.example.movies";

            /**
             * Get a particular student
             */
            case MOVIES_ID:
                return "vnd.android.cursor.item/android.example.movies";

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }
}

