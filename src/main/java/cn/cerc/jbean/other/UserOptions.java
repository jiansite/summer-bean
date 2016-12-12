package cn.cerc.jbean.other;

import java.util.HashMap;
import java.util.Map;
import cn.cerc.jbean.client.LocalService;
import cn.cerc.jdb.core.IHandle;
import cn.cerc.jdb.core.TDateTime;

public class UserOptions {
	// 用户级参数
	public static final String AllowViewProfit = "AllowViewProfit";
	public static final String AllowCouponInput = "AllowCouponInput";
	public static final String AllowBCCouponInput = "AllowBCCouponInput";
	public static final String AllowMaxDiscount = "AllowMaxDiscount";
	public static final String AllowMaxDiscountPrice = "AllowMaxDiscountPrice";
	public static final String HideHistoryData = "HideHistoryData";
	public static final String ShowAllCus = "ShowAllCus";
	public static final String AllowStockHA = "AllowStockHA";
	public static final String AllowViewAllUserLogs = "AllowViewAllUserLogs";
	public static final String AllowReportDesign = "AllowReportDesign";
	public static final String AllowSystemBaseDelete = "AllowSystemBaseDelete";
	public static final String AllowUseOtherClient = "AllowUseOtherClient";
	public static final String ShowAllMake = "ShowAllMake";
	// 导出参数
	public static final String AllowExportInUP = "AllowExportInUP";
	public static final String AllowExportOutUP = "AllowExportOutUP";
	public static final String AllowExportOther = "AllowExportOther";
	public static final String AllowExportCusInfo = "AllowExportCusInfo";
	// 设置我的喜好/行使业务助理职责
	public static final String SalesValueByCusInfo = "SalesValueByCusInfo";
	// 设置我的喜好/启用快速录单器
	public static final String FastInputWindow = "FastInputWindow";
	// 进货价
	public static final String ShowInUP = "ShowInUP";
	// 零售价
	public static final String ShowOutUP = "ShowOutUP";
	// 批发价
	public static final String ShowWholesaleUP = "ShowWholesaleUP";
	// 出厂价
	public static final String ShowBottomUP = "ShowBottomUP";
	// 不允许补登记非当天的登记收、付款单据
	public static final String DisableRegNotTodayAPAR = "DisableRegNotTodayAPAR";
	// 用户批发销售时最大允许折扣(为零时不管控)
	public static final String AllowBCMaxDiscount = "AllowBCMaxDiscount";

	private static Map<String, String> option = new HashMap<>();
	static {
		option.put(ShowInUP, "设置进货价查看、修改权限");
		option.put(ShowOutUP, "设置零售价（会员价）查看、修改权限（若关闭此项的查看权限，则出厂价、批发价会同时关闭）");
		option.put(ShowWholesaleUP, "设置批发价查看、修改权限");
		option.put(ShowBottomUP, "设置出厂价查看、修改权限");
		option.put(AllowViewProfit, "允许查看销售毛利（前提必须拥有进货价权限）");
		option.put(ShowAllCus, "允许操作其他作业人员建立的客户、订货单、销货单");
		option.put(AllowCouponInput, "允许在零售销售时给与客户临时优惠（一般授权于店长）");
		option.put(AllowStockHA, "允许进行库存盘点");
		option.put(AllowBCCouponInput, "允许在批发销售时给与客户临时优惠（一般授权于店长）");
		option.put(AllowExportInUP, "允许导出含进货价资料");
		option.put(AllowExportOutUP, "允许导出含销货价资料");
		option.put(AllowExportOther, "允许导出其他普通资料");
		option.put(AllowExportCusInfo, "允许导出客户资料");
		option.put(AllowViewAllUserLogs, "允许查看所有人员的操作日志(一般开放于老板助理)");
		option.put(AllowReportDesign, "允许增加、修改、删除系统报表格式");
		option.put(AllowSystemBaseDelete, "允许删除系统基本资料(使用系统数据删除功能)");
		option.put(AllowMaxDiscount, "用户零售时最大允许折扣(为零时不管控)");
		option.put(AllowMaxDiscountPrice, "业务员打折时最大允许优惠金额(为-1默认不管控)");
		option.put(AllowUseOtherClient, "允许使用除地藤客户端以外的其他设备登录系统，包括手机、微信、浏览器等");
		option.put(DisableRegNotTodayAPAR, "不允许补登记非当天的登记收、付款单据");
		option.put(AllowBCMaxDiscount, "用户批发销售时最大允许折扣(为零时不管控)");
		option.put(ShowAllMake, "允许操作其他生产管理员建立的生产计划");
	}

	public static String getOption(String key) {
		return option.get(key);
	}

	public static boolean UserOptionEnabled(IHandle session, String ACode) {
		return GetUserOption(session, ACode).equals("on");
	}

	public static String GetUserOption(IHandle Session, String ACode) {
		return GetUserOption(Session, ACode, "");
	}

	private static String GetUserOption(IHandle sess, String ACode, String ADefault) {
		try (MemoryBuffer buff = new MemoryBuffer(BufferType.getUserOption, sess.getUserCode(), ACode)) {
			if (buff.isNull()) {
				String Result = ADefault;
				LocalService ser = new LocalService(sess, "SvrUserOption");
				if(ser.exec("Code_", ACode) && !ser.getDataOut().eof()){
					Result = ser.getDataOut().getString("Value_");
				}		
				buff.setField("Value_", Result);

			}
			return buff.getString("Value_");
		}
	}

	 // 1.先判断当前用户是否有隐藏以前数据。如没隐藏则不增加控制，返回false
	 // 2.如有隐藏再判断是否是否有开放临时打开查看所有权限功能。如有打开并在开放的时间内则可以查看所有的的资料，返回false
	 // 3.如没有开发或开放已过去就只能查看指定时间内的单 返回 true及日期 范围的数据
	 
	public static boolean isHideHistoryData(IHandle session, Var_Integer FDay) {
		if (!GetUserOption(session, "HideHistoryData").equals("on"))
			return false;

		String HideHistoryDateTime = GetUserOption(session, "HideHistoryDateTime");

		if (HideHistoryDateTime.equals("")) {
			FDay.value = cn.cerc.jdb.other.utils.strToIntDef(GetUserOption(session, "HideHistoryDay"), 7);
			return true;
		}

		if (TDateTime.Now().compareTo(TDateTime.fromDate(HideHistoryDateTime)) < 0)
			FDay.value = cn.cerc.jdb.other.utils.strToIntDef(GetUserOption(session, "HideHistoryTmpDay"), 0);
		else
			FDay.value = cn.cerc.jdb.other.utils.strToIntDef(GetUserOption(session, "HideHistoryDay"), 7);
		return true;
	}

	public static boolean isHideHistoryData(IHandle session) {
		Var_Integer ADay = new Var_Integer();
		return isHideHistoryData(session, ADay);
	}
}
