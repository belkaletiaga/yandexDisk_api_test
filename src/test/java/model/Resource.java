package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Тело ответа для операций с ресурсами (/v1/disk/resources).
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Resource {

    private String path;
    private String type;
    private String name;
    private String created;
    private String modified;
    private long   size;

    @JsonProperty("mime_type")
    private String mimeType;

    private String md5;
    private String sha256;

    @JsonProperty("resource_id")
    private String resourceId;

    @JsonProperty("public_key")
    private String publicKey;

    @JsonProperty("public_url")
    private String publicUrl;

    private long revision;

    @JsonProperty("custom_properties")
    private Map<String, Object> customProperties;

    @JsonProperty("_embedded")
    private Embedded embedded;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Embedded {
        private int  total;
        private int  limit;
        private int  offset;
        private String path;
        private String sort;
        private List<Resource> items;
    }
}
