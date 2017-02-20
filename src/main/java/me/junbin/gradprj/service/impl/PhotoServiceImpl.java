package me.junbin.gradprj.service.impl;

import me.junbin.commons.util.Args;
import me.junbin.gradprj.domain.Photo;
import me.junbin.gradprj.repository.PhotoRepo;
import me.junbin.gradprj.service.PhotoService;
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

import java.util.List;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/17 12:47
 * @description :
 */
@Service("photoService")
@CacheConfig(cacheNames = "photoCache")
public class PhotoServiceImpl implements PhotoService {

    @Autowired
    private PhotoRepo photoRepo;
    private static final Logger log = LoggerFactory.getLogger(PhotoServiceImpl.class);

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(key = "'photoList'")})
    public int insert(Photo photo) {
        MyValidator.nullThrowsForProperty(photo, "id", "path", "createdTime");
        log.debug("添加新图片{}", photo.toString());
        return photoRepo.insert(photo);
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(key = "'photoList'")})
    public int batchInsert(List<Photo> photos) {
        MyValidator.emptyThrows(photos);
        for (Photo photo : photos) {
            MyValidator.nullThrowsForProperty(photo, "id", "path", "createdTime");
        }
        log.debug("批量添加图片{}", photos.toString());
        return photoRepo.batchInsert(photos);
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(key = "#id"), @CacheEvict(key = "'photoList'")})
    public int delete(String id) {
        Args.notNull(id);
        log.debug("删除id为{}的图片", id);
        return photoRepo.delete(id);
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(key = "#photo.id"), @CacheEvict(key = "'photoList'")})
    public int delete(Photo photo) {
        Args.notNull(photo);
        String id = photo.getId();
        Args.notNull(id);
        log.debug("删除id为{}的图片", id);
        return photoRepo.delete(id);
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(key = "#photo.id"), @CacheEvict(key = "'photoList'")})
    public int update(Photo photo) {
        MyValidator.nullThrowsForProperty(photo, "id", "path", "createdTime");
        log.debug("更新id为{}的图片信息{}", photo.getId(), photo);
        return photoRepo.update(photo);
    }

    @Override
    @Transactional(readOnly = true)
    @Caching(cacheable = {@Cacheable(key = "#id")})
    public Photo selectById(String id) {
        Args.notNull(id);
        log.debug("查询id为{}的图片信息", id);
        return photoRepo.selectById(id);
    }

    @Override
    @Transactional(readOnly = true)
    @Caching(cacheable = {@Cacheable(key = "'photoList'")})
    public List<Photo> selectAll() {
        log.debug("查询所有图片信息");
        return photoRepo.selectAll();
    }

}
