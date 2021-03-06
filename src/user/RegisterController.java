package user;

import model.DbUtil;
import utils.Encoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@WebServlet(name = "RegisterController", urlPatterns = "/user/userRegister")
public class RegisterController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        //前端验证：密码跟用户名都有时，才能发送请求


        //查找是否有重复用户，有则报错
        int userCount = DbUtil.selectToInt("select count(username) from user where username=\""
                + username + "\";", "count(username)");

        if (userCount > 0) {
            System.out.println("用户已存在");
            return;
        }

        //加密密码
        password = Encoder.encodeBase64(password);

        //将注册用户的信息插入数据库
        DbUtil.execute("insert into user values(0,\"" + username + "\",\"" + password + "\");");

        //用户创建成功后，为其创建文件表
        DbUtil.execute("create table " + username + "(id int auto_increment not null,filename varchar(50) not null," +
                "filesize bigint not null,time timestamp default current_timestamp,path varchar(100) not null,primary key(id));");

        //filesize long    bigint

        System.out.println("ok");
    }
}
