package com.example.Java2.utils;

import com.example.Java2.pojo.User;
import com.example.Java2.vo.RespBean;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class UserUtil {
    private static void createUser (int count) throws Exception{
        List<User> users = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            User user = new User();
            user.setId(15000000000L+i);
            user.setNickname("user"+i);
            user.setSlat("1a2b3c4d");
            user.setPassword(MD5Util.inputPassToDBPss("123456",user.getSlat()));
            user.setLoginCount(1);
            user.setLastLoginDate(new Date());
            users.add(user);
        }
        System.out.println("create user");
//        Connection conn = getConn();
//        String sql = "insert into t_user(id,nickname,password,slat,register_date,login_count) values(?,?,?,?,?,?)";
//        PreparedStatement pstmt = conn.pre pareStatement(sql);
//        for (int i = 0; i < users.size(); i++) {
//            User user = users.get(i);
//            pstmt.setInt(6,user.getLogin_count());
//            pstmt.setString(2,user.getNickname());
//            pstmt.setString(3,user.getPassword());
//            pstmt.setString(4,user.getSlat());
//            pstmt.setTimestamp(5,new Timestamp(user.getRegister_date().getTime()));
//            pstmt.setLong(1,user.getId());
//            pstmt.addBatch();
//        }
//        pstmt.executeBatch();
//        pstmt.clearParameters();
//        conn.close();
//        System.out.println("insert to db");


        //生成登录的userTicket
        String urlString = "http://localhost:8005/login";
        File file = new File("C:\\Users\\86152\\Desktop\\config.txt");
        if (file.exists()) {
            boolean delete = file.delete();
        }
        boolean newFile = file.createNewFile();

        RandomAccessFile raf = new RandomAccessFile(file,"rw");
        raf.seek(0);
        for (User user : users) {
            URL url = new URL(urlString);
            HttpURLConnection co = (HttpURLConnection) url.openConnection();
            co.setRequestMethod("POST");
            co.setDoOutput(true);
            OutputStream out = co.getOutputStream();
            String params = "mobile="+user.getId()+"&password="+MD5Util.inputPassToFromPass("123456");
            out.write(params.getBytes());
            out.flush();
            InputStream inputStream = co.getInputStream();
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            byte[] buff = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buff)) >= 0) {
                bout.write(buff,0,len);
            }
            inputStream.close();
            bout.close();
            String response = bout.toString();
            ObjectMapper mapper = new ObjectMapper();
            RespBean respBean = mapper.readValue(response,RespBean.class);
            String userTicket = ((String) respBean.getObj());
            System.out.println("create userTicket:"+user.getId()+userTicket);

            String row = user.getId()+","+userTicket;
            raf.seek(raf.length());
            raf.write(row.getBytes());
            raf.write("\r\n".getBytes());
            System.out.println("write to file:"+user.getId());
        }
        raf.close();
        System.out.println("over!!!");
    }


    private static Connection getConn ( ) throws Exception {
        String url = "jdbc:mysql://49.234.138.59:3306/seckill";
        String username = "seckill";
        String password = "my159357";
        String driver = "com.mysql.cj.jdbc.Driver";
        Class.forName(driver);
        return DriverManager.getConnection(url,username,password);

    }

    public static void main (String[] args) throws Exception {
        createUser(30);
    }
}
