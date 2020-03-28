package com.example.administrator.weatherforecast;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.weatherforecast.db.City;
import com.example.administrator.weatherforecast.db.County;
import com.example.administrator.weatherforecast.db.Province;
import com.example.administrator.weatherforecast.util.HttpUtil;
import com.example.administrator.weatherforecast.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChooseAreaFragment extends Fragment {

    public static final int LEVEL_PROVINCE = 0;

    public static final int LEVEL_CITY = 1;

    public static final int LEVEL_COUNTY = 2;

    private TextView titleTextview;
    private Button backButton;
    private ListView listview;
    private List<String> dataList = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private ProgressDialog progressdialog;

    /*
     * 省列表
     * */
    private List<Province> provinceList;

    /*
     * 市列表
     * */
    private List<City> cityList;

    /*
     * 县列表
     * */
    private List<County> countyList;

    /*
     * 选中的省份
     * */
    private Province selectProvince;

    /*
     *选中的城市
     * */
    private City selectCity;

    /*
     * 当前选中的级别
     * */
    private int currentLevel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area, container, false);
        titleTextview = (TextView) view.findViewById(R.id.title_text);
        backButton = (Button) view.findViewById(R.id.back_button);
        listview = (ListView) view.findViewById(R.id.list_view);
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, dataList);
        listview.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (currentLevel == LEVEL_PROVINCE) {
                    selectProvince = provinceList.get(i);
                    qureyCities();
                } else if (currentLevel == LEVEL_CITY) {
                    selectCity = cityList.get(i);
                    qureyCounties();
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentLevel == LEVEL_COUNTY) {
                    qureyCities();
                } else if (currentLevel == LEVEL_CITY) {
                    queryProvinces();
                }
            }
        });
        queryProvinces();
    }

    /*
     * 查询全国所有的省优先从本地服务器查找,如果没有再从服务器查询
     * */
    private void queryProvinces() {
        titleTextview.setText("中国");
        backButton.setVisibility(View.GONE);
        provinceList = DataSupport.findAll(Province.class);
        if (provinceList.size() > 0) {
            dataList.clear();
            for (Province province : provinceList) {
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listview.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        } else {
            String address = "http://guolin.tech/api/china";
            queryFromServer(address, "province");
        }
    }

    /*
     * 查询省内所有的市，优先从数据库查询，如果没有查询到再去服务器上查询
     * */
    private void qureyCities() {
        titleTextview.setText(selectProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);
        cityList = DataSupport.where("provinceid = ?", String.valueOf(selectProvince
                .getId())).find(City.class);
        if (cityList.size() > 0) {
            dataList.clear();
            for (City city : cityList) {
                dataList.add(city.getCityName());
                adapter.notifyDataSetChanged();
                listview.setSelection(0);
                currentLevel = LEVEL_CITY;
            }
        } else {
            int provinceCode = selectProvince.getProvinceCode();
            String address = "http://guolin.tech/api/china/" + provinceCode;
            queryFromServer(address, "city");
        }
    }

    /*
     * 查询选中市所有的县，优先从数据库查询，如果没有查询到再去服务器上查询
     * */
    private void qureyCounties() {
        titleTextview.setText(selectCity.getCityName());
        backButton.setVisibility(View.VISIBLE);
        countyList = DataSupport.where("cityid = ?", String.valueOf(selectCity
                .getId())).find(County.class);
        if (countyList.size() > 0) {
            dataList.clear();
            for (County county : countyList) {
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listview.setSelection(0);
            currentLevel = LEVEL_COUNTY;
        } else {
            int provinceCode = selectProvince.getProvinceCode();
            int cityCode = selectCity.getCityCode();
            String address = "http://guolin.tech/api/china/" + provinceCode + "/" + cityCode;
            queryFromServer(address, "county");
        }
    }

    /*
     * 根据传入的地址和类型从服务器上查询省市县数据
     * */
    private void queryFromServer(String address, final String type) {
        showProgerssDialog();
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseTest = response.body().string();
                boolean result = false;
                if ("province".equals(type)) {
                    result = Utility.handlerProvinceResponse(responseTest);
                } else if ("city".equals(type)) {
                    result = Utility.handlerCityResponse(responseTest, selectProvince.getId());
                } else if ("county".equals(type)) {
                    result = Utility.handlerCountyResponse(responseTest, selectCity.getId());
                }
                if (result) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type)) {
                                queryProvinces();
                            } else if ("city".equals(type)) {
                                qureyCities();
                            } else if ("county".equals(type)) {
                                qureyCounties();
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /*
     * 显示进度对话框
     * */
    private void showProgerssDialog() {
        if (progressdialog == null) {
            progressdialog = new ProgressDialog(getActivity());
            progressdialog.setTitle("正在加载...");
            progressdialog.setCanceledOnTouchOutside(false);
        }
        progressdialog.show();
    }

    /*
     * 关闭进度
     * */
    private void closeProgressDialog() {
        if (progressdialog != null) {
            progressdialog.dismiss();
        }
    }
}
