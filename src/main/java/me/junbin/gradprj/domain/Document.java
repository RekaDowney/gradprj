package me.junbin.gradprj.domain;

import me.junbin.gradprj.enumeration.DocumentType;
import me.junbin.gradprj.enumeration.MyGsonor;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/28 20:13
 * @description :
 */
public class Document extends DetailDomain implements Serializable {

    private String docUrl;
    private String docName;
    private DocumentType docType;
    private String categoryId;

    public Document() {
    }

    public Document(String docUrl, String docName, DocumentType docType) {
        this.docUrl = docUrl;
        this.docName = docName;
        this.docType = docType;
    }

    @Override
    public String toString() {
        return MyGsonor.SIMPLE.toJson(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Document document = (Document) o;
        return Objects.equals(id, document.id) &&
                Objects.equals(docUrl, document.docUrl) &&
                Objects.equals(docName, document.docName) &&
                Objects.equals(docType, document.docType) &&
                Objects.equals(categoryId, document.categoryId) &&
                Objects.equals(creator, document.creator) &&
                Objects.equals(createdTime, document.createdTime) &&
                Objects.equals(modifier, document.modifier) &&
                Objects.equals(modifiedTime, document.modifiedTime) &&
                valid == document.valid;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, docUrl, docName, docType, categoryId, creator, createdTime, modifier, modifiedTime, valid);
    }

    public String getDocUrl() {
        return docUrl;
    }

    public void setDocUrl(String docUrl) {
        this.docUrl = docUrl;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public DocumentType getDocType() {
        return docType;
    }

    public void setDocType(DocumentType docType) {
        this.docType = docType;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

}
