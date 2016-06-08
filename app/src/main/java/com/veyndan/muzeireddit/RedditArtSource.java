package com.veyndan.muzeireddit;

import android.util.Log;

import com.google.android.apps.muzei.api.RemoteMuzeiArtSource;

import rawjava.Credentials;
import rawjava.Reddit;
import rawjava.model.Link;
import rawjava.model.Listing;
import rawjava.model.Thing;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class RedditArtSource extends RemoteMuzeiArtSource {
    private static final String TAG = "veyndan_RedditArtSource";
    private static final String SOURCE_NAME = "RedditArtSource";

    public RedditArtSource() {
        super(SOURCE_NAME);
    }

    @Override
    protected void onTryUpdate(int reason) throws RetryException {
        Credentials credentials = Credentials.create(getResources().openRawResource(R.raw.credentials));
        Reddit reddit = new Reddit(credentials);

        reddit.subreddit("earthporn")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<Thing<Listing<Thing<Link>>>>() {
                    @Override
                    public void call(Thing<Listing<Thing<Link>>> thing) {
                        Log.d(TAG, "call: " + thing.data.children.get(1).data.title);
                    }
                });
    }
}
