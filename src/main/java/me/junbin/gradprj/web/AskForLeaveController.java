package me.junbin.gradprj.web;

import com.google.gson.JsonObject;
import com.zhuozhengsoft.pageoffice.FileSaver;
import com.zhuozhengsoft.pageoffice.OpenModeType;
import com.zhuozhengsoft.pageoffice.PageOfficeCtrl;
import com.zhuozhengsoft.pageoffice.PageOfficeLink;
import com.zhuozhengsoft.pageoffice.wordwriter.DataRegion;
import com.zhuozhengsoft.pageoffice.wordwriter.DataTag;
import com.zhuozhengsoft.pageoffice.wordwriter.WdParagraphAlignment;
import com.zhuozhengsoft.pageoffice.wordwriter.WordDocument;
import me.junbin.gradprj.domain.Account;
import me.junbin.gradprj.domain.AskForLeave;
import me.junbin.gradprj.domain.LeaveRequest;
import me.junbin.gradprj.service.AskForLeaveService;
import me.junbin.gradprj.util.DocumentUtils;
import me.junbin.gradprj.util.FileUtils;
import me.junbin.gradprj.util.Global;
import me.junbin.gradprj.util.Jsr310UtilExt;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import static me.junbin.commons.ansi.ColorfulPrinter.green;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/3/21 10:49
 * @description :
 */
@Controller
@RequestMapping("/")
public class AskForLeaveController extends BasePoController {

    @Autowired
    private AskForLeaveService askForLeaveService;

    private static final String IMAGE_PREFIX = "[image]";
    private static final String IMAGE_SUFFIX = "[/image]";
    /*
        private static final String COLLEGE_BOOKMARK = "PO_College";
        private static final String PROFESSION_BOOKMARK = "PO_Profession";
        private static final String STUDENT_BOOKMARK = "PO_Student";
        private static final String REASON_BOOKMARK = "PO_Reason";
        private static final String START_TIME_BOOKMARK = "PO_StartTime";
        private static final String END_TIME_BOOKMARK = "PO_EndTime";
        private static final String TOTAL_BOOKMARK = "PO_Total";
        private static final String CURRENT_BOOKMARK = "PO_Current";
        private static final String MATERIAL_BOOKMARK = "PO_Material";
    */
    private static final String COLLEGE_DATA_TAG = "{College}";
    private static final String PROFESSION_DATA_TAG = "{Profession}";
    private static final String STUDENT_DATA_TAG = "{Student}";
    private static final String REASON_DATA_TAG = "{Reason}";
    private static final String START_TIME_DATA_TAG = "{StartTime}";
    private static final String END_TIME_DATA_TAG = "{EndTime}";
    private static final String TOTAL_DATA_TAG = "{Total}";
    private static final String LEAVE_TYPE_DATA_TAG = "{LeaveType}";
    private static final String CURRENT_DATA_TAG = "{Now}";
    private static final String MATERIAL_DATA_TAG = "{Material}";

    private static final String IMAGE_BOOKMARK = "PO_Image";
    private static final String inPattern = "yyyy-MM-dd HH:mm";
    private static final String outPattern = "yyyy年MM月dd日 HH时";
    private static final String sessionLeaveKey = "SessionLeaveKey";


    @RequestMapping(value = "/ask/for/leave", method = RequestMethod.GET)
    @RequiresPermissions("ask:for:leave")
    public String askForLeave() {
        return "doc/askForLeave";
    }

    private String getLeaveDays(LocalDateTime startTime, LocalDateTime endTime) {
        long hours = ChronoUnit.HOURS.between(startTime, endTime);
        int days = (int) (hours / 24);
        boolean half = hours % 24 > 12;
        if (half) {
            return days + ".5";
        } else {
            return Integer.toString(days);
        }
    }

