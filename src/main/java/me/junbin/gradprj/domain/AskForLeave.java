package me.junbin.gradprj.domain;

import me.junbin.gradprj.enumeration.DocumentType;
import me.junbin.gradprj.enumeration.MyGsonor;

import java.util.Objects;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/3/23 13:48
 * @description :
 */
public class AskForLeave extends DetailDomain {

    private String docUrl;
    private String docName;
    private DocumentType docType = DocumentType.WORD;
    private boolean distributed = false;

    public AskForLeave() {
    }

    @Override
    public String toString() {
        return MyGsonor.SN_SIMPLE.toJson(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AskForLeave that = (AskForLeave) o;
        return distributed == that.distributed &&
                Objects.equals(docUrl, that.docUrl) &&
                Objects.equals(docName, that.docName) &&
                docType == that.docType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(docUrl, docName, docType, distributed);
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

    public boolean isDistributed() {
        return distributed;
    }

    public void setDistributed(boolean distributed) {
        this.distributed = distributed;
    }

}
