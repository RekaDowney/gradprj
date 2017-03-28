package me.junbin.gradprj.service.impl;

import me.junbin.commons.page.Page;
import me.junbin.commons.page.PageRequest;
import me.junbin.commons.util.Args;
import me.junbin.gradprj.domain.AskForLeave;
import me.junbin.gradprj.repository.AskForLeaveRepo;
import me.junbin.gradprj.service.AskForLeaveService;
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
import java.util.List;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/3/23 14:11
 * @description :
 */
@Service("askForLeaveService")
@CacheConfig(cacheNames = "aflCache")
public class AskForLeaveServiceImpl implements AskForLeaveService {

    @Autowired
    private AskForLeaveRepo askForLeaveRepo;
    private static final Logger log = LoggerFactory.getLogger(AskForLeaveServiceImpl.class);
    private static final List<AskForLeave> emptyAflList = Collections.emptyList();

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(key = "'aflList'")})
    public int insert(AskForLeave askForLeave) {
        MyValidator.nullThrowsForProperty(askForLeave, "id", "docUrl", "docName", "docType", "creator");
        String creator = askForLeave.getCreator();
        log.debug("{}申请了假期{}", creator, askForLeave);
        return askForLeaveRepo.insert(askForLeave);
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(key = "'aflList'")})
    public int batchInsert(List<AskForLeave> askForLeaves) {
        MyValidator.emptyThrows(askForLeaves);
        for (AskForLeave askForLeave : askForLeaves) {
            MyValidator.nullThrowsForProperty(askForLeave, "id", "id", "docUrl", "docName", "docType", "creator");
        }
        log.debug("批量申请假期{}", askForLeaves);
        return askForLeaveRepo.batchInsert(askForLeaves);
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(key = "#askForLeave.id"), @CacheEvict(key = "'aflList'")})
    public int delete(AskForLeave askForLeave) {
        Args.notNull(askForLeave);
        String id = askForLeave.getId();
        String modifier = askForLeave.getModifier();
        LocalDateTime modifiedTime = askForLeave.getModifiedTime();
        MyValidator.nullThrows(id, modifier, modifiedTime);
        log.debug("删除假条{}，删除操作发起人{}", id, modifier);
        return askForLeaveRepo.deleteAfl(id, modifier, modifiedTime);
    }

    @Override
    @Transactional(readOnly = true)
    @Caching(cacheable = {@Cacheable(key = "#id")})
    public AskForLeave selectById(String id) {
        Args.notNull(id);
        log.debug("查询id为{}的假条信息", id);
        return askForLeaveRepo.selectById(id);
    }

    @Override
    @Transactional(readOnly = true)
    @Caching(cacheable = {@Cacheable(key = "'aflList'")})
    public List<AskForLeave> selectAll() {
        log.debug("查询所有的假条信息");
        return askForLeaveRepo.selectAll();
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(key = "#askForLeave.id"), @CacheEvict(key = "'aflList'")})
    public int update(AskForLeave askForLeave) {
        Args.notNull(askForLeave);
        String id = askForLeave.getId();
        String modifier = askForLeave.getModifier();
        LocalDateTime modifiedTime = askForLeave.getModifiedTime();
        MyValidator.nullThrows(id, modifier, modifiedTime);
        log.debug("更新假条{}，更新操作发起人{}", id, modifier);
        return askForLeaveRepo.update(askForLeave);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AskForLeave> page(String creator, PageRequest pageRequest) {
        MyValidator.nullThrows(pageRequest);
        int pageOffset = pageRequest.getPageOffset();
        int pageSize = pageRequest.getPageSize();
        long total = askForLeaveRepo.total(creator);
        if (total == 0) {
            log.debug("没有任何名称中包含{}记录的假条", creator);
            return new Page.Builder<>(pageRequest, total, emptyAflList).create();
        }
        log.debug("名称中包含{}的假条共有{}条记录", creator, total);
        int skip = pageOffset * pageSize;
        if (skip >= total) {
            log.debug("查询所跳过的记录数{}大于等于实际总记录数{}", skip, total);
            return new Page.Builder<>(pageRequest, total, emptyAflList).create();
        }
        log.debug("查询名称中包含{}，第{}页（每页{}条记录）的记录", creator, pageOffset + 1, pageSize);
        List<AskForLeave> content = askForLeaveRepo.page(skip, pageSize, creator);
        return new Page.Builder<>(pageRequest, total, content).create();
    }

}
