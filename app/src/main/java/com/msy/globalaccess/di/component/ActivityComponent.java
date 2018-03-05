package com.msy.globalaccess.di.component;

import android.app.Activity;
import android.content.Context;

import com.msy.globalaccess.business.demo.activities.NewsActivity;
import com.msy.globalaccess.business.login.LoginActivity;
import com.msy.globalaccess.business.main.ui.AgentMainActivity;
import com.msy.globalaccess.business.main.ui.TourismMainActivity;
import com.msy.globalaccess.business.navigation.NavigationActivity;
import com.msy.globalaccess.business.navigation.StartupActivity;
import com.msy.globalaccess.business.touristAdmin.datapreview.ui.AuthTimeDistributionActivity;
import com.msy.globalaccess.business.touristAdmin.datapreview.ui.GuestAgeArrangeActivity;
import com.msy.globalaccess.business.touristAdmin.datapreview.ui.TouristSexStatisticsActivity;
import com.msy.globalaccess.business.travelAgency.search.ui.SearchActivity;
import com.msy.globalaccess.business.travelAgency.search.ui.SearchDelegateGuiderActivity;
import com.msy.globalaccess.business.travelAgency.search.ui.SearchResultActivity;
import com.msy.globalaccess.business.travelAgency.settlement.ui.SettlementDetailActivity;
import com.msy.globalaccess.business.travelAgency.team.guider.AddGuiderActivity;
import com.msy.globalaccess.business.travelAgency.team.guider.GuideListActivity;
import com.msy.globalaccess.business.travelAgency.team.modify.CreatScenicSpotActivity;
import com.msy.globalaccess.business.travelAgency.team.modify.TravelAttractionsActivity;
import com.msy.globalaccess.business.travelAgency.team.ui.ApprovalActivity;
import com.msy.globalaccess.business.travelAgency.team.ui.TeamDetailActivity;
import com.msy.globalaccess.business.travelAgency.touristSpots.ui.SearchSpotActivity;
import com.msy.globalaccess.business.travelAgency.touristSpots.ui.TouristSpotsActivity;
import com.msy.globalaccess.di.module.ActivityModule;
import com.msy.globalaccess.di.qualifier.ContextLife;
import com.msy.globalaccess.di.scope.PerActivity;

import dagger.Component;
import retrofit2.Retrofit;

/**
 * Created by hxy on 2016/12/7 0007.
 * <p>
 * description :
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    @ContextLife("Activity")
    Context getActivityContext();

    @ContextLife("Application")
    Context getApplicationContext();

    Activity getActivity();

    Retrofit getRetrofit();

    void inject(NewsActivity activity);

    void inject(AgentMainActivity activity);

    void inject(NavigationActivity activity);

    void inject(SettlementDetailActivity activity);

    //旅行社的搜索
    void inject(SearchActivity activity);

    void inject(LoginActivity activity);

    void inject(TeamDetailActivity activity);

    void inject(SearchResultActivity activity);

    void inject(ApprovalActivity activity);

    void inject(GuideListActivity activity);

    void inject(TravelAttractionsActivity activity);

    void inject(TouristSpotsActivity activity);

    void inject(CreatScenicSpotActivity activity);

    void inject(AddGuiderActivity activity);

    void inject(SearchSpotActivity activity);

    void inject(TourismMainActivity activity);

    void inject(StartupActivity activity);

    //景点认证时间统计
    void inject(AuthTimeDistributionActivity activity);

    //旅游局的搜索
    void inject(SearchDelegateGuiderActivity activity);

    void inject(TouristSexStatisticsActivity activity);

    //游客年龄分布统计
    void inject(GuestAgeArrangeActivity activity);
}
