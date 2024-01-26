package org.example.service.model.tables;

import org.example.service.model.OrderOld;

import javax.swing.table.AbstractTableModel;
import java.util.Comparator;
import java.util.List;

public class OrderTable extends AbstractTableModel {
    private final String[] COLUMNS = new String[]{"Id", "Status", "Products amount"};
    private List<OrderOld> orderList;

    public OrderTable(List<OrderOld> orderList){
        this.orderList = orderList.stream().sorted(Comparator.comparing(c -> c.getOrderID())).toList();
    }

    @Override
    public int getRowCount() {
        return orderList.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMNS.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(rowIndex == -1) {
            return "-";
        }
        OrderOld order = orderList.get(rowIndex);
        if(order == null) {
            return "-";
        }
        return switch (columnIndex){
            case 0 -> order.getOrderID();
            case 1 -> order.getOrderStatus();
            case 2 -> order.getProductList().size();
            default -> "-";
        };
    }
    @Override
    public String getColumnName(int column) {
        return COLUMNS[column];
    }
}