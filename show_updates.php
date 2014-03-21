<?php
  $host='localhost';
	$uname='****';
	$pwd='****';
	$db='****';
	$con = mysql_connect($host,$uname,$pwd) or die("connection failed");
	mysql_select_db($db,$con) or die("db selection failed");

    require 'jsonwrapper.php';

    $response = array();

	$query = "SELECT * FROM user_updates";
	$result = mysql_query($query) or die("cusnt fetch");

	$user_id[] = $post_id[] = $news_update[] = $anonymous[] = array();
    $time[] = $latitude[] = $longitude[] = $GeoLocation[] = $tick_marks[] = $reshouts[] = array();

    $rows = mysql_num_rows($result);

	if($result)
	{
          echo "{";
          echo "\"success\":\"1\",";
          echo "\"details\": [";
         $i=0;
         while($row = mysql_fetch_array($result))
         {
         	$user_id[$i] = $row['user_id'];
         	$post_id[$i] = $row['post_id'];
         	$news_update[$i] = $row['news_update'];
         	$anonymous[$i] = $row['anonymous'];
         	$time[$i] = $row['time'];
            $latitude[$i] = $row['latitude'];
            $longitude[$i] = $row['longitude'];
            $GeoLocation[$i] = $row['GeoLocation'];
            $tick_marks[$i] = $row['tick_marks'];
            $reshouts[$i] = $row['reshouts'];
            
         	//echo json_encode($row);
            //echo "\"details\": [";
            echo "{";
            echo "\"post_id\":\"".$row['post_id']."\",";
            echo "\"user_id\":\"".$row['user_id']."\",";
            echo "\"news_update\":\"".$row['news_update']."\",";
            echo "\"anonymous\":\"".$row['anonymous']."\",";
            echo "\"time_passed\":\"".$row['time']."\",";
            echo "\"latitude\":\"".$row['latitude']."\",";
            echo "\"longitude\":\"".$row['longitude']."\",";
            echo "\"geo_location\":\"".$row['GeoLocation']."\",";
            echo "\"tick_marks\":\"".$row['tick_marks']."\",";
            echo "\"re_shouts\":\"".$row['re_shouts']."\"";
            
            if($i != ($rows-1))
                echo "},";
            else
                echo "}";

            $i++;
         }
          echo "]}";
	}
	else
	{
		echo "\"success\":\"0\"";
		echo "\"message\":\"failed\"";
	}
	mysql_close();
?>
