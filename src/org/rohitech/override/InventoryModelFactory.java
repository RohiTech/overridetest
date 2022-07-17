package org.rohitech.override;

import java.sql.ResultSet;

import org.adempiere.base.IModelFactory;
import org.compiere.model.PO;
import org.compiere.util.Env;

public class InventoryModelFactory implements IModelFactory
{

	@Override
	public Class<?> getClass(String tableName) {
		// TODO Auto-generated method stub
		
		if(tableName.equals(MInventoryLineNEW.Table_Name))
			return MInventoryLineNEW.class;
		
		return null;
	}

	@Override
	public PO getPO(String tableName, int Record_ID, String trxName) {
		// TODO Auto-generated method stub
		
		if(tableName.equals(MInventoryLineNEW.Table_Name))
			return new MInventoryLineNEW(Env.getCtx(), Record_ID, trxName);
		
		return null;
	}

	@Override
	public PO getPO(String tableName, ResultSet rs, String trxName) {
		// TODO Auto-generated method stub
		
		if(tableName.equals(MInventoryLineNEW.Table_Name))
			return new MInventoryLineNEW(Env.getCtx(), rs, trxName);
		
		return null;
	}

}
