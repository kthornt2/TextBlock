<?php
require "conn.php";
$user_name = $_POST["user_name"];
$use_pass  = $_POST["password"];
$mysql_qry = "select * from employee_data where username like '$user_name' and password '$user_pass';";
$result = mysqli_query($conn ,$mysql_qry);
if(mysqli_num_rows($result) > 0){
echo "login success";
}
else {
echo "login not successful";
}

?>