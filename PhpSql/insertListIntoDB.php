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
$cont1 = 0;
$result1 = "";

foreach ($list as $listsDatas) {
    if (!empty($listsDatas)) {
        $datas1 = explode(";", $listsDatas);

        $query1 = "INSERT INTO `$dbname`.`tList` (sName, dCreated, dRealized, nPrice) VALUES (?, ?, ?, ?)";

        if ($stmt1 = $con->prepare($query1)) {
            $sNameValue = trim($datas1[0]);
            $dCreatedValue = $datas1[1];
            $dRealizedValue = ($datas1[2] === 'null' ? null : $datas1[2]);
            $nPriceValue = ($datas1[3] === 'null' ? null : $datas1[3]);

            $stmt1->bind_param("sssd", $sNameValue, $dCreatedValue, $dRealizedValue, $nPriceValue);
            $stmt1->execute();
            $cont1++;
            $stmt1->close();
        }
    }
}

if ($cont1 === count($list)) {
    $result1 = "OK";
}


$arrayOfProducts = $_POST['arrayOfProducts'];
$products = explode(",", $arrayOfProducts);
$cont2 = 0;
$result2 = "";

foreach ($products as $productsDatas) {
    if (!empty($productsDatas)) {
        $datas2 = explode(";", $productsDatas);
        reset($datas2);

        $query2 = "INSERT INTO `$dbname`.`tProducts` (sName, nIdType) VALUES (?, ?)";

        if ($stmt2 = $con->prepare($query2)) {
            $sNameValue = trim($datas2[0]);
            $nIdTypeValue = $datas2[1];

            $stmt2->bind_param("si", $sNameValue, $nIdTypeValue);
            $stmt2->execute();
            $cont2++;
            $stmt2->close();
        }
        // error_log($arrayOfProducts);
    }
}

if ($cont2 === count($products)) {
    $result2 = "OK";
}

if ($result1 === "OK" && $result2 === "OK") {
    $con->commit();
} else {
    $con->rollback();
}

$con->close();

print($result1 . "," . $result2);

?>