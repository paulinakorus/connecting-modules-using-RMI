package org.example.service.model.tables;

import org.example.shop.Item;

import javax.swing.table.AbstractTableModel;
import java.util.Comparator;
import java.util.List;

public class ProductsTable extends AbstractTableModel {
    private final String[] COLUMNS = new String[]{"Description", "Quantity"};
    private List<Item> itemList;

    public ProductsTable(List<Item> productList){
        this.itemList = productList.stream().sorted(Comparator.comparing(c -> c.getDescription())).toList();
    }

    @Override
    public int getRowCount() {
        return itemList.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMNS.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Item item = itemList.get(rowIndex);
        return switch (columnIndex){
            case 0 -> item.getDescription();
            case 1 -> item.getQuantity();
            default -> "-";
        };
    }
    @Override
    public String getColumnName(int column) {
        return COLUMNS[column];
    }
}