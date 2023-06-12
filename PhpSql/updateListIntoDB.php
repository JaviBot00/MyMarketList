<?php

$host = "localhost";
$port = 3306;
$socket = "";
$user = "hotguy";
$password = "qwerty";
$dbname = $_POST['username'];

$con = new mysqli($host, $user, $password, $dbname, $port, $socket)
or die ('Could not connect to the database server' . mysqli_connect_error());

$list2edit = $_POST['list2edit'];
$datas = explode(",", $list2edit);
$result1 = "";

$query1 = "UPDATE `$dbname`.`tList` (sName, dCreated, dRealized, nPrice) VALUES (?, ?, ?, ?)";

if ($stmt1 = $con->prepare($query)) {
    $sNameValue = $datas[0];
    $dCreatedValue = $datas[1];
    $dRealizedValue = $datas[2];
    $nPriceValue = $datas[3];

    $stmt1->bind_param("sssd", $sNameValue, $dCreatedValue, $dRealizedValue, $nPriceValue);
    $stmt1->execute();
    $stmt1->close();
}


if ($result1 === "OK") {
    $con->commit();
} else {
    $con->rollback();
}

$con->close();

print($result1 . "," . $result2);

?>