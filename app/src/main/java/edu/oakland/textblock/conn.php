<?php
$db_name = "txtblck";
$mysql_username = "root";
$mysql_password = "aligolbaji1";
$server_name = "localhost";
$conn = mysqli_connect($server_name, $mysql_username, $mysql_password, $db_name);
if($conn) {
echo "connection successful";
}
else {
echo " connection not successful";
}
?>