package me.junbin.gradprj.web;

import com.google.gson.JsonObject;
import com.zhuozhengsoft.pageoffice.FileSaver;
import com.zhuozhengsoft.pageoffice.PDFCtrl;
import com.zhuozhengsoft.pageoffice.PDFOpenModeType;
import com.zhuozhengsoft.pageoffice.PageOfficeCtrl;
import me.junbin.commons.charset.Charsets;
import me.junbin.commons.page.Page;
import me.junbin.commons.page.PageRequest;
import me.junbin.commons.util.CollectionUtils;
import me.junbin.gradprj.domain.Account;
import me.junbin.gradprj.domain.Document;
import me.junbin.gradprj.domain.Perm;
import me.junbin.gradprj.enumeration.DocSuffix;
import me.junbin.gradprj.enumeration.DocumentType;
import me.junbin.gradprj.enumeration.MyGsonor;
import me.junbin.gradprj.exception.DocumentNotExistsException;
import me.junbin.gradprj.exception.PdfUnsupportedEditException;
import me.junbin.gradprj.exception.TypeMismatchException;
import me.junbin.gradprj.service.DocumentService;
import me.junbin.gradprj.service.PermService;
import me.junbin.gradprj.util.DocumentUtils;
import me.junbin.gradprj.util.FileUtils;
import me.junbin.gradprj.util.Global;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static me.junbin.gradprj.util.DocumentUtils.getActualPath;
import static me.junbin.gradprj.util.Global.*;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/27 20:50
 * @description : 使用 FLUSHALL 命令清空整个 redis 数据库中的数据
 */
@Controller
@RequestMapping(value = "/doc")
public class DocumentController extends BasePoController {

    // JS 只有在值为 0 NaN 空字符串 null undefined false 这六种情况下条件判断返回 false

    @Autowired
    private DocumentService documentService;
    @Autowired
    private PermService permService;
    //@Autowired
    //private RequestMappingHandlerMapping handlerMapping;

    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    @RequiresPermissions(value = {"doc:*:upload"})
    public String upload(Model model) {
        model.addAttribute("menuTree", MyGsonor.SN_SIMPLE.toJson(Global.getCategoryTree()));
        return "doc/upload";
    }

    @RequestMapping(value = "/{categoryId:\\w{32}}/upload", method = RequestMethod.GET)
    @RequiresPermissions({"doc:*:upload"})
    public String upload(@PathVariable("categoryId") String categoryId, Model model) {
        model.addAttribute("selectedId", categoryId);
        model.addAttribute("menuTree", MyGsonor.SN_SIMPLE.toJson(Global.getCategoryTree()));
        return "doc/upload";
    }

    @ResponseBody
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @RequiresPermissions(value = {"doc:*:upload"})
    public Object upload(@ModelAttribute(LOGIN_ACCOUNT_KEY) Account account,
                         @RequestParam String categoryId,
                         HttpServletRequest request) throws IOException {
        JsonObject result = new JsonObject();
        if (StringUtils.length(categoryId) != 32) {
            String errorMsg = "未选择要上传的学科栏目！";
            log.error(errorMsg);
            result.addProperty(JSON_STATUS_KEY, STATUS_ERROR);
            result.addProperty(JSON_ERROR_MSG_KEY, errorMsg);
            return result;
        }
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        //List<MultipartFile> batch = multipartRequest.getFiles("batch");
        //int batchSize = batch.size();
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        int batchSize = fileMap.size();

        String originalFilename;
        Path filePath;

        List<String> errorFiles = new ArrayList<>(batchSize);
        List<Document> actualDocs = new ArrayList<>(batchSize);
        String id;
        Document document;
        DocSuffix docSuffix;
        String creator = account.getId();
        LocalDateTime createdTime = LocalDateTime.now();
        String filename;
        MultipartFile file;
        for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
            filename = entry.getKey();
            file = entry.getValue();
            originalFilename = file.getOriginalFilename();
            if (StringUtils.isEmpty(originalFilename)) {
                continue;
            }
            try {
                document = new Document();
                docSuffix = DocSuffix.of(originalFilename);
                id = Global.uuid();
                document.setId(id);
                document.setDocName(originalFilename);
                document.setDocUrl(Global.SLASH + id + docSuffix.getSuffix());
                document.setDocType(docSuffix.getType());
                document.setCreator(creator);
                document.setCategoryId(categoryId);
                document.setCreatedTime(createdTime);
                document.setValid(true);
                // 文件复制
                filePath = FileUtils.copyInputStreamToFile(file.getInputStream(), getActualPath(document.getDocUrl()));
            } catch (IOException e) {
                log.debug(String.format("文件[%s]上传失败！原因：%s", originalFilename, e.getMessage()), e);
                errorFiles.add(originalFilename);
                continue;
            }
            actualDocs.add(document);
        }
        if (actualDocs.isEmpty()) {
            return "error";
        }
        int actualInserted = documentService.batchInsert(actualDocs);

