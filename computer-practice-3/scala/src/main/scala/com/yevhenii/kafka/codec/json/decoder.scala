package com.aaron.kafka.codec.json

import kafka.serializer
import kafka.utils.VerifiableProperties
import org.json4s._
import org.json4s.jackson.JsonMethods._

/**
  *
  * @param properties Decoder properties
  */
class Decoder(properties: VerifiableProperties = null) extends serializer.Decoder[JValue] {

  private[this] val stringDecoder = new serializer.StringDecoder(properties)

  override def fromBytes(message: Array[Byte]): JValue =
    parse(stringDecoder.fromBytes(message))
}