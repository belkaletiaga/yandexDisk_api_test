package test;

import config.TestPropertiesConfig;
import controllers.DiskController;
import io.qameta.allure.Allure;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Базовый класс для всех API-тестов.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaseTest {

    private static final Logger log = LoggerFactory.getLogger(BaseTest.class);

    protected static TestPropertiesConfig config;
    protected DiskController diskController;

    @BeforeAll
    static void configureRestAssured() {
        config = ConfigFactory.create(TestPropertiesConfig.class);
        RestAssured.baseURI = config.getApiBaseUrl();
        RestAssured.filters(new AllureRestAssured());
        log.info("RestAssured настроен: baseURI={}", config.getApiBaseUrl());
        Allure.step("RestAssured настроен: baseURI= {" + config.getApiBaseUrl()+"}");
    }

    @BeforeEach
    void initToken() {
        String token = getToken();
        String maskedToken = maskToken(token);
        diskController = new DiskController(token);
        log.info("OAuth-токен получен: {}", maskedToken);
        Allure.step("OAuth-токен получен = {" + maskedToken + "}");
    }
    private String getToken() {
        String token = config.getOauthToken();
        if (token == null || token.isBlank()) {
            throw new IllegalStateException(
                    "OAuth-токен не задан");
        }
        return token;
    }

    private String maskToken(String token) {
        if (token == null || token.length() <= 8) {
            return "***";
        }
        return token.substring(0, 4) + "..." + token.substring(token.length() - 4);
    }
}