        if (errorFiles.size() > 0) {
            result.addProperty(JSON_STATUS_KEY, STATUS_ERROR);
            result.addProperty(JSON_ERROR_MSG_KEY, "文件上传失败");
        }
        result.addProperty(JSON_STATUS_KEY, STATUS_OK);
        result.addProperty(JSON_SUCCESS_MSG_KEY, "文档上传成功");
        return result;
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    @RequiresPermissions(value = {"doc:*:download"})
    public ResponseEntity<byte[]> download(@RequestParam("docIdArr") String[] docIdArr) {
        if (docIdArr == null || docIdArr.length == 0) {
            return null;
        }
        List<Document> documentList = documentService.findIdIn(docIdArr);
        return null;
    }

    @RequestMapping(value = "/{docId:\\w{32}}/download", method = RequestMethod.GET)
    @RequiresPermissions(value = {"doc:*:download"})
    public ResponseEntity<byte[]> download(@PathVariable("docId") String docId) throws IOException {
        Document document = documentService.selectById(docId);
        if (document == null) {
            throw new DocumentNotExistsException("文档不存在或者已经被删除");
        }
        Path path = DocumentUtils.getActualPath(document.getDocUrl());
        if (!Files.exists(path)) {
            throw new DocumentNotExistsException("文档不存在或者已经被删除");
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        httpHeaders.setContentDispositionFormData("attachment",
                new String(document.getDocName().getBytes(Charsets.UTF8), Charsets.ISO88591));
        return new ResponseEntity<>(Files.readAllBytes(path), httpHeaders, HttpStatus.CREATED);
    }

    private void urlViewRebuild(List<Document> documentList) {
        for (Document doc : documentList) {
            if (doc.getDocType() == DocumentType.PDF) {
                doc.setDocUrl("/doc/" + doc.getId() + "/pdf/view");
            } else {
                doc.setDocUrl("/doc/" + doc.getId() + "/view");
            }
        }
    }

/*
    @RequestMapping(value = "/{categoryId:\\w{32}}/list", method = RequestMethod.GET)
    public String listPage(@PathVariable("categoryId") String categoryId,
                           HttpServletRequest request, Model model) {
        Perm category = permService.selectById(categoryId);
        List<Document> documentList = documentService.underCategory(categoryId);
        urlViewRebuild(documentList);
        model.addAttribute("category", category);
        model.addAttribute("docList", documentList);
        return "doc/list";
    }
*/

    @RequiresAuthentication
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public String redirectPage(@ModelAttribute(Global.LOGIN_ACCOUNT_KEY) Account account) {
        Optional<Perm> perm = account.getPermList().stream().filter(Perm::isAttachable).findFirst();
        assert perm.isPresent();
        return "redirect:/doc/" + perm.get().getId() + "/page/0/10";
    }

    @RequiresPermissions(value = {"channel:*:latest"})
    @RequestMapping(value = "/latest", method = RequestMethod.GET)
    public String latest() {
        return "redirect:/doc/latest/0/10";
    }

    @RequiresPermissions(value = {"channel:*:latest"})
    @RequestMapping(value = "/latest/{pageOffset:\\d+}/{pageSize:\\d+}", method = RequestMethod.GET)
    public String latest(Model model, @PathVariable("pageOffset") int pageOffset,
                         @PathVariable("pageSize") int pageSize) {
        Page<Document> page = documentService.latest(new PageRequest(pageOffset, pageSize));
        List<Document> documentList = page.getContent();
        if (!CollectionUtils.isEmpty(documentList)) {
            urlViewRebuild(documentList);
        }
        model.addAttribute("page", page);
        return "doc/latest";
    }

    @ResponseBody
    @RequiresPermissions(value = {"channel:*:latest"})
    @RequestMapping(value = "/latest/{pageOffset:\\d+}/{pageSize:\\d+}", method = RequestMethod.POST)
    public Object latest(@PathVariable("pageOffset") int pageOffset,
                         @PathVariable("pageSize") int pageSize) {
        Page<Document> page = documentService.latest(new PageRequest(pageOffset, pageSize));
        List<Document> documentList = page.getContent();
        if (!CollectionUtils.isEmpty(documentList)) {
            urlViewRebuild(documentList);
        }
        return page;
    }

    @RequiresAuthentication
    @RequestMapping(value = "/excellence", method = RequestMethod.GET)
    public String excellence(Model model) {
        return "doc/excellence";
    }

    @RequiresAuthentication
    @RequestMapping(value = "/{categoryId:\\w{32}}/page/{pageOffset:\\d+}/{pageSize:\\d+}", method = RequestMethod.GET)
    public String page(@PathVariable("categoryId") String categoryId,
                       @PathVariable("pageOffset") int pageOffset,
                       @PathVariable("pageSize") int pageSize,
                       Model model) {
        Perm category = permService.selectById(categoryId);
        Page<Document> documentPage = documentService.pageCategory(categoryId,
                new PageRequest(pageOffset, pageSize));
        List<Document> documentList = documentPage.getContent();
        if (!CollectionUtils.isEmpty(documentList)) {
            urlViewRebuild(documentList);
        }
        model.addAttribute("menuTree", MyGsonor.SN_SIMPLE.toJson(Global.getCategoryTree()));
        model.addAttribute("category", category);
        model.addAttribute("page", documentPage);
        return "doc/page";
    }

    @ResponseBody
    @RequiresAuthentication
    @RequestMapping(value = "/{categoryId:\\w{32}}/page/{pageOffset:\\d+}/{pageSize:\\d+}", method = {RequestMethod.POST})
    public Object pageAjax(@PathVariable("categoryId") String categoryId,
                           @PathVariable("pageOffset") int pageOffset,
                           @PathVariable("pageSize") int pageSize) {
        Page<Document> documentPage = documentService.pageCategory(categoryId,
                new PageRequest(pageOffset, pageSize));
        List<Document> documentList = documentPage.getContent();
        if (!CollectionUtils.isEmpty(documentList)) {
            urlViewRebuild(documentList);
        }
        return documentPage;
    }


    @RequestMapping(value = {"/{docId:\\w{32}}/view"}, method = RequestMethod.GET)
    @RequiresPermissions(value = {"doc:*:view"})
    public String docView(@ModelAttribute(Global.LOGIN_ACCOUNT_KEY) Account account,
                          @PathVariable("docId") String docId,
                          HttpServletRequest request, Model model) {
        Document document = documentService.selectById(docId);
        if (document == null) {
            throw new DocumentNotExistsException("文档不存在或者已被删除");
        } else if (document.getDocType() == DocumentType.PDF) {
            throw new TypeMismatchException("PDF文档请使用其他方式打开");
        }

        PageOfficeCtrl ctrl = this.create(request);
        // 使用 webapp path 访问文档，不支持中文，可以被直接访问该路径以下载该文档，不建议使用
        // 建议关闭虚拟目录配置，采用 file 协议绝对路径访问
        //String url = DocumentUtils.getDocMappingUrl(document.getDocUrl());
        String url = DocumentUtils.getActualPathUri(document.getDocUrl());
        document.setDocName(DocumentUtils.simpleName(document.getDocName()));
        // 将 docUrl 替换成外链（浏览器无关）的只读地址
        document.setDocUrl(this.createPopupViewLink(request, docId));

        //ctrl.setAllowCopy(false); // 全局性禁止拷贝，导致所有包含 Ctrl C/V 的快捷键都无法使用
        ctrl.setMenubar(false); // 隐藏菜单栏
        ctrl.setOfficeToolbars(false);// 隐藏Office工具条
        ctrl.setCaption(document.getDocName()); // 设置控件标题栏

/*
        Subject subject = SecurityUtils.getSubject();
        // 用户拥有编辑编辑权限
        if (subject.isPermitted("doc:*:edit")) {
            ctrl.addCustomToolButton("编辑", "docEdit", 1);
            model.addAttribute("editUrl", this.createEditLink(request, docId));
        }
*/

        //ctrl.setOfficeVendor(OfficeVendorType.AutoSelect);
        // 文档打开之后执行 afterDocumentOpened 方法
        ctrl.setJsFunction_AfterDocumentOpened("afterDocumentOpened");
        ctrl.addCustomToolButton("切换全屏", "fullScreenSwitch", 4);

        ctrl.webOpen(url, document.getDocType().readOnly(), account.getPrincipal());
        ctrl.setTagId("docCtrl");

        model.addAttribute("document", document);

        return "doc/view";
    }

    @RequestMapping(value = {"/{docId:\\w{32}}/pdf/view"}, method = RequestMethod.GET)
    @RequiresPermissions(value = {"doc:*:view"})
    public String pdfView(@ModelAttribute(Global.LOGIN_ACCOUNT_KEY) Account account,
                          @PathVariable("docId") String docId,
                          HttpServletRequest request, Model model) {
        Document document = documentService.selectById(docId);
        if (document == null) {
            throw new DocumentNotExistsException("文档不存在或者已被删除");
        } else if (document.getDocType() != DocumentType.PDF) {
            throw new TypeMismatchException("Office文档请使用其他方式打开");
        }
        PDFCtrl pdfCtrl = new PDFCtrl(request);
        pdfCtrl.setServerPage(request.getContextPath() + "/poServer");
        pdfCtrl.addCustomToolButton("全屏切换", "fullScreenSwitch", 4);
        pdfCtrl.addCustomToolButton("打印", "print", 6);
        pdfCtrl.addCustomToolButton("隐藏/显示书签", "bookmarkSwitch", 0);
        pdfCtrl.addCustomToolButton("-", "", 0);
        pdfCtrl.addCustomToolButton("实际大小", "viewRealWidth", 16);
        pdfCtrl.addCustomToolButton("适合页面", "viewFitScreenWidth", 17);
        pdfCtrl.addCustomToolButton("适合宽度", "viewFitWidth", 18);
        pdfCtrl.addCustomToolButton("-", "", 0);
        pdfCtrl.addCustomToolButton("首页", "gotoFirstPage", 8);
        pdfCtrl.addCustomToolButton("上一页", "gotoPreviousPage", 9);
        pdfCtrl.addCustomToolButton("下一页", "gotoNextPage", 10);
        pdfCtrl.addCustomToolButton("尾页", "gotoLastPage", 11);
        pdfCtrl.addCustomToolButton("-", "", 0);
        pdfCtrl.addCustomToolButton("放大", "zoomIn", 14);
        pdfCtrl.addCustomToolButton("缩小", "zoomOut", 15);

        String url = DocumentUtils.getActualPathUri(document.getDocUrl());
        document.setDocName(DocumentUtils.simpleName(document.getDocName()));
        document.setDocUrl(this.createPopupPdfViewLink(request, docId));
        pdfCtrl.setCaption(document.getDocName());
        pdfCtrl.setMenubar(false); // 隐藏菜单栏
        pdfCtrl.webOpen(url, PDFOpenModeType.pdfViewOnly);
        pdfCtrl.setTagId("pdfCtrl");

        model.addAttribute("document", document);

        return "doc/viewPdf";
    }

    @RequestMapping(value = {"/{docId:\\w{32}}/view/popup"}, method = RequestMethod.GET)
    @RequiresPermissions(value = {"doc:*:view"})
    public String docViewPopup(@ModelAttribute(Global.LOGIN_ACCOUNT_KEY) Account account,
                               @PathVariable("docId") String docId,
                               HttpServletRequest request, Model model) {
        Document document = documentService.selectById(docId);
        if (document == null) {
            throw new DocumentNotExistsException("文档不存在或者已被删除");
        } else if (document.getDocType() == DocumentType.PDF) {
            throw new TypeMismatchException("PDF文档请使用其他方式打开");
        }
        PageOfficeCtrl ctrl = this.create(request);
        String url = DocumentUtils.getActualPathUri(document.getDocUrl());
        document.setDocName(DocumentUtils.simpleName(document.getDocName()));
        ctrl.setMenubar(false); // 隐藏菜单栏
        ctrl.setOfficeToolbars(false);// 隐藏Office工具条
        ctrl.setCaption(document.getDocName()); // 设置控件标题栏
        ctrl.setJsFunction_AfterDocumentOpened("afterDocumentOpened");
        ctrl.addCustomToolButton("切换全屏", "fullScreenSwitch", 4);

        ctrl.webOpen(url, document.getDocType().readOnly(), account.getPrincipal());
        ctrl.setTagId("docCtrl");

        model.addAttribute("document", document);

        return "doc/popupView";
    }


    @RequestMapping(value = {"/{docId:\\w{32}}/pdf/view/popup"}, method = RequestMethod.GET)
    @RequiresPermissions(value = {"doc:*:view"})
    public String pdfViewPopup(@ModelAttribute(Global.LOGIN_ACCOUNT_KEY) Account account,
                               @PathVariable("docId") String docId,
                               HttpServletRequest request, Model model) {
        Document document = documentService.selectById(docId);
        if (document == null) {
            throw new DocumentNotExistsException("文档不存在或者已被删除");
        } else if (document.getDocType() != DocumentType.PDF) {
            throw new TypeMismatchException("Office文档请使用其他方式打开");
        }

        PDFCtrl pdfCtrl = new PDFCtrl(request);
        pdfCtrl.setServerPage(request.getContextPath() + "/poServer");
        pdfCtrl.addCustomToolButton("全屏切换", "fullScreenSwitch", 4);
        pdfCtrl.addCustomToolButton("打印", "print", 6);
        pdfCtrl.addCustomToolButton("隐藏/显示书签", "bookmarkSwitch", 0);
        pdfCtrl.addCustomToolButton("-", "", 0);
        pdfCtrl.addCustomToolButton("实际大小", "viewRealWidth", 16);
        pdfCtrl.addCustomToolButton("适合页面", "viewFitScreenWidth", 17);
        pdfCtrl.addCustomToolButton("适合宽度", "viewFitWidth", 18);
        pdfCtrl.addCustomToolButton("-", "", 0);
        pdfCtrl.addCustomToolButton("首页", "gotoFirstPage", 8);
        pdfCtrl.addCustomToolButton("上一页", "gotoPreviousPage", 9);
        pdfCtrl.addCustomToolButton("下一页", "gotoNextPage", 10);
        pdfCtrl.addCustomToolButton("尾页", "gotoLastPage", 11);
        pdfCtrl.addCustomToolButton("-", "", 0);
        pdfCtrl.addCustomToolButton("放大", "zoomIn", 14);
        pdfCtrl.addCustomToolButton("缩小", "zoomOut", 15);

        String url = DocumentUtils.getActualPathUri(document.getDocUrl());
        document.setDocName(DocumentUtils.simpleName(document.getDocName()));
        pdfCtrl.setCaption(document.getDocName());
        pdfCtrl.setMenubar(false);

        pdfCtrl.webOpen(url, PDFOpenModeType.pdfViewOnly);
        pdfCtrl.setTagId("pdfCtrl");

        model.addAttribute("document", document);

        return "doc/popupViewPdf";
    }

    @RequestMapping(value = "/{docId:\\w{32}}/edit", method = RequestMethod.GET)
    @RequiresPermissions(value = {"doc:*:edit"})
    public String docEdit(@ModelAttribute(Global.LOGIN_ACCOUNT_KEY) Account account,
                          @PathVariable("docId") String docId,
                          HttpServletRequest request, Model model) {

        Document document = documentService.selectById(docId);
        if (document == null) {
            throw new DocumentNotExistsException("文档不存在或者已被删除");
        }
        if (document.getDocType() == DocumentType.PDF) {
            throw new PdfUnsupportedEditException("不支持编辑 PDF 文件");
        }

        String withoutCtxSavePath = "/doc/" + document.getId() + "/save";
        PageOfficeCtrl ctrl = this.create(request, withoutCtxSavePath);
        // 使用 webapp path 访问文档，不支持中文，可以被直接访问该路径以下载该文档，不建议使用
        // 建议关闭虚拟目录配置，采用 file 协议绝对路径访问
        //String url = DocumentUtils.getDocMappingUrl(document.getDocUrl());
        String url = DocumentUtils.getActualPathUri(document.getDocUrl());

        document.setDocName(DocumentUtils.simpleName(document.getDocName()));
        document.setDocUrl(this.createPopupEditLink(request, docId));
        ctrl.setCaption(document.getDocName()); // 设置控件标题栏
        ctrl.addCustomToolButton("切换全屏", "fullScreenSwitch", 4);
        ctrl.addCustomToolButton("保存", "saveFile", 1);

        ctrl.setMenubar(false);

        ctrl.webOpen(url, document.getDocType().normalEdit(), account.getPrincipal());
        ctrl.setTagId("docCtrl");
        model.addAttribute("document", document);

        return "doc/edit";
    }

    @RequestMapping(value = "/{docId:\\w{32}}/edit/popup", method = RequestMethod.GET)
    @RequiresPermissions(value = {"doc:*:edit"})
    public String docEditPopup(@ModelAttribute(Global.LOGIN_ACCOUNT_KEY) Account account,
                               @PathVariable("docId") String docId,
                               HttpServletRequest request, Model model) {

        Document document = documentService.selectById(docId);
        if (document == null) {
            throw new DocumentNotExistsException("文档不存在或者已被删除");
        }
        if (document.getDocType() == DocumentType.PDF) {
            throw new PdfUnsupportedEditException("不支持编辑 PDF 文件");
        }

        String withoutCtxSavePath = "/doc/" + document.getId() + "/save";
        PageOfficeCtrl ctrl = this.create(request, withoutCtxSavePath);
        // 使用 webapp path 访问文档，不支持中文，可以被直接访问该路径以下载该文档，不建议使用
        // 建议关闭虚拟目录配置，采用 file 协议绝对路径访问
        //String url = DocumentUtils.getDocMappingUrl(document.getDocUrl());
        String url = DocumentUtils.getActualPathUri(document.getDocUrl());

        document.setDocName(DocumentUtils.simpleName(document.getDocName()));
        ctrl.setCaption(document.getDocName()); // 设置控件标题栏
        ctrl.addCustomToolButton("切换全屏", "fullScreenSwitch", 4);
        ctrl.addCustomToolButton("保存", "saveFile", 1);
        ctrl.setMenubar(false);

        ctrl.webOpen(url, document.getDocType().normalEdit(), account.getPrincipal());
        ctrl.setTagId("docCtrl");
        model.addAttribute("document", document);

        return "doc/popupEdit";
    }

    @RequestMapping(value = "{docId:\\w{32}}/save", method = RequestMethod.POST)
    @RequiresPermissions(value = {"doc:*:edit"})
    public void docSave(@PathVariable("docId") String docId,
                        HttpServletRequest request, HttpServletResponse response) {
        Document document = documentService.selectById(docId);
        Path path = DocumentUtils.getActualPath(document.getDocUrl());
        FileSaver fileSaver = new FileSaver(request, response);
        fileSaver.saveToFile(path.toString());
        fileSaver.close();
    }

}
