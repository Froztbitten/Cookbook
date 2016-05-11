package com.shavedandbearded.tomato;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;

@SuppressWarnings("ConstantConditions")
public class AppContentProvider extends ContentProvider
{
    private DB theDB;
    private static final String AUTHORITY = "com.shavedandbearded.tomato";
    private static final String BASE_PATH = "shopping_list";
    private static final String BASE_PATH1 = "pantry_list";
    private static final String BASE_PATH2 = "cookbook";
    private static final String BASE_PATH3 = "ingredients";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
    public static final Uri CONTENT_URI1 = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH1);
    public static final Uri CONTENT_URI2 = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH2);
    public static final Uri CONTENT_URI3 = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH3);
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int SHOPPING_LIST = 1;
    private static final int SHOPPING_LIST_ID = 2;
    private static final int PANTRY = 3;
    private static final int PANTRY_ID = 4;
    private static final int COOKBOOK = 5;
    private static final int COOKBOOK_ID = 6;
    private static final int INGREDIENTS = 7;
    private static final int INGREDIENTS_ID = 8;

    static {
        uriMatcher.addURI(AUTHORITY, BASE_PATH, SHOPPING_LIST);
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", SHOPPING_LIST_ID);
        uriMatcher.addURI(AUTHORITY, BASE_PATH1, PANTRY);
        uriMatcher.addURI(AUTHORITY, BASE_PATH1 + "/#", PANTRY_ID);
        uriMatcher.addURI(AUTHORITY, BASE_PATH2, COOKBOOK);
        uriMatcher.addURI(AUTHORITY, BASE_PATH2 + "/#", COOKBOOK_ID);
        uriMatcher.addURI(AUTHORITY, BASE_PATH3, INGREDIENTS);
        uriMatcher.addURI(AUTHORITY, BASE_PATH3 + "/#", INGREDIENTS_ID);
    }

    @Override
    public boolean onCreate() {
        theDB = DB.getInstance(getContext());
        return true;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values)
    {
        Uri _uri = null;
        long id;
        SQLiteDatabase db = theDB.getWritableDatabase();

        switch (uriMatcher.match(uri)) {
            case SHOPPING_LIST:
                id = db.insert(BASE_PATH, null, values);
                _uri = ContentUris.withAppendedId(uri, id);
                break;
            case PANTRY:
                id = db.insert(BASE_PATH1, null, values);
                _uri = ContentUris.withAppendedId(uri, id);
                break;
            case COOKBOOK:
                id = db.insert(BASE_PATH2, null, values);
                _uri = ContentUris.withAppendedId(uri, id);
                break;
            case INGREDIENTS:
                id = db.insert(BASE_PATH3, null, values);
                _uri = ContentUris.withAppendedId(uri, id);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(_uri, null);
        return _uri;
    }


    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder)
    {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        SQLiteDatabase db = theDB.getReadableDatabase();
        Cursor cursor = null;

        switch (uriMatcher.match(uri)) {
            case SHOPPING_LIST:
                queryBuilder.setTables(BASE_PATH);
                cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null,
                        sortOrder);
                break;
            case SHOPPING_LIST_ID:
                queryBuilder.setTables(BASE_PATH);
                cursor = queryBuilder.query(db, projection,
                        appendIdToSelection(selection, uri.getLastPathSegment()), selectionArgs,
                        null, null, sortOrder);
                break;
            case PANTRY:
                queryBuilder.setTables(BASE_PATH1);
                cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null,
                        sortOrder);
                break;
            case PANTRY_ID:
                queryBuilder.setTables(BASE_PATH1);
                cursor = queryBuilder.query(db, projection,
                        appendIdToSelection(selection, uri.getLastPathSegment()), selectionArgs,
                        null, null, sortOrder);
                break;
            case COOKBOOK:
                queryBuilder.setTables(BASE_PATH2);
                cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null,
                        sortOrder);
                break;
            case COOKBOOK_ID:
                queryBuilder.setTables(BASE_PATH2);
                cursor = queryBuilder.query(db, projection,
                        appendIdToSelection(selection, uri.getLastPathSegment()), selectionArgs,
                        null, null, sortOrder);
                break;
            case INGREDIENTS:
                queryBuilder.setTables(BASE_PATH3);
                cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null,
                        sortOrder);
                break;
            case INGREDIENTS_ID:
                queryBuilder.setTables(BASE_PATH3);
                cursor = queryBuilder.query(db, projection,
                        appendIdToSelection(selection, uri.getLastPathSegment()), selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs)
    {
        int count;
        SQLiteDatabase db = theDB.getWritableDatabase();

        switch (uriMatcher.match(uri)){
            case SHOPPING_LIST:
                count = db.update(BASE_PATH, values, selection, selectionArgs);
                break;
            case SHOPPING_LIST_ID:
                count = db.update(BASE_PATH, values,
                        appendIdToSelection(selection, uri.getLastPathSegment()),
                        selectionArgs);
                break;
            case PANTRY:
                count = db.update(BASE_PATH1, values, selection, selectionArgs);
                break;
            case PANTRY_ID:
                count = db.update(BASE_PATH1, values,
                        appendIdToSelection(selection, uri.getLastPathSegment()),
                        selectionArgs);
                break;
            case COOKBOOK:
                count = db.update(BASE_PATH2, values, selection, selectionArgs);
                break;
            case COOKBOOK_ID:
                count = db.update(BASE_PATH2, values,
                        appendIdToSelection(selection, uri.getLastPathSegment()),
                        selectionArgs);
                break;
            case INGREDIENTS:
                count = db.update(BASE_PATH3, values, selection, selectionArgs);
                break;
            case INGREDIENTS_ID:
                count = db.update(BASE_PATH3, values,
                        appendIdToSelection(selection, uri.getLastPathSegment()),
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri );
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }


    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int count;
        SQLiteDatabase db = theDB.getWritableDatabase();

        switch (uriMatcher.match(uri)){
            case SHOPPING_LIST:
                count = db.delete(BASE_PATH, selection, selectionArgs);
                break;
            case SHOPPING_LIST_ID:
                count = db.delete(BASE_PATH,
                        appendIdToSelection(selection, uri.getLastPathSegment()),
                        selectionArgs);
                break;
            case PANTRY:
                count = db.delete(BASE_PATH1, selection, selectionArgs);
                break;
            case PANTRY_ID:
                count = db.delete(BASE_PATH1,
                        appendIdToSelection(selection, uri.getLastPathSegment()),
                        selectionArgs);
                break;
            case COOKBOOK:
                count = db.delete(BASE_PATH2, selection, selectionArgs);
                break;
            case COOKBOOK_ID:
                count = db.delete(BASE_PATH2,
                        appendIdToSelection(selection, uri.getLastPathSegment()),
                        selectionArgs);
                break;
            case INGREDIENTS:
                count = db.delete(BASE_PATH3, selection, selectionArgs);
                break;
            case INGREDIENTS_ID:
                count = db.delete(BASE_PATH3,
                        appendIdToSelection(selection, uri.getLastPathSegment()),
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    private String appendIdToSelection(String selection, String sId) {
        int id = Integer.valueOf(sId);

        if (selection == null || selection.trim().equals(""))
            return "_ID = " + id;
        else
            return selection + " AND _ID = " + id;
    }
}
