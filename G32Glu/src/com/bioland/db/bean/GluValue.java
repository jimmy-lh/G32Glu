
package com.bioland.db.bean;

import com.bioland.singleton.SingletonApplication;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 数据库对应的pojo类，注意一下三点 1、填写表的名称 @DatabaseTable 2、填写表中持久化项的 @DatabaseField
 * 还可使顺便设置其属性 3、保留一个无参的构造函数
 */
// 表名称
@DatabaseTable(tableName = "GluValue")
public class GluValue {
	// 主键 id 自增长
	@DatabaseField(generatedId = true)
	private int Id;
	// mCode,机器码；可为空
	@DatabaseField(canBeNull = true)
	private String MachineCode;
	// HideDate,时间；可为空；年月日
	@DatabaseField(canBeNull = true)
	private String Date;
	// TimePeriod,时间段；可为空；0~6
	@DatabaseField(canBeNull = true)
	private String TimePeriod;
	// Reason,可为空;0~4
	@DatabaseField(canBeNull = true)
	private int Reason;
	// ReasonString,可为空;其他原因
	@DatabaseField(canBeNull = true)
	private String ReasonString;
	// 结果；可为空
	@DatabaseField(canBeNull = true)
	private String Result;
	// Lac；可为空
	@DatabaseField(canBeNull = true)
	private int Lac;
	// Cid；可为空
	@DatabaseField(canBeNull = true)
	private int Cid;
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

	public GluValue() {
		// ORMLite 需要一个无参构造
	}

	/**
	 * @param MachineCode机器码
	 * @param Date日期
	 * @param TimePeriod时间段
	 * @param IsReason是否已经设置原因
	 * @param Reason所选择的原因
	 * @param ReasonString输入的其他原因
	 * @param Result测量结果
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
	public GluValue(String Date, String TimePeriod, int Reason, String ReasonString, String Result, int Lac, int Cid,
			String Reserve1, String Reserve2, String Reserve3, String Reserve4, String Reserve5, String Reserve6,
			String Reserve7, String Reserve8, String Reserve9, String Reserve10) {
		super();
		this.MachineCode = SingletonApplication.getMachineCode();

		this.Date = Date;
		this.TimePeriod = TimePeriod;
		this.Reason = Reason;
		this.ReasonString = ReasonString;

		this.Result = Result;

		this.Lac = Lac;
		this.Cid = Cid;

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

	public String getMachineCode() {
		return MachineCode;
	}

	public void setMachineCode(String machineCode) {
		MachineCode = machineCode;
	}

	public int getReason() {
		return Reason;
	}

	public void setReason(int reason) {
		Reason = reason;
	}

	public String getReasonString() {
		return ReasonString;
	}

	public void setReasonString(String reasonString) {
		ReasonString = reasonString;
	}

	public String getResult() {
		return Result;
	}

	public void setResult(String result) {
		Result = result;
	}

	public String getTimePeriod() {
		return TimePeriod;
	}

	public void setTimePeriod(String timePeriod) {
		TimePeriod = timePeriod;
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
