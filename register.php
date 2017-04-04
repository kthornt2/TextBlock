<?php
require "init.php";

$guar_imei = $_POST["guar_imei"];
$guar_imei = $_POST["guar_imei"];
$guar_imei = $_POST["guar_imei"];
$guar_imei = $_POST["guar_imei"];
$guar_imei = $_POST["guar_imei"];
$guar_imei = $_POST["guar_imei"];
$guar_imei = $_POST["guar_imei"];

$mysql_qry = "insert into guardian (guar_imei, guar_email, guar_password, guar_first_name,
guar_last_name, guar_street_address, guar_zip) values ('$guar_imei','$guar_email','$guar_password',
'$guar_first_name','$guar_last_name','$guar_street_address','$guar_street_address','$guar_zip')";

if($con->query($mysql_qry) === TRUE {
echo "Insert Successful";
}
else {
echo "Error: " . $mysql_qry . "<br>" . $con->error;
}
$con->close();
?>