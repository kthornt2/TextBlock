
<?php
if($_SERVER['request_method']=="POST"){
if (!isset($_FILES['photo']['error']) || is_array($_FILES['photo']['error'])) {
        throw new RuntimeException('Invalid parameters.');
    }
}
if(isset($_POST['photo']){
$photoName=$_FILES['photo']['name'];    /* here it will throw exception */
$savedPath="photos/".$photoName;
echo "photo: ".$photoName;
$isSaved=move_uploaded_file($_FILES['photo']['tmp_name'],$savedPath);
if($isSaved){
echo $photoName." has been saved";
}else{
echo $photoName." has not been saved successfully";
}
}

}
