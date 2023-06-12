<?php

$host = "localhost";
$port = 3306;
$socket = "";
$user = "hotguy";
$password = "qwerty";
$dbname = $_POST['username'];

$con = new mysqli($host, $user, $password, $dbname, $port, $socket)
or die ('Could not connect to the database server' . mysqli_connect_error());

$query = "SELECT * FROM `$dbname`.`tList`"; 

if ($stmt = $con->prepare($query)) {
    $stmt->execute();
    $stmt->bind_result($field1, $field2, $field3, $field4, $field5);
    while ($stmt->fetch()) {
        printf("%s,%s,%s,%s,%s;", $field1, $field2, $field3, $field4, $field5);
    }
    $stmt->close();
}

$con->close();

?>