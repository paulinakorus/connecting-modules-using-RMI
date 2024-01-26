package org.example.service.model.tables;

import org.example.service.model.Product;

import javax.swing.table.AbstractTableModel;
import java.util.Comparator;
import java.util.List;

public class ProductsTable extends AbstractTableModel {
    private final String[] COLUMNS = new String[]{"Id", "Name", "Product status", "Status at seller"};
    private List<Product> productList;

    public ProductsTable(List <Product> productList){
        this.productList = productList.stream().sorted(Comparator.comparing(c -> c.getId())).toList();
    }

    @Override
    public int getRowCount() {
        return productList.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMNS.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Product product = productList.get(rowIndex);
        return switch (columnIndex){
            case 0 -> product.getId();
            case 1 -> product.getName();
            case 2 -> product.getProductStatus();
            case 3 -> product.getProductStatusAtSeller();
            default -> "-";
        };
    }
    @Override
    public String getColumnName(int column) {
        return COLUMNS[column];
    }
}