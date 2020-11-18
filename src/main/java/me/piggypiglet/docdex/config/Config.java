package me.piggypiglet.docdex.config;

import com.google.inject.Singleton;
import me.piggypiglet.docdex.file.annotations.File;
import org.jetbrains.annotations.NotNull;

// ------------------------------
// Copyright (c) PiggyPiglet 2020
// https://www.piggypiglet.me
// ------------------------------
@File(
        internalPath = "/config.json",
        externalPath = "config.json"
)
@Singleton
public final class Config {
    private String host;
    private int port;

    @NotNull
    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}