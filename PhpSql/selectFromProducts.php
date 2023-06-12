<?php

$host = "localhost";
$port = 3306;
$socket = "";
$user = "hotguy";
$password = "qwerty";
$dbname = "BDMyMarketList";

$con = new mysqli($host, $user, $password, $dbname, $port, $socket)
or die ('Could not connect to the database server' . mysqli_connect_error());

$query = "SELECT * FROM `tProducts`"; 

if ($stmt = $con->prepare($query)) {
    $stmt->execute();
    $stmt->bind_result($field1, $field2, $field3);
    while ($stmt->fetch()) {
        printf("%s,%s,%s;", $field1, $field2, $field3);
    }
    $stmt->close();
}

$con->close();

?>