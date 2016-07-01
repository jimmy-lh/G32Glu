package com.bioland.db.dao;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;

import com.bioland.db.bean.AlarmValue;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.Where;

public class AlarmValueDao {
	private Context context;
	private Dao<AlarmValue, Integer> mDao = null;
	private RuntimeExceptionDao<AlarmValue, Integer> userRuntimeDao = null;
	private DatabaseHelper helper;
	AlarmValue alarmValue;

	public AlarmValueDao(Context context) {
		this.context = context;
		try {
			helper = DatabaseHelper.getHelper(context);
			// 初始化DAO
			mDao = getUserDao(helper);
			userRuntimeDao = getUserDataDao(helper);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return
	 * @throws SQLException
	 */
	private Dao<AlarmValue, Integer> getUserDao(DatabaseHelper helper) throws SQLException {
		if (mDao == null)
			mDao = helper.getDao(AlarmValue.class);
		return mDao;
	}

	private RuntimeExceptionDao<AlarmValue, Integer> getUserDataDao(DatabaseHelper helper) {
		if (userRuntimeDao == null) {
			userRuntimeDao = helper.getRuntimeExceptionDao(AlarmValue.class);
		}
		return userRuntimeDao;
	}

	/**
	 * 插入值
	 */
	public void insert(AlarmValue alarmValue) {
		userRuntimeDao.createIfNotExists(alarmValue);
	}

	/**
	 * 按照日期查询
	 * 
	 * @param date
	 * @return
	 */
	public List<AlarmValue> searchDate(String count) {
		try {
			// 查询的query 返回值是一个列表
			// 类似 select * from User where 'username' = username;
			List<AlarmValue> alarmValues = userRuntimeDao.queryBuilder().where().eq("Count", count).query();
			return alarmValues;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 按照日期和时间段查询
	 * 
	 * @param id
	 * @return
	 */
	public List<AlarmValue> searchDate(String date, String TimePeriod) {
		try {
			// 查询的query 返回值是一个列表
			// 类似 select * from User where 'username' = username;
			Where<AlarmValue, Integer> queryWhere = userRuntimeDao.queryBuilder().where();
			List<AlarmValue> alarmValues = queryWhere
					.and(queryWhere.eq("Date", date), queryWhere.eq("TimePeriod", TimePeriod)).query();
			return alarmValues;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 按照指定的id 与 username 删除一项
	 * 
	 * @param id
	 * @param username
	 * @return 删除成功返回true ，失败返回false
	 */
	public int delete(String count) {
		try {
			// 删除指定的信息，类似delete User where 'id' = id ;
			DeleteBuilder<AlarmValue, Integer> deleteBuilder = userRuntimeDao.deleteBuilder();
			deleteBuilder.where().eq("Count", count);

			return deleteBuilder.delete();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 按照指定的日期与时间段 删除一项
	 * 
	 * @param date
	 * @param TimePeriod
	 * @return 删除成功返回true ，失败返回false
	 */
	public void delete(String date, String TimePeriod) {
		userRuntimeDao.delete(searchDate(date, TimePeriod));
	}

	/**
	 * 删除全部
	 */
	public void deleteAll() {
		userRuntimeDao.delete(queryAll());
	}

	/**
	 * 查询所有的
	 */
	public List<AlarmValue> queryAll() {
		List<AlarmValue> alarmValues = userRuntimeDao.queryForAll();
		return alarmValues;
	}

	/**
	 * 更新
	 * 
	 * @param gluValue
	 *            待更新的user
	 */
	public void update(AlarmValue alarmValue) {
		userRuntimeDao.createOrUpdate(alarmValue);
	}
	/**
	 * =========================================================================
	 * =========================================================================
	 * =========================================================================
	 */
}
