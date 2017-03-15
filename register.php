<?php

require "init.php";
$guar_imei = "123456";
$guar_email = "krmacek@oakland.edu";
$gaur_password = "krmacek";
$guar_first_name = "Kevin";
$guar_last_name = "Kevin";
$guar_street_address = "1234 Textblock Lane";
$guar_zip = "12345";

$sql_query = "insert into guardian values('$guar_imei','guar_email','guar_password','guar_first__name','guar_last_name','guar_street_adress','guar_zip');";

if(mysqli_query($con,$sql_query))
{
echo "<h3> Data Insertion Success...</h3>";
}
else
{
echo "Data insertion error..".mysqli_error($con);
} 

?>