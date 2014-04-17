<?php
  $sleepMillis = intval($_GET['millisecond']);
  usleep($sleepMillis * 1000);
  header("content-type: application/javascript");
  echo '';
?>
