package me.junbin.gradprj.service.impl;

import me.junbin.commons.page.Page;
import me.junbin.commons.page.PageRequest;
import me.junbin.commons.util.Args;
import me.junbin.gradprj.domain.Document;
import me.junbin.gradprj.enumeration.DocumentType;
import me.junbin.gradprj.repository.DocumentRepo;
import me.junbin.gradprj.service.DocumentService;
import me.junbin.gradprj.util.Global;
import me.junbin.gradprj.util.validate.MyValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/28 21:00
 * @description :
 */
@Service("documentService")
@CacheConfig(cacheNames = "docCache")
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentRepo documentRepo;
    private static final Logger log = LoggerFactory.getLogger(DocumentServiceImpl.class);
    private static final List<Document> emptyDocList = Collections.emptyList();

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(key = "'docList'")})
    public int insert(Document document) {
        MyValidator.nullThrowsForProperty(document, "id", "docUrl", "docName", "docType", "creator");
        String creator = document.getCreator();
        log.debug("{}上传了文档{}", creator, document);
        return documentRepo.insert(document);
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(allEntries = true)})
    public int batchInsert(List<Document> documents) {
        MyValidator.emptyThrows(documents);
        for (Document document : documents) {
            MyValidator.nullThrowsForProperty(document, "id", "id", "docUrl", "docName", "docType", "creator");
        }
        log.debug("批量上传文档{}", documents);
        return documentRepo.batchInsert(documents);
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(key = "#document.id"),
            @CacheEvict(key = "'docList'"), @CacheEvict(key = "#document.categoryId")})
    public int delete(Document document) {
        Args.notNull(document);
        String id = document.getId();
        String modifier = document.getModifier();
        LocalDateTime modifiedTime = document.getModifiedTime();
        MyValidator.nullThrows(id, modifier, modifiedTime);
        log.debug("删除文档{}，删除操作发起人{}", id, modifier);
        return documentRepo.deleteDoc(id, modifier, modifiedTime);
    }

    @Override
    @Transactional(readOnly = true)
    @Caching(cacheable = {@Cacheable(key = "#id")})
    public Document selectById(String id) {
        Args.notNull(id);
        log.debug("查询id为{}的文档信息", id);
        return documentRepo.selectById(id);
    }

    @Override
    @Transactional(readOnly = true)
    @Caching(cacheable = {@Cacheable(key = "'docList'")})
    public List<Document> selectAll() {
        log.debug("查询所有的文档信息");
        return documentRepo.selectAll();
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(key = "#document.id"), @CacheEvict(key = "'docList'")})
    public int update(Document document) {
        Args.notNull(document);
        String id = document.getId();
        String modifier = document.getModifier();
        LocalDateTime modifiedTime = document.getModifiedTime();
        MyValidator.nullThrows(id, modifier, modifiedTime);
        log.debug("更新文档{}，更新操作发起人{}", id, modifier);
        return documentRepo.update(document);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Document> findIdIn(String[] idArr) {
        return documentRepo.findIdIn(idArr);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Document> allLatest() {
        log.debug("查询所有最新文档");
        return documentRepo.allLatest();
    }

    @Override
    @Transactional(readOnly = true)
    @Caching(cacheable = {@Cacheable(key = "#categoryId")})
    public List<Document> underCategory(String categoryId) {
        log.debug("查询栏目{}下的所有文档", categoryId);
        return documentRepo.underCategory(categoryId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Document> latest(PageRequest pageRequest) {
        Args.notNull(pageRequest);
        int pageOffset = pageRequest.getPageOffset();
        int pageSize = pageRequest.getPageSize();
        long total = documentRepo.totalLatest();
        if (total == 0) {
            log.debug("没有最新入库记录");
            return new Page.Builder<>(pageRequest, total, emptyDocList).create();
        }
        log.debug("共有{}条最新入库记录", total);
        int skip = pageOffset * pageSize;
        if (skip >= total) {
            log.debug("查询所跳过的记录数{}大于等于实际总记录数{}", skip, total);
            return new Page.Builder<>(pageRequest, total, emptyDocList).create();
        }
        log.debug("查询第{}页（每页{}条记录）的记录", pageOffset + 1, pageSize);
        List<Document> content = documentRepo.latest(skip, pageSize);
        return new Page.Builder<>(pageRequest, total, content).create();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Document> pageCategory(String categoryId, PageRequest pageRequest) {
        MyValidator.nullThrows(categoryId, pageRequest);
        int pageOffset = pageRequest.getPageOffset();
        int pageSize = pageRequest.getPageSize();
        long total = documentRepo.totalWithCategory(categoryId, null);
        if (total == 0) {
            log.debug("栏目{}下共有0条记录", categoryId);
            return new Page.Builder<>(pageRequest, total, emptyDocList).create();
        }
        log.debug("栏目{}下共有{}条记录", categoryId, total);
        int skip = pageOffset * pageSize;
        if (skip >= total) {
            log.debug("查询所跳过的记录数{}大于等于实际总记录数{}", skip, total);
            return new Page.Builder<>(pageRequest, total, emptyDocList).create();
        }
        log.debug("查询栏目{}下第{}页（每页{}条记录）的记录", categoryId, pageOffset + 1, pageSize);
        List<Document> content = documentRepo.pageWithCategory(skip, pageSize, categoryId, null);
        return new Page.Builder<>(pageRequest, total, content).create();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Document> pagePersonal(String accountId, PageRequest pageRequest) {
        return pagePersonal(accountId, pageRequest, null);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Document> pagePersonal(String accountId, PageRequest pageRequest, String docName) {
        MyValidator.nullThrows(accountId, pageRequest);
        int pageOffset = pageRequest.getPageOffset();
        int pageSize = pageRequest.getPageSize();
        String nameLike = Global.toLike(docName);
        long total = documentRepo.totalWithPersonal(accountId, nameLike);
        if (total == 0) {
            log.debug("账户{}拥有0篇文档", accountId);
            return new Page.Builder<>(pageRequest, total, emptyDocList).create();
        }
        log.debug("账户{}拥有{}篇文档", accountId, total);
        int skip = pageOffset * pageSize;
        if (skip >= total) {
            log.debug("查询所跳过的文档数{}大于等于实际总文档数{}", skip, total);
            return new Page.Builder<>(pageRequest, total, emptyDocList).create();
        }
        log.debug("查询账户{}下第{}页（每页{}篇文档）的文档", accountId, pageOffset + 1, pageSize);
        List<Document> content = documentRepo.pageWithPersonal(skip, pageSize, accountId, nameLike);
        return new Page.Builder<>(pageRequest, total, content).create();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Document> pageSearch(String docName, PageRequest pageRequest) {
        MyValidator.nullThrows(docName, pageRequest);
        int pageOffset = pageRequest.getPageOffset();
        int pageSize = pageRequest.getPageSize();
        String docNameLike = toLike(docName);
        long total = documentRepo.total(docNameLike);
        if (total == 0) {
            log.debug("没有任何名称中包含{}记录", docName);
            return new Page.Builder<>(pageRequest, total, emptyDocList).create();
        }
        log.debug("名称中包含{}的文档共有{}条记录", docName, total);
        int skip = pageOffset * pageSize;
        if (skip >= total) {
            log.debug("查询所跳过的记录数{}大于等于实际总记录数{}", skip, total);
            return new Page.Builder<>(pageRequest, total, emptyDocList).create();
        }
        log.debug("查询名称中包含{}，第{}页（每页{}条记录）的记录", docName, pageOffset + 1, pageSize);
        List<Document> content = documentRepo.page(skip, pageSize, docNameLike);
        return new Page.Builder<>(pageRequest, total, content).create();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Document> pageSearchCategory(String categoryId, String docName, PageRequest pageRequest) {
        MyValidator.nullThrows(categoryId, docName, pageRequest);
        int pageOffset = pageRequest.getPageOffset();
        int pageSize = pageRequest.getPageSize();
        String docNameLike = toLike(docName);
        long total = documentRepo.totalWithCategory(categoryId, docNameLike);
        if (total == 0) {
            log.debug("栏目{}下没有任何名称中包含{}记录", categoryId, docName);
            return new Page.Builder<>(pageRequest, total, emptyDocList).create();
        }
        log.debug("栏目{}下名称中包含{}的共有{}条记录", categoryId, docName, total);
        int skip = pageOffset * pageSize;
        if (skip >= total) {
            log.debug("查询所跳过的记录数{}大于等于实际总记录数{}", skip, total);
            return new Page.Builder<>(pageRequest, total, emptyDocList).create();
        }
        log.debug("查询栏目{}下名称中包含{}，第{}页（每页{}条记录）的记录", categoryId, docName, pageOffset + 1, pageSize);
        List<Document> content = documentRepo.pageWithCategory(skip, pageSize, categoryId, docNameLike);
        return new Page.Builder<>(pageRequest, total, content).create();
    }

    private String toLike(String val) {
        return '%' + val + '%';
    }

    @Override
    public Map<DocumentType, Integer> statistics() {
        List<Map<String, Object>> stat = documentRepo.statistics();
        Map<DocumentType, Integer> result = new HashMap<>();
        DocumentType docType;
        Integer num;
        for (Map<String, Object> item : stat) {
            docType = DocumentType.parse(item.get("docType").toString());
            num = Integer.parseInt(item.get("num").toString());
            result.put(docType, num);
        }
        for (DocumentType documentType : DocumentType.values()) {
            if (!result.containsKey(documentType)) {
                result.put(documentType, 0);
            }
        }
        return result;
    }

}
