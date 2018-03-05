/*
 * Copyright (c) 2016 shawn <shawn0729@foxmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.msy.globalaccess.di.component;

import android.app.Activity;
import android.content.Context;

import com.msy.globalaccess.business.touristAdmin.datapreview.ui.GuestSourceStatisticsFragment;
import com.msy.globalaccess.business.touristAdmin.statistics.ui.TourismAreaStatisticsFragment;
import com.msy.globalaccess.business.touristAdmin.statistics.ui.TourismAuthenticationStatisticsFragment;
import com.msy.globalaccess.business.touristAdmin.datapreview.ui.TourismDataPreFragment;
import com.msy.globalaccess.business.touristAdmin.statistics.ui.TourismTeamStatisticsFragment;
import com.msy.globalaccess.business.travelAgency.delegate.ui.SubTouristDelegateFragment;
import com.msy.globalaccess.business.travelAgency.search.SearchExaFragment;
import com.msy.globalaccess.business.travelAgency.settlement.ui.SubSettlementFragment;
import com.msy.globalaccess.business.travelAgency.team.ui.SubTeamFragment;
import com.msy.globalaccess.business.travelAgency.team.ui.TeamFragment;
import com.msy.globalaccess.di.module.FragmentModule;
import com.msy.globalaccess.di.qualifier.ContextLife;
import com.msy.globalaccess.di.scope.PerFragment;

import dagger.Component;

/**
 * @author shawn
 * @version 1.0 2016/6/23
 */
@PerFragment
@Component(dependencies = ApplicationComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {
    @ContextLife("Activity")
    Context getActivityContext();

    @ContextLife("Application")
    Context getApplicationContext();

    Activity getActivity();

    //    void inject(NewsListFragment newsListFragment);
    void inject(TeamFragment teamFragment);

    void inject(SubTeamFragment subTeamFragment);

    void inject(SubSettlementFragment subSettlementFragment);

    void inject(SearchExaFragment searchExaFragment);

    void inject(TourismAuthenticationStatisticsFragment tourismAuthenticationStatisticsFragment);

    void inject(TourismAreaStatisticsFragment teamAreaStatisticsFragment);

    void inject(TourismTeamStatisticsFragment tourismTeamStatisticsFragment);

    void inject(TourismDataPreFragment preFragment);

    void inject(SubTouristDelegateFragment subTouristDelegateFragment);

    void inject(GuestSourceStatisticsFragment fragment);

}
