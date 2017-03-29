package me.junbin.gradprj.util;

import me.junbin.commons.util.Args;
import me.junbin.gradprj.domain.Account;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.apache.poi.ss.usermodel.Cell.*;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/3/28 19:52
 * @description :
 */
public abstract class ExcelUtils {

    public static List<Account> parse(final String filePath) throws IOException, InvalidFormatException {
        return parse(Paths.get(filePath));
    }

    public static List<Account> parse(final File filePath) throws IOException, InvalidFormatException {
        return parse(Args.notNull(filePath).toPath());
    }

    public static List<Account> parse(final Path filePath) throws IOException, InvalidFormatException {
        Path path = Args.notNull(filePath).normalize();
        if (Files.notExists(path)) {
            throw new FileNotFoundException(String.format("文件%s不存在", filePath.toString()));
        }
        return parse(Files.newInputStream(path));
    }

    public static List<Account> parse(final InputStream is) throws IOException, InvalidFormatException {
        Workbook workbook = WorkbookFactory.create(is);
        Sheet sheet = workbook.getSheetAt(workbook.getActiveSheetIndex());
        Row row;
        String principal;
        String password;
        Account account;
        List<Account> result = new ArrayList<>(sheet.getLastRowNum() - 1);
        for (int i = 1, last = sheet.getLastRowNum(); i < last; i++) {
            row = sheet.getRow(i);
            principal = cellValueAt(row.getCell(0));
            if (StringUtils.isEmpty(principal)) {
                continue;
            }
            password = cellValueAt(row.getCell(1));
            if (StringUtils.length(password) < 6) {
                continue;
            }
            account = new Account(principal, EncryptUtils.md5Encrypt(password, principal), LocalDateTime.now());
            account.setId(Global.uuid());
            result.add(account);
        }
        return result;
    }

    public static String cellValueAt(final Cell cell) {
        if (cell == null) {
            return "";
        }
        String cellValue;
        switch (cell.getCellType()) {
            case CELL_TYPE_BLANK:
            case CELL_TYPE_ERROR:
                cellValue = "";
                break;
            case CELL_TYPE_BOOLEAN:
                cellValue = cell.getBooleanCellValue() ? "TRUE" : "FALSE";
                break;
            case CELL_TYPE_FORMULA:
                cellValue = cell.getCellFormula();
                break;
            case CELL_TYPE_STRING:
                cellValue = cell.getStringCellValue();
                break;
            case CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    cellValue = Jsr310UtilExt.format(cell.getDateCellValue(), "yyyy-MM-dd HH:mm:ss");
                } else {
                    double dblValue = cell.getNumericCellValue();
                    long longValue = (long) dblValue;
                    if (dblValue - longValue == 0) {
                        cellValue = String.valueOf(longValue);
                    } else {
                        cellValue = String.valueOf(dblValue);
                    }
                }
                break;
            default:
                cellValue = cell.toString().trim();
        }
        return cellValue.trim();
    }

}
