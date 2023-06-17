<?php

$username = $_POST['username'];
$subject = $_POST['subject'];
$message = $_POST['message'];

$directory = './suggests/';
$path = $directory . $username . ".txt";
$result = "";

if (!file_exists($directory)) {
    mkdir($directory, 0755, true);
}

// Create a timestamp for the suggestion file
$timestamp = date('Y-m-d H:i:s');

// Open the file in append mode
$file = fopen($path, 'a');

if ($file !== false) {
    // Write the suggestion details to the file
    fwrite($file, "Fecha: " . $timestamp . PHP_EOL);
    fwrite($file, "Asunto: " . $subject . PHP_EOL);
    fwrite($file, "Sugerencia: " . $message . PHP_EOL);
    fwrite($file, "\n");

    // Close the file
    fclose($file);

    $result = "OK";
}

print($result);

?>