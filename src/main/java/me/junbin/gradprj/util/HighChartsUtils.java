package me.junbin.gradprj.util;

import com.google.gson.JsonArray;
import me.junbin.gradprj.enumeration.DocumentType;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/3/27 21:56
 * @description :
 */
public abstract class HighChartsUtils {

    /**
     * @param statistic 必须确保 Map 中存在 WORD， POWERPOINT， EXCEL，PDF 这四个枚举对应的值
     * @return 对应的柱状图数据，一个二维数组
     */
    public static JsonArray constructPieData(Map<DocumentType, Integer> statistic) {
        int wordNum = statistic.get(DocumentType.WORD);
        int pptNum = statistic.get(DocumentType.POWERPOINT);
        int excelNum = statistic.get(DocumentType.EXCEL);
        int pdfNum = statistic.get(DocumentType.PDF);
        double total = wordNum + pptNum + excelNum + pdfNum;
        BigDecimal wordPercentage = new BigDecimal(wordNum / total).setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal pptPercentage = new BigDecimal(pptNum / total).setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal excelPercentage = new BigDecimal(excelNum / total).setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal pdfPercentage = new BigDecimal(pdfNum / total).setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal totalPercentage = wordPercentage.add(pptPercentage).add(excelPercentage).add(pdfPercentage);
        BigDecimal diff = totalPercentage.subtract(new BigDecimal("1"));
        if (diff.signum() != 0) {
            // diff 是 -1，说明总的少了，需要加上少的这部分；
            // diff 是 +1，说明总的多了，需要减去多的这部分；
            wordPercentage = wordPercentage.subtract(diff);
        }

/*
        List<Object[]> res = new ArrayList<>(DocumentType.values().length);
        res.add(new Object[]{"Word", wordPercentage.doubleValue()});
        res.add(new Object[]{"Presentation", pptPercentage.doubleValue()});
        res.add(new Object[]{"Excel", excelPercentage.doubleValue()});
        res.add(new Object[]{"Pdf", pdfPercentage.doubleValue()});
        return res;
*/

        JsonArray result = new JsonArray();
        JsonArray item = new JsonArray();
        item.add("Word");
        item.add(wordPercentage.doubleValue());
        result.add(item);
        item = new JsonArray();
        item.add("Presentation");
        item.add(pptPercentage.doubleValue());
        result.add(item);
        item = new JsonArray();
        item.add("Excel");
        item.add(excelPercentage.doubleValue());
        result.add(item);
        item = new JsonArray();
        item.add("Pdf");
        item.add(pdfPercentage.doubleValue());
        result.add(item);
        return result;

    }

    /**
     * @param statistic 必须确保 Map 中存在 WORD， POWERPOINT， EXCEL，PDF 这四个枚举对应的值
     * @return 对应的柱状图数据，一个一维数组
     */
    public static JsonArray constructColumnData(Map<DocumentType, Integer> statistic) {
        JsonArray result = new JsonArray();
        result.add(statistic.get(DocumentType.WORD));
        result.add(statistic.get(DocumentType.POWERPOINT));
        result.add(statistic.get(DocumentType.EXCEL));
        result.add(statistic.get(DocumentType.PDF));
        return result;
    }

}