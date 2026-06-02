package controllers;

import config.TestPropertiesConfig;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.aeonbits.owner.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static constants.CommonConstants.*;
import static io.restassured.RestAssured.given;

/**
 * Клиент для взаимодействия с API Яндекс.Диска.
 * Каждый метод соответствует одной точке доступа из спецификации.
 */
public class DiskController {
    private static final Logger log = LoggerFactory.getLogger(DiskController.class);

    private final TestPropertiesConfig config;
    private final RequestSpecification baseSpec;

    public DiskController(String oauthToken) {
        this.config = ConfigFactory.create(TestPropertiesConfig.class);
        this.baseSpec = given()
                .baseUri(config.getApiBaseUrl())
                .header("Authorization", "OAuth " + oauthToken)
                .accept("application/json");
    }

    @Step("Сформировать GET запрос на /v1/disk/")
    public Response getDiskInfo() {
        log.debug("Запрос информации о диске");
        return given(baseSpec)
                .get(DISK_PATH)
                .andReturn();
    }

    @Step("PUT /v1/disk/resources — создать папку: {path}")
    public Response createFolder(String path) {
        log.debug("Создание папки: {}", path);
        return given(baseSpec)
                .queryParam("path", path)
                .put(RESOURCES_PATH)
                .andReturn();
    }

    @Step("GET /v1/disk/resources — получить метаинформацию ресурса: {path}")
    public Response getResource(String path) {
        log.debug("Запрос метаинформации ресурса: {}", path);
        return given(baseSpec)
                .queryParam("path", path)
                .get(RESOURCES_PATH)
                .andReturn();
    }

    @Step("PATCH /v1/disk/resources — обновить custom_properties ресурса: {path}")
    public Response patchResource(String path, Map<String, Object> customProperties) {
        log.debug("PATCH ресурса: {}", path);
        Map<String, Object> body = Map.of("custom_properties", customProperties);
        return given(baseSpec)
                .queryParam("path", path)
                .contentType("application/json")
                .body(body)
                .patch(RESOURCES_PATH)
                .andReturn();
    }

    @Step("POST /v1/disk/resources/copy — копировать {from} в {to}")
    public Response copyResource(String from, String to) {
        log.debug("Копирование ресурса: {} в {}", from, to);
        return given(baseSpec)
                .queryParam("from", from)
                .queryParam("path", to)
                .post(COPY_PATH)
                .andReturn();
    }

    @Step("DELETE /v1/disk/resources — удалить ресурс: {path}, permanently={permanently}")
    public Response deleteResource(String path, boolean permanently) {
        log.debug("Удаление ресурса: {}, permanently={}", path, permanently);
        return given(baseSpec)
                .queryParam("path", path)
                .queryParam("permanently", permanently)
                .delete(RESOURCES_PATH)
                .andReturn();
    }
}
