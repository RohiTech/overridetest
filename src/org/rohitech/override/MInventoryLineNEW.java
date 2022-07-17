package org.rohitech.override;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.model.MInventory;
import org.compiere.model.MInventoryLine;
import org.compiere.util.DB;

public class MInventoryLineNEW extends MInventoryLine
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8929173544333236604L;
	
	/**************************************************************************
	 * 	Default Constructor
	 *	@param ctx context
	 *	@param M_InventoryLine_ID line
	 *	@param trxName transaction
	 */
	public MInventoryLineNEW (Properties ctx, int M_InventoryLine_ID, String trxName)
	{
		super (ctx, M_InventoryLine_ID, trxName);
	}	//	MInventoryLine

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MInventoryLineNEW (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MInventoryLine

	/**
	 * 	Detail Constructor.
	 * 	Locator/Product/AttributeSetInstance must be unique
	 *	@param inventory parent
	 *	@param M_Locator_ID locator
	 *	@param M_Product_ID product
	 *	@param M_AttributeSetInstance_ID instance
	 *	@param QtyBook book value
	 *	@param QtyCount count value
	 *  @param QtyInternalUse internal use value 
	 */
	public MInventoryLineNEW (MInventory inventory, 
		int M_Locator_ID, int M_Product_ID, int M_AttributeSetInstance_ID,
		BigDecimal QtyBook, BigDecimal QtyCount, BigDecimal QtyInternalUse)
	{
		this (inventory.getCtx(), 0, inventory.get_TrxName());
	}	//	MInventoryLine

	public MInventoryLineNEW (MInventory inventory, 
			int M_Locator_ID, int M_Product_ID, int M_AttributeSetInstance_ID,
			BigDecimal QtyBook, BigDecimal QtyCount)
	{
		this(inventory, M_Locator_ID, M_Product_ID, M_AttributeSetInstance_ID, QtyBook, QtyCount, null);
	}
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true if can be saved
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		if(isInternalUseInventory() == true)
		{
			// Se trata de un registro de Salida de Inventario
			MInventory inv = MInventory.get(this.getM_Inventory_ID());
			
			// Obtener la cantidad disponible del producto
			
			StringBuilder sql = new StringBuilder();
			
			sql.append("SELECT * FROM ");
			sql.append("adempiere.bomqtyavailable(");
			sql.append(this.getM_Product_ID());
			sql.append(",");
			sql.append(inv.getM_Warehouse_ID());
			sql.append(",");
			sql.append(this.getM_Locator_ID());
			sql.append(");");
			
			System.out.println(sql.toString());
			
			int v_disponible = DB.getSQLValue(get_TrxName(), sql.toString());
			
			int v_cantidad = this.getQtyInternalUse().intValue();
			
			if(v_cantidad <= 0)
			{
				log.saveError("Error", "La cantidad de salida debe ser mayor a cero");
				return false;
			}
			else
			{
				if(v_cantidad > v_disponible)
				{
					log.saveError("Error", "Cantidad insuficiente para realizar la salida");
					return false;
				}
			}
		}
		else
		{
			// Se trata de un registro de Inventario Fisico
		}
		
		return true;
	}	//	beforeSave
}
