
package com.bioland.db.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 数据库对应的pojo类，注意一下三点 1、填写表的名称 @DatabaseTable 2、填写表中持久化项的 @DatabaseField
 * 还可使顺便设置其属性 3、保留一个无参的构造函数
 */
// 表名称
@DatabaseTable(tableName = "AlarmValue")
public class AlarmValue {
	// 主键 id 自增长
	@DatabaseField(generatedId = true)
	private int Id;
	// Date,频率；可为空；几天闹一次
	@DatabaseField(canBeNull = true)
	private String Date;
	// Time,时间；可为空；时分
	@DatabaseField(canBeNull = true)
	private String Time;
	// 闹钟是否打开
	@DatabaseField(canBeNull = true)
	private boolean Setting;
	// Count,计数；
	@DatabaseField(canBeNull = true)
	private String Count;
	// Reserve1；可为空
	@DatabaseField(canBeNull = true)
	private String Reserve1;
	// Reserve2；可为空
	@DatabaseField(canBeNull = true)
	private String Reserve2;
	// Reserve3；可为空
	@DatabaseField(canBeNull = true)
	private String Reserve3;
	// Reserve4；可为空
	@DatabaseField(canBeNull = true)
	private String Reserve4;
	// Reserve5；可为空
	@DatabaseField(canBeNull = true)
	private String Reserve5;
	// Reserve6；可为空
	@DatabaseField(canBeNull = true)
	private String Reserve6;
	// Reserve7；可为空
	@DatabaseField(canBeNull = true)
	private String Reserve7;
	// Reserve8；可为空
	@DatabaseField(canBeNull = true)
	private String Reserve8;
	// Reserve9；可为空
	@DatabaseField(canBeNull = true)
	private String Reserve9;
	// Reserve10；可为空
	@DatabaseField(canBeNull = true)
	private String Reserve10;

	public AlarmValue() {
		// ORMLite 需要一个无参构造
	}

	/**
	 * @param Date日期
	 * @param Time时间
	 * @param Setting闹钟是否开启
	 * @param Reserve1保留位
	 * @param Reserve2保留位
	 * @param Reserve3保留位
	 * @param Reserve4保留位
	 * @param Reserve5保留位
	 * @param Reserve6保留位
	 * @param Reserve7保留位
	 * @param Reserve8保留位
	 * @param Reserve9保留位
	 * @param Reserve10保留位
	 */
	public AlarmValue(String Date, String Time, boolean Setting, String Count, String Reserve1, String Reserve2,
			String Reserve3, String Reserve4, String Reserve5, String Reserve6, String Reserve7, String Reserve8,
			String Reserve9, String Reserve10) {
		super();

		this.Date = Date;
		this.Time = Time;
		this.Setting = Setting;
		this.Count = Count;

		this.Reserve1 = Reserve1;
		this.Reserve2 = Reserve2;
		this.Reserve3 = Reserve3;
		this.Reserve4 = Reserve4;
		this.Reserve5 = Reserve5;
		this.Reserve6 = Reserve6;
		this.Reserve7 = Reserve7;
		this.Reserve8 = Reserve8;
		this.Reserve9 = Reserve9;
		this.Reserve10 = Reserve10;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getDate() {
		return Date;
	}

	public void setDate(String date) {
		Date = date;
	}

	public String getTime() {
		return Time;
	}

	public void setTime(String time) {
		Time = time;
	}

	public boolean isSetting() {
		return Setting;
	}

	public void setSetting(boolean setting) {
		Setting = setting;
	}

	public String getCount() {
		return Count;
	}

	public void setCount(String count) {
		Count = count;
	}

	public String getReserve1() {
		return Reserve1;
	}

	public void setReserve1(String reserve1) {
		Reserve1 = reserve1;
	}

	public String getReserve2() {
		return Reserve2;
	}

	public void setReserve2(String reserve2) {
		Reserve2 = reserve2;
	}

	public String getReserve3() {
		return Reserve3;
	}

	public void setReserve3(String reserve3) {
		Reserve3 = reserve3;
	}

	public String getReserve4() {
		return Reserve4;
	}

	public void setReserve4(String reserve4) {
		Reserve4 = reserve4;
	}

	public String getReserve5() {
		return Reserve5;
	}

	public void setReserve5(String reserve5) {
		Reserve5 = reserve5;
	}

	public String getReserve6() {
		return Reserve6;
	}

	public void setReserve6(String reserve6) {
		Reserve6 = reserve6;
	}

	public String getReserve7() {
		return Reserve7;
	}

	public void setReserve7(String reserve7) {
		Reserve7 = reserve7;
	}

	public String getReserve8() {
		return Reserve8;
	}

	public void setReserve8(String reserve8) {
		Reserve8 = reserve8;
	}

	public String getReserve9() {
		return Reserve9;
	}

	public void setReserve9(String reserve9) {
		Reserve9 = reserve9;
	}

	public String getReserve10() {
		return Reserve10;
	}

	public void setReserve10(String reserve10) {
		Reserve10 = reserve10;
	}

	@Override
	public String toString() {
		String text = "";
		String count = "";

		text += "\nid = " + Id;
		return text + count;
	}

}
