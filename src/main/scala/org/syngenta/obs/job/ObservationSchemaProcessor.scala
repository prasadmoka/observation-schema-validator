package scala.org.syngenta.obs.job

import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.api.java.typeutils.TypeExtractor
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

import scala.org.syngenta.obs.functions.ObservationSchemaFunction

class ObservationSchemaProcessor(config: ObservationSchemaConfig) {

  def process(): Unit ={
    implicit val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    implicit val eventTypeInfo: TypeInformation[String] = TypeExtractor.getForClass(classOf[String])
    //Kafka consumer to read the data from the input topic
    val inputStream: DataStream[String] = env.addSource(config.kafkaConsumer)

    //Output Stream data
    val outputStream: DataStream[String] = inputStream.process(new ObservationSchemaFunction(config))

    // write data to 3 different topics based on the overridden processElement() of ProcessFunction
    outputStream.getSideOutput(config.validObsOutputTag).addSink(config.flinkObsValidSink)
    outputStream.getSideOutput(config.failedObsOutputTag).addSink(config.flinkObsFailedSink)
    outputStream.getSideOutput(config.skippedObsOutputTag).addSink(config.flinkObsSkippedSink)

    env.execute(config.jobName)
  }
}
