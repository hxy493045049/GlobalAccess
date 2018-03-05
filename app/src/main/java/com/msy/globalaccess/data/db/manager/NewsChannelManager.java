package com.msy.globalaccess.data.db.manager;

import com.msy.globalaccess.R;
import com.msy.globalaccess.base.App;
import com.msy.globalaccess.data.bean.NewsChannel;
import com.msy.globalaccess.data.db.NewsChannelDao;
import com.msy.globalaccess.utils.PreferencesUitls;

import org.greenrobot.greendao.query.Query;

import java.util.Arrays;
import java.util.List;

/**
 * Created by hxy on 2016/12/13 0013.
 * <p>
 * description :  这只是个demo,明白了之后可以删除
 */
@Deprecated
public class NewsChannelManager {
    public static void initNewsChannels() {
        //第一次进入,db的表中没有数据,所以手动添加数据
        if (!PreferencesUitls.hasInitChannels()) {
            NewsChannelDao dao = App.getDaoSession().getNewsChannelDao();
            List<String> channelName = Arrays.asList(App.getAppContext().getResources()
                    .getStringArray(R.array.titleDescription));

            List<String> channelId = Arrays.asList(App.getAppContext().getResources()
                    .getStringArray(R.array.titleDescription));

            for (int i = 0; i < channelName.size(); i++) {
                NewsChannel channel = new NewsChannel(channelName.get(i), channelId.get(i)
                        , "", i <= 5, i, i == 0);
                dao.insert(channel);
            }
            PreferencesUitls.setInitChannelsFlag();
        }
    }

    public static List<NewsChannel> loadNewsChannelsMine() {
        Query<NewsChannel> newsChannelQuery = App.getDaoSession().getNewsChannelDao().queryBuilder().where
                (NewsChannelDao.Properties.NewsChannelSelect.eq(true)).orderAsc(NewsChannelDao.Properties
                .NewsChannelIndex).build();
        return newsChannelQuery.list();
    }
}
