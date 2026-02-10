package com.delivery.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.delivery.bean.Order;
import com.delivery.util.DBUtil;

public class OrderDAO {

    public Order findOrder(String orderID) {
        Order order = null;

        try {
            Connection conn = DBUtil.getDBConnection();

            String sql = "SELECT * FROM ORDER_TBL WHERE ORDER_ID = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, orderID);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                order = new Order();
                order.setOrderID(rs.getString("ORDER_ID"));
                order.setCustomerName(rs.getString("CUSTOMER_NAME"));
                order.setCustomerPhone(rs.getString("CUSTOMER_PHONE"));
                order.setItemsSummary(rs.getString("ITEMS_SUMMARY"));
                order.setTotalAmount(rs.getDouble("TOTAL_AMOUNT"));
                order.setOrderDate(rs.getDate("ORDER_DATE"));
                order.setStatus(rs.getString("STATUS"));
            }

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return order;
    }

    public List<Order> viewAllOrders() {
        List<Order> list = new ArrayList<>();

        try {
            Connection conn = DBUtil.getDBConnection();

            String sql = "SELECT * FROM ORDER_TBL";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                Order order = new Order();
                order.setOrderID(rs.getString("ORDER_ID"));
                order.setCustomerName(rs.getString("CUSTOMER_NAME"));
                order.setCustomerPhone(rs.getString("CUSTOMER_PHONE"));
                order.setItemsSummary(rs.getString("ITEMS_SUMMARY"));
                order.setTotalAmount(rs.getDouble("TOTAL_AMOUNT"));
                order.setOrderDate(rs.getDate("ORDER_DATE"));
                order.setStatus(rs.getString("STATUS"));

                list.add(order);
            }

            rs.close();
            st.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean insertOrder(Order order) {
        boolean result = false;

        try {
            Connection conn = DBUtil.getDBConnection();

            String sql = "INSERT INTO ORDER_TBL VALUES (?,?,?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, order.getOrderID());
            ps.setString(2, order.getCustomerName());
            ps.setString(3, order.getCustomerPhone());
            ps.setString(4, order.getItemsSummary());
            ps.setDouble(5, order.getTotalAmount());
            ps.setDate(6, new java.sql.Date(order.getOrderDate().getTime()));
            ps.setString(7, order.getStatus());

            int rows = ps.executeUpdate();
            conn.commit();

            if (rows > 0)
                result = true;

            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public boolean updateOrderStatus(String orderID, String status) {
        boolean result = false;

        try {
            Connection conn = DBUtil.getDBConnection();

            String sql = "UPDATE ORDER_TBL SET STATUS = ? WHERE ORDER_ID = ?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, status);
            ps.setString(2, orderID);

            int rows = ps.executeUpdate();
            conn.commit();

            if (rows > 0)
                result = true;

            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public boolean deleteOrder(String orderID) {
        boolean result = false;

        try {
            Connection conn = DBUtil.getDBConnection();

            String sql = "DELETE FROM ORDER_TBL WHERE ORDER_ID = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, orderID);

            int rows = ps.executeUpdate();
            conn.commit();

            if (rows > 0)
                result = true;

            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
