package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TestConfig {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input = TestConfig.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("config.properties not found in classpath");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    private TestConfig() {} // Utility class – no instantiation

    // ── Site ────────────────────────────────────────────────────────────────────
    public static String getWebsiteUrl() {
        return properties.getProperty("website.url");
    }

    // ── Search & Filters ────────────────────────────────────────────────────────
    public static String getSearchKeyword() {
        return properties.getProperty("search.keyword");
    }

    public static String getPriceFilter() {
        return properties.getProperty("price.filter");
    }

    public static int getMaxPrice() {
        return Integer.parseInt(properties.getProperty("max.price"));
    }

    public static String getBrandFilter() {
        return properties.getProperty("brand.filter");
    }

    public static String getBrandName() {
        return properties.getProperty("brand.name");
    }

    public static String getApplyButton() {
        return properties.getProperty("apply.button");
    }

    public static int getTopN() {
        return Integer.parseInt(properties.getProperty("top.n"));
    }

    // ── Gift Card Form Data ──────────────────────────────────────────────────────
    public static String getGcRecipientName() {
        return properties.getProperty("gc.recipient.name");
    }

    public static String getGcSenderName() {
        return properties.getProperty("gc.sender.name");
    }

    public static String getGcRecipientMobile() {
        return properties.getProperty("gc.recipient.mobile");
    }

    public static String getGcSenderMobile() {
        return properties.getProperty("gc.sender.mobile");
    }

    public static String getGcSenderEmail() {
        return properties.getProperty("gc.sender.email");
    }

    public static String getGcMessage() {
        return properties.getProperty("gc.message");
    }

    public static int getGcAmount() {
        return Integer.parseInt(properties.getProperty("gc.amount"));
    }
}