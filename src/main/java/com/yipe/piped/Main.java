package com.yipe.piped;

import com.yipe.piped.consts.Constants;
import com.yipe.piped.server.ServerLauncher;
import com.yipe.piped.utils.DownloaderImpl;
import com.yipe.piped.utils.ErrorResponse;
import com.yipe.piped.utils.Multithreading;
import com.yipe.piped.utils.RequestUtils;
import io.activej.inject.Injector;
import io.sentry.Sentry;
import okhttp3.OkHttpClient;
import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.localization.ContentCountry;
import org.schabi.newpipe.extractor.localization.Localization;
import org.schabi.newpipe.extractor.services.youtube.YoutubeJavaScriptPlayerManager;
import org.schabi.newpipe.extractor.services.youtube.YoutubeParsingHelper;
import org.schabi.newpipe.extractor.services.youtube.extractors.YoutubeStreamExtractor;
import org.schabi.newpipe.extractor.stream.StreamInfo;
import rocks.kavin.reqwest4j.ReqwestUtils;

import java.security.Security;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) throws Exception {
        Security.setProperty("crypto.policy", "unlimited");
        //Security.addProvider(new BouncyCastleProvider());

        ReqwestUtils.init(Constants.REQWEST_PROXY, Constants.REQWEST_PROXY_USER, Constants.REQWEST_PROXY_PASS);
        NewPipe.init(new DownloaderImpl(),new Localization("en", "US"),new ContentCountry("US"));
        //NewPipe.init(new DownloaderImpl(), new Localization("en", "US"), ContentCountry.DEFAULT, Multithreading.getCachedExecutor());
        YoutubeStreamExtractor.forceFetchAndroidClient(true);
        YoutubeStreamExtractor.forceFetchIosClient(true);
        YoutubeParsingHelper.setConsentAccepted(Constants.CONSENT_COOKIE);
        try {
            StreamInfo.getInfo("https://www.youtube.com/watch?v=dQw4w9WgXcQ");
        } catch (Exception ignored) {
        }

        Thread htmlThread = new Thread(() -> {
            try {
                var html = RequestUtils.sendGet("https://www.youtube.com/").get();
                var regex = Pattern.compile("GL\":\"([A-Z]{2})\"", Pattern.MULTILINE);
                var matcher = regex.matcher(html);
                if (matcher.find()) {
                    Constants.YOUTUBE_COUNTRY = matcher.group(1);
                }
            } catch (Exception ignored) {
                System.err.println("Failed to get country from YouTube!");
            }
        });
        htmlThread.setDaemon(true);
        htmlThread.start();

        Sentry.init(options -> {
            options.setDsn(Constants.SENTRY_DSN);
            options.setRelease(Constants.VERSION);
            options.addIgnoredExceptionForType(ErrorResponse.class);
            options.setTracesSampleRate(0.1);
        });
        Injector.useSpecializer();
        Thread htmlThrea = new Thread(() -> {
            new OkHttpClient.Builder().readTimeout(60,TimeUnit.SECONDS);
        });
        htmlThrea.setDaemon(true);
        htmlThrea.start();

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.printf("ThrottlingCache: %o entries%n", YoutubeJavaScriptPlayerManager.getThrottlingParametersCacheSize());
                YoutubeJavaScriptPlayerManager.clearThrottlingParametersCache();
            }
        }, 0, TimeUnit.MINUTES.toMillis(60));


        if (!Constants.DISABLE_SERVER) {
            new Thread(() -> {
                try {
                    new ServerLauncher().launch(args);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
        if (Constants.DISABLE_TIMERS)
            return;

    }
}
