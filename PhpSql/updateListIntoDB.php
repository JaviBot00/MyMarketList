<?php

$host = "localhost";
$port = 3306;
$socket = "";
$user = "hotguy";
$password = "qwerty";
$dbname = $_POST['username'];

$con = new mysqli($host, $user, $password, $dbname, $port, $socket)
or die ('Could not connect to the database server' . mysqli_connect_error());

$arrayOfLists = $_POST['arrayOfLists'];
$list = explode(",", $arrayOfLists);
$cont = 0;
$result = "";

foreach ($list as $listsDatas) {
    if (!empty($listsDatas)) {
        $datas = explode(";", $listsDatas);

        $query = "UPDATE `$dbname`.`tList` set dRealized = ?, nPrice = ? WHERE sName = ?";


        if ($stmt = $con->prepare($query)) {
            $sNameValue = $datas[0];
            $dCreatedValue = $datas[1];
            $dRealizedValue = $datas[2];
            $nPriceValue = $datas[3];

            $stmt->bind_param("sds", $dRealizedValue, $nPriceValue, $sNameValue);
            $stmt->execute();
            $cont++;
            $stmt->close();
        }
    }
}

if ($cont === count($list)) {
    $result = "OK";
}


if ($result === "OK") {
    $con->commit();
} else {
    $con->rollback();
}

$con->close();

print($result);

?>