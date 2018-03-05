package com.msy.globalaccess.business.travelAgency.settlement.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.msy.globalaccess.R;
import com.msy.globalaccess.base.App;
import com.msy.globalaccess.base.BaseContract;
import com.msy.globalaccess.base.BaseFragment;
import com.msy.globalaccess.event.RefreshSettlementBadgeEvent;
import com.msy.globalaccess.event.SettlementBadgeInfo;
import com.msy.globalaccess.utils.RxBus;
import com.msy.globalaccess.widget.BadgeView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by hxy on 2017/1/20 0020.
 * <p>
 * description :
 */
public class SettlementFragment extends BaseFragment {
    //    查询类型:0:预支付结算;1:追加支付结算;2：退款结算
    public static final String SETTLEMENT_DATA_TYPE_ALL = "";
    public static final String SETTLEMENT_DATA_TYPE_PRE_PAY = "0";
    public static final String SETTLEMENT_DATA_TYPE_APPEND = "1";
    public static final String SETTLEMENT_DATA_TYPE_REFUND = "2";
    @BindView(R.id.settlement_tabTeam)
    TabLayout tabSettlement;
    @BindView(R.id.settlement_vpTeam)
    ViewPager mViewPager;
    List<SubSettlementFragment> fragments = new ArrayList<>();
    /**
     * 结算查询tab集合
     */
    private List<String> module;
    /**
     * tab上的小圆点
     */
    private List<BadgeView> badgeCache = new ArrayList<>();

    @Inject
    public SettlementFragment() {
        module = Arrays.asList(App.getAppContext().getResources().getStringArray(R.array.settlementModule));
        String dataType = SETTLEMENT_DATA_TYPE_ALL;//默认
        for (int i = 0; i < module.size(); i++) {
            switch (i) {
                case 1:
                    dataType = SETTLEMENT_DATA_TYPE_PRE_PAY;//预支付
                    break;
                case 2:
                    dataType = SETTLEMENT_DATA_TYPE_APPEND;//追加预支付
                    break;
                case 3:
                    dataType = SETTLEMENT_DATA_TYPE_REFUND;//退款
                    break;
            }

            SubSettlementFragment fragment = SubSettlementFragment.newInstance(dataType);
            fragments.add(fragment);
        }
    }

    @Override
    public void initInjector() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        observerBadgeInit();
        observerBadgeUpdate();
    }

    @Override
    public BaseContract.Presenter getBasePresenter() {
        return null;
    }

    @Override
    public void init(View view) {
        mViewPager.setAdapter(new SettlementFragment.MyViewPagerAdapter(getChildFragmentManager()));
        tabSettlement.setupWithViewPager(mViewPager);
        for (int i = 1; i < tabSettlement.getTabCount(); i++) {
            View v = LayoutInflater.from(mFragmentComponent.getActivityContext()).inflate(R.layout.item_tab_badge,
                    tabSettlement, false);
            BadgeView badge = new BadgeView(getActivity());
            badge.setTargetView(v.findViewById(android.R.id.text1));
            tabSettlement.getTabAt(i).setCustomView(v);
            badgeCache.add(badge);
        }
        tabSettlement.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                SubSettlementFragment fragment = fragments.get(tab.getPosition());
                if (fragment != null) {
                    fragment.onRefresh();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                SubSettlementFragment fragment = fragments.get(tab.getPosition());
                if (fragment != null) {
                    fragment.onRefresh();
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_settlement;
    }

    public void update() {
        if (mViewPager == null) {
            return;
        }
        SubSettlementFragment fragment = fragments.get(mViewPager.getCurrentItem());
        if (fragment != null) {
            fragment.onRefresh();
        }
    }

    //---------------private --------------

    private void initBadge(SettlementBadgeInfo badgeInfo) {
        badgeCache.get(0).setBadgeCount(badgeInfo.getPrePayNum());
        badgeCache.get(1).setBadgeCount(badgeInfo.getAddPrePayNum());
        badgeCache.get(2).setBadgeCount(badgeInfo.getBackNum());

    }

    private void observerBadgeInit() {
        Subscription subscription = RxBus.getInstance().toObservable(SettlementBadgeInfo.class)
                .subscribe(new Action1<SettlementBadgeInfo>() {
                    @Override
                    public void call(SettlementBadgeInfo settlementBadgeInfo) {
                        initBadge(settlementBadgeInfo);
                    }
                });
        rxBusCache.add(subscription);
    }

    private void observerBadgeUpdate() {
        //注意这里比对的不是查询类型,而是服务器中记录的数据类型
        Subscription subscription = RxBus.getInstance().toObservable(RefreshSettlementBadgeEvent.class)
                .subscribe(new Action1<RefreshSettlementBadgeEvent>() {
                    @Override
                    public void call(RefreshSettlementBadgeEvent teamBadgeInfo) {
                        switch (teamBadgeInfo.getType()) {
                            case SETTLEMENT_DATA_TYPE_ALL:
                                switch (teamBadgeInfo.getAuditType()) {
                                    //0:预支付;1:追加预支付;2:支付;3:退款;
                                    case "0":
                                        badgeCache.get(0).minus();
                                        break;
                                    case "1":
                                        badgeCache.get(1).minus();
                                        break;
                                    case "3":
                                        badgeCache.get(2).minus();
                                        break;
                                }
                                break;
                            case SETTLEMENT_DATA_TYPE_PRE_PAY:
                                badgeCache.get(0).minus();
                                break;
                            case SETTLEMENT_DATA_TYPE_APPEND:
                                badgeCache.get(1).minus();
                                break;
                            case SETTLEMENT_DATA_TYPE_REFUND:
                                badgeCache.get(2).minus();
                                break;
                        }
                    }
                });
        rxBusCache.add(subscription);
    }


    class MyViewPagerAdapter extends FragmentPagerAdapter {


        MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return module.get(position);//页卡标题
        }
    }
}
