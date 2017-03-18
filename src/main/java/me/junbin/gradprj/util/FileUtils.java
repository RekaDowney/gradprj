package me.junbin.gradprj.util;

import me.junbin.commons.util.Args;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/27 21:02
 * @description :
 */
public abstract class FileUtils {

    public static Path forceMkDirIfParentNotExists(final String filePath) throws IOException {
        String notNullFilePath = Args.notNull(filePath)
                                     .trim();
        return forceMkDirIfParentNotExists(Paths.get(notNullFilePath));
    }

    public static Path forceMkDirIfParentNotExists(final File filePath) throws IOException {
        File notNullFile = Args.notNull(filePath);
        return forceMkDirIfParentNotExists(notNullFile.toPath());
    }

    public static Path forceMkDirIfParentNotExists(final Path filePath) throws IOException {
        Path notNullPath = Args.notNull(filePath)
                               .normalize();
        Path parent = notNullPath.getParent();
        if (Files.notExists(parent)) {
            Files.createDirectories(parent);
        }
        return notNullPath;
    }

    public static Path forceMkDirIfNotExists(final String filePath) throws IOException {
        String notNullFilePath = Args.notNull(filePath)
                                     .trim();
        return forceMkDirIfNotExists(Paths.get(notNullFilePath));
    }

    public static Path forceMkDirIfNotExists(final File filePath) throws IOException {
        File notNullFile = Args.notNull(filePath);
        return forceMkDirIfNotExists(notNullFile.toPath());
    }

    public static Path forceMkDirIfNotExists(final Path filePath) throws IOException {
        Path notNullPath = Args.notNull(filePath)
                               .normalize();
        if (Files.notExists(notNullPath)) {
            Files.createDirectories(notNullPath);
        }
        return notNullPath;
    }

    public static Path copyInputStreamToFile(InputStream source, String targetFilePath) throws IOException {
        String notNullFilePath = Args.notNull(targetFilePath)
                                     .trim();
        return copyInputStreamToFile(source, Paths.get(notNullFilePath));
    }

    public static Path copyInputStreamToFile(InputStream source, File targetFilePath) throws IOException {
        File notNullFile = Args.notNull(targetFilePath);
        return copyInputStreamToFile(source, notNullFile.toPath());
    }

    public static Path copyInputStreamToFile(InputStream source, Path targetFilePath) throws IOException {
        Args.notNull(source);
        Path notNullPath = forceMkDirIfParentNotExists(targetFilePath);
        long writeBytes = Files.copy(source, notNullPath, REPLACE_EXISTING);
        return notNullPath;
    }

}
