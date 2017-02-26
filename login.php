<?php
require "init.php";
$guar_first_name = "Kevin";
$guar_last_name = "Kevin";

$sql_query = "select first name from guardian where guar_first_name like '$guar_first_name' and guar_last_name like '$guar_last_name';";

$result = mysqli_guery($con,$sql_guery);

if(mysqli_num_rows($result) > 0)
{
$row = mysqli_fetch_assoc($result);
$name = $row["guar_first_name"];
echo "<h3>Hello welcome".$guar_first_name"</h3>";
}
else
{
echo "No info is available";
}


?>