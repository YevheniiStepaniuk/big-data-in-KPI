package com.aaron.kafka.codec.json

import kafka.serializer
import kafka.utils.VerifiableProperties
import org.json4s._
import org.json4s.jackson.JsonMethods._

/**
  *
  * @param properties Encoder properties
  */
class Encoder(properties: VerifiableProperties = null) extends serializer.Encoder[JValue] {

  private[this] val stringEncoder = new serializer.StringEncoder(properties)

  def toBytes(message: JValue): Array[Byte] =
    stringEncoder.toBytes(compact(render(message)))
}