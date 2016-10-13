package com.xiaoli.library.model;

import java.util.List;

/**
 * 升级json返回实体
 * xiaokx
 * hioyes@qq.com
 * 2016-6-17
 */
public class Update {
    // app名称
    private String appname;
    // app下载地址http://update.lubaocar.com/buyer_200.apk
    private String downurl;
    // 当前版本名称v3.9.1
    private String verName;
    // 当前版本code码103
    private int verCode;

    //当前版本大小
    private String size;

    //当前版本更新说明
    private List<Content> updateContent;

    //上一个版本更新说明
    private List<Content> preVersionInfo;
    //更新标题
    private String updateTitle;

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getDownurl() {
        return downurl;
    }

    public void setDownurl(String downurl) {
        this.downurl = downurl;
    }

    public String getVerName() {
        return verName;
    }

    public void setVerName(String verName) {
        this.verName = verName;
    }

    public int getVerCode() {
        return verCode;
    }

    public void setVerCode(int verCode) {
        this.verCode = verCode;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public List<Content> getUpdateContent() {
        return updateContent;
    }

    public void setUpdateContent(List<Content> updateContent) {
        this.updateContent = updateContent;
    }

    public List<Content> getPreVersionInfo() {
        return preVersionInfo;
    }

    public void setPreVersionInfo(List<Content> preVersionInfo) {
        this.preVersionInfo = preVersionInfo;
    }

    public String getUpdateTitle() {
        return updateTitle;
    }

    public void setUpdateTitle(String updateTitle) {
        this.updateTitle = updateTitle;
    }

    /**
     * 返回最新版本文案信息
     * @return
     */
    public String getInfoByUpdateContent(){
        int length = updateContent.size();
        StringBuilder sb = new StringBuilder();
        if (length > 0) {
            for (int i = 0; i < length; i++) {
                if (i != 0) {
                    sb.append("\n");
                }
                sb.append(updateContent.get(i).getContent());
            }
        }
        return sb.toString();
    }

    /**
     * 返回上一版本更新文案信息
     * @return
     */
    public String getInfoByPreUpdateContent(){
        int length = preVersionInfo.size();
        StringBuilder sb = new StringBuilder();
        if (length > 0) {
            for (int i = 0; i < length; i++) {
                if (i != 0) {
                    sb.append("\n");
                }
                sb.append(preVersionInfo.get(i).getContent());
            }
        }
        return sb.toString();
    }
}
