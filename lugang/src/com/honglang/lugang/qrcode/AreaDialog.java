package com.honglang.lugang.qrcode;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

import com.honglang.lugang.R;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class AreaDialog extends DialogFragment implements OnClickListener {

	private int[] cities = {R.array.beijin_province_item, R.array.tianjin_province_item, R.array.heibei_province_item, R.array.shanxi1_province_item, R.array.neimenggu_province_item, R.array.liaoning_province_item, R.array.jilin_province_item, R.array.heilongjiang_province_item, R.array.shanghai_province_item, R.array.jiangsu_province_item, R.array.zhejiang_province_item, R.array.anhui_province_item, R.array.fujian_province_item, R.array.jiangxi_province_item, R.array.shandong_province_item, R.array.henan_province_item, R.array.hubei_province_item, R.array.hunan_province_item, R.array.guangdong_province_item,  R.array.guangxi_province_item, R.array.hainan_province_item, R.array.chongqing_province_item, R.array.sichuan_province_item, R.array.guizhou_province_item, R.array.yunnan_province_item, R.array.xizang_province_item, R.array.shanxi2_province_item, R.array.gansu_province_item, R.array.qinghai_province_item, R.array.linxia_province_item, R.array.xinjiang_province_item, R.array.hongkong_province_item, R.array.aomen_province_item, R.array.taiwan_province_item};
	private int[] countyOfBeiJing = {R.array.beijin_city_item};
	private int[] countyOfTianJing = {R.array.tianjin_city_item};
	private int[] countyOfHeBei = {R.array.shijiazhuang_city_item, R.array.tangshan_city_item, R.array.qinghuangdao_city_item, R.array.handan_city_item, R.array.xingtai_city_item, R.array.baoding_city_item, R.array.zhangjiakou_city_item, R.array.chengde_city_item, R.array.cangzhou_city_item, R.array.langfang_city_item, R.array.hengshui_city_item};
	private int[] countyOfShanXi1 = {R.array.taiyuan_city_item, R.array.datong_city_item, R.array.yangquan_city_item, R.array.changzhi_city_item, R.array.jincheng_city_item, R.array.shuozhou_city_item, R.array.jinzhong_city_item, R.array.yuncheng_city_item, R.array.xinzhou_city_item, R.array.linfen_city_item, R.array.lvliang_city_item};
	private int[] countyOfNeiMengGu = {R.array.huhehaote_city_item, R.array.baotou_city_item, R.array.wuhai_city_item, R.array.chifeng_city_item, R.array.tongliao_city_item, R.array.eerduosi_city_item, R.array.hulunbeier_city_item, R.array.bayannaoer_city_item, R.array.wulanchabu_city_item, R.array.xinganmeng_city_item, R.array.xilinguolemeng_city_item, R.array.alashanmeng_city_item};
	private int[] countyOfLiaoNing = {R.array.shenyang_city_item, R.array.dalian_city_item, R.array.anshan_city_item, R.array.wushun_city_item, R.array.benxi_city_item, R.array.dandong_city_item, R.array.liaoning_jinzhou_city_item, R.array.yingkou_city_item, R.array.fuxin_city_item, R.array.liaoyang_city_item, R.array.panjin_city_item, R.array.tieling_city_item, R.array.zhaoyang_city_item, R.array.huludao_city_item};
	private int[] countyOfJiLin = {R.array.changchun_city_item, R.array.jilin_city_item, R.array.siping_city_item, R.array.liaoyuan_city_item, R.array.tonghua_city_item, R.array.baishan_city_item, R.array.songyuan_city_item, R.array.baicheng_city_item, R.array.yanbian_city_item};
	private int[] countyOfHeiLongJiang = {R.array.haerbing_city_item, R.array.qiqihaer_city_item, R.array.jixi_city_item, R.array.hegang_city_item, R.array.shuangyashan_city_item, R.array.daqing_city_item, R.array.heilongjiang_yichun_city_item, R.array.jiamusi_city_item, R.array.qitaihe_city_item, R.array.mudanjiang_city_item, R.array.heihe_city_item, R.array.suihua_city_item, R.array.daxinganling_city_item};
	private int[] countyOfShangHai = {R.array.shanghai_city_item};
	
	private int[] countyOfJiangSu = {R.array.nanjing_city_item, R.array.wuxi_city_item, R.array.xuzhou_city_item, R.array.changzhou_city_item, R.array.nanjing_suzhou_city_item, R.array.nantong_city_item, R.array.lianyungang_city_item, R.array.huaian_city_item, R.array.yancheng_city_item, R.array.yangzhou_city_item, R.array.zhenjiang_city_item, R.array.jiangsu_taizhou_city_item, R.array.suqian_city_item};
	private int[] countyOfZheJiang = {R.array.hangzhou_city_item, R.array.ningbo_city_item, R.array.wenzhou_city_item, R.array.jiaxing_city_item, R.array.huzhou_city_item, R.array.shaoxing_city_item, R.array.jinhua_city_item, R.array.quzhou_city_item, R.array.zhoushan_city_item, R.array.zejiang_huzhou_city_item, R.array.lishui_city_item};
	private int[] countyOfAnHui = {R.array.hefei_city_item, R.array.wuhu_city_item, R.array.bengbu_city_item, R.array.huainan_city_item, R.array.maanshan_city_item, R.array.huaibei_city_item, R.array.tongling_city_item, R.array.anqing_city_item, R.array.huangshan_city_item, R.array.chuzhou_city_item, R.array.fuyang_city_item, R.array.anhui_suzhou_city_item, R.array.chaohu_city_item, R.array.luan_city_item, R.array.haozhou_city_item, R.array.chizhou_city_item, R.array.xuancheng_city_item};
	private int[] countyOfFuJian = {R.array.huzhou_city_item, R.array.xiamen_city_item, R.array.putian_city_item, R.array.sanming_city_item, R.array.quanzhou_city_item, R.array.zhangzhou_city_item, R.array.nanp_city_item, R.array.longyan_city_item, R.array.ningde_city_item};
	private int[] countyOfJiangXi = {R.array.nanchang_city_item, R.array.jingdezhen_city_item, R.array.pingxiang_city_item, R.array.jiujiang_city_item, R.array.xinyu_city_item, R.array.yingtan_city_item, R.array.ganzhou_city_item, R.array.jian_city_item, R.array.jiangxi_yichun_city_item, R.array.jiangxi_wuzhou_city_item, R.array.shangrao_city_item};
	private int[] countyOfShanDong = {R.array.jinan_city_item, R.array.qingdao_city_item, R.array.zaobo_city_item, R.array.zaozhuang_city_item, R.array.dongying_city_item, R.array.yantai_city_item, R.array.weifang_city_item, R.array.jining_city_item, R.array.taian_city_item, R.array.weihai_city_item, R.array.rizhao_city_item, R.array.laiwu_city_item, R.array.linxi_city_item, R.array.dezhou_city_item, R.array.liaocheng_city_item, R.array.shandong_bingzhou_city_item, R.array.heze_city_item};
	private int[] countyOfHeNan = {R.array.zhenshou_city_item, R.array.kaifang_city_item, R.array.luoyang_city_item, R.array.kaipingshan_city_item, R.array.anyang_city_item, R.array.hebi_city_item, R.array.xinxiang_city_item, R.array.jiaozuo_city_item, R.array.buyang_city_item, R.array.xuchang_city_item, R.array.leihe_city_item, R.array.sanmenxia_city_item, R.array.nanyang_city_item, R.array.shangqiu_city_item, R.array.xinyang_city_item, R.array.zhoukou_city_item, R.array.zhumadian_city_item};
	private int[] countyOfHuBei = {R.array.wuhan_city_item, R.array.huangshi_city_item, R.array.shiyan_city_item, R.array.yichang_city_item, R.array.xiangpan_city_item, R.array.erzhou_city_item, R.array.jinmen_city_item, R.array.xiaogan_city_item, R.array.hubei_jinzhou_city_item, R.array.huanggang_city_item, R.array.xianning_city_item, R.array.suizhou_city_item, R.array.enshi_city_item, R.array.shenglongjia_city_item};
	
	private int[] countyOfHuNan = {R.array.changsha_city_item, R.array.zhuzhou_city_item, R.array.xiangtan_city_item, R.array.hengyang_city_item, R.array.shaoyang_city_item, R.array.yueyang_city_item, R.array.changde_city_item, R.array.zhangjiajie_city_item, R.array.yiyang_city_item, R.array.hunan_bingzhou_city_item, R.array.yongzhou_city_item, R.array.huaihua_city_item, R.array.loudi_city_item, R.array.xiangxi_city_item};
	private int[] countyOfGuangDong = {R.array.guangzhou_city_item, R.array.shaoguan_city_item, R.array.shenzhen_city_item, R.array.zhuhai_city_item, R.array.shantou_city_item, R.array.foshan_city_item, R.array.jiangmen_city_item, R.array.zhangjiang_city_item, R.array.maoming_city_item, R.array.zhaoqing_city_item, R.array.huizhou_city_item, R.array.meizhou_city_item, R.array.shanwei_city_item, R.array.heyuan_city_item, R.array.yangjiang_city_item, R.array.qingyuan_city_item, R.array.dongguan_city_item, R.array.zhongshan_city_item, R.array.chaozhou_city_item, R.array.jiyang_city_item, R.array.yunfu_city_item};
	private int[] countyOfGuangXi = {R.array.nanning_city_item, R.array.liuzhou_city_item, R.array.guilin_city_item, R.array.guangxi_wuzhou_city_item, R.array.beihai_city_item, R.array.fangchenggang_city_item, R.array.qinzhou_city_item, R.array.guigang_city_item, R.array.yuelin_city_item, R.array.baise_city_item, R.array.hezhou_city_item, R.array.hechi_city_item, R.array.laibing_city_item, R.array.chuangzuo_city_item};
	private int[] countyOfHaiNan = {R.array.haikou_city_item, R.array.sanya_city_item};
	private int[] countyOfChongQing = {R.array.chongqing_city_item};
	private int[] countyOfSiChuan = {R.array.chengdu_city_item, R.array.zigong_city_item, R.array.panzhihua_city_item, R.array.luzhou_city_item, R.array.deyang_city_item, R.array.mianyang_city_item, R.array.guangyuan_city_item, R.array.suining_city_item, R.array.neijiang_city_item, R.array.leshan_city_item, R.array.nanchong_city_item, R.array.meishan_city_item, R.array.yibing_city_item, R.array.guangan_city_item, R.array.dazhou_city_item, R.array.yaan_city_item, R.array.bazhong_city_item, R.array.ziyang_city_item, R.array.abei_city_item, R.array.ganmu_city_item, R.array.liangshan_city_item};
	private int[] countyOfGuiZhou = {R.array.guiyang_city_item, R.array.lupanshui_city_item, R.array.zhunyi_city_item, R.array.anshun_city_item, R.array.tongren_city_item, R.array.qingxinan_city_item, R.array.biji_city_item, R.array.qingdongnan_city_item, R.array.qingnan_city_item};
	private int[] countyOfYunNan = {R.array.kunming_city_item, R.array.qujing_city_item, R.array.yuexi_city_item, R.array.baoshan_city_item, R.array.zhaotong_city_item, R.array.lijiang_city_item, R.array.simao_city_item, R.array.lingcang_city_item, R.array.chuxiong_city_item, R.array.honghe_city_item, R.array.wenshan_city_item, R.array.xishuangbanna_city_item, R.array.dali_city_item, R.array.dehuang_city_item, R.array.nujiang_city_item, R.array.diqing_city_item};
	private int[] countyOfXiZang = {R.array.lasa_city_item, R.array.changdu_city_item, R.array.shannan_city_item, R.array.rgeze_city_item, R.array.naqu_city_item, R.array.ali_city_item, R.array.linzhi_city_item};
	
	private int[] countyOfShanXi2 = {R.array.xian_city_item, R.array.tongchuan_city_item, R.array.baoji_city_item, R.array.xianyang_city_item, R.array.weinan_city_item, R.array.yanan_city_item, R.array.hanzhong_city_item, R.array.yulin_city_item, R.array.ankang_city_item, R.array.shangluo_city_item};
	private int[] countyOfGanSu = {R.array.lanzhou_city_item, R.array.jiayuguan_city_item, R.array.jinchang_city_item, R.array.baiyin_city_item, R.array.tianshui_city_item, R.array.wuwei_city_item, R.array.zhangyue_city_item, R.array.pingliang_city_item, R.array.jiuquan_city_item, R.array.qingyang_city_item, R.array.dingxi_city_item, R.array.longnan_city_item, R.array.linxia_city_item, R.array.gannan_city_item};
	private int[] countyOfQingHai = {R.array.xining_city_item, R.array.haidong_city_item, R.array.haibai_city_item, R.array.huangnan_city_item, R.array.hainan_city_item, R.array.guluo_city_item, R.array.yushu_city_item, R.array.haixi_city_item};
	private int[] countyOfNingXia = {R.array.yinchuan_city_item, R.array.shizuishan_city_item, R.array.wuzhong_city_item, R.array.guyuan_city_item, R.array.zhongwei_city_item};
	private int[] countyOfXinJiang = {R.array.wulumuqi_city_item, R.array.kelamayi_city_item, R.array.tulyfan_city_item, R.array.hami_city_item, R.array.changji_city_item, R.array.boertala_city_item, R.array.bayinguolen_city_item, R.array.akesu_city_item, R.array.kemuleisu_city_item, R.array.geshen_city_item, R.array.hetian_city_item, R.array.yili_city_item, R.array.tacheng_city_item, R.array.aleitai_city_item, R.array.shihezi_city_item, R.array.alaer_city_item, R.array.tumushihe_city_item, R.array.wujiaqu_city_item};
	private int[] countyOfHongKong = {};
	private int[] countyOfAoMen = {};
	private int[] countyOfTaiWan = {};
	
	private Button confirm;
	private Button cancel;
	private WheelView province;
	private WheelView city;
	private WheelView district;
	
	private boolean scrolling = false;
	int id = 0;
	public static AreaDialog newInstance(int area){
		AreaDialog frag = new AreaDialog();
		Bundle args = new Bundle();
        args.putInt("area", area);
        frag.setArguments(args);
		return frag;
	}
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = super.onCreateDialog(savedInstanceState);
		dialog.setTitle("请选择区域");
		return dialog;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setStyle(0, android.R.style.Theme_Holo);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		return inflater.inflate(R.layout.dialog_area, null);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		confirm = (Button) view.findViewById(R.id.confirm);
		cancel = (Button) view.findViewById(R.id.cancel);
		confirm.setOnClickListener(this);
		cancel.setOnClickListener(this);
		
		province = (WheelView) view.findViewById(R.id.province);
		city = (WheelView) view.findViewById(R.id.city);
		district = (WheelView) view.findViewById(R.id.district);
		
		province.setVisibleItems(3);
		city.setVisibleItems(5);
		district.setVisibleItems(5);
		
		ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(getActivity(), getActivity().getResources().getStringArray(R.array.province_item));
		province.setViewAdapter(adapter);
		
		province.addChangingListener(new OnWheelChangedListener() {
			
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				if (!scrolling) {
					updateCities(city, newValue);;
				}
				
			}
		});
		province.addScrollingListener(new OnWheelScrollListener() {
			
			@Override
			public void onScrollingStarted(WheelView wheel) {
				scrolling = true;
			}
			
			@Override
			public void onScrollingFinished(WheelView wheel) {
				scrolling = false;
				updateCities(city, province.getCurrentItem());
			}
		});
		
		city.addChangingListener(new OnWheelChangedListener() {
			
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				if (!scrolling) {
					updateCounties(district, newValue);
				}
			}
		});
		city.addScrollingListener(new OnWheelScrollListener() {
			
			@Override
			public void onScrollingStarted(WheelView wheel) {
				scrolling = true;
			}
			
			@Override
			public void onScrollingFinished(WheelView wheel) {
				scrolling = false;
				updateCounties(district, city.getCurrentItem());
			}
		});
		
		province.setCurrentItem(19);
		super.onViewCreated(view, savedInstanceState);
	}

	private void updateCities(WheelView city, int index){
		ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(getActivity(), getActivity().getResources().getStringArray(cities[index]));
		city.setViewAdapter(adapter);
		city.setCurrentItem(1);
	}
	
	private void updateCounties(WheelView district, int index){
		switch (province.getCurrentItem()) {
		case 0:
			id = countyOfBeiJing[index];
			break;
		case 1:
			id = countyOfTianJing[index];
			break;
		case 2:
			id = countyOfHeBei[index];
			break;
		case 3:
			id = countyOfShanXi1[index];
			break;
		case 4:
			id = countyOfNeiMengGu[index];
			break;
		case 5:
			id = countyOfLiaoNing[index];
			break;
		case 6:
			id = countyOfJiLin[index];
			break;
		case 7:
			id = countyOfHeiLongJiang[index];
			break;
		case 8:
			id = countyOfShangHai[index];
			break;
		case 9:
			id = countyOfJiangSu[index];
			break;
		case 10:
			id = countyOfZheJiang[index];
			break;
		case 11:
			id = countyOfAnHui[index];
			break;
		case 12:
			id = countyOfFuJian[index];
			break;
		case 13:
			id = countyOfJiangXi[index];
			break;
		case 14:
			id = countyOfShanDong[index];
			break;
		case 15:
			id = countyOfHeNan[index];
			break;
		case 16:
			id = countyOfHuBei[index];
			break;
		case 17:
			id = countyOfHuNan[index];
			break;
		case 18:
			id = countyOfGuangDong[index];
			break;
		case 19:
			id = countyOfGuangXi[index];
			break;
		case 20:
			id = countyOfHaiNan[index];
			break;
		case 21:
			id = countyOfChongQing[index];
			break;
		case 22:
			id = countyOfSiChuan[index];
			break;
		case 23:
			id = countyOfGuiZhou[index];
			break;
		case 24:
			id = countyOfYunNan[index];
			break;
		case 25:
			id = countyOfXiZang[index];
			break;
		case 26:
			id = countyOfShanXi2[index];
			break;
		case 27:
			id = countyOfGanSu[index];
			break;
		case 28:
			id = countyOfQingHai[index];
			break;
		case 29:
			id = countyOfNingXia[index];
			break;
		case 30:
			id = countyOfXinJiang[index];
			break;
		case 31:
			id = countyOfHongKong[index];
			break;
		case 32:
			id = countyOfAoMen[index];
			break;
		case 33:
			id = countyOfTaiWan[index];
			break;

		default:
			break;
		}
		ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(getActivity(), getActivity().getResources().getStringArray(id));
		district.setViewAdapter(adapter);
		district.setCurrentItem(1);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.confirm:
			String p = getActivity().getResources().getStringArray(R.array.province_item)[province.getCurrentItem()];
			String c = getActivity().getResources().getStringArray(cities[province.getCurrentItem()])[city.getCurrentItem()];
			String d = getActivity().getResources().getStringArray(id)[district.getCurrentItem()];
			String a = p + "-" + c + "市-" + d;
			if (getArguments().getInt("area") == 0) {
				((BlankActivity)getActivity()).from.setText(a);
			}
			if (getArguments().getInt("area") == 1) {
				((BlankActivity)getActivity()).to.setText(a);
			}
			dismiss();
			break;
		case R.id.cancel:
			dismiss();
			break;
		}
		
	}

}
