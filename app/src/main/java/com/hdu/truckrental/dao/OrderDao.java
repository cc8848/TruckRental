package com.hdu.truckrental.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hdu.truckrental.domain.Driver;
import com.hdu.truckrental.domain.Order;

import java.util.ArrayList;
import java.util.List;

import static com.hdu.truckrental.tools.Check.SUCCEED;
import static com.hdu.truckrental.tools.Check.checkOrder;
import static com.hdu.truckrental.tools.Check.checkOrderState;

/**
 * Created by Even on 2017/2/3.
 */

public class OrderDao {
    private MyDBHelper myDBHelper;
    private String tag = "OrderDao.class";
    private DriverDao driverDao;

    //init
    public OrderDao(Context context){
        myDBHelper = new MyDBHelper(context);
        driverDao = new DriverDao(context);
    }

    //add
    public Integer addOrder(Order order){
        //valid check
        Driver driver = driverDao.findDriverById(order.getFk_driver_id());
        Integer car_type = driver.getDriver_car_type();
        Integer state = checkOrder(order,car_type);
        if(state != SUCCEED){
            return state;
        }
        //把String类型的日期转换成datetime类型的

        SQLiteDatabase database = myDBHelper.getWritableDatabase();
        if(database.isOpen()){
            database.execSQL("insert into orders(" +
                    "order_number," +
                    "fk_user_id," +
                    "fk_driver_id," +
                    "order_departure," +
                    "order_destination," +
                    "order_remarks," +
                    "order_distance," +
                    "order_price," +
                    "order_state," +
                    "order_score," +
                    "order_date," +
                    "order_back," +
                    "order_carry," +
                    "order_followers," +
                    "order_car_type," +
                    "order_start_date) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                    new Object[]{
                            order.getOrder_number(),
                            order.getFk_user_id(),
                            order.getFk_driver_id(),
                            order.getOrder_departure(),
                            order.getOrder_destination(),
                            order.getOrder_remarks(),
                            order.getOrder_distance(),
                            order.getOrder_price(),
                            order.getOrder_state(),
                            order.getOrder_score(),
                            order.getOrder_date(),
                            order.getOrder_back(),
                            order.getOrder_carry(),
                            order.getOrder_followers(),
                            order.getOrder_car_type(),
                            order.getOrder_start_date()
                    });
            database.close();
        }
        return state;
    }

    //add order without driver_id
    public Integer addOrderNoDriverId(Order order){
        //valid check
        Integer car_type = order.getOrder_car_type();
        Integer state = checkOrder(order,car_type);
        if(state != SUCCEED){
            return state;
        }
        SQLiteDatabase database = myDBHelper.getWritableDatabase();
        if(database.isOpen()){
            database.execSQL("insert into orders(" +
                            "order_number," +
                            "fk_user_id," +
                            "fk_driver_id," +
                            "order_departure," +
                            "order_destination," +
                            "order_remarks," +
                            "order_distance," +
                            "order_price," +
                            "order_state," +
                            "order_score," +
                            "order_date," +
                            "order_back," +
                            "order_carry," +
                            "order_followers," +
                            "order_car_type," +
                            "order_start_date) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                    new Object[]{
                            order.getOrder_number(),
                            order.getFk_user_id(),
                            null,
                            order.getOrder_departure(),
                            order.getOrder_destination(),
                            order.getOrder_remarks(),
                            order.getOrder_distance(),
                            order.getOrder_price(),
                            order.getOrder_state(),
                            order.getOrder_score(),
                            order.getOrder_date(),
                            order.getOrder_back(),
                            order.getOrder_carry(),
                            order.getOrder_followers(),
                            order.getOrder_car_type(),
                            order.getOrder_start_date()
                    });
            database.close();
        }
        return state;
    }

    //get order
    private Order getOrder(Cursor cursor){
        Order order = new Order();

        Integer order_id = cursor.getInt(cursor.getColumnIndex("order_id"));
        String order_number=
                cursor.getString(cursor.getColumnIndex("order_number"));
        Integer fk_user_id= cursor.getInt(cursor.getColumnIndex("fk_user_id"));
        Integer fk_driver_id= cursor.getInt(cursor.getColumnIndex("fk_driver_id"));
        String order_departure=
                cursor.getString(cursor.getColumnIndex("order_departure"));
        String order_destination=
                cursor.getString(cursor.getColumnIndex("order_destination"));
        String order_remarks=
                cursor.getString(cursor.getColumnIndex("order_remarks"));
        float order_distance=
                cursor.getFloat(cursor.getColumnIndex("order_distance"));
        float order_price= cursor.getFloat(cursor.getColumnIndex("order_price"));
        Integer order_state= cursor.getInt(cursor.getColumnIndex("order_state"));
        Integer order_score= cursor.getInt(cursor.getColumnIndex("order_score"));
        String order_date= cursor.getString(cursor.getColumnIndex("order_date"));
        Integer order_back= cursor.getInt(cursor.getColumnIndex("order_back"));
        Integer order_carry= cursor.getInt(cursor.getColumnIndex("order_carry"));
        Integer order_followers=
                cursor.getInt(cursor.getColumnIndex("order_followers"));
        Integer order_car_type = cursor.getInt(cursor.getColumnIndex("order_car_type"));
        String order_start_date = cursor.getString(cursor.getColumnIndex("order_start_date"));

        order.setOrder_id(order_id);
        order.setOrder_number(order_number);
        order.setFk_driver_id(fk_driver_id);
        order.setFk_user_id(fk_user_id);
        order.setOrder_departure(order_departure);
        order.setOrder_destination(order_destination);
        order.setOrder_remarks(order_remarks);
        order.setOrder_distance(order_distance);
        order.setOrder_price(order_price);
        order.setOrder_state(order_state);
        order.setOrder_score(order_score);
        order.setOrder_date(order_date);
        order.setOrder_back(order_back);
        order.setOrder_carry(order_carry);
        order.setOrder_followers(order_followers);
        order.setOrder_car_type(order_car_type);
        order.setOrder_start_date(order_start_date);
        return order;
    }

    //find by id
    public Order findOrderById(Integer order_id){
        Order order = new Order();
        SQLiteDatabase database = myDBHelper.getWritableDatabase();
        if(database.isOpen()){
            Cursor cursor = database.rawQuery("select * from orders where order_id=?",
                    new String[]{order_id.toString()});
            if(cursor.moveToFirst()){
                order = getOrder(cursor);
            }
            cursor.close();
            database.close();
        }
        return order;
    }

    //find by driver id
    public List<Order> findOrderByDriverId(Integer driver_id){
        List<Order> orderList = new ArrayList<>();
        SQLiteDatabase database = myDBHelper.getWritableDatabase();
        if(database.isOpen()){
            Cursor cursor = database.rawQuery("select * from orders where fk_driver_id=?",
                    new String[]{driver_id.toString()});
            while(cursor.moveToNext()){
                orderList.add(getOrder(cursor));
            }
            cursor.close();
            database.close();
        }
        return orderList;
    }

    //find all orders
    public List<Order> findAllOrder(){
        List<Order> orderList = new ArrayList<>();
        SQLiteDatabase database = myDBHelper.getWritableDatabase();
        if(database.isOpen()){
            Cursor cursor = database.rawQuery("select * from orders", null);
            while(cursor.moveToNext()){
                orderList.add(getOrder(cursor));
            }
            cursor.close();
            database.close();
        }
        return orderList;
    }

    //update
    //update state
    public Integer updateOrderState(Integer order_id,Integer order_state){
        Integer state = checkOrderState(order_state);
        if(state != SUCCEED){
            return state;
        }
        SQLiteDatabase database = myDBHelper.getWritableDatabase();
        if(database.isOpen()) {
            database.execSQL("update orders set order_state=? where order_id=?",
                    new Object[]{order_state, order_id});
            database.close();
        }
        return state;
    }
    //update driver id
    public void updateFkDriverId(Integer order_id,Integer driver_id){
        SQLiteDatabase database = myDBHelper.getWritableDatabase();
        if(database.isOpen()) {
            database.execSQL("update orders set fk_driver_id=? where order_id=?",
                    new Object[]{driver_id, order_id});
            database.close();
        }
    }

    //delete
    public void deleteOrder(Integer order_id){
        SQLiteDatabase database = myDBHelper.getWritableDatabase();
        if(database.isOpen()){
            database.execSQL("delete from orders where order_id = ?",
                    new String[]{order_id.toString()});
            database.close();
        }
    }
}
