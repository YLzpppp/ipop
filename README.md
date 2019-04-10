# <font size = 8>ipop communicating app</font>
<font size = 3 color = #1144bb > __author__ : jasoncareter</font>

<font size=3 color=#1144bb>__environment__ : Tomcat_Apache9.0, Android_Studio3.3 , gradle5.0 ,java8.0 </font>

<font size=3 color=#1144bb> __description__ : once the client connected to the server , it will record the ip_address of the client ,username and password . Of course it will store to database Mysql for later verifying and so on </font>
  
---
<font size = 6> Part of webServer </font>

|||
| --------------- |:--------------- |
| DataBase | Mysql8.0 |
| Username | root |
| Password |  |
| Dbname | idatabase |
| Url| idbc:mysql://localhost/idatabase?characterEncoding=utf8&useSSL=false&serverTimeZone=Asia/ShangHai
|||

Accounts_table_structure :

| Field |Type|Null|Key|Default|Extra|  
| ------|:------------:|-----|------|------|------|
| id|int(11)|NO|PRI|NULL|auto_increment|
|username|varchar(20)|NO||NULL||
|password|varchar(20)|NO||NULL||
|ipaddress|varchar(20)|YES||NULL||
|status|varchar(20)|YES||0||

status : if client's online or offline , 0 for offline ,1 for online
ipaddress : client's ip

  
__Tree of Server__   
-ipopServer   
\-\-src  
\-\-\-\-\-databaseInfo.properties  
\-\-\-\-\-com_lzp  
\-\-\-\-\-\-\-\-\-\-Registerserver  
\-\-\-\-\-\-\-\-\-\-Loginserver  
\-\-\-\-\-\-\-\-\-\-LoginOut  
\-\-\-\-\-\-\-\-\-\-TargetClientAddress  
\-\-\-\-\-\-\-\-\-\-AllPorts  
\-\-\-\-\-\-\-\-\-\-ipaddress  
\-\-\-\-\-\-\-\-\-\-TargetFriend  
\-\-\-\-\-\-\-\-\-\-TargetPort  
\-\-\-\-\-\-\-\-\-\-TargetStatus  
\-\-\-\-\-com_lzp_idb    
\-\-\-\-\-\-\-\-\-\-Dao  


  **<font color=#ee0000>! ! ! ! !</font>** __<font color=#ee0000>You should configure your own server with your own host</font>__  

Service : Catalina  {localhost , www.lzppp.cn}  


______especially : www.lzppp.cn 's dns resolution is provided by Aliyun(allocated dns server : dns7.hichina.com, dns8.hichina.com , target_ip : 192.168.1.222(my local static ip address))

> 1 Registerserver_url : http://www.lzppp.cn/ipopServer/server/Registerserver  
>
> 2 Loginserver_url : http://www.lzppp.cn/ipopServer/server/Loginserver
>
> 3 LoginOut_url : http://www.lzppp.cn/ipopServer/server/Loginout
>
> 4 TargetClientAddress : http://www.lzppp.cn/ipopServer/server/Targetclientaddress

>servlet 1 is used for registering user's information ,including username ,password ,if client's online and so on. It will finally store to Database.

>servlet 2 is used for logining , check that whether the user's infomation matched at least one record in the database

>servlet 3 is used for switch the client's status to offline mode ,so the messages sent to this client will be stored in our specific server for temporary .
>><font color=#ee0000>! ! ! however , the offline server for storing messages that sent to those offline is not finished yet , and i'm not intend to finish this module in the future</font> 

>servlet 4 is used for fetching the target client's ip address. apply in udp/tcp communication

---
<font size=6> Part of Android_Client </font>

At first time Users installed this app , they need to register a account for further usage.
The register Button near the Login Button is used for communicating with the server, once 
user clicked it ,it will carry on the information from the EditInputLayout and store it to
the remote database if the account's Doppelganger dosen't exist in the server's database;

After that , user can login in with the account registered before .

users can add anyone that have registered in the server.

the communication between two client is based on **UDP protocal**

I havan't designed this app for huge custome , cause the offline messages that users sended 
were supposed to stored in the remote server until the specific clients who were the receiver
logged on the server. Once this app's users group increased several times ,hundred times ,thound
times even more , my computer won't be able to handle these .

In summary , this is a light social intercourse app . Oh no , it cannot even be called chat app ,
it's just a little demo for testing kinds of network protocal.

> demonstrate down here

![one](https://github.com/YLzpppp/ipop/blob/master/one.gif)
