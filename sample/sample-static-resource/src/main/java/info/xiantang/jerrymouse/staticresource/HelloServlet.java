package info.xiantang.jerrymouse.staticresource;

import info.xiantang.jerrymouse.http.servlet.Request;
import info.xiantang.jerrymouse.http.servlet.Response;
import info.xiantang.jerrymouse.http.servlet.Servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;

class HelloServlet implements Servlet {
    @Override
    public void service(Request request, Response response) throws IOException {
        OutputStream responseOutputStream = response.getResponseOutputStream();
        responseOutputStream.write("hello\n".getBytes());
        Calendar c = Calendar.getInstance();//可以对每个时间域单独修改   对时间进行加减操作等
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int date = c.get(Calendar.DATE);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);
        String current = year + "/" + month + "/" + date + " " + hour + ":" + minute + ":" + second;
        responseOutputStream.write(current.getBytes());
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        return obj instanceof HelloServlet;
    }


}