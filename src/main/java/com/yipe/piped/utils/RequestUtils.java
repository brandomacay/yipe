package com.yipe.piped.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.yipe.piped.consts.Constants;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import rocks.kavin.reqwest4j.ReqwestUtils;
import rocks.kavin.reqwest4j.Response;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class RequestUtils {

    public static CompletableFuture<Response> sendGetRaw(String url) throws Exception {
        return ReqwestUtils.fetch(url, "GET", null, Map.of());
    }

    public static CompletableFuture<String> sendGet(String url) throws Exception {
        return ReqwestUtils.fetch(url, "GET", null, Map.of())
                .thenApply(Response::body)
                .thenApplyAsync(String::new);
    }

    public static CompletableFuture<String> sendGet(String url, String ua) throws Exception {
        return ReqwestUtils.fetch(url, "GET", null, Map.of("User-Agent", ua))
                .thenApply(Response::body)
                .thenApplyAsync(String::new);
    }

    public static JsonNode getJsonNode(OkHttpClient client, Request request) throws IOException {
        try (var resp = client.newCall(request).execute()) {
            try {
                return Constants.mapper.readTree(resp.body().byteStream());
            } catch (Exception e) {
                if (!resp.isSuccessful())
                    ExceptionHandler.handle(e);
                throw new RuntimeException("Failed to parse JSON", e);
            }
        }
    }

    public static CompletableFuture<JsonNode> sendGetJson(String url) {
        return ReqwestUtils.fetch(url, "GET", null, Map.of()).thenApply(Response::body).thenApplyAsync(resp -> {
            try {
                return Constants.mapper.readTree(resp);
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse JSON", e);
            }
        });
    }
}
