package config;

import org.aeonbits.owner.Config;


@Config.Sources({
        "classpath:default.properties",
        "system:properties"
})
public interface TestPropertiesConfig extends Config  {
    @Key("apiBaseUrl")
    String getApiBaseUrl();

    @Key("oauthToken")
    String getOauthToken();

}
