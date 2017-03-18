package me.junbin.gradprj.web;

import com.zhuozhengsoft.pageoffice.PageOfficeCtrl;
import com.zhuozhengsoft.pageoffice.PageOfficeLink;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/3/12 16:00
 * @description :
 */
public abstract class BasePoController extends BaseController {

    protected PageOfficeCtrl create(HttpServletRequest request, String withoutCtxSavePath) {
        PageOfficeCtrl pageOfficeCtrl = new PageOfficeCtrl(request);
        pageOfficeCtrl.setServerPage(request.getContextPath() + "/poServer");
        pageOfficeCtrl.setSaveFilePage(request.getContextPath() + withoutCtxSavePath);
        return pageOfficeCtrl;
    }

    protected PageOfficeCtrl create(HttpServletRequest request) {
        PageOfficeCtrl pageOfficeCtrl = new PageOfficeCtrl(request);
        pageOfficeCtrl.setServerPage(request.getContextPath() + "/poServer");
        return pageOfficeCtrl;
    }

    protected String createPopupLink(HttpServletRequest request, String path) {
        //String link =
        // PageOfficeLink.openWindow(request, request.getContextPath() + "/pol/open",
        // "width:100%;height:100%;");

        // 第二个参数代表打开 PageOffice 的地址，不可以携带 contextPath，
        // 因为默认就会添加 scheme://serverHost:serverPort/contextPath，如果重复添加将会造成无法打开 PageOffice

        return PageOfficeLink.openWindow(request, path, "width:100%;height:100%;");
    }

    protected String createPopupViewLink(HttpServletRequest request, String docId) {
        return createPopupLink(request, "/doc/" + docId + "/view/popup");
    }

    protected String createPopupPdfViewLink(HttpServletRequest request, String docId) {
        return createPopupLink(request, "/doc/" + docId + "/pdf/view/popup");
    }

    protected String createPopupEditLink(HttpServletRequest request, String docId) {
        return createPopupLink(request, "/doc/" + docId + "/edit/popup");
    }

}
