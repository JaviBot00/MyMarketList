<?php

$host = "localhost";
$port = 3306;
$socket = "";
$user = "hotguy";
$password = "qwerty";
$dbname = "";

$con = new mysqli($host, $user, $password, $dbname, $port, $socket)
or die('Could not connect to the database server' . mysqli_connect_error());

$username = $_POST['username'];
$pass = $_POST['pass'];
$email = $_POST['email'];
$imgProfileBase64 = $_POST['imgProfile'];


// En ByteArray
$imgProfileByteArray = base64_decode($imgProfileBase64);
// $imgProfileFilename = uniqid($username . "_") . ".png";
$imgProfileFilename = $username . ".png";
$imgProfilePath = "/photos/" . $imgProfileFilename;
file_put_contents("." . $imgProfilePath, $imgProfileByteArray);

// En Base64
// $imgProfileByteArray = file_get_contents($_FILES['imgProfile']['image.png']);
// $imgProfileFilename = uniqid($username . "_") . ".png";
// $imgProfilePath = "./photos/" . $imgProfileFilename;
// file_put_contents($imgProfilePath, $imgProfileByteArray);


$query1 = "CREATE DATABASE $username";
$result1 = "";

if ($con->query($query1) === TRUE) {
    $result1 = "OK";
}


$query2 = "CREATE TABLE `$username`.`tUser` (
    cUser VARCHAR(50) NOT NULL,
    cPassword VARCHAR(50) NOT NULL,
    cEmail VARCHAR(50) NOT NULL,
    cImgProfilePath VARCHAR(255) NOT NULL,
    PRIMARY KEY (cUser)
)";
$result2 = "";

if ($con->query($query2) === TRUE) {
    $result2 = "OK";
}


$query3 = "INSERT INTO `$username`.`tUser` (cUser, cPassword, cEmail, cImgProfilePath) VALUES (?, ?, ?, ?)";
$result3 = "";

if ($stmt3 = $con->prepare($query3)) {
    $stmt3->bind_param("ssss", $username, $pass, $email, $imgProfilePath);
    $stmt3->execute();
    $result3 = "OK";
    $stmt3->close();
}


$query4 = "CREATE TABLE `$username`.`tList` (
    nId INT NOT NULL AUTO_INCREMENT,
    sName VARCHAR(255) NOT NULL,
    dCreated DATE NOT NULL,
    dRealized DATE NULL,
    nPrice DECIMAL(10, 2) NULL,
    PRIMARY KEY (nID, sName)
)";
$result4 = "";

if ($con->query($query4) === TRUE) {
    $result4 = "OK";
}


$query5 = "CREATE TABLE `$username`.`tProducts` (
    nId INT NOT NULL AUTO_INCREMENT,
    sName VARCHAR(255) NOT NULL,
    nIdType INT NOT NULL,
    PRIMARY KEY (nID),
    FOREIGN KEY(nIdType) REFERENCES $username.tList(nId)
)";
$result5 = "";

if ($con->query($query5) === TRUE) {
    $result5 = "OK";
}


if ($result1 === "OK" && $result2 === "OK" && $result3 === "OK" && $result4 === "OK" && $result5 === "OK") {
    $con->commit();
} else {
    $con->rollback();
}

$con->close();

print($result1 . "," . $result2 . "," . $result3 . "," . $result4 . "," . $result5);

?>