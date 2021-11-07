package scala.org.syngenta.obs.functions

import org.apache.flink.streaming.api.functions.ProcessFunction
import org.apache.flink.util.Collector
import org.slf4j.LoggerFactory

import scala.org.syngenta.obs.job.ObservationSchemaConfig
import scala.org.syngenta.obs.util.ObservationSchemaValidator

class ObservationSchemaFunction(config: ObservationSchemaConfig) extends ProcessFunction[String, String]{
    private val logger = LoggerFactory.getLogger(classOf[ObservationSchemaFunction])

    private val obsSchemaValidator = new ObservationSchemaValidator(config)

  override def processElement(inputStr: String, context: ProcessFunction[String, String]#Context, collector: Collector[String]):
  Unit = {
    // Validate the input event against the schema
    // If it is valid write the event to valid topic
    // If it is not valid with the current schema, check for componentType
    // If it has failed due to missing componentType, then add it to the skipped topic
    // If it has failed due to any other reason, add it to the invalid topic
    val processingReport = obsSchemaValidator.validateAgainstSchema(inputStr)
    if(processingReport.isSuccess)
      context.output(config.validObsOutputTag,inputStr)
    else{
      val failedErrorMsg = obsSchemaValidator.getInvalidFieldName(processingReport.toString)
      logger.debug(s"Observation schema validation has failed with missing property for :"+failedErrorMsg)
      context.output(config.failedObsOutputTag,inputStr)
      //TODO write logic to check for the valid componentType
//      val validComponentType: Boolean = true
//      if(validComponentType)
//        context.output(config.validObsFailedOutputTag,inputStr)
//      else
//        context.output(config.validObsSkippedOutputTag,inputStr)
    }
  }
}
