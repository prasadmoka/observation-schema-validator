package scala.org.syngenta.obs.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.fge.jackson.JsonLoader
import com.github.fge.jsonschema.core.report.ProcessingReport
import com.github.fge.jsonschema.main.{JsonSchema, JsonSchemaFactory}
import org.slf4j.LoggerFactory

import java.nio.file.Paths
import scala.org.syngenta.obs.job.ObservationSchemaConfig

class ObservationSchemaValidator(config: ObservationSchemaConfig) extends Serializable {

  private val logger = LoggerFactory.getLogger(classOf[ObservationSchemaValidator])
  private val objectMapper = new ObjectMapper()
  private val schemaJson:  JsonSchema = {
    readResourceFile(s"${config.schemaPath}")
  }

  def readResourceFile(schemaUrl: String): JsonSchema = {
    val schemaFactory = JsonSchemaFactory.byDefault
    try {
      val file = Paths.get(schemaUrl+"/observation.json").toFile
       schemaFactory.getJsonSchema(JsonLoader.fromFile(file))
    } catch {
      case ex: Exception => ex.printStackTrace()
        throw ex
    }
  }


  def validateAgainstSchema(inputString: String): ProcessingReport = {
    logger.debug("validating for this json: "+inputString)
    val inputJson = objectMapper.readTree(inputString)
    val report: ProcessingReport = schemaJson.validate(inputJson)
    report
  }

  def getInvalidFieldName(errorInfo: String): String = {
    println(errorInfo)
    val message = errorInfo.split("schema:")
    val defaultValidationErrMsg = "Unable to obtain field name for failed validation"
    if (message.length > 1) {
      val fields = message(1).split(",")
      if (fields.length > 2) {
        val pointer = fields(1).split("\"pointer\":")
        pointer(1).substring(0, pointer(1).length - 2)
      } else {
        defaultValidationErrMsg
      }
    } else {
      defaultValidationErrMsg
    }
  }
}
