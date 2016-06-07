package com.veyndan.muzeireddit;

import com.google.android.apps.muzei.api.RemoteMuzeiArtSource;

public class RedditArtSource extends RemoteMuzeiArtSource {
    private static final String TAG = "veyndan_RedditArtSource";
    private static final String SOURCE_NAME = "RedditArtSource";

    public RedditArtSource() {
        super(SOURCE_NAME);
    }

    @Override
    protected void onTryUpdate(int reason) throws RetryException {

    }
}
