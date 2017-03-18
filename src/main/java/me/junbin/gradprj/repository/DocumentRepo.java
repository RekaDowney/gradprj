package me.junbin.gradprj.repository;

import me.junbin.gradprj.annotation.MyBatisMapper;
import me.junbin.gradprj.domain.Document;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/28 20:18
 * @description :
 */
@MyBatisMapper
public interface DocumentRepo extends BaseRepo<Document, String> {

    @Override
    @Deprecated
    int delete(String id);

    int deleteDoc(@Param("id") String id,
                  @Param("modifier") String modifier,
                  @Param("modifiedTime") LocalDateTime modifiedTime);

    List<Document> findIdIn(@Param("idArr") String[] idArr);

    List<Document> allLatest();

    long totalLatest();

    List<Document> latest(@Param("skip") int skip, @Param("size") int size);

    List<Document> underCategory(String categoryId);

    long total(@Param("docName") String docName);

    List<Document> page(@Param("skip") int skip,
                        @Param("pageSize") int pageSize,
                        @Param("docName") String docName);

    long totalWithCategory(@Param("categoryId") String categoryId,
                           @Param("docName") String docName);

    List<Document> pageWithCategory(@Param("skip") int skip,
                                    @Param("pageSize") int pageSize,
                                    @Param("categoryId") String categoryId,
                                    @Param("docName") String docName);

}
