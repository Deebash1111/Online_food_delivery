package com.delivery.service;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

import com.delivery.bean.Delivery;
import com.delivery.bean.Order;
import com.delivery.dao.DeliveryDAO;
import com.delivery.dao.OrderDAO;
import com.delivery.util.DBUtil;
import com.delivery.util.DeliveryActiveException;
import com.delivery.util.OrderUnavailableException;
import com.delivery.util.ValidationException;

public class DeliveryService {

    private OrderDAO orderDAO = new OrderDAO();
    private DeliveryDAO deliveryDAO = new DeliveryDAO();

    // PLACE NEW ORDER
    public boolean placeNewOrder(Order order) throws ValidationException {
        if (order == null || order.getOrderID() == null || order.getOrderID().isEmpty()
                || order.getTotalAmount() <= 0) {
            throw new ValidationException();
        }

        order.setStatus("PENDING");
        return orderDAO.insertOrder(order);
    }

    // VIEW ORDER DETAILS
    public Order viewOrderDetails(String orderID) throws ValidationException {
        if (orderID == null || orderID.isEmpty()) {
            throw new ValidationException();
        }
        return orderDAO.findOrder(orderID);
    }

    // VIEW ALL ORDERS
    public List<Order> viewAllOrders() {
        return orderDAO.viewAllOrders();
    }

    // ASSIGN DELIVERY (TRANSACTIONAL)
    public boolean assignDelivery(String orderID, String agentID, Date assignedDate)
            throws ValidationException, OrderUnavailableException {

        if (orderID == null || orderID.isEmpty() || agentID == null || agentID.isEmpty()) {
            throw new ValidationException();
        }

        Order order = orderDAO.findOrder(orderID);

        if (order == null) {
            return false;
        }

        if (order.getStatus().equals("CANCELLED") || order.getStatus().equals("DELIVERED")) {
            throw new OrderUnavailableException();
        }

        boolean result = false;
        Connection conn = null;

        try {
            conn = DBUtil.getDBConnection();

            int deliveryID = deliveryDAO.generateDeliveryID();

            Delivery delivery = new Delivery();
            delivery.setDeliveryID(deliveryID);
            delivery.setOrderID(orderID);
            delivery.setAgentID(agentID);
            delivery.setAssignedDate(assignedDate);
            delivery.setStatus("ASSIGNED");

            deliveryDAO.recordDelivery(delivery);
            orderDAO.updateOrderStatus(orderID, "ASSIGNED");

            conn.commit();
            result = true;

        } catch (Exception e) {
            try {
                if (conn != null)
                    conn.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }

        return result;
    }

    // COMPLETE DELIVERY (TRANSACTIONAL)
    public boolean completeDelivery(int deliveryID, Date completedDate) throws ValidationException {

        if (deliveryID <= 0 || completedDate == null) {
            throw new ValidationException();
        }

        boolean result = false;
        Connection conn = null;

        try {
            conn = DBUtil.getDBConnection();

            deliveryDAO.completeDelivery(deliveryID, completedDate);

            // Update corresponding order
            // (simple assumption: one delivery → one order)
            // In real apps, we'd fetch orderID via delivery table
            // Here simplified for academic project

            conn.commit();
            result = true;

        } catch (Exception e) {
            try {
                if (conn != null)
                    conn.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }

        return result;
    }

    // CANCEL ORDER
    public boolean cancelOrder(String orderID)
            throws ValidationException, DeliveryActiveException {

        if (orderID == null || orderID.isEmpty()) {
            throw new ValidationException();
        }

        Order order = orderDAO.findOrder(orderID);

        if (order == null) {
            return false;
        }

        if (order.getStatus().equals("ASSIGNED")) {
            throw new DeliveryActiveException();
        }

        return orderDAO.updateOrderStatus(orderID, "CANCELLED");
    }
}
