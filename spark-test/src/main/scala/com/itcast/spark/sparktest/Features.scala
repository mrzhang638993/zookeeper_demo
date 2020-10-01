package com.itcast.spark.sparktest

import org.json4s.JsonAST.JObject

/**
 * {
 * "type": "FeatureCollection",
	*"features": [
    *{
      *"type": "Feature",
      *"id": 0,
      *"properties": {
        *"boroughCode": 5,
        *"borough": "Staten Island",
        *"@id": "http:\/\/nyc.pediacities.com\/Resource\/Borough\/Staten_Island"
      *},
      *"geometry": {
        *"type": "Polygon",
        *"coordinates": [
          *[
            *[-74.050508064032471, 40.566422034160816],
            *[-74.049983525625748, 40.566395924928273]
          *]
        *]
      *}
    *}
  *]
*}
 * */


case class FeatureCollection(features:List[Feature])

