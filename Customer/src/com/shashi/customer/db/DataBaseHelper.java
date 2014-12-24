package com.shashi.customer.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DataBaseHelper extends OrmLiteSqliteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "Customer";
	private RuntimeExceptionDao<CustomerDatabase, Integer> DemoORMLiteRuntimeDao = null;
	private RuntimeExceptionDao<RequestIdRecent, Integer> DemoORMLiteRuntimeDaoReq = null;
	private RuntimeExceptionDao<ProviderAcceptedList, Integer> DemoORMLiteRuntimeDaoPro = null;

	public DataBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
		try {
			Log.i(DataBaseHelper.class.getName(), "onCreate");
			TableUtils.createTable(connectionSource, CustomerDatabase.class);
			TableUtils.createTable(connectionSource, RequestIdRecent.class);
			TableUtils
					.createTable(connectionSource, ProviderAcceptedList.class);
		} catch (java.sql.SQLException e) {
			e.printStackTrace();
			Log.e(DataBaseHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, ConnectionSource arg1, int arg2,
			int arg3) {
		try {
			Log.i(DataBaseHelper.class.getName(), "onUpgrade");
			TableUtils
					.dropTable(connectionSource, CustomerDatabase.class, true);
			TableUtils.dropTable(connectionSource, RequestIdRecent.class, true);
			TableUtils.dropTable(connectionSource, ProviderAcceptedList.class,
					true);
			onCreate(arg0, connectionSource);
		} catch (SQLException e) {
			Log.e(DataBaseHelper.class.getName(), "Can't drop databases", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void close() {
		super.close();
	}

	public void insert(CustomerDatabase database) {
		RuntimeExceptionDao<CustomerDatabase, Integer> dao = getSimpleDataDao();
		dao.create(database);
	}

	private RuntimeExceptionDao<CustomerDatabase, Integer> getSimpleDataDao() {
		if (DemoORMLiteRuntimeDao == null) {
			DemoORMLiteRuntimeDao = getRuntimeExceptionDao(CustomerDatabase.class);
		}
		return DemoORMLiteRuntimeDao;
	}

	private RuntimeExceptionDao<RequestIdRecent, Integer> getSimpleDataDaoReq() {
		if (DemoORMLiteRuntimeDaoReq == null) {
			DemoORMLiteRuntimeDaoReq = getRuntimeExceptionDao(RequestIdRecent.class);
		}
		return DemoORMLiteRuntimeDaoReq;
	}

	private RuntimeExceptionDao<ProviderAcceptedList, Integer> getSimpleDataDaoPro() {
		if (DemoORMLiteRuntimeDaoPro == null) {
			DemoORMLiteRuntimeDaoPro = getRuntimeExceptionDao(ProviderAcceptedList.class);
		}
		return DemoORMLiteRuntimeDaoPro;
	}

	public List<CustomerDatabase> getAllEntries() {
		RuntimeExceptionDao<CustomerDatabase, Integer> dao = getSimpleDataDao();
		return dao.queryForAll();
	}

	public List<RequestIdRecent> getRequestId() {
		RuntimeExceptionDao<RequestIdRecent, Integer> dao = getSimpleDataDaoReq();
		return dao.queryForAll();
	}

	public List<ProviderAcceptedList> getProviderList(String id) {
		RuntimeExceptionDao<ProviderAcceptedList, Integer> dao = getSimpleDataDaoPro();
		QueryBuilder<ProviderAcceptedList, Integer> queryBuilder = dao
				.queryBuilder();
		List<ProviderAcceptedList> list = new ArrayList<ProviderAcceptedList>();
		try {
			queryBuilder.where().eq("id", id);
			list = queryBuilder.query();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public void insertProvider(ProviderAcceptedList acceptedList) {
		RuntimeExceptionDao<ProviderAcceptedList, Integer> dao = getSimpleDataDaoPro();
		dao.create(acceptedList);
	}

	public void updateCustomerAcceptStatus(ProviderAcceptedList database) {
		RuntimeExceptionDao<ProviderAcceptedList, Integer> dao = getSimpleDataDaoPro();
		UpdateBuilder<ProviderAcceptedList, Integer> updateBuilder = dao
				.updateBuilder();
		try {
			updateBuilder.updateColumnValue("customerAcceptedStatus",
					database.getCustomerAcceptedStatus());
			updateBuilder.where().eq("providername", database.getId());
			updateBuilder.update();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
