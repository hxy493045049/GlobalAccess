package com.msy.globalaccess.data.interactor.impl;

import com.msy.globalaccess.R;
import com.msy.globalaccess.base.App;
import com.msy.globalaccess.business.touristAdmin.datapreview.contract.impl.DataPreviewPresenterImpl;
import com.msy.globalaccess.data.RequestCallBack;
import com.msy.globalaccess.data.api.StatisticsApi;
import com.msy.globalaccess.data.bean.base.BaseBean;
import com.msy.globalaccess.data.bean.statistics.GuestSourceBeanWrapper;
import com.msy.globalaccess.data.bean.statistics.ScenicCheckTeamCountListBean;
import com.msy.globalaccess.data.bean.statistics.ScenicOrderCustomCountListBean;
import com.msy.globalaccess.data.bean.statistics.TendencyBean;
import com.msy.globalaccess.data.bean.statistics.TourismSummaryBean;
import com.msy.globalaccess.data.bean.statistics.TouristAbroadStatisticsBean;
import com.msy.globalaccess.data.bean.statistics.TouristSexStatisticsBean;
import com.msy.globalaccess.data.interactor.IStatisticsInteractor;
import com.msy.globalaccess.utils.rxhelper.DefaultSubscriber;
import com.msy.globalaccess.utils.rxhelper.RxJavaUtils;

import org.greenrobot.greendao.annotation.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import cn.msy.zc.commonutils.TimeFormat;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.util.ChartUtils;
import retrofit2.Retrofit;
import rx.Subscription;
import rx.functions.Func1;

/**
 * Created by shawn on 2017/5/23 0023.
 * <p>
 * description : 旅游数据预览
 */

public class TourismDataInteractorImpl implements IStatisticsInteractor.ITourismDataSummaryInteractor {
    private StatisticsApi.TourismDataSummaryApi dataSummaryApi;
    /**
     * 饼图的六种颜色
     */
    private int[] pieColors = new int[]{
            R.color.guide_pie_blue_theme,
            R.color.guide_pie_green,
            R.color.guide_pie_orange,
            R.color.guide_pie_yellow,
            R.color.guide_pie_sky_blue,
            R.color.guide_pie_deep_blue
    };

    /**
     * 性别饼图的二种颜色
     */
    private int[] sexPieColors = new int[]{
            R.color.guide_pie_blue_theme,
            R.color.guide_pie_orange_light,
    };
    private int[] linearColor = new int[]{R.color.blue, R.color.orange_red, R.color.purple, R.color.yellow};

    @Inject
    public TourismDataInteractorImpl(Retrofit mRetrofit) {
        dataSummaryApi = mRetrofit.create(StatisticsApi.TourismDataSummaryApi.class);
    }

    @Override
    public Subscription getSummary(@NotNull RequestCallBack<DataPreviewPresenterImpl.ResultBean> callBack, @NotNull
            HashMap<String, String> param) {
        callBack.beforeRequest();
        return dataSummaryApi.getSummaryData(param)
                .compose(RxJavaUtils.<BaseBean<TourismSummaryBean>>defaultSchedulers())
                .flatMap(RxJavaUtils.<TourismSummaryBean>defaultBaseFlatMap())
                .map(new Func1<BaseBean<TourismSummaryBean>, DataPreviewPresenterImpl.ResultBean>() {
                    @Override
                    public DataPreviewPresenterImpl.ResultBean call(BaseBean<TourismSummaryBean> base) {
                        DataPreviewPresenterImpl.ResultBean resultBean = new DataPreviewPresenterImpl.ResultBean();
                        TourismSummaryBean origin = base.getData();
                        resultBean.setOrigenBean(origin);
                        resultBean.setCreateTeamCount(origin.getCreateTeamCount());
                        resultBean.setDomesticCustomCount(origin.getDomesticCustomCount());
                        resultBean.setDomesticTeamCount(origin.getDomesticTeamCount());
                        resultBean.setLineChartData(parseLinearChartData(origin.getScenicOrderCustomCountList()));
                        resultBean.setGuestsPieData(parseGuestsOrigin(origin.getCustomSourceCountList()));
                        resultBean.setSceneryPieData(parseSceneryData(origin));
                        resultBean.setIntentionData(parseIntentionData(origin.getTeamCountList()));
                        resultBean.setSexData(parseSexData(origin.getCustomSexCountList()));
                        resultBean.setAbroadLineChartData(parseAbroadLinearChartData(origin.getCustomFromCountList()));
                        resultBean.setStayDayLineChartData(parseTeamStayDayLinearChartData(origin
                                .getTeamStayDaysCountList()));
                        return resultBean;
                    }
                })
                .subscribe(new DefaultSubscriber<>(callBack));
    }

