<?php

$host = "localhost";
$port = 3306;
$socket = "";
$user = "hotguy";
$password = "qwerty";
$dbname = $_POST['username'];

$con = new mysqli($host, $user, $password, $dbname, $port, $socket)
or die ('Could not connect to the database server' . mysqli_connect_error());

$pass = $_POST['pass'];

$query = "SELECT * FROM `$dbname`.`tUser` WHERE cUser = '$dbname' AND cPassword = '$pass'";

if ($stmt = $con->prepare($query)) {
    $stmt->execute();
    $stmt->bind_result($field1, $field2, $field3, $field4);
    if ($stmt->fetch()) {
        printf("%s,%s,%s,%s", $field1, $field2, $field3, $field4);
    }
    $stmt->close();
}

$con->close();

?>
