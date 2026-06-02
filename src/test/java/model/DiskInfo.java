package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Представляет собой тело ответа {GET /v1/disk}.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DiskInfo {

    @JsonProperty("total_space")
    private long totalSpace;

    @JsonProperty("used_space")
    private long usedSpace;

    @JsonProperty("trash_size")
    private long trashSize;

    @JsonProperty("max_file_size")
    private long maxFileSize;

    @JsonProperty("is_paid")
    private boolean isPaid;

    @JsonProperty("revision")
    private long revision;

    @JsonProperty("system_folders")
    private SystemFolders systemFolders;

    private UserInfo user;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UserInfo {
        private String uid;
        private String login;

        @JsonProperty("display_name")
        private String displayName;

        private String country;
    }
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SystemFolders {
        private String applications;
        private String downloads;
        private String screenshots;
        private String photostream;
    }
}