    //解析景区客流趋势折线图
    private LineChartData parseLinearChartData(List<ScenicOrderCustomCountListBean> list) {
        if (list == null || list.size() == 0) {
            return null;
        }
        List<Line> lines = new ArrayList<>();
        List<AxisValue> X = new ArrayList<>();
        int pos = 0;
        int max = 0;
        for (int i = 0; i < list.size(); ++i) {
            List<PointValue> values = new ArrayList<>();
            ScenicOrderCustomCountListBean bean = list.get(i);
            if (bean.getScenicOrderCustomCountBillList() == null || bean.getScenicOrderCustomCountBillList().size()
                    == 0) {
                continue;
            }
            if (bean.getScenicOrderCustomCountBillList().size() > max) {
                max = bean.getScenicOrderCustomCountBillList().size();
                pos = i;
            }

            for (int j = 0; j < bean.getScenicOrderCustomCountBillList().size(); ++j) {
                values.add(new PointValue(j, Float.parseFloat(bean.getScenicOrderCustomCountBillList().get(j)
                        .getCountNum())));

            }
            Line line = new Line(values);
            line.setColor(App.getResource().getColor(linearColor[i]));
            line.setStrokeWidth(1);
            line.setPointRadius(3);
            line.setShape(ValueShape.CIRCLE);
            line.setCubic(false);
            line.setFilled(false);
            line.setHasLabels(false);
            line.setHasLabelsOnlyForSelected(true);
            line.setHasLines(true);
            line.setHasPoints(true);
            lines.add(line);
        }
        ScenicOrderCustomCountListBean bean = list.get(pos);
        for (int k = 0; k < bean.getScenicOrderCustomCountBillList().size(); ++k) {
            String time = TimeFormat.formatData(TimeFormat.regular11, TimeFormat.regular7, bean
                    .getScenicOrderCustomCountBillList().get(k).getDate());
            X.add(new AxisValue(k).setLabel(time));
        }

        LineChartData data = new LineChartData(lines);

        Axis axisX = new Axis(X);
        axisX.setTextSize(10);
        axisX.setTextColor(App.getResource().getColor(R.color.text_primary_light));

        Axis axisY = new Axis().setHasLines(true);
        axisY.setTextSize(10);//设置字体大小
        axisY.setTextColor(App.getResource().getColor(R.color.text_primary_light));
        axisY.setAutoGenerated(true);

        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);
        data.setBaseValue(Float.NEGATIVE_INFINITY);

