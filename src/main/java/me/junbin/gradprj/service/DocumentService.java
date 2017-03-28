package me.junbin.gradprj.service;

import me.junbin.commons.page.Page;
import me.junbin.commons.page.PageRequest;
import me.junbin.gradprj.domain.Document;
import me.junbin.gradprj.enumeration.DocumentType;

import java.util.List;
import java.util.Map;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/28 21:00
 * @description :
 */
public interface DocumentService extends BaseService<Document, String> {

    @Override
    @Deprecated
    int update(Document domain);

    List<Document> findIdIn(String[] idArr);

    List<Document> allLatest();

    List<Document> underCategory(String categoryId);

    Page<Document> latest(PageRequest pageRequest);

    Page<Document> pageCategory(String categoryId, PageRequest pageRequest);

    Page<Document> pagePersonal(String accountId, PageRequest pageRequest);

    Page<Document> pagePersonal(String accountId, PageRequest pageRequest, String docName);

    Page<Document> pageSearch(String docName, PageRequest pageRequest);

    Page<Document> pageSearchCategory(String categoryId, String docName, PageRequest pageRequest);

    Map<DocumentType, Integer> statistics();

}
