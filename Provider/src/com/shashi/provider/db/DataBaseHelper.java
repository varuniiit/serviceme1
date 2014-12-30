package com.shashi.provider.db;

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
	private static final String DATABASE_NAME = "Provider";
	private RuntimeExceptionDao<ProviderDatabase, Integer> DemoORMLiteRuntimeDao = null;

	public DataBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
		try {
			Log.i(DataBaseHelper.class.getName(), "onCreate");
			TableUtils.createTable(connectionSource, ProviderDatabase.class);
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
					.dropTable(connectionSource, ProviderDatabase.class, true);
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

	public void insert(ProviderDatabase database) {
		RuntimeExceptionDao<ProviderDatabase, Integer> dao = getSimpleDataDao();
		dao.create(database);
	}

	public RuntimeExceptionDao<ProviderDatabase, Integer> getSimpleDataDao() {
		if (DemoORMLiteRuntimeDao == null) {
			DemoORMLiteRuntimeDao = getRuntimeExceptionDao(ProviderDatabase.class);
		}
		return DemoORMLiteRuntimeDao;
	}

	public List<ProviderDatabase> getAllEntries() {
		RuntimeExceptionDao<ProviderDatabase, Integer> dao = getSimpleDataDao();
		return dao.queryForAll();
	}

	public List<ProviderDatabase> getProviderNonAccepted() {
		RuntimeExceptionDao<ProviderDatabase, Integer> dao = getSimpleDataDao();
		QueryBuilder<ProviderDatabase, Integer> queryBuilder = dao
				.queryBuilder();
		List<ProviderDatabase> list = new ArrayList<ProviderDatabase>();
		try {
			queryBuilder.where().eq("providerAcceptedStatus", "false");
			list = queryBuilder.query();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<ProviderDatabase> getProviderAccepted() {
		RuntimeExceptionDao<ProviderDatabase, Integer> dao = getSimpleDataDao();
		QueryBuilder<ProviderDatabase, Integer> queryBuilder = dao
				.queryBuilder();
		List<ProviderDatabase> list = new ArrayList<ProviderDatabase>();
		try {
			queryBuilder.where().eq("providerAcceptedStatus", "true");
			list = queryBuilder.query();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<ProviderDatabase> getCustomerNonAccepted() {
		RuntimeExceptionDao<ProviderDatabase, Integer> dao = getSimpleDataDao();
		QueryBuilder<ProviderDatabase, Integer> queryBuilder = dao
				.queryBuilder();
		List<ProviderDatabase> list = new ArrayList<ProviderDatabase>();
		try {
			queryBuilder.where().eq("customerAcceptedStatus", "false");
			list = queryBuilder.query();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<ProviderDatabase> getCustomerAccepted() {
		RuntimeExceptionDao<ProviderDatabase, Integer> dao = getSimpleDataDao();
		QueryBuilder<ProviderDatabase, Integer> queryBuilder = dao
				.queryBuilder();
		List<ProviderDatabase> list = new ArrayList<ProviderDatabase>();
		try {
			queryBuilder.where().eq("customerAcceptedStatus", "true");
			list = queryBuilder.query();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	public List<ProviderDatabase> getCustomerByRequestId(String requestId) {
		RuntimeExceptionDao<ProviderDatabase, Integer> dao = getSimpleDataDao();
		QueryBuilder<ProviderDatabase, Integer> queryBuilder = dao
				.queryBuilder();
		List<ProviderDatabase> list = new ArrayList<ProviderDatabase>();
		try {
			queryBuilder.where().eq("requestId", requestId);
			list = queryBuilder.query();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	public void updateProviderStatus(ProviderDatabase database) {
		RuntimeExceptionDao<ProviderDatabase, Integer> dao = getSimpleDataDao();
		UpdateBuilder<ProviderDatabase, Integer> updateBuilder = dao
				.updateBuilder();
		try {
			updateBuilder.updateColumnValue("providerAcceptedStatus", "true");
			updateBuilder.where().eq("id", database.getId());
			updateBuilder.update();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateCustomerStatus(ProviderDatabase database) {
		RuntimeExceptionDao<ProviderDatabase, Integer> dao = getSimpleDataDao();
		UpdateBuilder<ProviderDatabase, Integer> updateBuilder = dao
				.updateBuilder();
		try {
			updateBuilder.updateColumnValue("customerAcceptedStatus",
					database.getCustomerAcceptedStatus());
			updateBuilder.where().eq("requestId", database.getRequestId());
			updateBuilder.update();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
