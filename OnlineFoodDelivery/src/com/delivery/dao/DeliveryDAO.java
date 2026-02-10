package com.delivery.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import com.delivery.bean.Delivery;
import com.delivery.util.DBUtil;

public class DeliveryDAO {


    public int generateDeliveryID() {
        int id = 0;

        try {
            Connection conn = DBUtil.getDBConnection();
            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery("SELECT NVL(MAX(DELIVERY_ID),90000) + 1 FROM DELIVERY_TBL");

            if (rs.next()) {
                id = rs.getInt(1);
            }

            rs.close();
            st.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return id;
    }

    public boolean recordDelivery(Delivery delivery) {
        boolean result = false;

        try {
            Connection conn = DBUtil.getDBConnection();

            String sql = "INSERT INTO DELIVERY_TBL VALUES (?,?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, delivery.getDeliveryID());
            ps.setString(2, delivery.getOrderID());
            ps.setString(3, delivery.getAgentID());
            ps.setDate(4, new java.sql.Date(delivery.getAssignedDate().getTime()));
            ps.setDate(5, null);
            ps.setString(6, delivery.getStatus());

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

    public boolean completeDelivery(int deliveryID, Date completedDate) {
        boolean result = false;

        try {
            Connection conn = DBUtil.getDBConnection();

            String sql = "UPDATE DELIVERY_TBL SET COMPLETED_DATE = ?, STATUS = 'COMPLETED' WHERE DELIVERY_ID = ?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setDate(1, new java.sql.Date(completedDate.getTime()));
            ps.setInt(2, deliveryID);

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

    public boolean cancelDelivery(int deliveryID) {
        boolean result = false;

        try {
            Connection conn = DBUtil.getDBConnection();

            String sql = "UPDATE DELIVERY_TBL SET STATUS = 'CANCELLED' WHERE DELIVERY_ID = ?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, deliveryID);

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
