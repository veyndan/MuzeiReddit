package com.veyndan.muzeireddit;

import android.content.Intent;
import android.net.Uri;

import com.google.android.apps.muzei.api.Artwork;
import com.google.android.apps.muzei.api.RemoteMuzeiArtSource;

import rawjava.Credentials;
import rawjava.Reddit;
import rawjava.Sort;
import rawjava.model.Link;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RedditArtSource extends RemoteMuzeiArtSource {
    private static final String TAG = "veyndan_RedditArtSource";
    private static final String SOURCE_NAME = "RedditArtSource";

    private static final int ROTATE_TIME_MILLIS = 3 * 60 * 60 * 1000; // rotate every 3 hours

    public RedditArtSource() {
        super(SOURCE_NAME);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setUserCommands(BUILTIN_COMMAND_ID_NEXT_ARTWORK);
    }

    @Override
    protected void onTryUpdate(int reason) throws RetryException {
        Credentials credentials = Credentials.create(getResources().openRawResource(R.raw.credentials));
        Reddit reddit = new Reddit(credentials);

        reddit.subreddit("earthporn", Sort.NEW)
                .map(thing -> thing.data.children)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(children -> {
                    Link link = children.get(2).data;
                    publishArtwork(new Artwork.Builder()
                            .title(link.title)
                            .byline(link.author)
                            .imageUri(Uri.parse(link.url))
                            .token(link.id)
                            .viewIntent(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("https://www.reddit.com" + link.permalink)))
                            .build());
                });

        scheduleUpdate(System.currentTimeMillis() + ROTATE_TIME_MILLIS);
    }
}
