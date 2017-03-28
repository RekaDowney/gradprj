package me.junbin.gradprj.domain;

import me.junbin.gradprj.enumeration.MyGsonor;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.nio.file.Path;
import java.util.Objects;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/3/22 21:39
 * @description :
 */
public class LeaveRequest implements Serializable {

    private String college;
    private String profession;
    private String student;
    private String start;
    private String end;
    private String reason;
    private String leaveType;
    private Path imagePath;

    public LeaveRequest() {
    }

    @Override
    public String toString() {
        return MyGsonor.SN_SIMPLE.toJson(this);
    }

    public boolean validate() {
        return StringUtils.isNotEmpty(college)
                && StringUtils.isNotEmpty(profession)
                && StringUtils.isNotEmpty(student)
                && StringUtils.isNotEmpty(start)
                && StringUtils.isNotEmpty(end)
                && StringUtils.isNotEmpty(reason)
                && StringUtils.isNotEmpty(leaveType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LeaveRequest that = (LeaveRequest) o;
        return Objects.equals(college, that.college) &&
                Objects.equals(profession, that.profession) &&
                Objects.equals(student, that.student) &&
                Objects.equals(start, that.start) &&
                Objects.equals(end, that.end) &&
                Objects.equals(reason, that.reason) &&
                Objects.equals(leaveType, that.leaveType) &&
                Objects.equals(imagePath, that.imagePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(college, profession, student, start, end, reason, leaveType, imagePath);
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public Path getImagePath() {
        return imagePath;
    }

    public void setImagePath(Path imagePath) {
        this.imagePath = imagePath;
    }

}
