package com.mrwii.exoplayer;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
//import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
//import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class MainActivity extends AppCompatActivity implements PlaybackControlView.VisibilityListener {

	private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();

	private SimpleExoPlayerView simpleExoPlayerView;
	private SimpleExoPlayer player;

	private TrackSelector trackSelector;
	private LoadControl loadControl;
	private DataSource.Factory mediaDataSourceFactory;

	private boolean shouldAutoPlay;
	private MediaSource mediaSource;
	private Uri uri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		shouldAutoPlay = true;

		// 1. Create a default TrackSelector
		TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);
		trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

		String userAgent = Util.getUserAgent(this, "ExoPlayer");
		DataSource.Factory baseDataSourceFactory = new DefaultHttpDataSourceFactory(userAgent, BANDWIDTH_METER);
		mediaDataSourceFactory = new DefaultDataSourceFactory(this, BANDWIDTH_METER, baseDataSourceFactory);

		// 2. Create a default LoadControl
		loadControl = new DefaultLoadControl();

		// 3. Create the player
		SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);

		simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.player_view);
		simpleExoPlayerView.setControllerVisibilityListener(this);
		simpleExoPlayerView.requestFocus();

		initializePlayer();
	}

	private void initializePlayer() {
		Intent intent = getIntent();
		boolean needNewPlayer = player == null;
		if (needNewPlayer) {
			player = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
			simpleExoPlayerView.setPlayer(player);
			player.setPlayWhenReady(shouldAutoPlay);

//			uri = Uri.parse("http://vjs.zencdn.net/v/oceans.mp4");
//			mediaSource = new ExtractorMediaSource(uri, mediaDataSourceFactory, new DefaultExtractorsFactory(), null, null);

			uri = Uri.parse("http://devimages.apple.com/samplecode/adDemo/ad.m3u8");
			// This is the MediaSource representing the media to be played.
			mediaSource = new HlsMediaSource(uri, mediaDataSourceFactory, null, null);

			player.prepare(mediaSource, true, true);
		}
	}

	@Override
	public void onVisibilityChange(int visibility) {
	}
}
