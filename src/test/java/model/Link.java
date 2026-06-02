package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * Ссылка на ресурс или асинхронную операцию.
 * Возвращается при PUT (создание папки), POST (copy / move), DELETE и т.д.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Link {

    private String href;
    private String method;
    private boolean templated;
}

