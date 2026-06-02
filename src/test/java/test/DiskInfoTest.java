package test;

import io.qameta.allure.*;
import io.qameta.allure.Description;
import model.DiskInfo;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

@Epic("Тестирование API Яндекс.Диска")
@Feature("Disk")
public class DiskInfoTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(DiskInfoTest.class);

    @Test
    @Story("Получение метаинформации о диске")
    @DisplayName("TC-01:Получение метаинформации о диске пользователя (GET /v1/disk)")
    @Description("Успешное получение информации о диске (общий объём, использованное пространство, системные папки и т.д.)")
    @Tags({@Tag("Positive"), @Tag("API"), @Tag("disk")})
    void getDiskInfoTest() {
        DiskInfo diskInfo = diskController.getDiskInfo()
                .then()
                .statusCode(200)
                .extract()
                .as(DiskInfo.class);

        assertSoftly(softly -> {

            softly.assertThat(diskInfo.getTotalSpace())
                    .as("total_space должен быть > 0")
                    .isPositive();

            softly.assertThat(diskInfo.getUsedSpace())
                    .as("used_space должен быть >= 0")
                    .isGreaterThanOrEqualTo(0);

            softly.assertThat(diskInfo.getUsedSpace())
                    .as("used_space не должен превышать total_space")
                    .isLessThanOrEqualTo(diskInfo.getTotalSpace());

            softly.assertThat(diskInfo.getTrashSize())
                    .as("trash_size должен быть >= 0")
                    .isGreaterThanOrEqualTo(0);

            softly.assertThat(diskInfo.getUser())
                    .as("user не должен быть null")
                    .isNotNull();

            softly.assertThat(diskInfo.getUser().getUid())
                    .as("user.uid не должен быть пустым")
                    .isNotBlank();

            softly.assertThat(diskInfo.getUser().getLogin())
                    .as("user.login не должен быть пустым")
                    .isNotBlank();

            softly.assertThat(diskInfo.getSystemFolders())
                    .as("system_folders не должен быть null")
                    .isNotNull();
        });
    }

    @Test
    @Story("Получение метаинформации о диске")
    @DisplayName("TC-02: Запрос без токена на получение метаинформации о диске пользователя (GET /v1/disk)")
    @Description("Запрос без заголовка авторизации")
    @Tags({@Tag("Negative"), @Tag("API"), @Tag("disk")})
    void getDiskInfo_withoutTokenTest() {
        new controllers.DiskController("")
                .getDiskInfo()
                .then()
                .statusCode(401);
    }


}


