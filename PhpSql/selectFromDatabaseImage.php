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

        // $imgProfile = file_get_contents($field4);
        // $imgProfileBase64 = base64_encode($imgProfile);

        $imgProfilePath = $field4;
        
        if (file_exists($imgProfilePath)) {
            // Obtener el tamaño del archivo de imagen
            $imgProfileSize = filesize($imgProfilePath);

            // Establecer las cabeceras para indicar que se devuelve una imagen
            header('Content-Type: application/octet-stream');
            header('Content-Disposition: attachment; filename="image.png"');
            header('Content-Length: ' . $imgProfileSize);

            // Leer y enviar el contenido del archivo de imagen
            readfile($imgProfilePath);

        // echo $imgProfile;
        }
        $stmt->close();
    }
}

$con->close();

?>