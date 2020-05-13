package com.jive.restapi.automation.cloud.configs;

import static org.apache.commons.lang.StringUtils.EMPTY;

import java.io.InputStream;

import org.yaml.snakeyaml.Yaml;

public final class AppConfig {

    public static final String DEFAULT = "default";
    private static final String CONFIG_YML = "config.yml";
    private static final String DASH = "-";
    private transient ClientConfig clientConfig;
    private transient String filename;
    private static AppConfig instance;

    private AppConfig() {
    }

    private boolean fileStartsWith(final String env) {
        return filename.startsWith(env);
    }

    public static void init(final String environment) {
        if (instance != null && instance.fileStartsWith(environment)) {
            return;
        }
        instance = new AppConfig();
        updateFileName(environment);

        loadFromYaml();
    }

    private static void loadFromYaml() {
        final InputStream inputStream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(instance.filename);
        Yaml yaml = new Yaml();
        instance.clientConfig = yaml.loadAs(inputStream, ClientConfig.class);
    }

    private static void updateFileName(final String environment) {
        instance.filename = EMPTY;
        if (environment.isEmpty() || environment.equalsIgnoreCase(DEFAULT)) {
            instance.filename = CONFIG_YML;
        } else {
            instance.filename = environment + DASH + CONFIG_YML;
        }
    }

    public static AppConfig getInstance() {
        if (null == instance) {
            init(EMPTY);
        }
        return instance;
    }

    public User getRole(final Role role) {
        return clientConfig.get(role);
    }
}