    private Path getFirstFile(MultipartHttpServletRequest multipartRequest) throws IOException {
        Path result = null;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        int size = fileMap.size();
        if (size >= 1) {
            @SuppressWarnings("OptionalGetWithoutIsPresent")
            Map.Entry<String, MultipartFile> entry = fileMap.entrySet().stream().findFirst().get();
            MultipartFile file = entry.getValue();
            String filename = file.getOriginalFilename();
            int idx;
            String suffix = "";
            if ((idx = filename.lastIndexOf(Global.DOT)) != -1) {
                suffix = filename.substring(idx);
            }
            if (StringUtils.isNotEmpty(filename)) {
                result = FileUtils.copyInputStreamToFile(file.getInputStream(), DocumentUtils.getActualImagePath(Global.uuid() + suffix));
            }
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/ask/for/leave", method = RequestMethod.POST)
    @RequiresPermissions("ask:for:leave")
    public Object askForLeave(LeaveRequest leaveRequest,
                              HttpServletRequest request,
                              HttpSession session) throws IOException {
        JsonObject result = new JsonObject();
        if (!leaveRequest.validate()) {
            result.addProperty("error", "请假信息不完整！");
            return result;
        }
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Path imagePath = getFirstFile(multipartRequest);
        leaveRequest.setImagePath(imagePath);
        session.setAttribute(sessionLeaveKey, leaveRequest);
        String url = PageOfficeLink.openWindow(request, "/open/leave", "width:100%;height:100%;toolbar=yes;menubar=yes");
        result.addProperty("url", url);
        return result;
    }

    @RequestMapping(value = "/open/leave", method = RequestMethod.GET)
    @RequiresPermissions("ask:for:leave")
    public String openLeave(HttpSession session,
                            HttpServletRequest request,
                            @ModelAttribute(Global.LOGIN_ACCOUNT_KEY) Account account) throws IOException {
        LeaveRequest leaveRequest = (LeaveRequest) session.getAttribute(sessionLeaveKey);
        session.removeAttribute(sessionLeaveKey);
        if (leaveRequest == null || !leaveRequest.validate()) {
            throw new IllegalStateException();
        }

        // 第一步：将模板文件复制一份
        String id = Global.uuid();
        String docName = DocumentUtils.getLeaveDocName(id);
        Path docPath = Files.copy(DocumentUtils.getLeaveTemplatePath(), DocumentUtils.getActualLeaveDocPath(docName), StandardCopyOption.REPLACE_EXISTING);

        // 第二步：创建 WordDocument 处理渲染事件
        LocalDateTime startTime = Jsr310UtilExt.parseDateTime(leaveRequest.getStart(), inPattern);
        LocalDateTime endTime = Jsr310UtilExt.parseDateTime(leaveRequest.getEnd(), inPattern);
        String start = Jsr310UtilExt.format(startTime, outPattern);
        String end = Jsr310UtilExt.format(endTime, outPattern);
        LocalDateTime current = LocalDateTime.now();
        String now = Jsr310UtilExt.format(current, outPattern);
        String leaveDays = getLeaveDays(startTime, endTime);

        // 入库
        AskForLeave askForLeave = new AskForLeave();
        askForLeave.setId(id);
        askForLeave.setDocName(Jsr310UtilExt.format(current, "yyyy年MM月dd日请假条"));
        askForLeave.setDocUrl(Global.SLASH + docName);
        askForLeave.setCreator(account.getId());
        askForLeave.setCreatedTime(current);
        askForLeave.setDistributed(false);
        askForLeave.setValid(true);
        askForLeaveService.insert(askForLeave);

        WordDocument document = new WordDocument();

        DataTag dataTag = document.openDataTag(COLLEGE_DATA_TAG);
        dataTag.setValue(leaveRequest.getCollege());
        dataTag = document.openDataTag(PROFESSION_DATA_TAG);
        dataTag.setValue(leaveRequest.getProfession());
        dataTag = document.openDataTag(STUDENT_DATA_TAG);
        dataTag.setValue(leaveRequest.getStudent());
        dataTag = document.openDataTag(REASON_DATA_TAG);
        dataTag.setValue(leaveRequest.getReason());
        dataTag = document.openDataTag(START_TIME_DATA_TAG);
        dataTag.setValue(start);
        dataTag = document.openDataTag(END_TIME_DATA_TAG);
        dataTag.setValue(end);
        dataTag = document.openDataTag(TOTAL_DATA_TAG);
        dataTag.setValue(leaveDays);
        dataTag = document.openDataTag(LEAVE_TYPE_DATA_TAG);
        dataTag.setValue(leaveRequest.getLeaveType());
        dataTag = document.openDataTag(CURRENT_DATA_TAG);
        dataTag.setValue(now);
        dataTag = document.openDataTag(MATERIAL_DATA_TAG);
        DataRegion imageRegion = document.openDataRegion(IMAGE_BOOKMARK);
        Path imagePath = leaveRequest.getImagePath();
        if (imagePath != null) {
            dataTag.getFont().setBold(true);
            dataTag.setValue("相关材料");
            // 图片居中
            imageRegion.getParagraphFormat().setAlignment(WdParagraphAlignment.wdAlignParagraphCenter);
            imageRegion.setValue(IMAGE_PREFIX + imagePath.toUri().toString() + IMAGE_SUFFIX);
        } else {
            dataTag.setValue("");
            imageRegion.setValue("");
        }

        // 第三步：创建 PageOfficeCtrl 将调用 setWriter 方法将 WordDocument 内容渲染出来
        // 第四步：跳转到页面
        PageOfficeCtrl ctrl = this.create(request, Global.SLASH + id + "/save/docx");
        ctrl.setWriter(document);

        ctrl.setCaption("请假条"); // 设置控件标题栏
        ctrl.addCustomToolButton("切换全屏", "fullScreenSwitch", 4);
        ctrl.addCustomToolButton("保存", "saveFile", 1);
        ctrl.setJsFunction_AfterDocumentOpened("saveFile"); // 启动之后立即保存该文档
        ctrl.setJsFunction_AfterDocumentClosed("afterClosed"); // 文档关闭之后执行该 JS 方法
        ctrl.setMenubar(false);
        ctrl.webOpen(docPath.toUri().toString(), OpenModeType.docNormalEdit, account.getPrincipal());
        ctrl.setTagId("docCtrl");

        return "doc/leave";
    }

    /**
     * 注意这个方法是在关闭 Office 文档的时候发送的请求，此时该请求发送完毕之后就直接关闭了页面，
     * 所以如果由返回值的话将会将抛出 远程链接已关闭 的异常
     */
    @ResponseBody
    @RequestMapping(value = "/after/closed", method = RequestMethod.GET)
    public void afterClosed() {
        green("关闭了文档");
        green("关闭了文档");
        green("关闭了文档");
        green("关闭了文档");
        green("关闭了文档");
        green("关闭了文档");
    }

    @RequestMapping(value = "{docId:\\w{32}}/save/{docSuffix:\\w+}", method = RequestMethod.POST)
    @RequiresPermissions(value = {"ask:for:leave"})
    public void docSave(@PathVariable("docId") String docId,
                        @PathVariable("docSuffix") String docSuffix,
                        HttpServletRequest request, HttpServletResponse response) {
        FileSaver fileSaver = new FileSaver(request, response);
        String docName = docId + Global.DOT + docSuffix;
        // 保存文档的时候不可以使用 file 协议，只能够使用绝对路径
        fileSaver.saveToFile(DocumentUtils.getActualLeaveDocPath(docName).toString());
        fileSaver.close();
    }

/*
    @ResponseBody
    @RequestMapping(value = "/ask/for/leave", method = RequestMethod.POST)
    @RequiresPermissions("ask:for:leave")
    public Object askForLeave(@RequestParam String college,
                              @RequestParam String profession,
                              @RequestParam String student,
                              @RequestParam String start,
                              @RequestParam String end,
                              @RequestParam String reason,
                              @RequestParam String leaveType,
                              HttpServletRequest request) throws IOException {
        LocalDateTime startTime = Jsr310UtilExt.parseDateTime(start, inPattern);
        LocalDateTime endTime = Jsr310UtilExt.parseDateTime(end, inPattern);
        start = Jsr310UtilExt.format(startTime, outPattern);
        end = Jsr310UtilExt.format(endTime, outPattern);
        String now = Jsr310UtilExt.format(LocalDateTime.now(), outPattern);
        String leaveDays = getLeaveDays(startTime, endTime);
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Path imagePath = getFirstFile(multipartRequest);

        // 模板渲染
        //final String tableTitleBookmark = "PO_TABLE01_TITLE";
        //DataRegion tableTitleRegion = document.createDataRegion(tableTitleBookmark, DataRegionInsertType.After, BLANK_LINE_BOOKMARK);
        //tableTitleRegion.getParagraphFormat().setAlignment(WdParagraphAlignment.wdAlignParagraphLeft);
        //tableTitleRegion.getFont().setName("宋体");
        //tableTitleRegion.getFont().setSize(14);
        //tableTitleRegion.getFont().setBold(true);
        //tableTitleRegion.setValue("　　受大风影响的路段有：");
        WordDocument document = new WordDocument();

        DataTag dataTag = document.openDataTag(COLLEGE_DATA_TAG);
        dataTag.setValue(college);
        dataTag = document.openDataTag(PROFESSION_DATA_TAG);
        dataTag.setValue(profession);
        dataTag = document.openDataTag(STUDENT_DATA_TAG);
        dataTag.setValue(student);
        dataTag = document.openDataTag(REASON_DATA_TAG);
        dataTag.setValue(reason);
        dataTag = document.openDataTag(START_TIME_DATA_TAG);
        dataTag.setValue(start);
        dataTag = document.openDataTag(END_TIME_DATA_TAG);
        dataTag.setValue(end);
        dataTag = document.openDataTag(TOTAL_DATA_TAG);
        dataTag.setValue(leaveDays);
        dataTag = document.openDataTag(LEAVE_TYPE_DATA_TAG);
        dataTag.setValue(leaveType);
        dataTag = document.openDataTag(CURRENT_DATA_TAG);
        dataTag.setValue(now);
        dataTag = document.openDataTag(MATERIAL_DATA_TAG);
        DataRegion imageRegion = document.openDataRegion(IMAGE_BOOKMARK);
        if (imagePath != null) {
            dataTag.getFont().setBold(true);
            dataTag.setValue("相关材料");
            // 图片居中
            imageRegion.getParagraphFormat().setAlignment(WdParagraphAlignment.wdAlignParagraphCenter);
            imageRegion.setValue(IMAGE_PREFIX + imagePath.toUri().toString() + IMAGE_SUFFIX);
        } else {
            dataTag.setValue("");
            imageRegion.setValue("");
        }

        PageOfficeCtrl ctrl = this.create(request);
        ctrl.setWriter(document);

        JsonObject res = new JsonObject();
        res.addProperty("url", "/doc/" + Global.uuid() + "/view");
        return res;
    }
*/

}
