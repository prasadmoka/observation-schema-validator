package scala.org.syngenta.obs.job

import com.typesafe.config.Config
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.api.java.typeutils.TypeExtractor
import org.apache.flink.streaming.api.scala.OutputTag
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer, FlinkKafkaProducer}

import java.util.Properties

class ObservationSchemaConfig(val config: Config) {

  val jobName: String = config.getString("job.name")
  val kafkaServer: String = config.getString("kafka.bootstrap.servers")// = "localhost:9092"
  val kafkaZookeeper: String = config.getString("kafka.zookeeper.connect")// = "localhost:2181"
  val inputObsTopic: String = config.getString("kafka.input.topic")// = ${job.env}".observation.input"
  val validObsTopic: String = config.getString("kafka.valid.topic")// = ${job.env}".observation.valid"
  val failedObsTopic: String = config.getString("kafka.failed.topic")// = ${job.env}".observation.failed"
  val skippedObsTopic: String = config.getString("kafka.skipped.topic")// = ${job.env}".observation.skipped"
  val validatorGroup: String =  config.getString("kafka.validator.groupId")// = ${job.env}".observation.group"
  val schemaPath: String =  config.getString("observation.schema.path") // "obs/schemas"

  def kafkaConsumerProperties: Properties = {
    val properties = new Properties()
    properties.setProperty("bootstrap.servers", kafkaServer)
    properties.setProperty("group.id", validatorGroup)
    properties
  }

  def kafkaProducerProperties: Properties = {
    val properties = new Properties()
    properties.setProperty("bootstrap.servers", kafkaServer)
    properties
  }

  implicit val typeInfoString: TypeInformation[String] = TypeExtractor.getForClass(classOf[String])

  val validObsOutputTag: OutputTag[String] = OutputTag[String]("observation-valid")
  val failedObsOutputTag: OutputTag[String] = OutputTag[String]("observation-failed")
  val skippedObsOutputTag: OutputTag[String] = OutputTag[String]("observation-skipped")


  val kafkaConsumer = new FlinkKafkaConsumer[String](inputObsTopic, new SimpleStringSchema(),kafkaConsumerProperties)

  val flinkObsValidSink = new FlinkKafkaProducer[String](validObsTopic, new SimpleStringSchema, kafkaProducerProperties)
  val flinkObsFailedSink = new FlinkKafkaProducer[String](failedObsTopic, new SimpleStringSchema, kafkaProducerProperties)
  val flinkObsSkippedSink = new FlinkKafkaProducer[String](skippedObsTopic, new SimpleStringSchema, kafkaProducerProperties)

}
