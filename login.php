<?php

require "init.php";
$user_name = $_POST["user_name"];
$user_pass = $_POST["password"];
$mysql_qry = "select * from guardian where guar_email like '$user_name' and guar_password like '$user_pass';";
$result = mysqli_query($con,$mysql_qry);
if(mysqli_num_rows($result) > 0) {
echo "login success";
}
else {
echo "login not success";
}

?>