package me.junbin.gradprj.enumeration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.junbin.commons.converter.gson.LocalDateTimeTypeAdapter;
import me.junbin.commons.converter.gson.LocalDateTypeAdapter;
import me.junbin.commons.converter.gson.LocalTimeTypeAdapter;
import me.junbin.commons.util.Jsr310Utils;
import me.junbin.gradprj.api.Jdk8GsonUtils;
import me.junbin.gradprj.converter.PermTypeConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static me.junbin.commons.gson.strategy.GsonStrategies.IGNORE_FILED_EQUALS_PASSWORD;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/15 21:59
 * @description :
 */
public enum MyGsonor implements Jdk8GsonUtils {

    SIMPLE(new GsonBuilder()
            .registerTypeAdapter(PermType.class,
                    new PermTypeConverter())
            .registerTypeAdapter(LocalTime.class,
                    new LocalTimeTypeAdapter(Jsr310Utils.colon_HHmmss))
            .registerTypeAdapter(LocalDate.class,
                    new LocalDateTypeAdapter(Jsr310Utils.hyphen_yyyyMMdd))
            .registerTypeAdapter(LocalDateTime.class,
                    new LocalDateTimeTypeAdapter(Jsr310Utils.hyphen_yyyyMMddHHmmss))
            .create()),

    PRETTY(new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(PermType.class,
                    new PermTypeConverter())
            .registerTypeAdapter(LocalTime.class,
                    new LocalTimeTypeAdapter(Jsr310Utils.colon_HHmmss))
            .registerTypeAdapter(LocalDate.class,
                    new LocalDateTypeAdapter(Jsr310Utils.hyphen_yyyyMMdd))
            .registerTypeAdapter(LocalDateTime.class,
                    new LocalDateTimeTypeAdapter(Jsr310Utils.hyphen_yyyyMMddHHmmss))
            .create()),

    EXCLUDE_PWD_FIELD_SIMPLE(new GsonBuilder()
            .setExclusionStrategies(IGNORE_FILED_EQUALS_PASSWORD)
            .registerTypeAdapter(PermType.class,
                    new PermTypeConverter())
            .registerTypeAdapter(LocalTime.class,
                    new LocalTimeTypeAdapter(Jsr310Utils.colon_HHmmss))
            .registerTypeAdapter(LocalDate.class,
                    new LocalDateTypeAdapter(Jsr310Utils.hyphen_yyyyMMdd))
            .registerTypeAdapter(LocalDateTime.class,
                    new LocalDateTimeTypeAdapter(Jsr310Utils.hyphen_yyyyMMddHHmmss))
            .create()),

    EXCLUDE_PWD_FIELD_PRETTY(new GsonBuilder()
            .setPrettyPrinting()
            .setExclusionStrategies(IGNORE_FILED_EQUALS_PASSWORD)
            .registerTypeAdapter(PermType.class,
                    new PermTypeConverter())
            .registerTypeAdapter(LocalTime.class,
                    new LocalTimeTypeAdapter(Jsr310Utils.colon_HHmmss))
            .registerTypeAdapter(LocalDate.class,
                    new LocalDateTypeAdapter(Jsr310Utils.hyphen_yyyyMMdd))
            .registerTypeAdapter(LocalDateTime.class,
                    new LocalDateTimeTypeAdapter(Jsr310Utils.hyphen_yyyyMMddHHmmss))
            .create()),


    SN_SIMPLE(new GsonBuilder()
            .serializeNulls()
            .registerTypeAdapter(PermType.class,
                    new PermTypeConverter())
            .registerTypeAdapter(LocalTime.class,
                    new LocalTimeTypeAdapter(Jsr310Utils.colon_HHmmss))
            .registerTypeAdapter(LocalDate.class,
                    new LocalDateTypeAdapter(Jsr310Utils.hyphen_yyyyMMdd))
            .registerTypeAdapter(LocalDateTime.class,
                    new LocalDateTimeTypeAdapter(Jsr310Utils.hyphen_yyyyMMddHHmmss))
            .create()),

    SN_PRETTY(new GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .registerTypeAdapter(PermType.class,
                    new PermTypeConverter())
            .registerTypeAdapter(LocalTime.class,
                    new LocalTimeTypeAdapter(Jsr310Utils.colon_HHmmss))
            .registerTypeAdapter(LocalDate.class,
                    new LocalDateTypeAdapter(Jsr310Utils.hyphen_yyyyMMdd))
            .registerTypeAdapter(LocalDateTime.class,
                    new LocalDateTimeTypeAdapter(Jsr310Utils.hyphen_yyyyMMddHHmmss))
            .create()),

    SN_EXCLUDE_PWD_FIELD_SIMPLE(new GsonBuilder()
            .serializeNulls()
            .setExclusionStrategies(IGNORE_FILED_EQUALS_PASSWORD)
            .registerTypeAdapter(PermType.class,
                    new PermTypeConverter())
            .registerTypeAdapter(LocalTime.class,
                    new LocalTimeTypeAdapter(Jsr310Utils.colon_HHmmss))
            .registerTypeAdapter(LocalDate.class,
                    new LocalDateTypeAdapter(Jsr310Utils.hyphen_yyyyMMdd))
            .registerTypeAdapter(LocalDateTime.class,
                    new LocalDateTimeTypeAdapter(Jsr310Utils.hyphen_yyyyMMddHHmmss))
            .create()),

    SN_EXCLUDE_PWD_FIELD_PRETTY(new GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .setExclusionStrategies(IGNORE_FILED_EQUALS_PASSWORD)
            .registerTypeAdapter(PermType.class,
                    new PermTypeConverter())
            .registerTypeAdapter(LocalTime.class,
                    new LocalTimeTypeAdapter(Jsr310Utils.colon_HHmmss))
            .registerTypeAdapter(LocalDate.class,
                    new LocalDateTypeAdapter(Jsr310Utils.hyphen_yyyyMMdd))
            .registerTypeAdapter(LocalDateTime.class,
                    new LocalDateTimeTypeAdapter(Jsr310Utils.hyphen_yyyyMMddHHmmss))
            .create());


    private final Gson gson;

    MyGsonor(Gson gson) {
        this.gson = gson;
    }

    public Gson getGson() {
        return gson;
    }

    @Override
    public Gson get() {
        return gson;
    }

}
