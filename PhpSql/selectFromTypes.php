<?php

$host = "localhost";
$port = 3306;
$socket = "";
$user = "hotguy";
$password = "qwerty";
$dbname = "BDMyMarketList";

$con = new mysqli($host, $user, $password, $dbname, $port, $socket)
or die('Could not connect to the database server' . mysqli_connect_error());

$query = "SELECT * FROM `tTypes`";

if ($stmt = $con->prepare($query)) {
    $stmt->execute();
    $stmt->bind_result($field1, $field2);
    while ($stmt->fetch()) {
        printf("%s,%s;", $field1, $field2);
    }
    $stmt->close();
}

$con->close();

?>