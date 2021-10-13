<?php
include('./conexion.inc.php');
$link = conectar();//invocamos la funcion de conexion
//datos desde el formulario de android studio 

$cod = $_REQUEST['codigo'];
$nom =  $_REQUEST['nombre'];
//insertar los datos en la BD
//los caracteres van con comillas
$sql  = "insert into cursos values($cod,'$nom')";
//ejecuta la sentencia
$res  = mysqli_query($link,$sql)
        or  die("error en la consulta $sql".mysqli_error($link));
//cierro coneccion
mysqli_close($link);
?>