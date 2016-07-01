package com.bioland.db.dao;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.widget.TextView;

import com.bioland.db.bean.GluValue;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.Where;

public class GluValueDao {
	private Context context;
	private Dao<GluValue, Integer> mDao = null;
	private RuntimeExceptionDao<GluValue, Integer> userRuntimeDao = null;
	private DatabaseHelper helper;
	GluValue gluValue;

	public GluValueDao(Context context) {
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
	private Dao<GluValue, Integer> getUserDao(DatabaseHelper helper) throws SQLException {
		if (mDao == null)
			mDao = helper.getDao(GluValue.class);
		return mDao;
	}

	private RuntimeExceptionDao<GluValue, Integer> getUserDataDao(DatabaseHelper helper) {
		if (userRuntimeDao == null) {
			userRuntimeDao = helper.getRuntimeExceptionDao(GluValue.class);
		}
		return userRuntimeDao;
	}

	/**
	 * 插入值
	 */
	public void insert(GluValue gluValue) {
		userRuntimeDao.createIfNotExists(gluValue);
	}

	/**
	 * 按照日期查询
	 * 
	 * @param date
	 * @return
	 */
	public List<GluValue> searchDate(String date) {
		try {
			// 查询的query 返回值是一个列表
			// 类似 select * from User where 'username' = username;
			List<GluValue> gluValues = userRuntimeDao.queryBuilder().where().eq("Date", date).query();
			return gluValues;
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
	public List<GluValue> searchDate(String date, String TimePeriod) {
		try {
			// 查询的query 返回值是一个列表
			// 类似 select * from User where 'username' = username;
			Where<GluValue, Integer> queryWhere = userRuntimeDao.queryBuilder().where();
			List<GluValue> gluValues = queryWhere
					.and(queryWhere.eq("Date", date), queryWhere.eq("TimePeriod", TimePeriod)).query();
			return gluValues;
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
	public int delete(String date) {
		try {
			// 删除指定的信息，类似delete User where 'id' = id ;
			DeleteBuilder<GluValue, Integer> deleteBuilder = userRuntimeDao.deleteBuilder();
			deleteBuilder.where().eq("Date", date);

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
	public List<GluValue> queryAll() {
		List<GluValue> gluValues = userRuntimeDao.queryForAll();
		return gluValues;
	}

	/**
	 * 更新
	 * 
	 * @param gluValue
	 *            待更新的user
	 */
	public void update(GluValue gluValue) {
		userRuntimeDao.createOrUpdate(gluValue);
	}
	/**
	 * =========================================================================
	 * =========================================================================
	 * =========================================================================
	 */

	/**
	 * 增加一个用户
	 * 
	 * @param gluValue
	 * @throws SQLException
	 */
	public void add(GluValue gluValue) {
		/*
		 * //事务操作
		 * TransactionManager.callInTransaction(helper.getConnectionSource(),
		 * new Callable<Void>() {
		 * 
		 * @Override public Void call() throws Exception { return null; } });
		 */
		try {
			mDao.create(gluValue);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public GluValue get(int id) {
		try {
			return mDao.queryForId(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 按照id查询user
	 * 
	 * @param id
	 * @return
	 */
	public GluValue search(String username) {
		try {
			// 查询的query 返回值是一个列表
			// 类似 select * from User where 'username' = username;
			List<GluValue> gluValues = userRuntimeDao.queryBuilder().where().eq("username", username).query();
			if (gluValues.size() > 0)
				return gluValues.get(0);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 显示所有的
	 */
	private void display(TextView view) {
		List<GluValue> gluValues = queryAll();
		for (GluValue gluValue : gluValues) {
			view.append(gluValue.toString());
		}
	}

}