        return data;
    }

    //解析客源地总览饼形图数据
    private PieChartData parseGuestsOrigin(List<GuestSourceBeanWrapper.GuestSourceBean> data) {
        if (data == null || data.size() <= 0) {
            return null;
        }
        PieChartData pieChartData = new PieChartData();
        pieChartData.setHasLabels(false);
        pieChartData.setHasCenterCircle(true);
        pieChartData.setSlicesSpacing(0);
        List<SliceValue> sliceValues = new ArrayList<>();
        int totalNum = 0;
        for (int i = 0; i < data.size(); i++) {
            totalNum = totalNum + Integer.parseInt(data.get(i).getCountNum());
            SliceValue sliceValue = new SliceValue();
            float value = Float.parseFloat(data.get(i).getCountNum());
            if (value == 0) {
                value = 0.000001f;
            }
            sliceValue.setValue(value);
            sliceValue.setColor(App.getResource().getColor(pieColors[i]));
            sliceValues.add(sliceValue);
        }
        pieChartData.setCenterText1(totalNum + "人次");
        pieChartData.setCenterText1FontSize(12);
        pieChartData.setCenterText1Color(App.getResource().getColor(R.color.colorPrimary));
        return pieChartData.setValues(sliceValues);
    }

    //解析境内外游客折线图
    private LineChartData parseAbroadLinearChartData(List<TouristAbroadStatisticsBean> list) {
        if (list == null || list.size() == 0) {
            return null;
        }
        List<Line> lines = new ArrayList<>();
        List<AxisValue> X = new ArrayList<>();
        int maxLabelChars = 0;
        int pos = 0;
        int max = 0;
        for (int i = 0; i < list.size(); ++i) {
            List<PointValue> values = new ArrayList<>();
            TouristAbroadStatisticsBean bean = list.get(i);
            if (bean.getCustomFromCountBillList() == null || bean.getCustomFromCountBillList().size() == 0) {
                continue;
            }
            if (bean.getCustomFromCountBillList().size() > max) {
                max = bean.getCustomFromCountBillList().size();
                pos = i;
            }

            for (int j = 0; j < bean.getCustomFromCountBillList().size(); ++j) {
                float count = Float.parseFloat(bean.getCustomFromCountBillList().get(j).getCountNum());
                values.add(new PointValue(j, count));
                if (count > maxLabelChars) {
                    maxLabelChars = (int) count;
                }

            }
            Line line = new Line(values);
            line.setColor(App.getResource().getColor(linearColor[i]));
            line.setStrokeWidth(1);
            line.setPointRadius(3);
            line.setShape(ValueShape.CIRCLE);
            line.setCubic(false);
            line.setFilled(false);
            line.setHasLabels(false);
            line.setHasLabelsOnlyForSelected(true);
            line.setHasLines(true);
            line.setHasPoints(true);
            lines.add(line);
        }
        TouristAbroadStatisticsBean bean = list.get(pos);
        for (int k = 0; k < bean.getCustomFromCountBillList().size(); ++k) {
            X.add(new AxisValue(k).setLabel(bean.getCustomFromCountBillList().get(k).getDate()));
        }

        LineChartData data = new LineChartData(lines);

        Axis axisX = new Axis(X);
        axisX.setTextSize(10);
        axisX.setTextColor(App.getResource().getColor(R.color.text_primary_light));

        Axis axisY = new Axis().setHasLines(true);
        axisY.setTextSize(10);//设置字体大小
        axisY.setTextColor(App.getResource().getColor(R.color.text_primary_light));
        axisY.setAutoGenerated(true);
        axisY.setMaxLabelChars((maxLabelChars + "").length());

        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);
        data.setBaseValue(Float.NEGATIVE_INFINITY);

        return data;
    }

    //解析团队停留天数折线图
    private LineChartData parseTeamStayDayLinearChartData(List<GuestSourceBeanWrapper.GuestSourceBean> list) {
        if (list == null || list.size() == 0) {
            return null;
        }
        List<Line> lines = new ArrayList<>();
        List<AxisValue> X = new ArrayList<>();
        List<PointValue> values = new ArrayList<>();

        for (int i = 0; i < list.size(); ++i) {
            values.add(new PointValue(i, Float.parseFloat(list.get(i).getCountNum())));
            X.add(new AxisValue(i).setLabel(list.get(i).getName()));
        }
        Line line = new Line(values);
        line.setColor(App.getResource().getColor(linearColor[0]));
        line.setStrokeWidth(1);
        line.setPointRadius(3);
        line.setShape(ValueShape.CIRCLE);
        line.setCubic(false);
        line.setFilled(false);
        line.setHasLabels(false);
        line.setHasLabelsOnlyForSelected(true);
        line.setHasLines(true);
        line.setHasPoints(true);
        lines.add(line);

        LineChartData data = new LineChartData(lines);

        Axis axisX = new Axis(X);
        axisX.setTextSize(10);
        axisX.setTextColor(App.getResource().getColor(R.color.text_primary_light));

        Axis axisY = new Axis().setHasLines(true);
        axisY.setTextSize(10);//设置字体大小
        axisY.setTextColor(App.getResource().getColor(R.color.text_primary_light));
        axisY.setAutoGenerated(true);

        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);
        data.setBaseValue(Float.NEGATIVE_INFINITY);

        return data;
    }


    //解析今日景区认证的饼形图数据
    private PieChartData parseSceneryData(TourismSummaryBean bean) {
        List<ScenicCheckTeamCountListBean> data = bean.getScenicCheckTeamCountList();
        if (data == null || data.size() <= 0) {
            return null;
        }
        PieChartData pieChartData = new PieChartData();
        pieChartData.setHasLabels(false);
        pieChartData.setHasCenterCircle(true);
        pieChartData.setSlicesSpacing(0);
        List<SliceValue> sliceValues = new ArrayList<>();
        int totalNum = 0;
        int size;
        if (data.size() >= 6) {
            size = 6;
        } else {
            size = data.size();
        }
        for (int i = 0; i < size; i++) {
            totalNum = totalNum + Integer.parseInt(data.get(i).getCountNum());
            SliceValue sliceValue = new SliceValue();
            float value = Float.parseFloat(data.get(i).getCountNum());
            if (value == 0) {
                value = 0.000001f;
            }
            sliceValue.setValue(value);
            sliceValue.setColor(App.getResource().getColor(pieColors[i]));
            sliceValues.add(sliceValue);
        }
        pieChartData.setValues(sliceValues);
        pieChartData.setCenterText1(bean.getScenicCheckCustomCount() + "人次");
        pieChartData.setCenterText2(bean.getScenicCheckTeamCount() + "团队");
        return pieChartData;
    }

    //解析趋势分析柱形图数据
    private ColumnChartData parseIntentionData(List<TendencyBean> data) {
        if (data == null || data.size() <= 0) {
            return null;
        }
        ColumnChartData columnChartData = new ColumnChartData();
        List<Column> columns = new ArrayList<>();
        List<AxisValue> axisValues = new ArrayList<>();
        int size = data.size();
        int maxCount = 0;
        for (int i = 0; i < size; i++) {
            Column column = new Column();
            List<SubcolumnValue> subcolumnValues = new ArrayList<>();
            SubcolumnValue subcolumnValue = new SubcolumnValue();
            float count = Float.parseFloat(data.get(i).getCountNum());
            subcolumnValue.setValue(count);
            subcolumnValue.setColor(App.getResource().getColor(R.color.guide_pie_blue_theme));
            subcolumnValues.add(subcolumnValue);
            column.setValues(subcolumnValues);
            column.setHasLabels(true);
            columns.add(column);
            String label = data.get(i).getDate();
            axisValues.add(new AxisValue(i).setLabel(label));
            if (count > maxCount) {
                maxCount = (int) count;
            }
        }
        columnChartData.setFillRatio(0.6f);
        columnChartData.setColumns(columns);
        columnChartData.setValueLabelBackgroundEnabled(false);
        columnChartData.setValueLabelsTextColor(App.getResource().getColor(R.color.colorPrimary));
        columnChartData.setValueLabelTextSize(ChartUtils.px2sp(App.getResource().getDisplayMetrics().scaledDensity,
                (int) App.getResource().getDimension(R.dimen.chart_text_size)));
        columnChartData.setAxisYLeft(new Axis().setHasLines(true).setHasSeparationLine(true).setMaxLabelChars(
                (maxCount + "").length()).setTextColor(App.getResource().getColor(R.color.text_primary_light)));
        columnChartData.setAxisXBottom(new Axis(axisValues).setHasLines(false).setHasSeparationLine
                (true).setTextColor(App.getResource().getColor(R.color.text_primary_light)));
        return columnChartData;
    }

    //解析游客性别
    private PieChartData parseSexData(List<TouristSexStatisticsBean> data) {
        if (data == null || data.size() <= 0) {
            return null;
        }
        PieChartData pieChartData = new PieChartData();
        pieChartData.setHasLabels(false);
        pieChartData.setHasCenterCircle(true);
        pieChartData.setSlicesSpacing(0);
        List<SliceValue> sliceValues = new ArrayList<>();
        int totalNum = 0;
        for (int i = 0; i < data.size(); i++) {
            totalNum = totalNum + Integer.parseInt(data.get(i).getCountNum());
            SliceValue sliceValue = new SliceValue();
            float value = Float.parseFloat(data.get(i).getCountNum());
            if (value == 0) {
                value = 0.000001f;
            }
            sliceValue.setValue(value);
            sliceValue.setColor(App.getResource().getColor(sexPieColors[i]));
            sliceValues.add(sliceValue);
        }
        pieChartData.setCenterText1(totalNum + "人次");
        pieChartData.setCenterText1FontSize(12);
        pieChartData.setCenterText1Color(App.getResource().getColor(R.color.colorPrimary));
        return pieChartData.setValues(sliceValues);
    }
}
