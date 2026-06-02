package test;

import controllers.DiskController;
import io.qameta.allure.*;
import model.Link;
import model.Resource;
import org.junit.jupiter.api.*;

import java.util.Map;
import java.util.UUID;

import static constants.CommonConstants.DISK_PREFIX;
import static constants.CommonConstants.TYPE_DIR;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@Epic("Тестирование API Яндекс.Диска")
@Feature("Resource")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ResourceTest extends BaseTest {

    private static String testFolder;
    private static String copiedFolder;

    @BeforeAll
    static void generateFolderNames() {
        String uid  = UUID.randomUUID().toString().substring(0, 8);
        testFolder   = DISK_PREFIX + "tests_" + uid;
        copiedFolder = DISK_PREFIX + "tests_copy_" + uid;
    }

    @AfterAll
    static void cleanup() {
        // Удалить все созданные папки после всех тестов
        try {
            if (testFolder != null) {
                new DiskController(config.getOauthToken()).deleteResource(testFolder, true);
            }
        } catch (Exception ignored) {}
        try {
            if (copiedFolder != null) {
                new DiskController(config.getOauthToken()).deleteResource(copiedFolder, true);
            }
        } catch (Exception ignored) {}
    }

    @Test
    @Order(1)
    @Story("Создание папки")
    @DisplayName("TC-03:Создание папки (PUT /v1/disk/resources)/")
    @Description("Успешное создание новой папки на диске.")
    @Tags({@Tag("Positive"), @Tag("API"), @Tag("folder")})
    void createFolderTest() {
        Link link = diskController.createFolder( testFolder)
                .then()
                .statusCode(201)
                .extract()
                .as(Link.class);

        assertSoftly(softly -> {
            softly.assertThat(link.getHref()).as("href не должен быть пустым").isNotBlank();
            softly.assertThat(link.getMethod()).as("method не должен быть пустым").isNotBlank();
        });
    }

    @Test
    @Order(2)
    @Story("Получение метаинформации о папке")
    @DisplayName("TC-04: Получение метаинформации папки (GET /v1/disk/resources)")
    @Description("Успешное получение метаинформации о существующей папке.")
    @Tags({@Tag("Positive"), @Tag("API"), @Tag("folder")})

    void getResourceTest() {
        Resource resource = diskController.getResource(testFolder)
                .then()
                .statusCode(200)
                .extract()
                .as(Resource.class);

        assertSoftly(softly -> {
            softly.assertThat(resource.getType())
                    .as("тип ресурса должен быть 'dir'")
                    .isEqualTo(TYPE_DIR);

            softly.assertThat(resource.getPath())
                    .as("path в ответе должен совпадать с запрошенным")
                    .isEqualTo(testFolder);

            softly.assertThat(resource.getName())
                    .as("name не должен быть пустым")
                    .isNotBlank();

            softly.assertThat(resource.getCreated())
                    .as("created не должен быть пустым")
                    .isNotBlank();

            softly.assertThat(resource.getModified())
                    .as("modified не должен быть пустым")
                    .isNotBlank();
        });
    }

    @Test
    @Order(3)
    @Story("Обновление метаинформации папки")
    @DisplayName("TC-05: Обновление custom_properties папки (PATCH /v1/disk/resources)")
    @Description("Добавление пользовательских атрибутов к папке")
    @Tags({@Tag("Positive"), @Tag("API"), @Tag("folder")})
    void updateCustomPropertiesTest()  {
        Map<String, Object> props = Map.of("env", "autotest", "version", "2");

        Resource resource = diskController.patchResource(testFolder, props)
                .then()
                .statusCode(200)
                .extract()
                .as(Resource.class);

        assertSoftly(softly -> {
            softly.assertThat(resource.getCustomProperties())
                    .as("custom_properties не должны быть null")
                    .isNotNull();

            softly.assertThat(resource.getCustomProperties())
                    .as("custom_properties должны содержать переданные ключи")
                    .containsEntry("env", "autotest")
                    .containsEntry("version", "2");
        });
    }

    @Test
    @Order(4)
    @Story("Копирование папки")
    @DisplayName("TC-06: Копирование папки (POST /v1/disk/resources/copy)")
    @Description("Копирование существующей папки.")
    @Tags({@Tag("Positive"), @Tag("API"), @Tag("folder")})
    void copyFolderTest() {
       diskController.copyResource(testFolder, copiedFolder)
               .then()
               .statusCode(201);

       diskController.getResource(copiedFolder)
                .then()
                .statusCode(200)
                .extract()
                .as(Resource.class);
    }

    @Test
    @Order(5)
    @Story("Создание папки")
    @DisplayName("TC-07: Создание уже существующей папки (PUT /v1/disk/resources)")
    @Description("Попытка создать папку, которая уже существует.")
    @Tags({@Tag("Negative"), @Tag("API"), @Tag("folder")})
    void createFolder_alreadyExists_shouldReturn409() {
        diskController.createFolder(testFolder)
                .then()
                .statusCode(409);
    }

    @Test
    @Order(6)
    @Story("Удаление папки (DELETE /v1/disk/resources)")
    @DisplayName("TC-08: DELETE /v1/disk/resources — безвозвратное удаление папки")
    @Description("Безвозвратное удаление папки(не помещая в корзину).")
    @Tags({@Tag("Positive"), @Tag("delete"), @Tag("folder")})
    void deleteFolderTest() {
        diskController.deleteResource(testFolder, true)
                .then()
                .statusCode(204);
        diskController.deleteResource(testFolder, true)
                .then()
                .statusCode(404);
    }
}

