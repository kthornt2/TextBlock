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
                $photo_extension=pathinfo($photo_name_on_client)['extension'];
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
