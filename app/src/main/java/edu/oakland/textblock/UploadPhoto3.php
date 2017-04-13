<?php
if ($_SERVER['REQUEST_METHOD'] == "POST") {
        // to get the file saved
        // to make sure it is an valid request.
//      if (isset ( $_POST ['filename'] )) {
                $photoName = $_POST ['filename'];
                echo "it is from the request parameter: ".$photoName."\n";
                $savedPath="photos/".$photoName;
                $photo_temp_name_on_sever=$_FILES['photo']['tmp_name'];
                // $photo_name_on_client=$_FILES['photo']['name'];
                $photo_type=$_FILES['photo']['type'];
                $photo_extension=pathinfo($photoName)['extension'];
                echo "Temporary name on server: ".$photo_temp_name_on_sever."\n";
                echo "Original name on client: ".$photoName."\n";
                echo "Type of file: ".$photo_type."\n";
                echo "Extentsion: ".$photo_extension."\n";
                $isUploadedSuccessfully = move_uploaded_file ( $_FILES ['photo'] ['tmp_name'], $savedPath);
                if($isUploadedSuccessfully){
                        echo " Image has been received.\n";
                }else{
                        echo " Image failed to save.\n";
                }

                if(isset($_POST['IMEI'])){
                        $IMEI=$_POST['IMEI'];
                        echo $_POST['IMEI'];
                }

                // to update database about photos
                $servername="localhost";
                $username="root";
                $password="TextblocK1!";
                $dabname="TEXTBLOCK_DB";

                $photo_datetime=date_create_from_format("Ymd_His",substr($photoName,4));
                $photo_path=$savedPath;
                $photo_url="52.41.167.226/".$savedPath;
                $sql="INSERT INTO PHOTO("IMEI","PHOTO_DATETIME","PHOTO_PATH","PHOTO_URL") VALUES($IMEI,$photo_datetime,$photo_path,$photo_url)";
                // create a connection
                $connection=new mysqli($servername,$username,$password,$dabname);
                if($connection->connect_error){
                        die("connection failed: ".$connection->connect_error);
                }

                if($connection->query($sql)=== true){
                        echo "update successfully";
                }else{
                        echo "update failed.";
                }

                $connection->close();
//      }else{
//              echo "Invalid request broke in.\n"
//      }
}elseif($_SERVER['REQUEST_METHOD'] == "GET"){
        echo "Get request.\n";
}else{
        echo "Unknow request.\n";
}
//echo $_server;
?>
