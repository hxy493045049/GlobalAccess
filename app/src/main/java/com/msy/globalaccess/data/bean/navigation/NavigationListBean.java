package com.msy.globalaccess.data.bean.navigation;

import java.util.List;

/**
 * Created by WuDebin on 2017/2/6.
 */

public class NavigationListBean {

    private String title;

    private List<GuideSubListBean> subListBean;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<GuideSubListBean> getSubListBean() {
        return subListBean;
    }

    public void setSubListBean(List<GuideSubListBean> subListBean) {
        this.subListBean = subListBean;
    }

    public class GuideSubListBean{
        private String description;
        private String content;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
